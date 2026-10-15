/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.DamageSource
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityFireball
 *  net.minecraft.server.v1_8_R3.EntityLargeFireball
 *  net.minecraft.server.v1_8_R3.MovingObjectPosition
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.craftbukkit.v1_8_R3.CraftServer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.entity.Explosive
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.ExplosionPrimeEvent
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityCustomFireball extends EntityLargeFireball {
    public EntityCustomFireball(World world) {
        super(world);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isClientSide) {
            if (movingobjectposition.entity != null) {
                movingobjectposition.entity.damageEntity(DamageSource.fireball((EntityFireball)this, (Entity)this.shooter), 6.0f);
                this.a(this.shooter, movingobjectposition.entity);
            }
            ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive)CraftEntity.getEntity((CraftServer)this.world.getServer(), (Entity)this));
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled()) {
                this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 1.0f, 1.0f);
            }
            this.die();
        }
    }
}

