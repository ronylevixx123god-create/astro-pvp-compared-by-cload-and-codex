package me.ifamasssxd.cosmicbosses.bosses.yijki;

import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.PathfinderGoalBossArrowAttack;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.IRangedEntity;
import net.minecraft.server.v1_8_R3.MathHelper;

import java.util.concurrent.ThreadLocalRandom;

public class PathfinderGoalWitherBossArrowAttack extends PathfinderGoalBossArrowAttack {
    private Boss boss;

    public PathfinderGoalWitherBossArrowAttack(final IRangedEntity irangedentity, final double d0, final int i, final int j, final float f) {
        super(irangedentity, d0, i, j, f);
    }

    public PathfinderGoalWitherBossArrowAttack(final Boss boss, final IRangedEntity irangedentity, final double d0, final int i, final int j, final float f) {
        super(irangedentity, d0, i, j, f);
        this.boss = boss;
    }

    public void setSpeed(final int speed) {
        this.g = speed;
    }

    @Override
    public void e() {
        final double d0 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
        final boolean flag = this.a.getEntitySenses().a(this.c);
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
            if (this.boss != null) {
                this.d = ((this.boss.getPhase() == Phase.TWO) ? 5 : 10);
            } else {
                final int possible = Math.max(1, this.h - this.g);
                this.d = ThreadLocalRandom.current().nextInt(possible) + this.g;
            }
            /*if (this.b instanceof EvilAbominationEntity) {
                ((EvilAbominationEntity)this.b).shootNearby(this.c);
            }*/
        } else if (this.d < 0) {
            final float f = MathHelper.sqrt(d0) / this.i;
            this.d = 10;
        }
    }
}
