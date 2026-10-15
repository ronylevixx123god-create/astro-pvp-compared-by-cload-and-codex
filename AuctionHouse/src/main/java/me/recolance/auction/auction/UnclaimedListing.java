/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.recolance.auction.auction;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class UnclaimedListing {
    private UUID id;
    private UUID listedBy;
    private ItemStack item;
    private long expireTime;

    public UnclaimedListing(UUID id, UUID listedBy, ItemStack item, long expireTime) {
        this.id = id;
        this.listedBy = listedBy;
        this.item = item;
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        return this.expireTime <= System.currentTimeMillis();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getListedBy() {
        return this.listedBy;
    }

    public void setListedBy(UUID listedBy) {
        this.listedBy = listedBy;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}

