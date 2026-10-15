package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class PhaseDying extends PhaseBase {
    private Vec3D targetLocation;
    private int time;

    public PhaseDying(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        if (this.time++ % 10 == 0) {
            final float f = (this.dragon.random.nextFloat() - 0.5f) * 8.0f;
            final float f2 = (this.dragon.random.nextFloat() - 0.5f) * 4.0f;
            final float f3 = (this.dragon.random.nextFloat() - 0.5f) * 8.0f;
            this.dragon.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.dragon.locX + f, this.dragon.locY + 2.0 + f2, this.dragon.locZ + f3, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void doLocalUpdate() {
        ++this.time;
        if (this.targetLocation == null) {
            final Location home = this.dragon.homeLocation;
            this.targetLocation = new Vec3D(home.getX(), home.getY(), home.getZ());
        }
        double d1 = this.dragon.locX - this.targetLocation.a;
        double d2 = this.dragon.locY - this.targetLocation.b;
        double d3 = this.dragon.locZ - this.targetLocation.c;

        final double d0 = MathHelper.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
        if (d0 >= 100.0 && d0 <= 22500.0 && !this.dragon.bA) {
            this.dragon.setHealth(1.0f);
        } else {
            this.dragon.setHealth(0.0f);
        }
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
        this.time = 0;
    }

    @Override
    public float getMaxRiseOrFall() {
        return 3.0f;
    }

    @Nullable
    @Override
    public Vec3D getTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public PhaseList<PhaseDying> getType() {
        return PhaseList.DYING;
    }
}
