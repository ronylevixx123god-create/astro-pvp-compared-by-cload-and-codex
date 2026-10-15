/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicbosses.me.astropvp.cratekeys.net.astropvp.armorsets.utils.BossUtils
 *  net.minecraft.server.v1_7_R4.Entity
 *  net.minecraft.server.v1_7_R4.EntityGhast
 *  net.minecraft.server.v1_7_R4.EntityLiving
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftGhast
 */
package com.cosmicpvp.cosmicutils.utils;

import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftGhast;
import org.bukkit.entity.Ghast;

import java.io.File;
import java.lang.reflect.Field;

public class EntityUtils {
    private static Class<?> endDragonClass;
    private static boolean bossesEnabled;
    private static Field ghastTarget;

    static {
        bossesEnabled = new File("plugins/Bosses.jar").exists();
    }

    public static void removeAI(org.bukkit.entity.Entity entity) {
        EntityLiving ent = (EntityLiving) ((CraftEntity) entity).getHandle();
        ent.fromMobSpawner = true;
    }

    public static boolean isEndDragonSpawned() {
        if (endDragonClass == null && bossesEnabled) {
            try {
                endDragonClass = Class.forName("me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragon");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bossesEnabled && !BossUtils.getAllBossesFromType((Class<? extends Boss>) endDragonClass).isEmpty();
    }

    public static Field getGhastTarget() throws NoSuchFieldException {
        if (ghastTarget == null) {
            ghastTarget = ReflectionUtils.getField(EntityGhast.class, "target");
        }
        return ghastTarget;
    }

    public static void setGhastTarget(org.bukkit.entity.Entity entity, org.bukkit.entity.Entity target) throws NoSuchFieldException, IllegalAccessException {
        if (entity instanceof Ghast) {
            Ghast ghast = (Ghast) entity;
            EntityGhast entGhast = ((CraftGhast) ghast).getHandle();
            CraftEntity craftEnt = (CraftEntity) target;
            EntityLiving toSet = null;
            if (target != null && craftEnt.getHandle() instanceof EntityLiving) {
                toSet = (EntityLiving) craftEnt.getHandle();
            }
            entGhast.setGoalTarget(toSet);
            ReflectionUtils.setObject(entGhast, EntityUtils.getGhastTarget(), toSet);
        }
    }

    public static org.bukkit.entity.Entity getGhastTarget(org.bukkit.entity.Entity entity) {
        if (entity instanceof Ghast) {
            Ghast ghast = (Ghast) entity;
            EntityGhast entGhast = ((CraftGhast) ghast).getHandle();
            try {
                Entity internal = (Entity) EntityUtils.getGhastTarget().get(entGhast);
                return internal.getBukkitEntity();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

