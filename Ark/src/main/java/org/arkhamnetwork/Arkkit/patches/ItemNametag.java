/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemNametag
        extends ArkPlugin {
    public static String pending_itemtag_metadata = "pending_itemtag";
    public static String itemtag_confirm_metadata = "itemNameTag_confirmation";
    private static ItemStack pickaxe_nametag = null;

    public ItemNametag(Plugin p) {
        super(p);
    }

    public static boolean isNametagItem(ItemStack i) {
        return i.isSimilar(ItemNametag.getNametagItem());
    }

    public static ItemStack getNametagItem() {
        if (pickaxe_nametag == null) {
            pickaxe_nametag = ItemUtils.buildItem(Material.NAME_TAG, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Item Nametag " + ChatColor.GRAY + "(Right Click)", ChatColor.GRAY + "Rename and customize your equipment.");
        }
        return pickaxe_nametag.clone();
    }

    @Override
    public void enable() {
        this.registerListener(new ItemNametagListener());
        this.registerCommand(new CommandGiveItemNametag("giveitemtag", "/<command>", "Spawn item rename tag."));
    }

    @Override
    public void disable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.hasMetadata(pending_itemtag_metadata)) continue;
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(ItemNametag.getNametagItem());
            }
            pl.removeMetadata(pending_itemtag_metadata, Arkkit.get());
        }
    }
}

