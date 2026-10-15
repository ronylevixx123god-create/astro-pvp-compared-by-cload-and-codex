/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.patches.chunkgc.ChunkAPI
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Firework
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.FireworkMeta
 */
package com.cosmicpvp.CosmicSpaceChests.tasks;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class CosmicEnvoyPingerTask
implements Runnable {
    CosmicSpaceChestsPlugin plugin;

    public CosmicEnvoyPingerTask(CosmicSpaceChestsPlugin fdp) {
        this.plugin = fdp;
    }

    @Override
    public void run() {
        for (Location loc : CosmicSpaceChestsPlugin.chest_locations.keySet()) {
            if (loc.getWorld().getNearbyEntities(loc, 4, 4, 4).isEmpty()) continue;
            this.launchFirework(loc, CosmicSpaceChestsPlugin.chest_locations.getChestRarity(loc));
        }
    }

    public void launchFirework(Location l, ItemRarity ir) {
        Firework fw = (Firework)l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Color c = null;
        if (ir == ItemRarity.SIMPLE) {
            c = Color.WHITE;
        }
        if (ir == ItemRarity.UNIQUE) {
            c = Color.GREEN;
        }
        if (ir == ItemRarity.ELITE) {
            Color color = c = l.getWorld().getName().equals("world_arena") ? Color.ORANGE : Color.AQUA;
        }
        if (ir == ItemRarity.ULTIMATE) {
            c = Color.YELLOW;
        }
        if (ir == ItemRarity.LEGENDARY) {
            c = Color.ORANGE;
        }
        if (ir == ItemRarity.GOD) {
            c = Color.BLACK;
        }
        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(FireworkEffect.Type.STAR).trail(true).build();
        fwm.addEffect(effect);
        fwm.setPower(1);
        fw.setFireworkMeta(fwm);
    }
}

