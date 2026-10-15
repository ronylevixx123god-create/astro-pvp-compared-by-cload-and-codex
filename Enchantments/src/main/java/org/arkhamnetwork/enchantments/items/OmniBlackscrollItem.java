package org.arkhamnetwork.enchantments.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OmniBlackscrollItem {
    private ItemStack item;

    private int slot;

    private Player player;

    public OmniBlackscrollItem(ItemStack item, int slot, Player player) {
        this.item = item;
        this.slot = slot;
        this.player = player;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }

    public Player getPlayer() {
        return this.player;
    }
}
