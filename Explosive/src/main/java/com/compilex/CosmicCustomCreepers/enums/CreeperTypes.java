/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.compilex.CosmicCustomCreepers.enums;

import com.compilex.CosmicCustomCreepers.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CreeperTypes {
    Huge(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Gigantic Creeper", ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Explodes in a much larger radius."),
    Raid(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Raid Creeper", ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "100% chance of spawner drops."),
    Lucky(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Lucky Creeper", ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Increases chances of spawner drops.");
    String name;
    String[] lore;

    private CreeperTypes(String name, String ... lore) {
        this.name = name;
        this.lore = lore;
    }

    public String getItemName() {
        return this.name.replace(ChatColor.BOLD.toString(), "");
    }

    public String getMobName() {
        return this.name;
    }

    public static ItemStack getCCItem(CreeperTypes tt) {
        return ItemUtils.buildItem(Material.MONSTER_EGG, (short)50, tt.getItemName(), tt.lore);
    }

}

