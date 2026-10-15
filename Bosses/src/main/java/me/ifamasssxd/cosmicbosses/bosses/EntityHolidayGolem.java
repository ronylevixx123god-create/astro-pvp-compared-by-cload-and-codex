package me.ifamasssxd.cosmicbosses.bosses;

import com.google.common.cache.CacheBuilder;
import net.minecraft.server.v1_8_R3.EntityGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.World;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class EntityHolidayGolem extends EntityGolem implements MultiProjectileShooter {
    public ConcurrentMap<Object, Object> recentlySplitPlayer;
    private float newF;
    private float newJ;

    public EntityHolidayGolem(final World world) {
        super(world);
        this.recentlySplitPlayer = CacheBuilder.newBuilder().expireAfterWrite(3L, TimeUnit.SECONDS).build().asMap();
        this.newF = 10.0f;
        this.newJ = this.newF * this.newF;
    }

    @Override
    public void shootNearby(EntityLiving p0) {

    }

    @Override
    public void a(EntityLiving entityLiving, float v) {

    }
    
    /*public boolean rollShootSkull() {
        return ThreadLocalRandom.current().nextInt(10) <= 1;
    }
    
    public void shootAtTarget(final EntityLiving living, final boolean witherSkull) {
        this.shootAtTarget((EntityLiving)this, living.locX, living.locY + living.getHeadHeight(), living.locZ, witherSkull);
    }
    
    public void shootAtTarget(final EntityLiving originateFrom, final double targetX, final double targetY, final double targetZ, final boolean witherSkull) {
        final double d0 = targetX - originateFrom.locX;
        final double d2 = targetZ - originateFrom.locZ;
        double d3 = targetY - 1.100000023841858 - originateFrom.locY;
        Entity projectile;
        if (witherSkull) {
            projectile = (Entity)new EntityWitherSkull(this.world, this, d0, d3, d2) {
                protected void a(final MovingObjectPosition movingobjectposition) {
                    final Entity colliding = movingobjectposition.entity;
                    if (colliding != null && !(colliding instanceof EntityHuman)) {
                        Bukkit.getLogger().info("Ignoring collision for Holiday Spirit wither skull!");
                        return;
                    }
                    super.a(movingobjectposition);
                }
            };
            projectile.locY = originateFrom.locY + 1.5;
            projectile.locX = originateFrom.locX;
            projectile.locZ = originateFrom.locZ;
            final Entity entity = projectile;
            entity.motX *= 1.5;
            final Entity entity2 = projectile;
            entity2.motZ *= 1.5;
        }
        else {
            projectile = (Entity)new EntitySnowball(originateFrom.world, this) {
                public void shoot(double d0, double d1, double d2, final float f, final float f1) {
                    final float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 /= f2;
                    d1 /= f2;
                    d2 /= f2;
                    d0 *= f;
                    d1 *= f;
                    d2 *= f;
                    this.motX = d0;
                    this.motY = d1;
                    this.motZ = d2;
                    final float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                    final float n = (float)(Math.atan2(d0, d2) * 180.0 / 3.1415927410125732);
                    this.yaw = n;
                    this.lastYaw = n;
                    final float n2 = (float)(Math.atan2(d1, f3) * 180.0 / 3.1415927410125732);
                    this.pitch = n2;
                    this.lastPitch = n2;
                }
            };
            d3 = targetY - 1.100000023841858 - projectile.locY;
            final float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2f;
            ((EntitySnowball)projectile).shoot(d0, d3 + f1, d2, 1.6f, 12.0f);
        }
        this.makeSound("random.bow", 1.0f, 1.0f / (this.aI().nextFloat() * 0.4f + 0.8f));
        this.world.addEntity(projectile);
    }
    
    public void shootNearby(final EntityLiving target) {
        int shots = 0;
        final int max = this.random.nextInt(4) + 5;
        for (final org.bukkit.entity.Entity entity : this.getBukkitEntity().getNearbyEntities(16.0, 16.0, 16.0)) {
            if (shots >= max) {
                break;
            }
            if (!(entity instanceof Player)) {
                continue;
            }
            final Entity ent = ((CraftEntity)entity).getHandle();
            if (ent == target) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)ent;
            if (player.getBukkitEntity().getGameMode() != GameMode.SURVIVAL) {
                continue;
            }
            ++shots;
            this.shoot(ent);
        }
    }
    
    public void a(final EntityLiving entityLiving, final float v) {
        this.shootAtTarget(entityLiving, this.rollShootSkull());
    }
    
    public float getF(final Entity target) {
        final double d0 = this.e(target.locX, target.getBoundingBox().b, target.locZ);
        if (d0 > this.newJ || !this.getEntitySenses().a(target)) {
            return -1.0f;
        }
        float f2;
        final float f = f2 = MathHelper.sqrt(d0) / this.newF;
        if (f < 0.1f) {
            f2 = 0.1f;
        }
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        return f2;
    }
    
    public void shoot(final Entity target) {
        final float f = this.getF(target);
        if (target instanceof EntityHuman) {
            this.a((EntityLiving)target, f);
        }
    }*/
}
