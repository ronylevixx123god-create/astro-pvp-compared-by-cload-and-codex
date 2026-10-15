package net.astropvp.core.conaxgames.staff.staffmode;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class PlayerInventoryContents {

    public ItemStack[] inventoryContents;
    public ItemStack[] armorContents;

    public PlayerInventoryContents(Player player) {
        this.inventoryContents = player.getInventory().getContents().clone();
        this.armorContents = player.getInventory().getArmorContents().clone();
    }

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
