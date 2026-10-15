package net.astropvp.gambling.mines.inventory;

import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import  net.astropvp.gambling.mines.enums.DifficultyType;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinesDifficultySelectionInventory {


    private final Gui mineInventory;

    public Gui getUI() {
        return this.mineInventory;
    }


    public MinesDifficultySelectionInventory(Player player, double bet) {
        this.mineInventory = new Gui(GuiType.HOPPER, "Astro Mines Difficulty");
        this.mineInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemBuilder easyBuilder = new ItemBuilder(Material.WOOL, 1, (short) 5,
                "&a&lEASY",
                "&7An easy mine...",
                "&7Less bombs");
        ItemBuilder mediumBuilder = new ItemBuilder(Material.WOOL, 1, (short) 4,
                "&e&lMEDIUM",
                "&7An intermediate mine...",
                "&7some bombs");
        ItemBuilder hardBuilder = new ItemBuilder(Material.WOOL, 1, (short) 14,
                "&c&lHARD",
                "&7A difficult mine...",
                "&7lots of bombs");
        this.mineInventory.setItem(1, new GuiItem(easyBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            MinesGameInventory minesGameInventory = new MinesGameInventory(player, DifficultyType.EASY, bet);
            minesGameInventory.getUI().open(player);
        }));
        this.mineInventory.setItem(2, new GuiItem(mediumBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            MinesGameInventory minesGameInventory = new MinesGameInventory(player, DifficultyType.MEDIUM, bet);
            minesGameInventory.getUI().open(player);
        }));
        this.mineInventory.setItem(3, new GuiItem(hardBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            MinesGameInventory minesGameInventory = new MinesGameInventory(player, DifficultyType.HARD, bet);
            minesGameInventory.getUI().open(player);
        }));
        this.mineInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.mineInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit mines menu").colorize().build();
    }
}
