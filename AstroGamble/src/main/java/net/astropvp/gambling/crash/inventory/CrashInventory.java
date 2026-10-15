package net.astropvp.gambling.crash.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import  net.astropvp.gambling.crash.CrashAPI;
import  net.astropvp.gambling.crash.manager.CrashManager;
import  net.astropvp.gambling.data.GambleUser;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import net.astropvp.gambling.GamblingPlugin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CrashInventory {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final GamblingPlugin plugin = GamblingPlugin.getInstance();
    private final CrashManager crashManager = GamblingPlugin.getInstance().getCrashManager();


    private final Gui crashInventory;

    public Gui getUI() {
        return this.crashInventory;
    }

    public CrashInventory(Player player) {
        this.crashInventory = new Gui(6, "Astro Crash");
        this.crashInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.crashInventory.getFiller().fillBorder(new GuiItem(createFillerItem()));

        addLast10CrashesToGui();
        addStatsToGui(player);
        addInfoToGui();
        addAutoCashOutToGui(player);
        addReEnterBetToGui(player);
        addSoundToggleToGui(player);
        addLast100XToGui();

        updateCrashStar(this.crashManager.getCurrentCrashX());

        addPlayersToInventory();

    }

    private void addLast100XToGui() {
        ItemBuilder lastX = new ItemBuilder(Material.DIAMOND, "&6&lRecent High Crashes");
        lastX.addLoreLine("&fThe following lines show the last time");
        lastX.addLoreLine("&feach crash multiplier has crashed at");
        for(int bigX : new int[] {5, 10, 25, 50, 100, 250, 500, 1000})
            lastX.addLoreLine(" &f" + moneyFormat.format(bigX) + "x: &e" + moneyFormat.format(this.crashManager.getLastBigCrashes().get(bigX)) + " &fGames ago");
        lastX.setGlowing();
        this.crashInventory.setItem(1, 9, new GuiItem(lastX.colorize().build()));
    }

    private void addPlayersToInventory() {
        this.crashManager.getBets().forEach((player, bet) -> {
            ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3,
                    "&fPlayer: &a" + player.getName(),
                    "&fBet: &a$" + moneyFormat.format(bet)).setSkullOwner(player.getName());
            this.crashInventory.addItem(new GuiItem(itemBuilder.colorize().build()));
        });

        if(!crashManager.isActive()) {
            this.crashManager.getWaitingToJoinCrash().forEach((player, bet) -> {
                ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3,
                        "&fPlayer: &a" + player.getName(),
                        "&fBet: &a$" + moneyFormat.format(bet)).setSkullOwner(player.getName());
                this.crashInventory.addItem(new GuiItem(itemBuilder.colorize().build()));
            });
        }

        this.crashManager.getCashedAt().forEach((player, pair) -> {
            List<String> lore = new ArrayList<>();
            lore.add("&fBet: &a$" + moneyFormat.format(pair.getRight()));
            lore.add("&fCashed Out: &a" + decimalFormat.format(pair.getLeft()) + "x");
            lore.add("&fCashed Amount: &a$" + moneyFormat.format(pair.getLeft() * pair.getRight()));
            ItemBuilder cashedOutBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5,
                    "&fPlayer: &a" + player.getName());
            cashedOutBuilder.setLore(lore);
            cashedOutBuilder.setGlowing();

            this.crashInventory.addItem(new GuiItem(cashedOutBuilder.colorize().build()));
        });
    }


    public void updateCrashStar(double newCrashAmount) {
        if(!crashManager.isActive()) {

            String crashStarName = "&c&lCRASH @ " + decimalFormat.format(crashManager.getFinalCrashX()) + "x";

            ItemStack crashStar = new ItemBuilder(Material.BARRIER,
                    crashStarName,
                    "&7&oA new game will be starting soon...").colorize().build();

            this.crashInventory.updateItem(49, new GuiItem(crashStar));
            return;
        }

        String crashStarName = newCrashAmount < 1.98
                ? "&fCashout: &c" + decimalFormat.format(newCrashAmount) + "x"
                : "&fCashout: &a" + decimalFormat.format(newCrashAmount) + "x";


        ItemStack crashStar = new ItemBuilder(Material.NETHER_STAR,
                crashStarName).colorize().build();
        this.crashInventory.updateItem(49, new GuiItem(crashStar));
    }

    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(" "));
        item.setItemMeta(meta);
        return item;
    }

    private void addInfoToGui() {
        ItemStack info = new ItemBuilder(Material.BOOK_AND_QUILL,
                "&6Crash Gambling",
                "&7Crash is a net.astropvp.gambling game where",
                "&7a crash &6multiplier &7quickly &fincreases",
                "&7starting at &61.00x. &7The multiplier (if you cash",
                "&7out), determines how much &aprofit &7you make!",
                "",
                "&cBe careful, &7the game can &cend &7at any time,",
                "&7and if you haven't &fcashed out &7by then",
                "&7you get &cnothing.",
                "",
                "&7This crash game has an &fauto cashout",
                "&7feature, which &eautomatically &7cashes out",
                "&7at the multiplier value set by the user",
                "&f/crash setautopayout <auto payout>",
                "",
                "&7You can view the crash &fstats &7of",
                "&7other players using /crash stats <player>",
                "",
                "&6&lTIP: &fUse &e/crash <amount> &fto enter the game"
        ).colorize().build();

        this.crashInventory.updateItem(1, 5, new GuiItem(info));
    }

    private void addLast10CrashesToGui() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.REDSTONE,
                "&6Last 10 Crashes");
        itemBuilder.addLoreLine("&7The most recent crash is at the top");

        for (int crashNumber = this.crashManager.getRecentCrashes().size(); crashNumber > 1; crashNumber--) {
            double crash = this.crashManager.getRecentCrashes().get(crashNumber-1);
            String line = crash > 1.98 ? "&fCrash #" + (crashNumber-1) + ": &a" + decimalFormat.format(crash) + "x" : "&fCrash #" + (crashNumber-1) + ": &c" + decimalFormat.format(crash) + "x";
            itemBuilder.addLoreLine(line);
        }

        this.crashInventory.setItem(3, 9, new GuiItem(itemBuilder.colorize().build()));
    }

    private void addStatsToGui(Player player) {
        ItemStack stats = new ItemBuilder(Material.SIGN,
                "&6Crash Statistics",
                "&7Click to view stats").colorize().build();
        this.crashInventory.setItem(3, 1, new GuiItem(stats, event -> {
            player.closeInventory();
            StatsInventory statsInventory = new StatsInventory(player, player);
            statsInventory.getUI().open(player);
        }));
    }

    private void addReEnterBetToGui(Player player) {
        String lastBet = crashManager.getLastBets().containsKey(player) ? "&a$" + moneyFormat.format(crashManager.getLastBets().get(player)) : "&cNot Entered";
        ItemBuilder reenterBuilder = new ItemBuilder(Material.MINECART, "&6&lRe Enter Bet",
                "&fLast Bet: " + lastBet,
                "&fBalance: &a$" + moneyFormat.format(EconomyUtils.getBalance(player)));
        this.crashInventory.updateItem(6, 9, new GuiItem(reenterBuilder.colorize().setGlowing().build(), event -> {
            if(crashManager.getLastBets().containsKey(player)) CrashAPI.tryBet(player, crashManager.getLastBets().get(player));
        }));

    }

    private void addAutoCashOutToGui(Player player) {
        GambleUser gambleUser = plugin.getGambleManager().getGambleUser(player.getUniqueId());
        this.crashInventory.updateItem(6, 1, new GuiItem(buildAutoCrashPayoutItem(gambleUser), event -> {

            if(event.isLeftClick())
                gambleUser.setCrashAutoPayout(gambleUser.getCrashAutoPayout() + 1);

            if(event.isRightClick() && gambleUser.getCrashAutoPayout() >= 2)
                gambleUser.setCrashAutoPayout(gambleUser.getCrashAutoPayout() + - 1);

            addAutoCashOutToGui(player);

        }));
    }

    private ItemStack buildAutoCrashPayoutItem(GambleUser gambleUser) {
        ItemBuilder autoCashOut = new ItemBuilder(Material.GOLD_INGOT, "&d&lAuto Cash Out",
                "&7The value set here will be the",
                "&7multiplier that is automatically cashed",
                "&7out at when you join the crash game",
                "",
                "&fAuto Payout: &d" + gambleUser.getCrashAutoPayout() + "x");

        return autoCashOut.colorize().build();
    }

    private void addSoundToggleToGui(Player player) {
        GambleUser gambleUser = this.plugin.getGambleManager().getGambleUser(player.getUniqueId());
        this.crashInventory.updateItem(1, 1, new GuiItem(getSoundToggleItem(gambleUser), event -> {
            player.playSound(player.getLocation(), Sound.CLICK, 2.0F, 2.0F);
            gambleUser.setCrashSound(!gambleUser.isCrashSound());
            this.crashInventory.updateItem(1, 1, getSoundToggleItem(gambleUser));
        }));

    }

    private ItemStack getSoundToggleItem(GambleUser gambleUser) {
        boolean hasSound = gambleUser.isCrashSound();
        String sound = hasSound ? "&a&lON" : "&c&lOFF";

        ItemBuilder soundToggleBuilder = new ItemBuilder(Material.REDSTONE_TORCH_ON, "&dToggle Crash Sounds",
                "&fToggled: " + sound);

        return soundToggleBuilder.colorize().build();
    }

}
