/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.HCFOutpostUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  com.google.common.collect.Lists
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.astropvp.core.cosmiccoinflip.struct;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.HCFOutpostUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.google.common.collect.Lists;
import net.astropvp.core.cosmiccoinflip.utils.ItemBuilder;
import net.astropvp.core.AstroCore;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CoinFlipMatch {
    private final Player starterPlayer;
    private Player secondPlayer;
    private CoinColor firstColor;
    private CoinColor secondColor;
    private CoinColor winningColor;
    private MatchState matchState = MatchState.WAITING;
    private double wager = 0.0;
    public static double TAX = 0.05;
    private Inventory coinFlipMenu;
    private boolean gavePrize = false;
    private final String playerName;
    private String player2Name;
    private final UUID playerUUID;
    private UUID player2UUID;
    private static final Map<UUID, ItemStack> defaultHeadStacks = new HashMap<UUID, ItemStack>();
    private CoinColor previousColor = CoinColor.BLUE;
    private final DecimalFormat format = new DecimalFormat("#,###");

    public CoinFlipMatch(Player starter, double wager, CoinColor firstColor) {
        this.wager = wager;
        this.starterPlayer = starter;
        this.previousColor = firstColor;
        this.playerUUID = starter.getUniqueId();
        this.playerName = starter.getDisplayName();
        this.firstColor = firstColor;
        AstroCore.getInstance().getEcon().withdrawPlayer(starter, this.wager);
        starter.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "- $" + ChatColor.RED + this.format.format(this.wager));
    }

    public double getTax() {
        return TAX * this.wager;
    }

    public ItemStack getSkull(Player player, Player other) {
        ItemStack defaultSkill = defaultHeadStacks.computeIfAbsent(player.getUniqueId(), k -> ItemBuilder.createSkull(player.getName(), null, new String[]{null}));
        ItemStack skull = defaultSkill.clone();
        ItemMeta im = skull.getItemMeta();
        im.setDisplayName(FactionUtils.getFactionTag(other, player) + ChatColor.BOLD.toString() + " " + player.getDisplayName());
        im.setLore((List) Lists.newArrayList(new String[]{ChatColor.GREEN + "Balance: " + ChatColor.GREEN + ChatColor.BOLD + "$" + ChatColor.GREEN + this.format.format(AstroCore.getInstance().getEcon().getBalance(player))}));
        skull.setItemMeta(im);
        return skull;
    }

    public void buildFlipMenu() {
        String title = "Coin Flip Match - $" + this.format.format(this.wager * 2.0);
        if (title.length() >= 32) {
            title = title.substring(0, 31);
        }
        this.coinFlipMenu = Bukkit.createInventory(null, 9, title);
        this.coinFlipMenu.setItem(0, this.getSkull(this.getStarterPlayer(), this.getSecondPlayer()));
        this.coinFlipMenu.setItem(1, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, this.getFirstColor().getDyeColor().getWoolData(), this.getFirstColor().getChatColor() + this.getStarterPlayer().getName() + "'s Color", Lists.newArrayList()));
        this.coinFlipMenu.setItem(7, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, this.getSecondColor().getDyeColor().getWoolData(), this.getSecondColor().getChatColor() + this.getSecondPlayer().getName() + "'s Color", Lists.newArrayList()));
        this.coinFlipMenu.setItem(8, this.getSkull(this.getSecondPlayer(), this.getStarterPlayer()));
        this.matchState = MatchState.STARTING;
        this.coinFlipMenu.setItem(4, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, 5, DyeColor.LIME.getWoolData(), ChatColor.GREEN + ChatColor.BOLD.toString() + 5, ChatColor.GRAY + "Rolling in " + 5 + "s"));
        new BukkitRunnable() {
            int timer = 0;

            public void run() {
                ++this.timer;
                if (this.timer >= 5) {
                    this.cancel();
                    CoinFlipMatch.this.startRoll();
                    return;
                }
                for (Player pl : CoinFlipMatch.this.getPlayers()) {
                    if (pl == null || !pl.isOnline()) {
                        this.cancel();
                        return;
                    }
                    pl.playSound(pl.getLocation(), Sound.NOTE_PIANO, 1.0f, 1.4f);
                }
                CoinFlipMatch.this.coinFlipMenu.setItem(4, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, 5 - this.timer, this.timer <= 2 ? DyeColor.LIME.getWoolData() : (this.timer <= 4 ? DyeColor.YELLOW.getWoolData() : DyeColor.RED.getWoolData()), ChatColor.GREEN + ChatColor.BOLD.toString() + (5 - this.timer), ChatColor.GRAY + "Rolling in " + (5 - this.timer) + "s"));
            }
        }.runTaskTimer(AstroCore.getInstance(), 20L, 20L);
    }

    public void startRoll() {
        this.matchState = MatchState.FLIPPING;
        (new BukkitRunnable() {
            int ticks = 0;

            int maxTicks = 70 + ThreadLocalRandom.current().nextInt(2) + ThreadLocalRandom.current().nextInt(2) + ThreadLocalRandom.current().nextInt(2);

            public void run() {
                if (CoinFlipMatch.this.gavePrize) {
                    cancel();
                    return;
                }
                if (this.ticks >= this.maxTicks) {
                    cancel();
                    ItemStack winner = CoinFlipMatch.this.coinFlipMenu.getItem(4);
                    if (winner != null && winner.getType() == Material.WOOL) {
                        CoinFlipMatch.this.winningColor = CoinColor.getFromDyeColor(winner.getDurability());
                        CoinFlipMatch.this.endCoinFlip(CoinFlipMatch.RemoveReason.WON, (CoinFlipMatch.this.getFirstColor() == CoinFlipMatch.this.winningColor) ? CoinFlipMatch.this.getStarterPlayer() : CoinFlipMatch.this.getSecondPlayer());
                        UUID loser = (CoinFlipMatch.this.getFirstColor() == CoinFlipMatch.this.winningColor) ? CoinFlipMatch.this.getSecondPlayer().getUniqueId() : CoinFlipMatch.this.getStarterPlayer().getUniqueId();
                        AstroCore.getInstance().getCoinFlipManager().addLoss(loser, CoinFlipMatch.this.playerUUID.equals(loser) ? CoinFlipMatch.this.player2UUID : CoinFlipMatch.this.playerUUID, CoinFlipMatch.this.wager);
                    }
                    return;
                }
                this.ticks++;
                boolean lastTick = (this.ticks == this.maxTicks);
                if (!lastTick && this.ticks >= 20 && ((this.ticks >= 50 && this.ticks % 2 == 0) || (this.ticks >= 40 && this.ticks % 3 == 0) || (this.ticks >= 30 && this.ticks % 4 == 0)))
                    return;
                for (Player player : CoinFlipMatch.this.getPlayers()) {
                    if (player == null || !player.isOnline()) {
                        cancel();
                        return;
                    }
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.1F);
                }
                CoinColor color = !lastTick ? ((CoinFlipMatch.this.previousColor == CoinFlipMatch.this.getFirstColor()) ? CoinFlipMatch.this.getSecondColor() : CoinFlipMatch.this.getFirstColor()) : CoinFlipMatch.this.getWinnerColor();
                String winnerName = (lastTick && color.equals(CoinFlipMatch.this.getFirstColor())) ? (CoinFlipMatch.this.getFirstColor().getChatColor() + ChatColor.BOLD.toString() + CoinFlipMatch.this.getStarterPlayer().getName()) : ((lastTick && color.equals(CoinFlipMatch.this.getSecondColor())) ? (CoinFlipMatch.this.getSecondColor().getChatColor() + ChatColor.BOLD.toString() + CoinFlipMatch.this.getSecondPlayer().getName()) : null);
                CoinFlipMatch.this.previousColor = color;
                CoinFlipMatch.this.coinFlipMenu.setItem(4, ItemBuilder.buildItem(Material.WOOL, (short) color.getDyeColor().getWoolData(), (winnerName != null) ? winnerName : color.getName(), Lists.newArrayList(new String[]{(winnerName != null) ? (winnerName + ChatColor.GRAY + " has won the Coin Flip!") : (ChatColor.GRAY + "Rolling...")})));
                for (Player pl : CoinFlipMatch.this.getPlayers()) {
                    if (pl != null)
                        pl.updateInventory();
                }
            }
        }).runTaskTimer((Plugin) AstroCore.getInstance(), 20L, 2L);
    }

    public CoinColor getWinnerColor() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return this.getFirstColor();
        }
        return this.getSecondColor();
    }

    public void endCoinFlip(RemoveReason reason, Player forceWinner) {
        if (this.matchState == MatchState.DONE) {
            return;
        }
        this.matchState = MatchState.DONE;
        AstroCore.getInstance().getCoinFlipManager().getCoinFlipMatches().remove(this);
        if (reason == RemoveReason.LEAVE) {
            Player loser;
            Player winner;
            Player player = winner = ThreadLocalRandom.current().nextBoolean() ? this.starterPlayer : this.secondPlayer;
            if (winner != null) {
                System.out.println("Winning " + winner.getName() + " due to RNG.");
                this.givePrize(winner);
            }
            Player player2 = loser = winner != null && winner.equals(this.starterPlayer) ? this.secondPlayer : this.starterPlayer;
            if (loser != null && winner != null) {
                AstroCore.getInstance().getCoinFlipManager().addLoss(loser.getUniqueId(), winner.getUniqueId(), this.getWager());
            }
        } else if (reason == RemoveReason.WON && forceWinner != null) {
            for (Player player : this.getPlayers()) {
                if (player == null) continue;
                player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + forceWinner.getName() + " has won the Coin Flip with " + this.winningColor.getName() + ChatColor.GREEN + "!");
            }
            this.givePrize(forceWinner);
        }
    }

    public CoinColor getCoinColor(Player player) {
        if (this.starterPlayer.equals(player)) {
            return this.getFirstColor();
        }
        return this.getSecondColor();
    }

    public void onAcceptMatch(Player player) {
        this.secondPlayer = player;
        this.player2Name = player.getDisplayName();
        this.player2UUID = player.getUniqueId();
        AstroCore.getInstance().getEcon().withdrawPlayer(player, this.wager);
        this.buildFlipMenu();
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "- $" + ChatColor.RED + this.format.format(this.wager));
        for (Player pl : this.getPlayers()) {
            pl.playSound(pl.getLocation(), Sound.NOTE_PIANO, 1.0f, 1.1f);
            pl.sendMessage(ChatColor.RED + player.getName() + " has accepted the Coin Flip Wager!");
            pl.openInventory(this.coinFlipMenu);
        }
        this.coinFlipMenu.setItem(0, this.getSkull(this.getStarterPlayer(), this.getSecondPlayer()));
        this.coinFlipMenu.setItem(8, this.getSkull(this.getSecondPlayer(), this.getStarterPlayer()));
    }

    public void givePrize(Player player) {
        if (this.gavePrize) {
            return;
        }
        this.gavePrize = true;
        UUID loser = player.getUniqueId().equals(this.playerUUID) ? this.player2UUID : this.playerUUID;
        double totalWines = this.wager * 2.0;
        double taxRate = TAX;
        double win;
        if (HCFOutpostUtils.doesControlOutpost(player, "frozen")) {
            taxRate = 0.0;
        }
        if (player.hasPermission("astrocore.apollo") || player.hasPermission("astrocore.nebula")) {
            win = totalWines;
            player.sendMessage(CC.translate("&b&lAPOLLO: &fNo CF tax!"));
        } else {
            win = totalWines * (1.0 - taxRate);
        }
        CoinFlipManager manager = AstroCore.getInstance().getCoinFlipManager();
        manager.addWin(player.getUniqueId(), loser, win, taxRate * this.wager);
        EconomyResponse response = AstroCore.getInstance().getEcon().depositPlayer((OfflinePlayer) player, win);
        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ $" + this.format.format(win));
        for (Player pl : PlayerUtil.getOnlinePlayers()) {
            if (AstroCore.getInstance().getCoinFlipManager().getToggledNotifications().contains(pl.getUniqueId()))
                continue;
            pl.sendMessage("");
            pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Coin Flip " + ChatColor.DARK_GRAY + "➥ " + this.getColorChosen(player.getUniqueId()).getChatColor() + "\u25a0 " + FactionUtils.getFactionTag(pl, player) + ChatColor.GREEN + " " + player.getDisplayName() + ChatColor.GRAY + " (" + manager.getWinLossRatio(player.getUniqueId(), "-") + ") has defeated " + ChatColor.RED + this.getColorChosen(loser).getChatColor() + "\u25a0 " + FactionUtils.getFactionTag(pl, Bukkit.getPlayer(loser)) + " " + ChatColor.RED + (player.getDisplayName().equals(this.playerName) ? this.player2Name : this.playerName) + ChatColor.GRAY + " (" + manager.getWinLossRatio(loser, "-") + ") in a " + ChatColor.GRAY + ChatColor.UNDERLINE + "$" + this.format.format(totalWines) + ChatColor.GRAY + " /coinflip");
            pl.sendMessage("");
        }

        Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(player, "CF_WIN", win, this.wager, loser));
        Bukkit.getLogger().info("Giving " + player.getName() + " $" + win + " for beating " + (this.playerName.equals(player.getName()) ? this.player2Name : this.playerName) + " in a Coin Flip! (transactionSuccess: " + response.transactionSuccess() + ")");
    }

    public CoinColor getColorChosen(UUID uuid) {
        if (this.playerUUID.equals(uuid)) {
            return this.getFirstColor();
        }
        return this.getSecondColor();
    }

    public List<Player> getPlayers() {
        return Lists.newArrayList(this.starterPlayer, this.secondPlayer);
    }

    public void onPlayerLogout(Player player) {
        if (this.matchState == MatchState.FLIPPING || this.matchState == MatchState.STARTING) {
            this.endCoinFlip(RemoveReason.LEAVE, null);
        } else if (this.matchState == MatchState.WAITING && this.getSecondPlayer() == null) {
            this.matchState = MatchState.DONE;
            AstroCore.getInstance().getEcon().depositPlayer((OfflinePlayer) player, this.wager);
            Bukkit.getLogger().info("Returning wager of " + this.wager + " to " + player.getName());
            AstroCore.getInstance().getCoinFlipManager().getCoinFlipMatches().remove(this);
        }
    }

    public Player getStarterPlayer() {
        return this.starterPlayer;
    }

    public Player getSecondPlayer() {
        return this.secondPlayer;
    }

    public CoinColor getFirstColor() {
        return this.firstColor;
    }

    public CoinColor getSecondColor() {
        return this.secondColor;
    }

    public CoinColor getWinningColor() {
        return this.winningColor;
    }

    public void setFirstColor(CoinColor firstColor) {
        this.firstColor = firstColor;
    }

    public void setSecondColor(CoinColor secondColor) {
        this.secondColor = secondColor;
    }

    public void setWinningColor(CoinColor winningColor) {
        this.winningColor = winningColor;
    }

    public MatchState getMatchState() {
        return this.matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public double getWager() {
        return this.wager;
    }

    public enum MatchState {
        WAITING,
        STARTING,
        FLIPPING,
        DONE

    }

    public enum RemoveReason {
        LEAVE,
        WON

    }
}

