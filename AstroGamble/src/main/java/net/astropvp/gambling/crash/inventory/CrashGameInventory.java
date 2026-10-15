package net.astropvp.gambling.crash.inventory;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import  net.astropvp.gambling.crash.struct.CrashGame;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CrashGameInventory {


    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final Gui crashGameInventory;

    public Gui getUI() {
        return this.crashGameInventory;
    }


    public CrashGameInventory(Player player, CrashGame crashGame) {
        this.crashGameInventory = new Gui(6, "Crash Game: " + String.valueOf(crashGame.getCrashGameID()).split("-")[0]);
        this.crashGameInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.crashGameInventory.getFiller().fillBorder(new GuiItem(createFillerItem()));

        crashGame.getPlayers().forEach(crashPlayer -> {
            OfflinePlayer crashPlayerAsPlayer = Bukkit.getOfflinePlayer(crashPlayer.getPlayerUUID());

            List<String> lore = new ArrayList<>();
            String line = crashGame.getCrashX() >= 1.98D ? "&fCrashed: &a" + decimalFormat.format(crashGame.getCrashX())  + "x" : "&fCrashed: &c" + decimalFormat.format(crashGame.getCrashX())  + "x";
            lore.add(line);

            ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(crashPlayerAsPlayer.getName());

            if(crashPlayer.isWinner()) {
                itemBuilder.setName("&fCrash Win: &a" + crashPlayerAsPlayer.getName());
                lore.add("&fCashout: &a" + decimalFormat.format(crashPlayer.getCashout()) + "x");
                lore.add("&fBet: &a$" + moneyFormat.format(crashPlayer.getMoney()));
                lore.add("&fProfit: &a$" + moneyFormat.format((crashPlayer.getCashout() * crashPlayer.getMoney()) - crashPlayer.getMoney()));
                itemBuilder.setLore(lore);
            } else {
                itemBuilder.setName("&fCrash Lose: &c" + crashPlayerAsPlayer.getName());
                lore.add("&fCashout: &cBUST");
                lore.add("&fBet: &a$" + moneyFormat.format(crashPlayer.getMoney()));
                lore.add("&fProfit: &c$0.0");
                itemBuilder.setLore(lore);
            }

            itemBuilder.addLoreLine("");
            itemBuilder.addLoreLine("&7Click to view player crash stats");

            this.crashGameInventory.addItem(new GuiItem(itemBuilder.colorize().build(), event -> {
                player.closeInventory();
                StatsInventory statsInventory = new StatsInventory(player, Bukkit.getOfflinePlayer(crashPlayer.getPlayerUUID()));
                statsInventory.getUI().open(player);
            }));

        });

        this.crashGameInventory.setItem(6, 9, new GuiItem(createExitItem(), event -> {
            player.closeInventory();
            StatsInventory menu = new StatsInventory(player, player);
            menu.getUI().open(player);
        }));

        this.crashGameInventory.setItem(6, 1, new GuiItem(createExitItem(), event -> {
            player.closeInventory();
            StatsInventory menu = new StatsInventory(player, player);
            menu.getUI().open(player);
        }));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cGo back to your stats").colorize().build();
    }

    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }


}
