package com.conaxgames.customskulls.ui;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.ItemBuilderUtils;
import com.conaxgames.customskulls.util.inventoryui.InventoryUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class CustomBlocksAdminUI {
    public static void open(Player player) {

        InventoryUI inventoryUI = new InventoryUI("Astro Custom Blocks", 1);
        for (Map.Entry<String, CustomBlock> customBlock : CoreSkulls.customBlocks.entrySet()) {
            inventoryUI.addItem(new InventoryUI.AbstractClickableItem(
                    new ItemBuilderUtils(customBlock.getValue().getItem()).setLore(customBlock.getValue().addLore())
                            .toItemStack()) {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (player.hasPermission("customblocks.admin")) {
                        giveItem(player, customBlock.getValue(), true);
                    }
                }
            });
        }
        player.openInventory(inventoryUI.getCurrentPage());
    }

    public static void giveItem(Player player, CustomBlock customBlock, boolean msg) {
        ItemBuilderUtils item = new ItemBuilderUtils(customBlock.getItem()).setLore(customBlock.addLore());
        player.getInventory().addItem(item.toItemStack());
        if (msg) { player.sendMessage(CC.B_GREEN + "+ " + customBlock.getName()); }
    }
}
