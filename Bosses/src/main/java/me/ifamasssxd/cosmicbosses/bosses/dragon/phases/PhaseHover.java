package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import net.minecraft.server.v1_8_R3.Vec3D;

import javax.annotation.Nullable;

public class PhaseHover extends PhaseBase {
    private Vec3D targetLocation;

    public PhaseHover(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            this.targetLocation = new Vec3D(this.dragon.locX, this.dragon.locY, this.dragon.locZ);
        }
    }

    @Override
    public boolean getIsStationary() {
        return true;
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
    }

    @Override
    public float getMaxRiseOrFall() {
        return 1.0f;
    }

    @Nullable
    @Override
    public Vec3D getTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public PhaseList<PhaseHover> getType() {
        return PhaseList.HOVER;
    }
}
