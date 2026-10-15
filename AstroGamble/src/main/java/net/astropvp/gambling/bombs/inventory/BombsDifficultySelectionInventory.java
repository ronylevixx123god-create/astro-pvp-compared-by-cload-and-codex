package net.astropvp.gambling.bombs.inventory;

import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import  net.astropvp.gambling.bombs.enums.DifficultyType;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BombsDifficultySelectionInventory {


    private final Gui mineInventory;

    public Gui getUI() {
        return this.mineInventory;
    }


    public BombsDifficultySelectionInventory(Player player, double bet) {
        this.mineInventory = new Gui(GuiType.HOPPER, "Astro Bombs Difficulty");
        this.mineInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemBuilder easyBuilder = new ItemBuilder(Material.WOOL, 1, (short) 5,
                "&a&lEASY",
                "&7An easy landfield...",
                "&7A simple 3x3 grid with 4 bombs!");
        ItemBuilder mediumBuilder = new ItemBuilder(Material.WOOL, 1, (short) 4,
                "&e&lMEDIUM",
                "&7An intermediate landfield...",
                "&7A 4x7 grid with 18 bombs!");
        ItemBuilder hardBuilder = new ItemBuilder(Material.WOOL, 1, (short) 14,
                "&c&lHARD",
                "&7A difficult landfield...",
                "&7A 6x9 grid with 49 bombs!");
        this.mineInventory.setItem(1, new GuiItem(easyBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            BombsGameInventory minesGameInventory = new BombsGameInventory(player, DifficultyType.EASY, bet);
            minesGameInventory.getUI().open(player);
        }));
        this.mineInventory.setItem(2, new GuiItem(mediumBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            BombsGameInventory minesGameInventory = new BombsGameInventory(player, DifficultyType.MEDIUM, bet);
            minesGameInventory.getUI().open(player);
        }));
        this.mineInventory.setItem(3, new GuiItem(hardBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            BombsGameInventory minesGameInventory = new BombsGameInventory(player, DifficultyType.HARD, bet);
            minesGameInventory.getUI().open(player);
        }));
        this.mineInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.mineInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit bomb menu").colorize().build();
    }
}
