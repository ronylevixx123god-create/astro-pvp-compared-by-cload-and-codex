package me.ifamasssxd.cosmicbosses.dungeonboss;

import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.PathfinderGoalBossArrowAttack;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.IRangedEntity;
import net.minecraft.server.v1_8_R3.MathHelper;

public class PathfinderGoalDungeonWitherBossAttack extends PathfinderGoalBossArrowAttack {
    private DungeonYijki boss;

    public PathfinderGoalDungeonWitherBossAttack(IRangedEntity irangedentity, double d0, int i, int j, float f) {
        super(irangedentity, d0, i, j, f);
    }

    public PathfinderGoalDungeonWitherBossAttack(DungeonYijki boss, IRangedEntity irangedentity, double d0, int i, int j, float f) {
        super(irangedentity, d0, i, j, f);
        this.boss = boss;
    }

    public void e() {
        double d0 = this.a.e(this.c.locX, (this.c.getBoundingBox()).b, this.c.locZ);
        boolean flag = this.a.getEntitySenses().a((Entity)this.c);
        if (flag) {
            this.f++;
        } else {
            this.f = 0;
        }
        if (d0 <= this.j && this.f >= 10) {
            this.a.getNavigation().n();
        } else {
            this.a.getNavigation().a((Entity)this.c, this.e);
        }
        this.a.getControllerLook().a((Entity)this.c, 30.0F, 30.0F);
        int d2 = this.d - 1;
        this.d = d2;
        if (d2 == 0) {
            if (d0 > this.j || !flag)
                return;
            float f2 = MathHelper.sqrt(d0) / this.i, f = f2;
            if (f < 0.1F)
                f2 = 0.1F;
            if (f2 > 1.0F)
                f2 = 1.0F;
            this.b.a(this.c, f2);
            this.d = this.boss.getShootDelay();
        } else if (this.d < 0) {
            float f = MathHelper.sqrt(d0) / this.i;
            this.d = 5;
        }
    }
}
