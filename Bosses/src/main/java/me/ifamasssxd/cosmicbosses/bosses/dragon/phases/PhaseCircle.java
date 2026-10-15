package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;

public class PhaseCircle extends PhaseBase {
    public PhaseCircle(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public PhaseList<? extends IPhase> getType() {
        return null;
    }
}
