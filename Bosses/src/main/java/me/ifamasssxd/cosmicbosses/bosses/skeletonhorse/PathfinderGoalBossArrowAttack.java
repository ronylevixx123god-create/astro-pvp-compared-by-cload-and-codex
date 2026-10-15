package me.ifamasssxd.cosmicbosses.bosses.skeletonhorse;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

public class PathfinderGoalBossArrowAttack extends PathfinderGoalArrowAttack {
    public EntityInsentient a;
    public IRangedEntity b;
    public EntityLiving c;
    public int d;
    public double e;
    public int f;
    public int g;
    public int h;
    public float i;
    public float j;

    public PathfinderGoalBossArrowAttack(final IRangedEntity irangedentity, final double d0, final int i, final int j, final float f) {
        super(irangedentity, d0, i, j, f);
        if (!(irangedentity instanceof EntityLiving)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.b = irangedentity;
        this.a = (EntityInsentient) irangedentity;
        this.e = d0;
        this.g = i;
        this.h = j;
        this.i = f;
        this.j = f * f;
        this.a(3);
    }

    public boolean a() {
        final EntityLiving entityliving = this.a.getGoalTarget();
        if (entityliving == null) {
            return false;
        }
        this.c = entityliving;
        return true;
    }

    public boolean b() {
        return this.a() || !this.a.getNavigation().m();
    }

    public void d() {
        final EntityTargetEvent.TargetReason reason = this.c.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        CraftEventFactory.callEntityTargetEvent((Entity) this.b, (Entity) null, reason);
        this.c = null;
        this.f = 0;
        this.d = -1;
    }

    public void e() {
        final double d0 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
        final boolean flag = this.a.getEntitySenses().a((Entity) this.c);
        if (flag) {
            ++this.f;
        } else {
            this.f = 0;
        }
        if (d0 <= this.j && this.f >= 20) {
            this.a.getNavigation().n();
        } else {
            this.a.getNavigation().a((Entity) this.c, this.e);
        }
        this.a.getControllerLook().a((Entity) this.c, 30.0f, 30.0f);
        final int d2 = this.d - 1;
        this.d = d2;
        if (d2 == 0) {
            if (d0 > this.j || !flag) {
                return;
            }
            float f2;
            final float f = f2 = MathHelper.sqrt(d0) / this.i;
            if (f < 0.1f) {
                f2 = 0.1f;
            }
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            this.b.a(this.c, f2);
            this.d = 25;
        } else if (this.d < 0) {
            final float f = MathHelper.sqrt(d0) / this.i;
            this.d = 25;
        }
    }
}
