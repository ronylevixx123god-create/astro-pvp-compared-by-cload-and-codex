package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Vec3D;

public class PhaseSittingScanning extends PhaseSittingBase {
    private int scanningTime;

    public PhaseSittingScanning(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() {
        ++this.scanningTime;
        EntityLiving entitylivingbase = BossUtils.findNearbyVulnerablePlayer(this.dragon.world, this.dragon, 20.0);
        if (entitylivingbase != null) {
            if (this.dragon.ticksLived % 30 == 0) {
                this.dragon.shootTNT();
            }
            if (this.scanningTime > 40) {
                this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
                this.dragon.shootTNT();
            } else {
                final Vec3D vec3d = new Vec3D(entitylivingbase.locX - this.dragon.locX, 0.0, entitylivingbase.locZ - this.dragon.locZ).a();
                final Vec3D vec3d2 = new Vec3D((double) MathHelper.sin(this.dragon.yaw * 0.017453292f), 0.0, (double) (-MathHelper.cos(this.dragon.yaw * 0.017453292f))).a();
                final float f = (float) vec3d2.b(vec3d);
                final float f2 = (float) (Math.acos(f) * 57.29577951308232) + 0.5f;
                if (f2 < 0.0f || f2 > 10.0f) {
                    final double d0 = entitylivingbase.locX - this.dragon.head.locX;
                    final double d2 = entitylivingbase.locZ - this.dragon.head.locZ;
                    final double d3 = MathHelper.a(MathHelper.g(180.0 - Math.atan2(d0, d2) * 57.29577951308232 - this.dragon.yaw), -100.0, 100.0);
                    this.dragon.setRandomYawVelocity(this.dragon.getRandomYawVelocity() * 0.8f);
                    final float f4;
                    float f3 = f4 = MathHelper.sqrt(d0 * d0 + d2 * d2) + 1.0f;
                    if (f3 > 40.0f) {
                        f3 = 40.0f;
                    }
                    this.dragon.setRandomYawVelocity((float) (this.dragon.getRandomYawVelocity() + d3 * (0.7f / f3 / f4)));
                    final EndDragonEntity dragon = this.dragon;
                    dragon.yaw += this.dragon.getRandomYawVelocity();
                }
            }
        } else if (this.scanningTime >= 60) {
            entitylivingbase = BossUtils.findNearbyVulnerablePlayer(this.dragon.world, this.dragon, 100.0);
            this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
            this.dragon.shootTNT();
            if (entitylivingbase != null) {
                this.dragon.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
                this.dragon.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER).setTarget(new Vec3D(entitylivingbase.locX, entitylivingbase.locY, entitylivingbase.locZ));
            }
        }
    }

    @Override
    public void initPhase() {
        this.scanningTime = 0;
    }

    @Override
    public PhaseList<PhaseSittingScanning> getType() {
        return PhaseList.SITTING_SCANNING;
    }
}
