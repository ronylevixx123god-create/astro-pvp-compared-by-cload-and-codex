package me.ifamasssxd.cosmicbosses.entities;

import net.minecraft.server.v1_8_R3.*;

public class CaveSpiderMinion extends EntityCaveSpider {
    public CaveSpiderMinion(final World arg0) {
        super(arg0);
    }

    protected Entity findTarget() {
        final EntityHuman human = this.world.findNearbyPlayer(this, 16.0);
        this.setGoalTarget((EntityLiving) human);
        return human;
    }
}
