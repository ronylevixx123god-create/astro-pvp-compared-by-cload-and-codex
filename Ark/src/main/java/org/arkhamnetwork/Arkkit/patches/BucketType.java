/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.Material;

enum BucketType {
    WATER(Material.WATER_BUCKET, "Magic Water Bucket", ChatColor.AQUA),
    LAVA(Material.LAVA_BUCKET, "Magic Lava Bucket", ChatColor.RED);

    public Material m;
    public String itemPrefix;
    public ChatColor color;

    BucketType(Material m, String name, ChatColor cc) {
        this.m = m;
        this.itemPrefix = name;
        this.color = cc;
    }

    public String getItemPrefix() {
        return this.color + "" + ChatColor.BOLD + this.itemPrefix;
    }
}

