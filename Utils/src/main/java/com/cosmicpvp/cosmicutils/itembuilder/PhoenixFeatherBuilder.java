/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PhoenixFeatherBuilder
        extends ItemBuilder {
    public PhoenixFeatherBuilder() {
        super(Material.FEATHER);
    }

    @Override
    public ItemStack build() {
        ItemStack i = ItemUtils.buildItem((Material) this.getType(), (short) 0, (String) ChatColor.translateAlternateColorCodes('&', "&c&lPhoenix Feather &7(1 use)"), (String[]) new String[]{(Object) ((Object) ChatColor.GRAY) + "An extremely rare consumable", (Object) ((Object) ChatColor.GRAY) + "that contains the power of " + (Object) ((Object) ChatColor.RED) + (Object) ((Object) ChatColor.BOLD) + "Phoenix III.", "", (Object) ((Object) ChatColor.GRAY) + "An attack that would normally kill you", (Object) ((Object) ChatColor.GRAY) + "will heal you to full HP instead, and this", (Object) ((Object) ChatColor.GRAY) + "item will be automatically destroyed as a result.", "", (Object) ((Object) ChatColor.GRAY) + "This trinket will only attempt to activate", (Object) ((Object) ChatColor.GRAY) + "if your Phoenix is on cooldown, or if you", (Object) ((Object) ChatColor.GRAY) + "do not have the Phoenix enchantment equipped."});
        NBTWrapper wrapper = new NBTWrapper(i);
        wrapper.setString("cosmicType", "phoenixFeather");
        i = wrapper.build();
        return i;
    }
}

