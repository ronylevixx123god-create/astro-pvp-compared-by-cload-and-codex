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

public class YijkiStaffBuilder
        extends ItemBuilder {
    public YijkiStaffBuilder() {
        super(Material.BLAZE_ROD);
    }

    @Override
    public ItemStack build() {
        ItemStack i = ItemUtils.buildItem((Material) this.getType(), (short) 0, (String) ChatColor.translateAlternateColorCodes('&', "&f&lBattlestaff of Yijki &7(Right Click)"), (String[]) new String[]{(Object) ((Object) ChatColor.GRAY) + "An evil nether bark staff", (Object) ((Object) ChatColor.GRAY) + "imbued with the essence of Yijki.", "", ChatColor.translateAlternateColorCodes('&', "&8&nEffect:&f REVENGE OF YIJKI"), ChatColor.translateAlternateColorCodes('&', "&8&nSoul Cost:&f 375/use"), ChatColor.translateAlternateColorCodes('&', "&8&nCooldown:&f 120s")});
        NBTWrapper wrapper = new NBTWrapper(i);
        wrapper.setString("cosmicType", "yijkiStaff");
        i = wrapper.build();
        return i;
    }
}

