/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.InventoryHolder
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.struct;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DuelHistoryManager {
    private HashMap<UUID, Inventory> previousInventories = new HashMap();

    public Inventory getStoredInventory(UUID uuid) {
        return this.previousInventories.get(uuid);
    }

    public UUID storeInventorySnapshot(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)54, (String)("Duel Items: " + player.getName()));
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                item = item.clone();
            }
            inventory.setItem(i + 9, item);
        }
        int index = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null) {
                inventory.setItem(index, armor.clone());
            }
            ++index;
        }
        UUID uuid = UUID.randomUUID();
        this.previousInventories.put(uuid, inventory);
        return uuid;
    }

    public HashMap<UUID, Inventory> getPreviousInventories() {
        return this.previousInventories;
    }
}

