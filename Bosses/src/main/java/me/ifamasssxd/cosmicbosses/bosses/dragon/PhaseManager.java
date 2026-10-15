package me.ifamasssxd.cosmicbosses.bosses.dragon;

import me.ifamasssxd.cosmicbosses.bosses.dragon.phases.IPhase;
import me.ifamasssxd.cosmicbosses.bosses.dragon.phases.PhaseList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseManager {
    private static final Logger LOGGER;
    private final EndDragonEntity dragon;
    private final IPhase[] phases;
    private IPhase phase;

    public PhaseManager(final EndDragonEntity dragonIn) {
        this.phases = new IPhase[PhaseList.getTotalPhases()];
        this.dragon = dragonIn;
        this.setPhase(PhaseList.HOVER);
    }

    public void setPhase(final PhaseList<?> phaseIn) {
        if (this.phase == null || phaseIn != this.phase.getType()) {
            if (this.phase != null) {
                this.phase.removeAreaEffect();
            }
            this.phase = this.getPhase(phaseIn);
            if (!this.dragon.world.isClientSide) {
                this.dragon.phase = this.phase;
            }
            PhaseManager.LOGGER.debug("Dragon is now in phase {} on the {}", phaseIn, this.dragon.world.isClientSide ? "client" : "server");
            this.phase.initPhase();
        }
    }

    public IPhase getCurrentPhase() {
        return this.phase;
    }

    public <T extends IPhase> T getPhase(final PhaseList<T> phaseIn) {
        final int i = phaseIn.getId();
        if (this.phases[i] == null) {
            this.phases[i] = phaseIn.createPhase(this.dragon);
        }
        return (T) this.phases[i];
    }

    static {
        LOGGER = LogManager.getLogger();
    }
}
