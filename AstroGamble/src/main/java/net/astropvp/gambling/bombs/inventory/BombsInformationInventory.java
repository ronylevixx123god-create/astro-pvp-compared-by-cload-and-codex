package net.astropvp.gambling.bombs.inventory;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import  net.astropvp.gambling.data.GambleUser;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.astropvp.gambling.GamblingPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BombsInformationInventory {

    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");


    private final Gui mineInventory;

    public Gui getUI() {
        return this.mineInventory;
    }


    public BombsInformationInventory(Player player) {
        this.mineInventory = new Gui(GuiType.HOPPER, "Astro Bombs");
        this.mineInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemStack info = new ItemBuilder(Material.BOOK_AND_QUILL,
                "&cBombs Gambling",
                "&7The game is &fsimple. &7It's a",
                "&clandfield, &7filled with &cbombs",
                "&7Choose a slot that contains a &cbomb,",
                "&7and your &amoney &cblows up&7, poof.",
                "&7otherwise, be fortunate and &fincrease",
                "&7your &amoney &7generously",
                "",
                "&fUse &c/bombs <amount> &fto enter the game"
        ).colorize().build();

        GambleUser gambleUser = GamblingPlugin.getInstance().getGambleManager().getGambleUser(player.getUniqueId());
        List<String> playerStatsLore = new ArrayList<>();
        playerStatsLore.add("&fWins: &a" + gambleUser.getBombsWins());
        playerStatsLore.add("&fWinnings: &a$" + moneyFormat.format(gambleUser.getBombsWinnings()));
        playerStatsLore.add("&fLosses: &c" + gambleUser.getBombsLosses());
        playerStatsLore.add("&fLosings: &c$" + moneyFormat.format(gambleUser.getBombsLosings()));
        playerStatsLore.add("&fRatio: &7[" + gambleUser.getMinesWinLossRatio(":") + "]");
        ItemBuilder playerStatsItemBuilder = new ItemBuilder(Material.PAPER,
                "&cBombs Stats").setLore(playerStatsLore);

        this.mineInventory.setItem(3, new GuiItem(playerStatsItemBuilder.setGlowing().colorize().build()));
        this.mineInventory.setItem(2, new GuiItem(info));

        this.mineInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.mineInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit bombs menu").colorize().build();
    }


}
