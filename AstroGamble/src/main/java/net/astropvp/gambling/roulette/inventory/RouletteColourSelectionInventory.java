package net.astropvp.gambling.roulette.inventory;

import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import  net.astropvp.gambling.roulette.struct.RouletteColour;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RouletteColourSelectionInventory {


    private final Gui mineInventory;

    public Gui getUI() {
        return this.mineInventory;
    }


    public RouletteColourSelectionInventory(Player player, double bet) {
        this.mineInventory = new Gui(GuiType.HOPPER, "Roulette Colour Selection");
        this.mineInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemBuilder blackBuilder = new ItemBuilder(Material.WOOL, 1, (short) 15,
                "&f&lBLACK",
                "&7CLick to select black",
                "");
        ItemBuilder redBuilder = new ItemBuilder(Material.WOOL, 1, (short) 14,
                "&f&lRED",
                "&7CLick to select red",
                "");
        ItemBuilder greenBuilder = new ItemBuilder(Material.WOOL, 1, (short) 5,
                "&f&lGREEN",
                "&7CLick to select green",
                "");
        this.mineInventory.setItem(1, new GuiItem(blackBuilder.colorize().build(), event -> openGameInv(player, bet, RouletteColour.BLACK)));
        this.mineInventory.setItem(2, new GuiItem(greenBuilder.colorize().build(), event -> openGameInv(player, bet, RouletteColour.GREEN)));
        this.mineInventory.setItem(3, new GuiItem(redBuilder.colorize().build(), event -> openGameInv(player, bet, RouletteColour.RED)));
        this.mineInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.mineInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private void openGameInv(Player player, double bet, RouletteColour rouletteColour) {
        if(! EconomyUtils.hasBalance(player, bet)) player.closeInventory();
        RouletteGameInventory rouletteGameInventory = new RouletteGameInventory(player, rouletteColour);
        rouletteGameInventory.getUI().open(player);
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit roulette menu").colorize().build();
    }
}
