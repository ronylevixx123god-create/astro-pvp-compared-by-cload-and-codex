package net.crankedpvp.hypeboxes.utils;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public final class InventoryUtils {

    private InventoryUtils() {

    }

    public static boolean hasAvailableSlot(Player player) {
        return player.getInventory().firstEmpty() != -1;
    }

    public static boolean hasAvaliableSlot(Player player, int amount) {
        int amountOpen = 0;
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if(item == null) {
                amountOpen += 1;
            }
        }
        return amountOpen >= amount;
    }

    public static boolean canPutItemsInInventory(Player player, List<ItemStack> items) {
        if(hasAvaliableSlot(player, 1)) return true;
        int amountOpen = 0;
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if(item == null) {
                amountOpen += 1;
            }

            ItemStack itemInInventoryClone = item.clone();

            for(ItemStack itemStack : items) {
                ItemStack itemToSearchClone = itemStack.clone();
                itemToSearchClone.setAmount(1);
                if(itemInInventoryClone == itemToSearchClone && ((item.getAmount() + itemStack.getAmount()) <= 64)) {
                    amountOpen++;
                }
            }
        }
        return amountOpen >= items.size();

    }

    public static void giveItemToPlayer(Player player, ItemStack itemStack) {
        if (!InventoryUtils.hasAvailableSlot(player)) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }

    public static int fitSize(int size) {
        return size <= 9 ? 9 : (size <= 18 ? 18 : (size <= 27 ? 27 : (size <= 36 ? 36 : (size <= 45 ? 45 : (54)))));
    }

    public static void giveItemToPlayer(Player player, List<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            giveItemToPlayer(player, itemStack);
        }
    }

    public static void giveItemToPlayer(Player player, ItemStack[] itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            giveItemToPlayer(player, itemStack);
        }
    }

    public static boolean isEmptyInventory(Player player) {
        if(isWearingArmor(player)) return false;

        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if(!ItemUtils.isAirOrNull(item)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isWearingArmor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        if(playerInventory.getHelmet() != null) return true;
        if(playerInventory.getChestplate() != null) return true;
        if(playerInventory.getLeggings() != null) return true;
        return playerInventory.getBoots() != null;
    }



}
