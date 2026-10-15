/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class ShowcaseExpander
        extends ArkPlugin {
    public ShowcaseExpander(Plugin p) {
        super(p);
    }

    public static ItemStack getShowcaseExpanderItem() {
        return ItemUtils.buildItem(Material.PAINTING, (short) 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Showcase Expansion " + ChatColor.GRAY + "(Right Click)", ChatColor.GRAY + "Adds " + ChatColor.YELLOW + "+9 slots" + ChatColor.GRAY + " to your " + ChatColor.UNDERLINE + "/showcase");
    }

    public static boolean isShowcaseExpanderItem(ItemStack i) {
        return i != null && i.getType() == Material.PAINTING && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().get(0).equals(ShowcaseExpander.getShowcaseExpanderItem().getItemMeta().getLore().get(0));
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandShowcaseExpander("/showcaseexpander", "/<command>", "Root command for showcase expanders.", Arrays.asList("sce")));
        this.registerListener(new ShowcaseExpanderListener());
    }

    @Override
    public void disable() {
    }
}

