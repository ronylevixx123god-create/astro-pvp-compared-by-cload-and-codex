/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.EssentialsUtils
 *  net.minecraft.server.v1_8_R3.Block
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityFallingBlock
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.World
 *  net.minecraft.server.v1_8_R3.WorldServer
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package com.compilex.CosmicDungeons.utils;

import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FallingBlockUtils {
    public static FallingBlock createCustomFallingBlock(Location location, Material material, byte data, boolean doesHurt) {
        WorldServer server = ((CraftWorld)location.getWorld()).getHandle();
        EntityFallingBlock block = new EntityFallingBlock(location, (World)server, location.getX(), location.getY(), location.getZ(), Block.getById((int)material.getId()).getBlockData()){

            public void h() {
                super.h();
                if (this.ticksLived >= 3) {
                    Predicate<Entity> predicate = ent -> ent instanceof EntityHuman;
                    List<Entity> nearby = this.world.getEntities((Entity)this, this.getBoundingBox());
                    for (Entity ent2 : nearby) {
                        if (!this.isAlive()) break;
                        if (!(ent2 instanceof EntityHuman)) continue;
                        Player pl = (Player)((EntityHuman)ent2).getBukkitEntity();
                        pl.setVelocity(new Vector(this.random.nextGaussian() / 2.0, 0.1, this.random.nextGaussian() / 2.0));
                        if (EssentialsUtils.isInGodMode((Player)pl)) continue;
                        pl.damage(1.0);
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                    }
                }
            }
        };
        block.ticksLived = 1;
        block.dropItem = false;
        block.a(doesHurt);
        server.addEntity((Entity)block, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (FallingBlock)block.getBukkitEntity();
    }
}

