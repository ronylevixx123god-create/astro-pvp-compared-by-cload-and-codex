package me.ifamasssxd.cosmicbosses.bosses;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IRangedEntity;

public interface MultiProjectileShooter extends IRangedEntity {
    void shootNearby(final EntityLiving p0);
}
