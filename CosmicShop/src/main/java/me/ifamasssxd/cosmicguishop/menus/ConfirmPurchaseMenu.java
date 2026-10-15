/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.ifamasssxd.cosmicguishop.menus;

import me.ifamasssxd.cosmicguishop.struct.ShopManager;
import me.ifamasssxd.cosmicguishop.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class ConfirmPurchaseMenu
        extends GUIMenu {
    public ConfirmPurchaseMenu(String title, int size, ItemStack purchasing, String name, int buying, double cost, final Consumer<Boolean> acceptOrDenyCallback) {
        super(title, size);
        int i;
        ClickableItemStack acceptItem = new ClickableItemStack(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short) DyeColor.LIME.getWoolData(), (Object) ChatColor.GREEN + "Confirm Purchase", (Object) ChatColor.GRAY + "Confirm purchase of ", ChatColor.GREEN.toString() + (Object) ChatColor.BOLD + buying + "x " + (Object) ChatColor.GRAY + name + " for " + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + "$" + ShopManager.moneyFormat.format(cost))).setClickCallback(new GUIItemCallback() {

            @Override
            public void onClick(InventoryClickEvent event) {
                acceptOrDenyCallback.accept(true);
            }
        });
        ClickableItemStack denyItem = new ClickableItemStack(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short) DyeColor.RED.getWoolData(), (Object) ChatColor.RED + "Deny Purchase", (Object) ChatColor.GRAY + "Click to deny and cancel purchase.")).setClickCallback(new GUIItemCallback() {

            @Override
            public void onClick(InventoryClickEvent event) {
                acceptOrDenyCallback.accept(false);
            }
        });
        for (i = 0; i < 4; ++i) {
            this.setItem(i, acceptItem);
        }
        for (i = 5; i < this.getSize(); ++i) {
            this.setItem(i, denyItem);
        }
        ItemMeta im = purchasing.getItemMeta();
        im.setDisplayName((Object) ChatColor.AQUA + name + " x " + buying);
        purchasing.setItemMeta(im);
        this.setItem(4, new ClickableItemStack(purchasing));
    }
}

