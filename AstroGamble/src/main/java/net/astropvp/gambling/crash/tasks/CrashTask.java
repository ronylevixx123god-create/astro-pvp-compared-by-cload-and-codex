package net.astropvp.gambling.crash.tasks;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import  net.astropvp.gambling.crash.CrashAPI;
import  net.astropvp.gambling.crash.manager.CrashManager;
import  net.astropvp.gambling.crash.struct.CounterSpeed;
import  net.astropvp.gambling.crash.struct.CrashGame;
import  net.astropvp.gambling.crash.struct.CrashGraph;
import  net.astropvp.gambling.crash.struct.CrashPlayer;
import  net.astropvp.gambling.data.GambleUser;
import net.astropvp.gambling.utils.ItemBuilder;
import net.astropvp.gambling.utils.Pair;
import net.astropvp.gambling.utils.TitleUtils;
import org.bukkit.Bukkit;
import net.astropvp.gambling.GamblingPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;

public class CrashTask {

    private final GamblingPlugin plugin;
    private final CrashManager crashManager;
    private UUID crashGameUUID;

    public CrashTask(GamblingPlugin plugin) {
        this.plugin = plugin;
        this.crashManager = plugin.getCrashManager();
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private int crashTaskID;

    public void initializeGame() {
        this.crashGameUUID = UUID.randomUUID();
        this.crashManager.setActive(true);
        this.crashManager.randomizeFinalCrashX();
        crashManager.setCurrentCrashX(1.0D);
        plugin.getServer().getScheduler().cancelTask(this.crashTaskID);
        this.startCounter(CounterSpeed.FIRST);
    }


    private void startCounter(CounterSpeed counterSpeed) {
        this.crashTaskID = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if(crashManager.getCurrentCrashX() >= crashManager.getFinalCrashX()) {
                setGameFinished();
                return;
            }

            if(crashManager.getCurrentCrashX() >= counterSpeed.getThreshold()) {
                plugin.getServer().getScheduler().cancelTask(this.crashTaskID);
                startCounter(counterSpeed.getNextCounterSpeed());
                return;
            }

            finalizeCounter(counterSpeed.getMultiplier());
        }, 0L, counterSpeed.getTicks());
    }


    private void finalizeCounter(double multiplier) {
        crashManager.setCurrentCrashX(crashManager.getCurrentCrashX() + multiplier);

        String crashStarName = crashManager.getCurrentCrashX() < 1.98
                ? "&cCashout: &f" + decimalFormat.format(crashManager.getCurrentCrashX()) + "x"
                : "&aCashout: &f" + decimalFormat.format(crashManager.getCurrentCrashX()) + "x";

        ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR,
                crashStarName);

        for(Player player : CrashAPI.getFixedCrashOpenInventoryList()) {
            if(CrashAPI.playerHasCrashGUIOpen(player)) {
                if(crashManager.hasBetted(player) && !crashManager.getCashedAt().containsKey(player)) {
                    itemBuilder.setLore(Collections.singletonList("&aCashout: $" + moneyFormat.format(crashManager.getCurrentCrashX() * crashManager.getBets().get(player))));
                }
                player.getOpenInventory().setItem(49, itemBuilder.colorize().build());
                player.updateInventory();
            }
        }

        crashManager.getBets().entrySet().removeIf(this::checkIfPlayerIsAtAutoPayout);

    }

    private boolean checkIfPlayerIsAtAutoPayout(Map.Entry<Player, Double> playerDoubleEntry) {
        Player player = playerDoubleEntry.getKey();
        GambleUser gamblePlayer = plugin.getGambleManager().getGambleUser(player.getUniqueId());
        if(crashManager.getCurrentCrashX() >= gamblePlayer.getCrashAutoPayout()) {


            double moneyBetted = crashManager.getBets().get(player);
            double cashedOutAmount = moneyBetted * gamblePlayer.getCrashAutoPayout();
            EconomyUtils.depositBalance(player, cashedOutAmount);
            player.sendMessage(CC.translate("&6Crash &8| &eYou have cashed out for &a$" + moneyFormat.format(cashedOutAmount) + " &eusing auto payout"));
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 3.0F, 3.0F);
            crashManager.getCashedAt().put(player, new Pair<>(gamblePlayer.getCrashAutoPayout(), moneyBetted));

            List<String> lore = new ArrayList<>();
            lore.add("&fBet: &a$" + moneyFormat.format(moneyBetted));
            lore.add("&fCashed Out: &a" + decimalFormat.format(gamblePlayer.getCrashAutoPayout()) + "x");
            lore.add("&fCashed Amount: &a$" + moneyFormat.format(cashedOutAmount));
            ItemBuilder cashedOutBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5,
                    "&fPlayer: &a" + player.getName());
            cashedOutBuilder.setLore(lore);
            cashedOutBuilder.setGlowing();

            setPlayerCashedOutInGUI(player, cashedOutBuilder.colorize().build());

            return true;
        }

        return false;
    }

    private void setPlayerCashedOutInGUI(Player player, ItemStack itemStack) {
        CrashAPI.getFixedCrashOpenInventoryList().forEach(loopPlayer -> {
            if(CrashAPI.playerHasCrashGUIOpen(loopPlayer)) {
                Inventory inventory = loopPlayer.getOpenInventory().getTopInventory();
                for(int i = 0; i < 50; i++) {
                    if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) continue;
                    String toLookIn = ChatColor.stripColor(inventory.getItem(i).getItemMeta().getDisplayName());
                    if(!toLookIn.contains(":")) continue;
                    toLookIn = toLookIn.split(": ")[1];
                    if(Objects.equals(toLookIn, player.getName())) {
                        inventory.setItem(i, itemStack);
                        loopPlayer.updateInventory();
                    }
                }
            }
        });
    }

    private void check(int[] checks) {
        for(int check : checks) {
            if(crashManager.getFinalCrashX() >= check) {
                this.crashManager.getLastBigCrashes().put(check, 0);
            } else {
                this.crashManager.getLastBigCrashes().put(check, this.crashManager.getLastBigCrashes().get(check) + 1);
            }
        }
    }

    private void addLast100XToGui() {
        ItemBuilder lastX = new ItemBuilder(Material.DIAMOND, "&c&lRecent High Crashes");
        lastX.addLoreLine("&fThe following lines show the last time");
        lastX.addLoreLine("&feach crash multiplier has crashed at");
        for(int bigX : new int[] {5, 10, 25, 50, 100, 250, 500, 1000})
            lastX.addLoreLine(" &f" + moneyFormat.format(bigX) + "x: &c" + moneyFormat.format(this.crashManager.getLastBigCrashes().get(bigX)) + " &fGames ago");
        lastX.setGlowing();

        CrashAPI.getFixedCrashOpenInventoryList().forEach(player -> {
            player.getOpenInventory().setItem(8, lastX.colorize().build());
            player.updateInventory();
        });
    }

    private void setGameFinished() {

        check(new int[] {5, 10, 25, 50, 100, 250, 500, 1000});

        plugin.getServer().getScheduler().cancelTask(this.crashTaskID);
        plugin.getServer().getScheduler().runTaskLater(plugin, this::initializeGame, 20 * 6L); // 6 seconds
        addLast100XToGui();
        setupCrashGame();
        ifFinalCrashOver(10);

        crashManager.getRecentCrashes().add(crashManager.getFinalCrashX());
        if(crashManager.getRecentCrashes().size() >= 12) crashManager.getRecentCrashes().remove(0);

        this.crashManager.setActive(false);
        crashedGUI();

    }

    private void ifFinalCrashOver(int over) {
        if(crashManager.getFinalCrashX() < over) return;
        int playersEntered = 0;
        int playersCashedOut = 0;
        playersEntered += crashManager.getBets().size();
        playersEntered += crashManager.getCashedAt().size();
        playersCashedOut += crashManager.getCashedAt().size();

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&6&lAstro Crash &7&o(Game Seed: &f" + this.crashGameUUID.toString().split("-")[0] + "&7)"));
        String lastCrash = crashManager.getFinalCrashX() > 100 ? " &8● &7Last Crash: &4" : " &8● &7Last Crash: &a";
        Bukkit.broadcastMessage(CC.translate( lastCrash + decimalFormat.format(crashManager.getFinalCrashX()) + "x"));
        Bukkit.broadcastMessage(CC.translate(" &8● &7Entered: &c" + playersEntered));
        Bukkit.broadcastMessage(CC.translate(" &8● &7Cashed Out: &c" + playersCashedOut));
        Bukkit.broadcastMessage("");
    }

    private void crashedGUI() {
        String crashStarName = "&c&lCRASH @ " + decimalFormat.format(crashManager.getFinalCrashX()) + "x";

        ItemStack crashStar = new ItemBuilder(Material.BARRIER,
                crashStarName,
                "&7&oA new game will be starting soon...").colorize().build();

        ItemBuilder itemBuilder = new ItemBuilder(Material.REDSTONE,
                "&cLast 10 Crashes");
        for (int crashNumber = this.crashManager.getRecentCrashes().size(); crashNumber > 1; crashNumber--) {
            double crash = this.crashManager.getRecentCrashes().get(crashNumber-1);
            String line = crash > 1.98 ? "&fCrash #" + (crashNumber-1) + ": &a" + decimalFormat.format(crash) + "x" : "&fCrash #" + (crashNumber-1) + ": &c" + decimalFormat.format(crash) + "x";
            itemBuilder.addLoreLine(line);
        }

        CrashAPI.getFixedCrashOpenInventoryList().forEach(player -> {
            if(CrashAPI.playerHasCrashGUIOpen(player)) {
                player.getOpenInventory().setItem(49, crashStar);
                player.getOpenInventory().setItem(26, itemBuilder.colorize().build());
                int[] toEmptySlot = new int[]{10, 11, 12, 13, 14, 15, 16,
                        19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
                for(int emptySlot : toEmptySlot) {
                    player.getOpenInventory().setItem(emptySlot, null);
                }
                player.updateInventory();
            }
        });
        CrashAPI.getFixedCrashOpenInventoryList().forEach(player -> {
            crashManager.getBets().forEach((loopPlayer, loopBet) -> {
                ItemBuilder testBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3,
                        "&fPlayer: &a" + loopPlayer.getName(),
                        "&fBet: &a$" + moneyFormat.format(loopBet)).setSkullOwner(loopPlayer.getName());
                player.getOpenInventory().getTopInventory().addItem(testBuilder.colorize().build());
                player.updateInventory();
            });
        });
    }

    private void setupCrashGame() {
        List<CrashPlayer> crashPlayers = new ArrayList<>();
        crashManager.getCashedAt().forEach((player, pair) -> {
            CrashPlayer crashPlayer = new CrashPlayer(
                    player.getUniqueId(),
                    pair.getRight(),
                    pair.getLeft(),
                    crashManager.getFinalCrashX());
            crashPlayers.add(crashPlayer);

            GambleUser gambleUser = plugin.getGambleManager().getGambleUser(player.getUniqueId());
            gambleUser.addCrashWin(crashPlayer.getWinnings() - crashPlayer.getMoney());
        });

        crashManager.getBets().forEach((player, bet) -> {
            CrashPlayer crashPlayer =  new CrashPlayer(player.getUniqueId(), bet, null, crashManager.getFinalCrashX());
            crashPlayers.add(crashPlayer);
            GambleUser gambleUser = plugin.getGambleManager().getGambleUser(player.getUniqueId());
            gambleUser.addCrashLoss(crashPlayer.getMoney());
        });

        List<CrashGame> crashGames = new ArrayList<>();
        CrashGame crashGame = new CrashGame(UUID.randomUUID(), crashPlayers, crashManager.getFinalCrashX());
        crashGames.add(crashGame);
        this.crashGameUUID = crashGame.getCrashGameID();

        crashPlayers.stream()
                .filter(crashPlayer -> crashPlayer.getMoney() != null)
                .map(CrashPlayer::getPlayerUUID)
                .forEach(playerUUID -> {

                    if(crashManager.getPlayerCrashGraphMap().containsKey(playerUUID)) {
                        if(!crashManager.getPlayerCrashGraphMap().get(playerUUID).getCrashGames().isEmpty()) {
                            crashManager.getPlayerCrashGraphMap().get(playerUUID).getCrashGames().add(crashGame);
                            return;
                        }
                        crashManager.getPlayerCrashGraphMap().get(playerUUID).setCrashGames(crashGames);
                        return;
                    }
                    crashManager.getPlayerCrashGraphMap().put(playerUUID, new CrashGraph(playerUUID, crashGames));
                });

        crashManager.getBets().keySet().forEach(player -> {
            if(CrashAPI.playerHasCrashGUIOpen(player)) {
                player.closeInventory();
            }

            GambleUser gambleUser = plugin.getGambleManager().getGambleUser(player.getUniqueId());
            if(gambleUser.isCrashSound()) player.playSound(player.getLocation(), Sound.ITEM_BREAK, 4.0F, 4.0F);
            TitleUtils.sendTitle(player, CC.translate("&c&lCRASHED @ " + decimalFormat.format(crashManager.getFinalCrashX()) + "x"), CC.translate("&7&oBetter luck next time..."), 1, 3, 1);

        });
        crashManager.getBets().clear();
        crashManager.getCashedAt().clear();

        crashManager.getWaitingToJoinCrash().forEach((player, bet) -> {
            if(player.isOnline()) {
                if(EconomyUtils.hasBalance(player, bet)) {

                    EconomyUtils.withdrawBalance(player, bet);
                    crashManager.getBets().put(player, bet);
                    crashManager.getLastBets().put(player, bet);

                    player.sendMessage(CC.translate(CrashAPI.prefix + "&aYou have joined the crash game with $" + moneyFormat.format(bet)));
                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
                }
            }
        });
        crashManager.getWaitingToJoinCrash().clear();
    }



}
