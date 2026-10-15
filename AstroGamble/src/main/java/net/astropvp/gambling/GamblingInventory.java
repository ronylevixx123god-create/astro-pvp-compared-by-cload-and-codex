package net.astropvp.gambling;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import net.astropvp.gambling.crash.inventory.CrashInventory;
import net.astropvp.gambling.mines.inventory.MinesInformationInventory;
import net.astropvp.gambling.tower.inventory.TowerInformationInventory;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GamblingInventory {


    private final Gui gamblingInventory;

    public Gui getUI() {
        return this.gamblingInventory;
    }


    public GamblingInventory(Player player) {
        this.gamblingInventory = new Gui(1, "Astro Gambling");
        this.gamblingInventory.setDefaultClickAction(event -> event.setCancelled(true));
        ItemBuilder crashBuilder = new ItemBuilder(Material.NETHER_STAR, "&c&lCrash",
                "&7Click to enter the crash menu");
        //ItemBuilder bombsBuilder = new ItemBuilder(Material.TNT, "&4&lBombs",
                //"&7Click to enter the bombs menu");
        ItemBuilder minesBuilder = new ItemBuilder(Material.STONE_PLATE, "&e&lMines",
                "&7Click to enter the mines menu");
        ItemBuilder towerBuilder = new ItemBuilder(Material.ARMOR_STAND, "&d&lTowers",
                "&7Click to enter the towers menu");
        //ItemBuilder rouletteBuilder = new ItemBuilder(Material.STONE_BUTTON, "&a&lRoulette",
                //"&7Click to enter the roulette menu");

        ItemBuilder coinflipBuilder = new ItemBuilder(Material.GOLD_INGOT, "&b&lCoinflip",
                "&7Click to enter the coinflip menu");
        ItemBuilder jackpotBuilder = new ItemBuilder(Material.DOUBLE_PLANT, "&6&lJackpot",
                "&7Click to enter the jackpot menu");



        this.gamblingInventory.addItem(new GuiItem(crashBuilder.colorize().build(), event -> {
            CrashInventory crashInventory = new CrashInventory(player);
            crashInventory.getUI().open(player);
        }));
        this.gamblingInventory.addItem(new GuiItem(towerBuilder.colorize().build(), event -> {
            TowerInformationInventory towerInformationInventory = new TowerInformationInventory(player);
            towerInformationInventory.getUI().open(player);
        }));
        /*
        this.gamblingInventory.addItem(new GuiItem(bombsBuilder.colorize().build(), event -> {
            BombsInformationInventory bombsInformationInventory = new BombsInformationInventory(player);
            bombsInformationInventory.getUI().open(player);
        }));

         */
        this.gamblingInventory.addItem(new GuiItem(minesBuilder.colorize().build(), event -> {
            MinesInformationInventory minesInformationInventory = new MinesInformationInventory(player);
            minesInformationInventory.getUI().open(player);
        }));
        /*


        this.gamblingInventory.addItem(new GuiItem(rouletteBuilder.colorize().build(), event -> {
            RouletteInformationInventory rouletteInformationInventory = new RouletteInformationInventory(player);
            rouletteInformationInventory.getUI().open(player);
        }));

         */

        this.gamblingInventory.addItem(new GuiItem(coinflipBuilder.colorize().build(), event -> {
            player.chat("/cf");
        }));

        this.gamblingInventory.addItem(new GuiItem(jackpotBuilder.colorize().build(), event -> {
            player.closeInventory();
            player.chat("/jackpot");
        }));


        this.gamblingInventory.setItem(8, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit gambling menu").colorize().build();
    }
}
