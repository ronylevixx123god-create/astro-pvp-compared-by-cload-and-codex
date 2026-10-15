package net.astropvp.gambling.tower.inventory;

import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import  net.astropvp.gambling.tower.enums.DifficultyType;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TowerDifficultySelectionInventory {

    private final Gui towerDifficultySelectionInventory;

    public Gui getUI() {
        return this.towerDifficultySelectionInventory;
    }


    public TowerDifficultySelectionInventory(Player player, double bet) {
        this.towerDifficultySelectionInventory = new Gui(GuiType.HOPPER, "Astro Towers Difficulty");
        this.towerDifficultySelectionInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemBuilder easyBuilder = new ItemBuilder(Material.WOOL, 1, (short) 5,
                "&a&lEASY",
                "&7There are three towers in easy mode",
                "&7Only one row of the tower is bad");
        ItemBuilder mediumBuilder = new ItemBuilder(Material.WOOL, 1, (short) 4,
                "&e&lMEDIUM",
                "&7There are two towers in medium mode",
                "&7One row is good, the other is bad");
        ItemBuilder hardBuilder = new ItemBuilder(Material.WOOL, 1, (short) 14,
                "&c&lHARD",
                "&7There are three towers in hard mode",
                "&7Two of the rows of the tower are bad");
        this.towerDifficultySelectionInventory.setItem(1, new GuiItem(easyBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            TowerGameInventory towerGameInventory = new TowerGameInventory(player, DifficultyType.EASY, bet);
            towerGameInventory.getUI().open(player);
        }));
        this.towerDifficultySelectionInventory.setItem(2, new GuiItem(mediumBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            TowerGameInventory towerGameInventory = new TowerGameInventory(player, DifficultyType.MEDIUM, bet);
            towerGameInventory.getUI().open(player);
        }));
        this.towerDifficultySelectionInventory.setItem(3, new GuiItem(hardBuilder.colorize().build(), event -> {
            if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
            TowerGameInventory towerGameInventory = new TowerGameInventory(player, DifficultyType.HARD, bet);
            towerGameInventory.getUI().open(player);
        }));
        this.towerDifficultySelectionInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.towerDifficultySelectionInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit towers menu").colorize().build();
    }
}
