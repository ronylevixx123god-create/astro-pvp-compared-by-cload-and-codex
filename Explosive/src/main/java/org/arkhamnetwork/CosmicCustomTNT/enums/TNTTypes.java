/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.CosmicCustomTNT.enums;

import java.util.Random;
import org.arkhamnetwork.CosmicCustomTNT.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum TNTTypes {
    Huge((Object)ChatColor.AQUA + "Gigantic TNT", new String[]{ChatColor.AQUA.toString() + ChatColor.ITALIC.toString() + "Explodes in a much larger radius.", "", (Object)ChatColor.GRAY + "Spawns 2 extra explosions at", (Object)ChatColor.GRAY + "y+3 and y-3 of TNT, and another", (Object)ChatColor.GRAY + "2 explosions at either x+3, z+3 or", (Object)ChatColor.GRAY + "x-3 and z-3."}),
    Lethal((Object)ChatColor.DARK_RED + "Lethal TNT", new String[]{ChatColor.DARK_RED.toString() + ChatColor.ITALIC.toString() + "A more dense, dangerous explosion.", "", (Object)ChatColor.GRAY + "Creates 4x extra TNT explosions at", (Object)ChatColor.GRAY + "same location as the original."}),
    Lucky((Object)ChatColor.YELLOW + "Lucky TNT", new String[]{ChatColor.YELLOW.toString() + ChatColor.ITALIC.toString() + "Increases chances of spawner drops.", "", (Object)ChatColor.GRAY + "200% the normal chance of a", (Object)ChatColor.GRAY + "spawner dropping from explosion."}),
    Mimic((Object)ChatColor.GREEN + "Mimic TNT", new String[]{ChatColor.GREEN.toString() + ChatColor.ITALIC.toString() + "Repeats the same explosion many times.", "", (Object)ChatColor.GRAY + "Spawns a TNT explosion at", (Object)ChatColor.GRAY + "same location every 5s", (Object)ChatColor.GRAY + "for 15s total, or 3 explosions."});

    String name;
    String[] lore;

    private TNTTypes(String name, String[] lore) {
        this.name = name;
        this.lore = lore;
    }

    public String getItemName() {
        return this.name;
    }

    public static ItemStack getTNTItem(TNTTypes tt) {
        return ItemUtils.buildItem(Material.TNT, (short)0, tt.name, tt.lore);
    }

}

