package net.crankedpvp.pvptop.inventory;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.crankedpvp.pvptop.data.FactionData;
import net.crankedpvp.pvptop.utils.GuiUtil;
import net.crankedpvp.pvptop.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class ObtainPointsMethodsInventory {

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final Gui obtainPointsMethodInventory;

    public Gui getUI() {
        return this.obtainPointsMethodInventory;
    }

    public ObtainPointsMethodsInventory() {
        this.obtainPointsMethodInventory = new Gui(4, "Obtain Points Methods");
        this.obtainPointsMethodInventory.setDefaultClickAction(event -> event.setCancelled(true));

        this.obtainPointsMethodInventory.getFiller().fillBorder(GuiUtil.createFillerGuiItem());

        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildKillsItemStack()));
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildDeathsItemStack()));
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildKothsCapturedItemStack()));
        /*
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildDeadmansChestCapturedItemStack()));
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildWarpKothsCapturedItemStack()));
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildCitadelsCapturedItemStack()));
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildCerberusKillItemStack()));
        this.obtainPointsMethodInventory.addItem(new GuiItem(this.buildStrongholdControlledItemStack()));

         */
    }


    private ItemStack buildKillsItemStack() {
        return new ItemBuilder(Material.GOLD_SWORD, "&aPlayer Kills",
                "&f+" + FactionData.Points.KILL.getPoints() + " &bFaction Point"
        ).colorize().build();
    }

    private ItemStack buildKothsCapturedItemStack() {
        return new ItemBuilder(Material.WOOL, 1, DyeColor.RED.getWoolData(), "&aKOTH Capture",
                "&f+" + FactionData.Points.KOTH_CAPTURE.getPoints() + " &bFaction Points"
        ).colorize().build();
    }

    private ItemStack buildDeathsItemStack() {
        return new ItemBuilder(Material.INK_SACK, 1, DyeColor.RED.getDyeData(), "&cPlayer Deaths",
                "&f-" + FactionData.Points.DEATH.getPoints() + " &bFaction Point"
        ).colorize().build();
    }

    /*
    private ItemStack buildDeadmansChestCapturedItemStack() {
        return new ItemBuilder(Material.ENDER_CHEST, "&aDead Mans Chest Capture",
                "&f+" + FactionData.Points.DEADMANS_CHEST.getPoints() + " &bFaction Points").colorize().build();
    }

    private ItemStack buildWarpKothsCapturedItemStack() {
        return new ItemBuilder(Material.ENDER_PORTAL_FRAME, "&aWarp KOTH Capture",
                "&f+" + FactionData.Points.WARP_KOTH_CAPTURE.getPoints() + " &bFaction Points"
        ).colorize().build();
    }

    private ItemStack buildCitadelsCapturedItemStack() {
        return new ItemBuilder(Material.BEACON, "&aCitadel Capture",
                "&f+" + FactionData.Points.CITADEL_CAPTURE.getPoints() + " &bFaction Points"
        ).colorize().build();
    }

    private ItemStack buildStrongholdControlledItemStack() {
        return new ItemBuilder(Material.BLAZE_POWDER, "&aStronghold Controlled &7(30 Minutes)",
                "&f+" + FactionData.Points.STRONHOLD_CONTROL.getPoints() + " &bFaction Points"
        ).colorize().build();
    }

    private ItemStack buildCerberusKillItemStack() {
        return new ItemBuilder(Material.MONSTER_EGG, "&aCerberus Kills",
                "&f+" + FactionData.Points.CERBERUS_KILLED.getPoints() + " &bFaction Points"
        ).colorize().build();
    }

     */
}
