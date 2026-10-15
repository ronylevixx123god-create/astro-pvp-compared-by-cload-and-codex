package com.conaxgames.customskulls.ui;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.ItemBuilderUtils;
import com.conaxgames.customskulls.util.inventoryui.InventoryUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ViewCustomBlocksFromFileUI {
    public static void open(Player player) {

        InventoryUI inventoryUI = new InventoryUI("Custom Blocks", 6);
        for (String key : AbilityData.getAbilityDataMap().keySet()) {
            if (key == null) continue;
            if (AbilityData.getByUUID(key) == null) continue;
            AbilityData data = AbilityData.getByUUID(key);
            if (data == null) continue;
            if (data.getAbilityType() == null) continue;
            if (CoreSkulls.customBlocks.get(data.getAbilityType().getName()) == null) continue;
            CustomBlock customBlock = CoreSkulls.customBlocks.get(data.getAbilityType().getName());
            inventoryUI.addItem(new InventoryUI.AbstractClickableItem(
                    new ItemBuilderUtils(customBlock.getItem())
                            .setName(CC.YELLOW + data.getAbilityType().getName())
                            .setDurability((short) 3)
                            .addLoreLine("")
                            .addLoreLine(CC.GOLD + "UUID: " + CC.YELLOW + data.getUuid())
                            .addLoreLine(CC.GOLD + "Owner: " + CC.YELLOW + data.getOwner())
                            .addLoreLine(CC.GOLD + "Type: " + CC.YELLOW + data.getAbilityType())
                            .addLoreLine(CC.GOLD + "Location: ")
                            .addLoreLine(CC.GOLD + " " + CC.YELLOW + data.getLocation().getBlockX())
                            .addLoreLine(CC.GOLD + " " + CC.YELLOW + data.getLocation().getBlockY())
                            .addLoreLine(CC.GOLD + " " + CC.YELLOW + data.getLocation().getBlockZ())
                            .addLoreLine("")
                            .addLoreLine(CC.AQUA + "Click to teleport to this location.")
                            .toItemStack()) {
                @Override
                public void onClick(InventoryClickEvent event) {
                    player.teleport(data.getLocation());
                    player.sendMessage(CC.GOLD + "Teleported you to " + AbilityData.getUUID(data.getLocation()));
                }
            });
        }
        player.openInventory(inventoryUI.getCurrentPage());
    }
}
