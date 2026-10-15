package net.astropvp.gambling.crash.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;

import  net.astropvp.gambling.crash.CrashAPI;
import  net.astropvp.gambling.crash.manager.CrashManager;
import  net.astropvp.gambling.crash.struct.CrashPlayer;
import  net.astropvp.gambling.data.GambleUser;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import net.astropvp.gambling.GamblingPlugin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatsInventory {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");


    private final PaginatedGui statsMenu;

    public PaginatedGui getUI() {
        return this.statsMenu;
    }


    public StatsInventory(Player player, OfflinePlayer statsOf) {
        this.statsMenu = new PaginatedGui(6, 28, "Crash Stats: " + statsOf.getName());
        this.statsMenu.setDefaultClickAction(event -> event.setCancelled(true));
        this.statsMenu.getFiller().fillBorder(new GuiItem(createFillerItem()));

        List<String> statsLore = new ArrayList<>();
        GamblingPlugin gamblingPlugin = GamblingPlugin.getInstance();
        GambleUser gambleUser = gamblingPlugin.getGambleManager().getGambleUser(statsOf.getUniqueId());
        statsLore.add("&fOverall Wins: &a" + gambleUser.getCrashWins());
        statsLore.add("&fOverall Losses: &c" + gambleUser.getCrashLosses());
        statsLore.add("&fMoney Won: &a$" + NumberUtils.formatMoney(gambleUser.getCrashWinnings()));
        statsLore.add("&fMoney Lost: &c$" + NumberUtils.formatMoney(gambleUser.getCrashLosings()));
        double profit = gambleUser.getCrashWinnings() - gambleUser.getCrashLosings();
        String netProfit = profit >= 0 ? "&fNet Profit: &a$" + NumberUtils.formatMoney(profit) :  "&fNet Profit: &c$" +  NumberUtils.formatMoney(profit);
        statsLore.add(netProfit);

        ItemStack info = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3, "&c" + statsOf.getName() + " Stats")
                .setSkullOwner(statsOf.getName()).setLore(statsLore).colorize().build();

        ItemStack prevPage = new ItemBuilder(Material.ARROW, "&c<- Previous Page", "&7Right-Click&7 to go to the previous page.").colorize().build();
        ItemStack nextPage = new ItemBuilder(Material.ARROW, "&aNext Page ->", "&7Right-Click&7 to go to the next page.").colorize().build();

        this.statsMenu.setItem(6, 1, new GuiItem(prevPage, event -> {
            this.statsMenu.previous();
            event.setCancelled(true);
        }));
        this.statsMenu.setItem(6, 9, new GuiItem(nextPage, event -> {
            this.statsMenu.next();
            event.setCancelled(true);
        }));

        this.statsMenu.setItem(6, 5, new GuiItem(createExitItem(), event -> {
            player.closeInventory();
            CrashInventory menu = new CrashInventory(player);
            menu.getUI().open(player);
        }));

        this.statsMenu.setItem(4, new GuiItem(info));
        CrashManager crashManager = GamblingPlugin.getInstance().getCrashManager();
        if(crashManager.getPlayerCrashGraphMap().containsKey(statsOf.getUniqueId()) && !crashManager.getPlayerCrashGraphMap().get(statsOf.getUniqueId()).getCrashGames().isEmpty()) {
            crashManager.getPlayerCrashGraphMap().get(statsOf.getUniqueId()).getCrashGames().forEach(crashGame -> {
                ItemStack itemStack;
                List<String> lore = new ArrayList<>();
                CrashPlayer crashPlayer = CrashAPI.getPlayerFromCrashGame(statsOf, crashGame);
                String line = crashGame.getCrashX() >= 1.98D ? "&fCrashed: &a" + decimalFormat.format(crashGame.getCrashX()) + "x" : "&fCrashed: &c" + decimalFormat.format(crashGame.getCrashX()) + "x";
                lore.add(line);
                if(crashPlayer.isWinner()) {
                    ItemBuilder itemBuilder = new ItemBuilder(Material.SUGAR, "&aCrash Win");
                    lore.add("&fCashout: &a" + decimalFormat.format(crashPlayer.getCashout()) + "x");
                    lore.add("&fBet: &a$" + moneyFormat.format(crashPlayer.getMoney()));
                    lore.add("&fProfit: &a$" + moneyFormat.format((crashPlayer.getCashout() * crashPlayer.getMoney()) - crashPlayer.getMoney()));
                    itemBuilder.setLore(lore);
                    itemStack = itemBuilder.colorize().build();
                } else {
                    ItemBuilder itemBuilder = new ItemBuilder(Material.REDSTONE, "&cCrash Lose");
                    lore.add("&fCashout: &cBUST");
                    lore.add("&fBet: &a$" + moneyFormat.format(crashPlayer.getMoney()));
                    lore.add("&fProfit: &c$0.0");
                    itemBuilder.setLore(lore);
                    itemStack = itemBuilder.colorize().build();
                }

                this.statsMenu.addItem(new GuiItem(itemStack, event -> {
                    CrashGameInventory crashGameInventory = new CrashGameInventory(player, crashGame);
                    crashGameInventory.getUI().open(player);
                }));

            });
        }

    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cGo back to Crash Menu").colorize().build();
    }

    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&6Astro&fPvP"));
        item.setItemMeta(meta);
        return item;
    }
}
