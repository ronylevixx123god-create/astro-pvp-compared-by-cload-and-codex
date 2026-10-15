/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.cosmicpvp.cosmicutils.inventory.rewards;

import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.DecimateInventory;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;

import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.serialization.Base64Serialization;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Optional;

public class RewardsPageInventory
        extends DecimateInventory {
    private final SerializedRewards rewards;
    private final int page;

    public RewardsPageInventory(SerializedRewards rewards, int page) {
        super("Rewards Page #" + (page + 1), 54);
        this.rewards = rewards;
        this.page = page;
        this.clickable = true;
        this.listener = Optional.of(new RewardsSaveListener(rewards, page));
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack nextPage = CommonsUtil.createItem(Material.WOOL, "&a&lNext Page", 1, 5, "&7Click here to go the next page.");
        ItemStack backPage = CommonsUtil.createItem(Material.WOOL, "&c&lPrevious Page", 1, 14, "&7Click here to go the previous page.");
        ItemStack dirPage = CommonsUtil.createItem(Material.CHEST, "&6&lOpen Directory", "&7Click here to browse pages.");
        ItemStack newPage = CommonsUtil.createItem(Material.PAPER, "&e&lNew Page", "&7Click here to create a new page.");
        ItemStack delPage = CommonsUtil.createItem(Material.BARRIER, "&4&lDelete Page", "&7Click here to delete this page.");
        contents.set(45, ClickableItem.of(backPage, event -> {
            event.setCancelled(true);
            int destinationPage = this.page - 1;
            if (destinationPage < 0) {
                CommonsUtil.sendMessage(event.getWhoClicked(), "&c&l(!)&c You've reached the first page.");
                return;
            }
            new RewardsPageInventory(this.rewards, destinationPage).open((Player) event.getWhoClicked());
        }));
        contents.set(53, ClickableItem.of(nextPage, event -> {
            event.setCancelled(true);
            int destinationPage = this.page + 1;
            if (destinationPage > this.rewards.getPageMap().size() - 1) {
                CommonsUtil.sendMessage(event.getWhoClicked(), "&c&l(!)&c You've reached the last page.");
                return;
            }
            new RewardsPageInventory(this.rewards, destinationPage).open((Player) event.getWhoClicked());
        }));
        contents.set(49, ClickableItem.of(dirPage, event -> {
            event.setCancelled(true);
            new PageDirectoryInventory(this.rewards).open(player);
        }));
        contents.set(50, ClickableItem.of(newPage, event -> {
            int highestPage = Collections.max(this.rewards.getPageMap().keySet());
            event.setCancelled(true);
            this.rewards.getPageMap().put(highestPage + 1, "");
            CommonsUtil.sendMessage(event.getWhoClicked(), "&a&l(!)&a You've created a new page &f#" + (highestPage + 1));
        }));
        contents.set(48, ClickableItem.of(delPage, event -> {
            if (this.page == 0) {
                CommonsUtil.sendMessage(event.getWhoClicked(), "&cThis page cannot be deleted.");
                return;
            }
            player.closeInventory();
            event.setCancelled(true);
            this.rewards.getConfig().set("pages." + this.page, null);
            this.rewards.getPageMap().remove(this.page);
            CommonsUtil.sendMessage(event.getWhoClicked(), "&a&l(!)&a You've deleted page &f#" + (this.page + 1));
        }));
        if (!this.rewards.getPageData(this.page).equalsIgnoreCase("")) {
            ItemStack[] rewardContents;
            for (ItemStack item : rewardContents = Base64Serialization.arrayFromBase64(this.rewards.getPageMap().get(this.page))) {
                contents.set(contents.firstEmpty().get(), ClickableItem.empty(item));
            }
        }
    }

    public class RewardsSaveListener
            extends InventoryListener<InventoryCloseEvent> {
        public RewardsSaveListener(SerializedRewards rewards, int page) {
            super(InventoryCloseEvent.class, event -> rewards.getPageMap().put(page, Base64Serialization.toBase64(Lists.newArrayList(event.getInventory().getContents()).subList(0, 44).toArray(new ItemStack[44]))));
        }
    }
}

