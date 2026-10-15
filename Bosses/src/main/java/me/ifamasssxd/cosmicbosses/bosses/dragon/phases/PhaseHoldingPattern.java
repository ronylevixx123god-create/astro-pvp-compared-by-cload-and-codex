package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.struct.BossPath;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class PhaseHoldingPattern extends PhaseBase {
    private BossPath currentPath;
    private Vec3D targetLocation;
    private boolean clockwise;

    public PhaseHoldingPattern(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public PhaseList<PhaseHoldingPattern> getType() {
        return PhaseList.HOLDING_PATTERN;
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

    @Override
    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Nullable
    @Override
    public Vec3D getTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() throws NoSuchFieldException {
        if (this.currentPath != null && this.currentPath.isFinished()) {
            final Location blockpos = this.dragon.homeLocation;
            final int i = (this.dragon.getFightManager() == null) ? 2 : (this.dragon.getFightManager().getNumAliveCrystals() + 2);
            final int secondsSince = (int) ((System.currentTimeMillis() - this.dragon.timeSinceLastApproach) / 1000L);
            final int offset = Math.max(0, 15 - secondsSince);
            if (this.dragon.random.nextInt(i + 3 + offset) == 0) {
                this.dragon.timeSinceLastApproach = System.currentTimeMillis();
                this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
                return;
            }
            double d0 = 128.0;
            final EntityHuman entityplayer = this.dragon.world.findNearbyPlayer(blockpos.getX(), blockpos.getY(), blockpos.getZ(), d0);
            if (entityplayer != null) {
                d0 = entityplayer.e((double) blockpos.getBlockX(), (double) blockpos.getBlockY(), (double) blockpos.getBlockZ()) / 512.0;
            }
            if (entityplayer != null && (this.dragon.random.nextInt(Math.abs((int) d0) + 2) == 0 || this.dragon.random.nextInt(i + 2) == 0)) {
                this.strafePlayer((EntityPlayer) entityplayer);
                return;
            }
        }
        if (this.currentPath == null || this.currentPath.isFinished()) {
            int k;
            final int j = k = this.dragon.initPathPoints();
            if (this.dragon.random.nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                k = j + 6;
            }
            if (this.clockwise) {
                ++k;
            } else {
                --k;
            }
            if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() >= 0) {
                k %= 12;
                if (k < 0) {
                    k += 12;
                }
            } else {
                k -= 12;
                k &= 0x7;
                k += 12;
            }
            this.currentPath = this.dragon.findPath(j, k, null);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }
        this.navigateToNextPathNode();
    }

    private void strafePlayer(final EntityPlayer player) {
        //this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
        //this.dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER).setTarget((EntityLiving)player);
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

    @Override
    public void onCrystalDestroyed(final EntityEnderCrystal crystal, final Location pos, final DamageSource dmgSrc, final EntityPlayer plyr) {
        if (plyr != null && !plyr.abilities.isInvulnerable) {
            this.strafePlayer(plyr);
        }
    }
}
