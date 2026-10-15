package me.ifamasssxd.cosmicbosses.bosses.skeletonhorse;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

public class PathfinderGoalBossMeleeAttack extends PathfinderGoalMeleeAttack {
    World a;
    EntityCreature b;
    int c;
    double d;
    boolean e;
    PathEntity f;
    Class<?> g;
    private int h;
    private double i;
    private double j;
    private double k;

    public PathfinderGoalBossMeleeAttack(final EntityCreature entitycreature, final Class<?> oclass, final double d0, final boolean flag) {
        super(entitycreature, (Class) oclass, d0, flag);
        this.b = entitycreature;
    }

    public boolean a() {
        final EntityLiving entityliving = this.b.getGoalTarget();
        if (entityliving == null) {
            return false;
        }
        if (!entityliving.isAlive()) {
            return false;
        }
        if (this.g != null && !this.g.isAssignableFrom(entityliving.getClass())) {
            return false;
        }
        this.f = this.b.getNavigation().a((Entity) entityliving);
        return this.f != null;
    }

    public boolean b() {
        final EntityLiving entityliving = this.b.getGoalTarget();
        final EntityTargetEvent.TargetReason reason = (this.b.getGoalTarget() == null) ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        if (this.b.getGoalTarget() == null || (this.b.getGoalTarget() != null && !this.b.getGoalTarget().isAlive())) {
            CraftEventFactory.callEntityTargetEvent((Entity) this.b, (Entity) null, reason);
        }
        return entityliving != null && entityliving.isAlive() && (this.e ? this.b.e(new BlockPosition(MathHelper.floor(entityliving.locX), MathHelper.floor(entityliving.locY), MathHelper.floor(entityliving.locZ))) : (!this.b.getNavigation().m()));
    }

    public void c() {
        this.b.getNavigation().a(this.f, this.d);
        this.h = 0;
    }

    public void d() {
        this.b.getNavigation().n();
    }

    public void e() {
        final EntityLiving entityliving = this.b.getGoalTarget();
        this.b.getControllerLook().a((Entity) entityliving, 30.0f, 30.0f);
        final double distance = this.b.e(entityliving.locX, entityliving.getBoundingBox().b, entityliving.locZ);
        final double d1 = this.b.width * 2.0f * this.b.width * 2.0f + entityliving.width;
        --this.h;
        if ((this.e || this.b.getEntitySenses().a((Entity) entityliving)) && this.h <= 0 && ((this.i == 0.0 && this.j == 0.0 && this.k == 0.0) || entityliving.e(this.i, this.j, this.k) >= 1.0 || this.b.bc().nextFloat() < 0.05f)) {
            this.i = entityliving.locX;
            this.j = entityliving.getBoundingBox().b;
            this.k = entityliving.locZ;
            this.h = 4 + this.b.bc().nextInt(7);
            if (distance > 1024.0) {
                this.h += 10;
            } else if (distance > 256.0) {
                this.h += 5;
            }
            if (!this.b.getNavigation().a((Entity) entityliving, this.d)) {
                this.h += 15;
            }
        }
        this.c = Math.max(this.c - 1, 0);
        if (distance <= d1 && this.c <= 20) {
            this.c = 20;
            if (this.b.bA() != null) {
                this.b.bw();
            }
            this.b.n((Entity) entityliving);
        }
    }
}
