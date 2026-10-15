package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;

public class PhaseSittingAttacking extends PhaseSittingBase {
    private int attackingTicks;

    public PhaseSittingAttacking(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        this.dragon.world.makeSound(this.dragon.locX, this.dragon.locY, this.dragon.locZ, "mob.enderdragon.growl", 2.5f, 0.8f + this.dragon.random.nextFloat() * 0.3f);
    }

    @Override
    public void doLocalUpdate() {
        if (this.attackingTicks++ >= 5) {
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
        }
    }

    @Override
    public void initPhase() {
        this.attackingTicks = 0;
    }

    @Override
    public PhaseList<PhaseSittingAttacking> getType() {
        return PhaseList.SITTING_ATTACKING;
    }
}
