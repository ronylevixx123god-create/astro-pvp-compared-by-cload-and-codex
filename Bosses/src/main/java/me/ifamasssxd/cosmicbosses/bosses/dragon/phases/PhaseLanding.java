package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Vec3D;

import javax.annotation.Nullable;

public class PhaseLanding extends PhaseBase {
    private Vec3D targetLocation;

    public PhaseLanding(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        final Vec3D vec3d = BossUtils.oldJ(this.dragon, 1.0f).a();
        vec3d.b(-0.7853982f);
        final double d0 = this.dragon.head.locX;
        final double d2 = this.dragon.head.locY + this.dragon.head.getHeadHeight() / 2.0f;
        final double d3 = this.dragon.head.locZ;
        for (int i = 0; i < 8; ++i) {
            final double d4 = d0 + this.dragon.random.nextGaussian() / 2.0;
            final double d5 = d2 + this.dragon.random.nextGaussian() / 2.0;
            final double d6 = d3 + this.dragon.random.nextGaussian() / 2.0;
            this.dragon.world.addParticle(EnumParticle.PORTAL, d4, d5, d6, -vec3d.a * 0.07999999821186066 + this.dragon.motX, -vec3d.b * 0.30000001192092896 + this.dragon.motY, -vec3d.c * 0.07999999821186066 + this.dragon.motZ);
            vec3d.b(0.19634955f);
        }
    }

    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            this.targetLocation = new Vec3D(this.dragon.homeLocation.getX(), this.dragon.homeLocation.getY(), this.dragon.homeLocation.getZ());
        }
        this.doClientRenderEffects();
        double d1 = this.dragon.locX - this.targetLocation.a;
        double d2 = this.dragon.locY - this.targetLocation.b;
        double d3 = this.dragon.locZ - this.targetLocation.c;

        final double d02 = MathHelper.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
        if (d02 < 1.0) {
            this.dragon.getPhaseManager().getPhase(PhaseList.SITTING_FLAMING).resetFlameCount();
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
        }
    }

    @Override
    public float getMaxRiseOrFall() {
        return 1.5f;
    }

    @Override
    public float getYawFactor() {
        final float f = (float) (Math.sqrt(this.dragon.motX * this.dragon.motX + this.dragon.motZ * this.dragon.motZ) + 1.0);
        final float f2 = Math.min(f, 40.0f);
        return f2 / f;
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
    }

    @Nullable
    @Override
    public Vec3D getTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public PhaseList<PhaseLanding> getType() {
        return PhaseList.LANDING;
    }
}
