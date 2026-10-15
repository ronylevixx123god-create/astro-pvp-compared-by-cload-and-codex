/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.ifamasssxd.cosmicguishop.menus;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ClickableItemStack
        extends ItemStack {
    private GUIItemCallback itemCallback;

    public ClickableItemStack(ItemStack clone) {
        super(clone);
    }

    public ClickableItemStack setClickCallback(GUIItemCallback callback) {
        this.itemCallback = callback;
        return this;
    }

    public ClickableItemStack setLore(List<String> lore) {
        ItemMeta im = this.getItemMeta();
        im.setLore(lore);
        this.setItemMeta(im);
        return this;
    }

    public ClickableItemStack setDura(short dura) {
        this.setDurability(dura);
        return this;
    }

    public GUIItemCallback getItemCallback() {
        return this.itemCallback;
    }
}

