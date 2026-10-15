package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;

public abstract class PhaseBase implements IPhase {
    protected final EndDragonEntity dragon;

    public PhaseBase(final EndDragonEntity dragonIn) {
        this.dragon = dragonIn;
    }

    @Override
    public boolean getIsStationary() {
        return false;
    }

    public void doClientRenderEffects() {
    }

    @Override
    public void doLocalUpdate() throws NoSuchFieldException {
    }

    @Override
    public void onCrystalDestroyed(final EntityEnderCrystal crystal, final Location pos, final DamageSource dmgSrc, final EntityPlayer plyr) {
    }

    @Override
    public void initPhase() {
    }

    @Override
    public void removeAreaEffect() {
    }

    @Override
    public float getMaxRiseOrFall() {
        return 0.6f;
    }

    @Override
    public Vec3D getTargetLocation() {
        return null;
    }

    @Override
    public float getAdjustedDamage(final EntityComplexPart pt, final DamageSource src, final float damage) {
        return damage;
    }

    @Override
    public float getYawFactor() {
        final float f = MathHelper.sqrt(this.dragon.motX * this.dragon.motX + this.dragon.motZ * this.dragon.motZ) + 1.0f;
        final float f2 = Math.min(f, 40.0f);
        return 0.7f / f2 / f;
    }
}
