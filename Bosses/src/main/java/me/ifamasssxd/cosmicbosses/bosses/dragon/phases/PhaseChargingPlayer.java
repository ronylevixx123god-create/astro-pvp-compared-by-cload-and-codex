package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class PhaseChargingPlayer extends PhaseBase {
    private static final Logger LOGGER;
    private Vec3D targetLocation;
    private int timeSinceCharge;

    public PhaseChargingPlayer(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            PhaseChargingPlayer.LOGGER.warn("Aborting charge player as no target was set.");
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        } else if (this.timeSinceCharge > 0 && this.timeSinceCharge++ >= 10) {
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        } else {
            double d1 = this.dragon.locX - this.targetLocation.a;
            double d2 = this.dragon.locY - this.targetLocation.b;
            double d3 = this.dragon.locZ - this.targetLocation.c;

            final double d0 = MathHelper.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            if (d0 < 100.0 || d0 > 22500.0 || this.dragon.bA) {
                ++this.timeSinceCharge;
            }
        }
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
        this.timeSinceCharge = 0;
    }

    public void setTarget(final Vec3D target) {
        this.targetLocation = target;
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
    public PhaseList<PhaseChargingPlayer> getType() {
        return PhaseList.CHARGING_PLAYER;
    }

    static {
        LOGGER = LogManager.getLogger();
    }
}
