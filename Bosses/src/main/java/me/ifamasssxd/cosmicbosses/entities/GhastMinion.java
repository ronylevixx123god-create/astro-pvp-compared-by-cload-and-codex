package me.ifamasssxd.cosmicbosses.entities;

import me.ifamasssxd.cosmicbosses.bosses.plaguebloater.PlagueCarriers;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.World;

public class GhastMinion extends EntityGhast {
    private Entity target;
    private int br;
    private int explosionPower;
    private PlagueCarriers carrier;

    public GhastMinion(final World world) {
        super(world);
        this.explosionPower = 1;
    }
    
    /*protected void bq() {
        if (!this.world.isClientSide && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.die();
        }
        this.w();
        this.bo = this.bp;
        final double d0 = this.i - this.locX;
        final double d2 = this.bm - this.locY;
        final double d3 = this.bn - this.locZ;
        double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        if (d4 < 1.0 || d4 > 3600.0) {
            this.i = this.locX + (this.random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.bm = this.locY + (this.random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.bn = this.locZ + (this.random.nextFloat() * 2.0f - 1.0f) * 16.0f;
        }
        if (this.h-- <= 0) {
            this.h += this.random.nextInt(5) + 2;
            d4 = MathHelper.sqrt(d4);
            if (this.a(this.i, this.bm, this.bn, d4)) {
                this.motX += d0 / d4 * 0.1;
                this.motY += d2 / d4 * 0.1;
                this.motZ += d3 / d4 * 0.1;
            }
            else {
                this.i = this.locX;
                this.bm = this.locY;
                this.bn = this.locZ;
            }
        }
        if (this.target != null && this.target.dead) {
            final EntityTargetEvent event = new EntityTargetEvent((org.bukkit.entity.Entity)this.getBukkitEntity(), (org.bukkit.entity.Entity)null, EntityTargetEvent.TargetReason.TARGET_DIED);
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.target = null;
                }
                else {
                    this.target = ((CraftEntity)event.getTarget()).getHandle();
                }
            }
        }
        if (this.target == null || this.br-- <= 0) {
            final Entity target = (Entity)this.world.findNearbyVulnerablePlayer((Entity)this, 100.0);
            if (target != null) {
                final EntityTargetEvent event2 = new EntityTargetEvent((org.bukkit.entity.Entity)this.getBukkitEntity(), (org.bukkit.entity.Entity)target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.world.getServer().getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    if (event2.getTarget() == null) {
                        this.target = null;
                    }
                    else {
                        this.target = ((CraftEntity)event2.getTarget()).getHandle();
                    }
                }
            }
            if (this.target != null) {
                this.br = 20;
            }
        }
        final double d5 = 64.0;
        if (this.target != null && this.target.f((Entity)this) < d5 * d5) {
            final double d6 = this.target.locX - this.locX;
            final double d7 = this.target.getBoundingBox().b + this.target.length / 2.0f - (this.locY + this.length / 2.0f);
            final double d8 = this.target.locZ - this.locZ;
            final float n = -(float)Math.atan2(d6, d8) * 180.0f / 3.1415927f;
            this.yaw = n;
            this.aM = n;
            if (this.hasLineOfSight(this.target)) {
                if (this.bp == 10) {
                    this.world.a((EntityHuman)null, 1007, (int)this.locX, (int)this.locY, (int)this.locZ, 0);
                }
                ++this.bp;
                if (this.bp == 20) {
                    this.world.a((EntityHuman)null, 1008, (int)this.locX, (int)this.locY, (int)this.locZ, 0);
                    final EntityLargeFireball entityLargeFireball2;
                    final EntityLargeFireball entityLargeFireball;
                    final EntityLargeFireball entitylargefireball = entityLargeFireball = (entityLargeFireball2 = new EntityLargeFireball(this.world, (EntityLiving)this, d6, d7, d8));
                    final int explosionPower = this.explosionPower;
                    entityLargeFireball.yield = explosionPower;
                    entityLargeFireball2.bukkitYield = (float)explosionPower;
                    final double d9 = 4.0;
                    final Vec3D vec3d = this.j(1.0f);
                    entitylargefireball.locX = this.locX + vec3d.a * d9;
                    entitylargefireball.locY = this.locY + this.length / 2.0f + 0.5;
                    entitylargefireball.locZ = this.locZ + vec3d.c * d9;
                    this.world.addEntity((Entity)entitylargefireball);
                    this.bp = -100;
                    try {
                        if (this.carrier == null) {
                            this.getCarrier();
                        }
                        if (this.carrier != null) {
                            this.carrier.onCarrierShoot((LargeFireball)entitylargefireball.getBukkitEntity());
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (this.bp > 0) {
                --this.bp;
            }
        }
        else {
            final float n2 = -(float)Math.atan2(this.motX, this.motZ) * 180.0f / 3.1415927f;
            this.yaw = n2;
            this.aM = n2;
            if (this.bp > 0) {
                --this.bp;
            }
        }
        if (!this.world.isClientSide) {
            final byte b0 = this.datawatcher.getByte(16);
            final byte b2 = (byte)((this.bp > 10) ? 1 : 0);
            if (b0 != b2) {
                this.datawatcher.watch(16, (Object)b2);
            }
        }
    }
    
    private boolean a(final double d0, final double d1, final double d2, final double d3) {
        final double d4 = (this.i - this.locX) / d3;
        final double d5 = (this.bm - this.locY) / d3;
        final double d6 = (this.bn - this.locZ) / d3;
        final AxisAlignedBB axisalignedbb = this.getBoundingBox().clone();
        for (int i = 1; i < d3; ++i) {
            axisalignedbb.d(d4, d5, d6);
            if (!this.world.getCubes((Entity)this, axisalignedbb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public void getCarrier() {
        if (this.getBukkitEntity().hasMetadata("plague_carrier") && this.getBukkitEntity().getMetadata("plague_carrier").size() > 0) {
            final UUID uuid = (UUID)this.getBukkitEntity().getMetadata("plague_carrier").get(0).value();
            final Boss boss = Boss.bosses.get(uuid);
            if (boss == null) {
                this.die();
                return;
            }
            final PlagueBloater bloater = (PlagueBloater)boss;
            this.carrier = bloater.getPlagueCarrier();
        }
    }
    
    public void setCarrier(final PlagueCarriers carrier) {
        this.carrier = carrier;
    }*/
}
