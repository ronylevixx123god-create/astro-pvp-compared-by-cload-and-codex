package net.astropvp.astroevents.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public class PlayerInventoryContents {

    public ItemStack[] inventoryContents;
    public ItemStack[] armorContents;

    public void restoreInventory(Player player) {
        player.getInventory().setContents(inventoryContents);
    }

    public void restoreArmor(Player player) {
        player.getInventory().setArmorContents(armorContents);
    }

    public void restore(Player player) {
        restoreInventory(player);
        restoreArmor(player);
        player.updateInventory();
    }

}
