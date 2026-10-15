package net.crankedpvp.pvptop.inventory;

import com.massivecraft.factions.Faction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.crankedpvp.pvptop.PvPTopPlugin;
import net.crankedpvp.pvptop.data.FactionData;
import net.crankedpvp.pvptop.utils.GuiUtil;
import net.crankedpvp.pvptop.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class FactionPointsBreakdownInventory {

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final Faction faction;
    private final FactionData factionData;


    private final Gui pointsBreakdownInventory;

    public Gui getUI() {
        return this.pointsBreakdownInventory;
    }

    public FactionPointsBreakdownInventory(Faction faction) {
        this.faction = faction;
        this.factionData = PvPTopPlugin.getInstance().getPvpTopManager().getOrCreate(faction);
        this.pointsBreakdownInventory = new Gui(4, faction.getTag() + " Points Breakdown");
        this.pointsBreakdownInventory.setDefaultClickAction(event -> event.setCancelled(true));

        this.pointsBreakdownInventory.getFiller().fillBorder(GuiUtil.createFillerGuiItem());

        ItemStack info = new ItemBuilder(Material.BOOK, "&f" + faction.getTag())
                .setLore(
                        "",
                        "&b&lInformation",
                        " &f• Leader: &3" + faction.getLeader().getName(),
                        "",
                        "&b&lStatistics",
                        " &f• Faction Points: &3" + moneyFormat.format(factionData.calculateAllPoints())
                )
        .colorize().build();

        this.pointsBreakdownInventory.setItem(1, 5, new GuiItem(info));

        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildKillsItemStack()));
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildDeathsItemStack()));
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildKothsCapturedItemStack()));
        /*
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildDeadmansChestCapturedItemStack()));
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildWarpKothsCapturedItemStack()));
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildCitadelsCapturedItemStack()));
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildCerberusKillItemStack()));
        this.pointsBreakdownInventory.addItem(new GuiItem(this.buildStrongholdControlledItemStack()));

         */
    }


    private ItemStack buildKillsItemStack() {
        return new ItemBuilder(Material.GOLD_SWORD, "&aPlayer Kills",
                "&f+" + FactionData.Points.KILL.getPoints() + " &bFaction Point",
                "",
                " &f• Total: &e" + this.factionData.getKills(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getKills() * FactionData.Points.KILL.getPoints()) + "&8)"
        ).colorize().build();
    }

    private ItemStack buildKothsCapturedItemStack() {
        return new ItemBuilder(Material.WOOL, 1, DyeColor.RED.getWoolData(), "&aKOTH Capture",
                "&f+" + FactionData.Points.KOTH_CAPTURE.getPoints() + " &bFaction Points",
                "",
                " &f• Total: &e" + this.factionData.getKothCaptured(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getKothCaptured() * FactionData.Points.KOTH_CAPTURE.getPoints()) + "&8)"
        ).colorize().build();
    }

    private ItemStack buildDeathsItemStack() {
        return new ItemBuilder(Material.INK_SACK, 1, DyeColor.RED.getDyeData(), "&cPlayer Deaths",
                "&f-" + FactionData.Points.DEATH.getPoints() + " &bFaction Point",
                "",
                " &f• Total: &e" + this.factionData.getDeaths(),
                " &f• Points Gained: &8(&c-" + (this.factionData.getDeaths() * FactionData.Points.DEATH.getPoints()) + "&8)"
        ).colorize().build();
    }

    /*

    private ItemStack buildWarpKothsCapturedItemStack() {
        return new ItemBuilder(Material.ENDER_PORTAL_FRAME, "&aWarp KOTH Capture",
                "&f+" + FactionData.Points.WARP_KOTH_CAPTURE.getPoints() + " &bFaction Points",
                "",
                " &f• Total: &e" + this.factionData.getWarpKothCaptured(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getWarpKothCaptured() * FactionData.Points.WARP_KOTH_CAPTURE.getPoints()) + "&8)"
        ).colorize().build();
    }

    private ItemStack buildCitadelsCapturedItemStack() {
        return new ItemBuilder(Material.BEACON, "&aCitadel Capture",
                "&f+" + FactionData.Points.CITADEL_CAPTURE.getPoints() + " &bFaction Points",
                "",
                " &f• Total: &e" + this.factionData.getCitadelCaptured(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getCitadelCaptured() * FactionData.Points.CITADEL_CAPTURE.getPoints()) + "&8)"
        ).colorize().build();
    }

    private ItemStack buildDeadmansChestCapturedItemStack() {
        return new ItemBuilder(Material.ENDER_CHEST, "&aDead Mans Chest Capture",
                "&f+" + FactionData.Points.DEADMANS_CHEST.getPoints() + " &bFaction Points",
                "",
                " &f• Total: &e" + this.factionData.getDeadmansChestCaptures(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getDeadmansChestCaptures() * FactionData.Points.DEADMANS_CHEST.getPoints()) + "&8)"
        ).colorize().build();
    }

    private ItemStack buildStrongholdControlledItemStack() {
        return new ItemBuilder(Material.BLAZE_POWDER, "&aStronghold Controlled &7(30 Minutes)",
                "&f+" + FactionData.Points.STRONHOLD_CONTROL.getPoints() + " &bFaction Points",
                "",
                " &f• Total: &e" + this.factionData.getStrongholdControlled(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getStrongholdControlled() * FactionData.Points.STRONHOLD_CONTROL.getPoints()) + "&8)"
        ).colorize().build();
    }

    private ItemStack buildCerberusKillItemStack() {
        return new ItemBuilder(Material.MONSTER_EGG, "&aCerberus Kills",
                "&f+" + FactionData.Points.CERBERUS_KILLED.getPoints() + " &bFaction Points",
                "",
                " &f• Total: &e" + this.factionData.getCerberusKills(),
                " &f• Points Gained: &8(&a+" + (this.factionData.getCerberusKills() * FactionData.Points.CERBERUS_KILLED.getPoints()) + "&8)"
        ).colorize().build();
    }

     */
}
