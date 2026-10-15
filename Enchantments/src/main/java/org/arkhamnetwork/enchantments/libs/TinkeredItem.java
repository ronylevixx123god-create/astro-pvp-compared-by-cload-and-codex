package org.arkhamnetwork.enchantments.libs;

import org.bukkit.inventory.ItemStack;

public class TinkeredItem {
    private ItemStack item;
    private double xpValue;
    private double expModifier;
    private ItemStack offerForItem;

    public TinkeredItem(final ItemStack item, final double xpValue, final double expModifier, final ItemStack offerForItem) {
        this.item = item;
        this.xpValue = xpValue;
        this.expModifier = expModifier;
        this.offerForItem = offerForItem;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(final ItemStack item) {
        this.item = item;
    }

    public double getXpValue() {
        return this.xpValue;
    }

    public void setXpValue(final double xpValue) {
        this.xpValue = xpValue;
    }

    public double getExpModifier() {
        return this.expModifier;
    }

    public void setExpModifier(final double expModifier) {
        this.expModifier = expModifier;
    }

    public ItemStack getOfferForItem() {
        return this.offerForItem;
    }

    public void setOfferForItem(final ItemStack offerForItem) {
        this.offerForItem = offerForItem;
    }
}
