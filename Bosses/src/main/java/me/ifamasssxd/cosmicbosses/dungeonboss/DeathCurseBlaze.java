package me.ifamasssxd.cosmicbosses.dungeonboss;

import net.minecraft.server.v1_8_R3.*;

public class DeathCurseBlaze extends EntityBlaze {
    private int br;

    public DeathCurseBlaze(final World world) {
        super(world);
        this.goalSelector.a(4, new DeathCurseBlaze.PathfinderGoalBlazeFireball(this));
    }

    protected Item getLoot() {
        return null;
    }

    static class PathfinderGoalBlazeFireball extends PathfinderGoal {
        private EntityBlaze a;
        private int b;
        private int c;

        public PathfinderGoalBlazeFireball(EntityBlaze var1) {
            this.a = var1;
            this.a(3);
        }

        public boolean a() {
            EntityLiving var1 = this.a.getGoalTarget();
            return var1 != null && var1.isAlive();
        }

        public void c() {
            this.b = 0;
        }

        public void d() {
            this.a.a(false);
        }

        public void e() {
            --this.c;
            EntityLiving var1 = this.a.getGoalTarget();
            double var2 = this.a.h(var1);
            if (var2 < 4.0D) {
                if (this.c <= 0) {
                    this.c = 20;
                    this.a.r(var1);
                }

                this.a.getControllerMove().a(var1.locX, var1.locY, var1.locZ, 1.0D);
            } else if (var2 < 256.0D) {
                double var4 = var1.locX - this.a.locX;
                double var6 = var1.getBoundingBox().b + (double) (var1.length / 2.0F) - (this.a.locY + (double) (this.a.length / 2.0F));
                double var8 = var1.locZ - this.a.locZ;
                if (this.c <= 0) {
                    ++this.b;
                    if (this.b == 1) {
                        this.c = 60;
                        this.a.a(true);
                    } else if (this.b <= 4) {
                        this.c = 6;
                    } else {
                        this.c = 100;
                        this.b = 0;
                        this.a.a(false);
                    }

                    if (this.b > 1) {
                        float var10 = MathHelper.c(MathHelper.sqrt(var2)) * 0.5F;
                        this.a.world.a((EntityHuman) null, 1009, new BlockPosition((int) this.a.locX, (int) this.a.locY, (int) this.a.locZ), 0);

                        for (int var11 = 0; var11 < 1; ++var11) {
                            EntitySmallFireball var12 = new EntitySmallFireball(this.a.world, this.a, var4 + this.a.bc().nextGaussian() * (double) var10, var6, var8 + this.a.bc().nextGaussian() * (double) var10) {
                                public void setDirection(final double d0, final double d1, final double d2) {
                                    final double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                    this.dirX = d0 / d3 * 0.1;
                                    this.dirY = d1 / d3 * 0.1;
                                    this.dirZ = d2 / d3 * 0.1;
                                }
                            };
                            var12.locY = this.a.locY + (double) (this.a.length / 2.0F) + 0.5D;
                            this.a.world.addEntity(var12);
                        }
                    }
                }

                this.a.getControllerLook().a(var1, 10.0F, 10.0F);
            } else {
                this.a.getNavigation().n();
                this.a.getControllerMove().a(var1.locX, var1.locY, var1.locZ, 1.0D);
            }

            super.e();
        }
    }
}
