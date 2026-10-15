/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.cosmicpvp.cosmicutils.inventory.rewards;

import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.DecimateInventory;

import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PageDirectoryInventory
        extends DecimateInventory {
    private final SerializedRewards rewards;

    public PageDirectoryInventory(SerializedRewards rewards) {
        super("Rewards Directory", 54);
        this.rewards = rewards;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for (int page : this.rewards.getPageMap().keySet()) {
            contents.set(contents.firstEmpty().get(), this.getPageItem(page));
        }
    }

    public ClickableItem getPageItem(int page) {
        ItemStack item = CommonsUtil.createItem(Material.CHEST, "&6&lPage #" + (page + 1), "&7Click to open page.", "", "&7Total Rewards: &e" + this.rewards.getRewards(page).size());
        return ClickableItem.of(item, e -> this.rewards.open((Player) e.getWhoClicked(), page));
    }
}

