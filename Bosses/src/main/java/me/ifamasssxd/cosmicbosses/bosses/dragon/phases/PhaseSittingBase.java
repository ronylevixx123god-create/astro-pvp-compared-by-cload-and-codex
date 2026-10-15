package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import net.minecraft.server.v1_8_R3.*;

public abstract class PhaseSittingBase extends PhaseBase {
    public PhaseSittingBase(final EndDragonEntity p_i46794_1_) {
        super(p_i46794_1_);
    }

    @Override
    public boolean getIsStationary() {
        return true;
    }

    @Override
    public float getAdjustedDamage(final EntityComplexPart pt, final DamageSource src, final float damage) {
        if (src.i() instanceof EntityArrow || src.i() instanceof EntityWitherSkull || src.i() instanceof EntityFireball) {
            src.i().setOnFire(1);
            return 0.0f;
        }
        return super.getAdjustedDamage(pt, src, damage);
    }
}
