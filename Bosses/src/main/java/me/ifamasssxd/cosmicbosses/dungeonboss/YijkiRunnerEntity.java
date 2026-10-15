package me.ifamasssxd.cosmicbosses.dungeonboss;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class YijkiRunnerEntity extends EntityMonster implements IRangedEntity {
    private Location targetLocation;
    private int aliveTime;
    private Vector direction;

    public YijkiRunnerEntity(final World world, final Location spawn, final Location target, final int aliveTime) {
        super(world);
        this.targetLocation = target;
        this.aliveTime = aliveTime * 20;
        this.setHealth(this.getMaxHealth());
        this.a(0.9f, 4.0f);
        this.fireProof = true;
        ((Navigation) this.getNavigation()).e(true);
        //this.as = EnumEntitySize.SIZE_6;
        this.direction = target.toVector().subtract(spawn.toVector());
        if (this.direction.length() != 0.0) {
            this.direction.normalize();
        }
        Bukkit.getLogger().info("Yijki Runner direction set to " + this.direction + " to " + target);
    }

    public boolean damageEntity(final DamageSource damagesource, final float f) {
        return false;
    }

    public void m() {
        if (this.getHealth() != this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
        final double motionMultiplier = 0.1;
        final double gravityMultiplier = 0.6000000238418579;
        if (this.aliveTime != 0 && this.ticksLived >= this.aliveTime) {
            this.die();
            return;
        }
        this.motX += this.direction.getX() * motionMultiplier;
        this.motZ += this.direction.getZ() * motionMultiplier;
        if (this.direction.getY() > 0.0) {
            this.motY += this.direction.getY() * (gravityMultiplier / 2.0);
        }
        this.motY *= gravityMultiplier;
        super.m();
        if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806) {
            this.yaw = (float) Math.atan2(this.motZ, this.motX) * 57.295776f - 90.0f;
        }
    }

    public void a(final EntityLiving entityLiving, final float v) {
    }
}
