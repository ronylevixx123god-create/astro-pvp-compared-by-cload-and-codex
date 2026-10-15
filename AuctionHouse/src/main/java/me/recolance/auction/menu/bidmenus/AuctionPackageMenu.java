/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.compat.ASound
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  com.google.common.collect.Lists
 *  net.minecraft.util.org.apache.commons.lang3.tuple.ImmutablePair
 *  net.minecraft.util.org.apache.commons.lang3.tuple.Pair
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.google.common.collect.Lists;
import me.recolance.auction.command.CommandHandler;
import me.recolance.auction.util.ItemUtils;
import me.recolance.auction.util.Util;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AuctionPackageMenu
        extends AuctionGUI {
    public long bidLength5 = TimeUnit.MINUTES.toSeconds(5L);
    public long bidLength10 = TimeUnit.MINUTES.toSeconds(10L);
    public long bidLength15 = TimeUnit.MINUTES.toSeconds(15L);
    public long bidLength30 = TimeUnit.MINUTES.toSeconds(30L);
    public int bidLength = (int) TimeUnit.MINUTES.toSeconds(15L);
    private List<ItemStack> toReturn = Lists.newArrayList();
    private long startingBid;
    private long maxBid;
    private long buyoutPrice;
    private Consumer<Pair<List<ItemStack>, Integer>> callback;
    private boolean confirmed = false;

    public AuctionPackageMenu(Player player, long startingBid, long incrementAmount, long buyoutPrice, ItemStack selling, Consumer<Pair<List<ItemStack>, Integer>> successCallback) {
        super(player, "Enter Auction Package Items", 54);
        this.startingBid = startingBid;
        this.maxBid = incrementAmount;
        this.buyoutPrice = buyoutPrice;
        this.callback = successCallback;
        if (selling != null && selling.getType() != Material.AIR && CommandHandler.canSellItem(selling)) {
            player.getInventory().removeItem(new ItemStack[]{selling});
            this.toReturn.add(selling);
        }
    }

    public void onInventoryClose() {
        super.onInventoryClose();
        if (!this.confirmed) {
            this.player.sendMessage(CC.Red + "Auction bid " + CC.RedB + "CANCELLED");
            if (this.toReturn.isEmpty()) {
                this.confirmed = true;
                return;
            }
            this.player.sendMessage(CC.Gray + "Your items have been returned to you.");
            this.confirmed = true;
            for (ItemStack retr : this.toReturn) {
                if (PlayerUtil.giveOrDropProtectedItem((Player) this.player, (ItemStack) retr, (int) -1)) continue;
                this.player.sendMessage(CC.Green + "No inventory space available! A protected version of your item has been dropped on the ground!");
            }
            this.toReturn.clear();
        }
    }

    public void redraw() {
        this.inventory.clear();
        this.getInventoryItems().clear();
        int slot = 0;
        for (ItemStack itemStack : this.toReturn) {
            ItemStack clone = itemStack.clone();
            this.setItem(slot++, new InventoryItem(new ItemBuilder(clone).build()).click(() -> {
                if (!this.toReturn.remove((Object) itemStack)) {
                    this.redraw();
                    Bukkit.getLogger().info("Didnt find item in toReturn: " + (Object) itemStack + " for " + this.player.getName());
                    return;
                }
                if (!PlayerUtil.giveOrDropProtectedItem((Player) this.player, (ItemStack) itemStack, (int) -1)) {
                    this.player.sendMessage(CC.Green + "No inventory space available! A protected version of your item has been dropped on the ground!");
                }
                this.player.playSound(this.player.getLocation(), Sound.LAVA_POP, 1.0f, 1.4f);
                this.redraw();
            }));
        }
        ArrayList lore = Lists.newArrayList( new String[]{"", CC.Gray + "Confirm to auction " + CC.GrayB + this.toReturn.size() + "x" + CC.Gray + " Items:"});
        for (ItemStack item3 : this.toReturn) {
            lore.add(CC.GoldB + "* " + CC.White + Util.getItemName(item3));
        }
        lore.add("");
        lore.add(CC.Gold + "Starting Bid: " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.startingBid));
        lore.add(CC.Gold + "Bid Increments: " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.maxBid));
        if (this.buyoutPrice > 1L) {
            lore.add(CC.Gold + "Buy Now Price: " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.buyoutPrice));
        }
        lore.add("");
        lore.add(CC.RedB + "NOTE: " + CC.Red + "You cannot cancel this");
        lore.add(CC.Red + "auction once it has started!");
        InventoryItem inventoryItem = new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) DyeColor.LIME.getWoolData()).name(CC.GreenB + "Confirm Auction").lore((List) lore).build()).click(() -> {
            if (this.toReturn.isEmpty()) {
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must provide at least 1 item to auction!");
                return;
            }
            if (this.confirmed) {
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Package already confirmed!");
                return;
            }
            if (!this.canClick()) {
                return;
            }
            this.click();
            this.confirmed = true;
            this.callback.accept((Pair<List<ItemStack>, Integer>) new ImmutablePair(this.toReturn, (Object) this.bidLength));
        });
        for (int index = this.size - 9; index < this.size; ++index) {
            if (index < this.size - 5) {
                this.setItem(index, inventoryItem);
                continue;
            }
            this.setItem(index, new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) DyeColor.RED.getWoolData()).name(CC.RedB + "Cancel Auction").lore(new String[]{"", CC.Gray + "Click to cancel this auction."}).build()).click(() -> {
                if (this.confirmed) {
                    return;
                }
                Util.closePlayerInventoryOnClick(this.player);
            }));
        }
        if (!this.toReturn.isEmpty()) {
            ItemStack sellingItem = this.toReturn.get(0);
            this.setItem(this.size - 5, new InventoryItem(sellingItem));
        }
        for (int i = this.size - 18; i < this.size - 9; ++i) {
            this.setItem(i, new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) DyeColor.GRAY.getWoolData()).name(" ").build()));
        }
        boolean min = (long) this.bidLength == this.bidLength5;
        boolean min2 = (long) this.bidLength == this.bidLength10;
        boolean min5 = (long) this.bidLength == this.bidLength15;
        boolean min30 = (long) this.bidLength == this.bidLength30;
        boolean maxRank = this.player.hasPermission("enchantments.max.20");
        int slots = this.size - (maxRank ? 16 : 15);
        this.setItem(slots, new InventoryItem(ItemUtils.setFakeEnchantment(new ItemBuilder(Material.WATCH).name(CC.GreenB + "5 Minute Duration").lore("", CC.Gray + "Click to set auction bid duration!").build(), min)).click(() -> {
            if (!this.canClick()) {
                return;
            }
            this.bidLength = (int) this.bidLength5;
            this.click();
            this.redraw();
        }));
        this.setItem(++slots, new InventoryItem(ItemUtils.setFakeEnchantment(new ItemBuilder(Material.WATCH).name(CC.GreenB + "10 Minute Duration").lore("", CC.Gray + "Click to set auction bid duration!").build(), min2)).click(() -> {
            if (!this.canClick()) {
                return;
            }
            this.bidLength = (int) this.bidLength10;
            this.click();
            this.redraw();
        }));
        if (maxRank) {
            ++slots;
        }
        this.setItem(++slots, new InventoryItem(ItemUtils.setFakeEnchantment(new ItemBuilder(Material.WATCH).name(CC.GreenB + "15 Minute Duration").lore("", CC.Gray + "Click to set auction bid duration!").build(), min5)).click(() -> {
            if (!this.canClick()) {
                return;
            }
            this.bidLength = (int) this.bidLength15;
            this.click();
            this.redraw();
        }));
        if (maxRank) {
            this.setItem(++slots, new InventoryItem(ItemUtils.setFakeEnchantment(new ItemBuilder(Material.WATCH).name(CC.GreenB + "30 Minute Duration").lore("", CC.Gray + "Click to set auction bid duration!").build(), min30)).click(() -> {
                if (!this.canClick()) {
                    return;
                }
                this.bidLength = (int) this.bidLength30;
                this.click();
                this.redraw();
            }));
        }
    }

    public void onUnknownItemClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) {
            return;
        }
        if (!this.canClick()) {
            return;
        }
        this.click();
        if (event.getRawSlot() >= event.getInventory().getSize()) {
            ItemStack item = event.getCurrentItem();
            if (item == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            if (!CommandHandler.canSellItem(item)) {
                this.player.sendMessage(CC.Red + "You cannot sell this item.");
                return;
            }
            if (this.toReturn.size() >= 5) {
                this.player.sendMessage((Object) ChatColor.RED + "You can only sell up to 5 items in a single Auction Package!");
                return;
            }
            event.getClickedInventory().setItem(event.getSlot(), null);
            this.toReturn.add(item);
            this.player.sendMessage(CC.GreenB + "(!) " + CC.Green + "Item added into Auction Package!");
            ASound.ORB_PICKUP.play(this.player, 1.4f);
            this.redraw();
        }
    }
}

