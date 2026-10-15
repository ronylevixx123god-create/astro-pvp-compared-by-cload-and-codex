package com.massivecraft.factions.gui;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class FDisbandConfirm
        extends CosmicGUI {
    private final Faction faction;

    public FDisbandConfirm(Player player, Faction faction) {
        super(player, "Confirm /f disband", 9);
        this.faction = faction;
    }

    public void redraw() {
        int i;
        InventoryItem item = new InventoryItem(new ItemBuilder(new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) DyeColor.LIME.getWoolData())).name(CC.GreenB + "Confirm").lore("", CC.Gray + "Confirm disbanding of " + CC.GreenB + this.faction.getTag() + CC.Gray + "!").build()).click(() -> {
            this.player.closeInventory();
            this.player.setMetadata("disband_confirm", new FixedMetadataValue(FactionsPlugin.getInstance(), System.currentTimeMillis()));
            this.player.performCommand("f disband");
        });
        for (i = 0; i < 4; ++i) {
            this.setItem(i, item);
        }
        this.setItem(4, new ItemBuilder(Material.getMaterial("STAINED_GLASS_PANE")).data((short) DyeColor.BLACK.getWoolData()).name(" ").build(), null);
        for (i = 5; i < this.size; ++i) {
            this.setItem(i, new ItemBuilder(Material.getMaterial("STAINED_GLASS_PANE"), CC.RedB + "Cancel", "", CC.Gray + "Click to cancel faction disband.").data((short) DyeColor.RED.getWoolData()).build(), () -> {
                this.player.closeInventory();
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Disband cancelled.");
            });
        }
    }
}

