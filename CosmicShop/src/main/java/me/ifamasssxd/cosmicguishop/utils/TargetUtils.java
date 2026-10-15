/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.util.BlockIterator
 */
package me.ifamasssxd.cosmicguishop.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class TargetUtils {
    public static LivingEntity getTarget(Player pl, int radius) {
        List<Entity> nearbyE = pl.getNearbyEntities((double)radius, (double)radius, (double)radius);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
        LivingEntity target = null;
        for (Entity e : nearbyE) {
            if (e.equals((Object)pl) || !(e instanceof LivingEntity)) continue;
            livingE.add((LivingEntity)e);
        }
        try {
            BlockIterator bItr = new BlockIterator((LivingEntity)pl, radius);
            block3: while (bItr.hasNext()) {
                Block block = bItr.next();
                int bx = block.getX();
                int by = block.getY();
                int bz = block.getZ();
                for (LivingEntity e : livingE) {
                    Location loc = e.getLocation();
                    double ex = loc.getX();
                    double ey = loc.getY();
                    double ez = loc.getZ();
                    if (!((double)bx - 0.75 <= ex) || !(ex <= (double)bx + 1.75) || !((double)bz - 0.75 <= ez) || !(ez <= (double)bz + 1.75) || !((double)(by - 1) <= ey) || !(ey <= (double)by + 2.5)) continue;
                    target = e;
                    continue block3;
                }
            }
        }
        catch (Exception err) {
            if (!(err instanceof IllegalStateException)) {
                err.printStackTrace();
            }
            return null;
        }
        return target;
    }
}

