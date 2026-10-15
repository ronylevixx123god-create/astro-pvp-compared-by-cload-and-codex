package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.struct.BossPath;
import me.ifamasssxd.cosmicbosses.struct.BossPathPoint;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class PhaseLandingApproach extends PhaseBase {
    private BossPath currentPath;
    private Vec3D targetLocation;

    public PhaseLandingApproach(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public PhaseList<PhaseLandingApproach> getType() {
        return PhaseList.LANDING_APPROACH;
    }

    @Override
    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Override
    public void doLocalUpdate() throws NoSuchFieldException {
        double d1 = this.dragon.locX - this.targetLocation.a;
        double d2 = this.dragon.locY - this.targetLocation.b;
        double d3 = this.dragon.locZ - this.targetLocation.c;

        final double d02 = MathHelper.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
        final double d0 = (this.targetLocation == null) ? 0.0 : d02;
        if (d0 < 100.0 || d0 > 22500.0 || this.dragon.bA) {
            this.findNewTarget();
        }
    }

    @Nullable
    @Override
    public Vec3D getTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() throws NoSuchFieldException {
        if (this.currentPath == null || this.currentPath.isFinished()) {
            final int i = this.dragon.initPathPoints();
            final Location blockloc = this.dragon.homeLocation;
            final EntityHuman entityplayer = this.dragon.world.findNearbyPlayer(blockloc.getX(), blockloc.getY(), blockloc.getZ(), 128.0);
            int j;
            if (entityplayer != null) {
                final Vec3D vec3d = new Vec3D(entityplayer.locX, 0.0, entityplayer.locZ).a();
                j = this.dragon.getNearestPpIdx(entityplayer.locX, 105.0, entityplayer.locZ);
                Bukkit.getLogger().info("Searching for player landing path: " + entityplayer.locX + " , z: " + entityplayer.locZ);
            } else {
                j = this.dragon.getNearestPpIdx(this.dragon.getHomeLocation().getX(), blockloc.getY(), 0.0 + this.dragon.getHomeLocation().getZ());
            }
            final BossPathPoint pathpoint = new BossPathPoint(blockloc.getBlockX(), blockloc.getBlockY(), blockloc.getBlockZ());
            this.currentPath = this.dragon.findPath(i, j, pathpoint);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }
        this.navigateToNextPathNode();
        if (this.currentPath != null && this.currentPath.isFinished()) {
            this.dragon.getPhaseManager().setPhase(PhaseList.LANDING);
        }
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            final Vec3D vec3d = this.currentPath.getCurrentPos();
            this.currentPath.incrementPathIndex();
            final double d0 = vec3d.a;
            final double d2 = vec3d.c;
            double d3;
            do {
                d3 = vec3d.b + this.dragon.random.nextFloat() * 20.0f;
            } while (d3 < vec3d.b);
            this.targetLocation = new Vec3D(d0, d3, d2);
        }
    }
}
