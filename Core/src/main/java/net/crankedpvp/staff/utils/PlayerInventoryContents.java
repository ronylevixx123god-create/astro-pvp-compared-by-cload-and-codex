package net.crankedpvp.staff.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class PlayerInventoryContents {

    public ItemStack[] inventoryContents;
    public ItemStack[] armorContents;

    public PlayerInventoryContents(ItemStack[] inventoryContents, ItemStack[] armorContents) {
        this.inventoryContents = inventoryContents.clone();
        this.armorContents = armorContents.clone();
    }

    public void restoreInventory(Player player) {
        player.getInventory().setContents(this.inventoryContents);
    }

    public void restoreArmor(Player player) {
        player.getInventory().setArmorContents(this.armorContents);
    }

    public void restore(Player player) {
        restoreInventory(player);
        restoreArmor(player);
        player.updateInventory();
    }

}