package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.struct.BossPath;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class PhaseTakeoff extends PhaseBase {
    private boolean firstTick;
    private BossPath currentPath;
    private Vec3D targetLocation;

    public PhaseTakeoff(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() throws NoSuchFieldException {
        if (!this.firstTick && this.currentPath != null) {
            final Location blockpos = this.dragon.homeLocation;
            final double d0 = this.dragon.distanceSqToCenter(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            if (d0 > 100.0) {
                this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
            }
        } else {
            this.firstTick = false;
            this.findNewTarget();
        }
    }

    @Override
    public void initPhase() {
        this.firstTick = true;
        this.currentPath = null;
        this.targetLocation = null;
    }

    private void findNewTarget() throws NoSuchFieldException {
        final int i = this.dragon.initPathPoints();
        final Vec3D vec3d = this.dragon.getHeadLookVec(1.0f);
        Bukkit.getLogger().info("Vec3d HeadLook: " + vec3d.a + ", b" + vec3d.b + " z: " + vec3d.c);
        int j = this.dragon.getNearestPpIdx(-vec3d.a * this.dragon.getHomeLocation().getX(), 105.0, -vec3d.c * this.dragon.getHomeLocation().getZ());
        if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() > 0) {
            j %= 12;
            if (j < 0) {
                j += 12;
            }
        } else {
            j -= 12;
            j &= 0x7;
            j += 12;
        }
        this.currentPath = this.dragon.findPath(i, j, null);
        if (this.currentPath != null) {
            this.currentPath.incrementPathIndex();
            this.navigateToNextPathNode();
        }
    }

    private void navigateToNextPathNode() {
        final Vec3D vec3d = this.currentPath.getCurrentPos();
        this.currentPath.incrementPathIndex();
        double d0;
        do {
            d0 = vec3d.b + this.dragon.random.nextFloat() * 20.0f;
        } while (d0 < vec3d.b);
        this.targetLocation = new Vec3D(vec3d.a, d0, vec3d.c);
    }

    @Nullable
    @Override
    public Vec3D getTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public PhaseList<PhaseTakeoff> getType() {
        return PhaseList.TAKEOFF;
    }
}
