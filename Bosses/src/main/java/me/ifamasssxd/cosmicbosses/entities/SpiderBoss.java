package me.ifamasssxd.cosmicbosses.entities;

import net.minecraft.server.v1_8_R3.*;

public class SpiderBoss extends EntitySpider {
    public SpiderBoss(final World world) {
        super(world);
        this.getAttributeInstance(GenericAttributes.c).setValue(1.0);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(20.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(1.3);
    }

    public void m() {
        if (this.getGoalTarget() != null && !this.getGoalTarget().isAlive()) {
            this.setGoalTarget(null);
        }
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(20.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(2.0);
        super.m();
    }

    protected Entity findTarget() {
        if (this.getGoalTarget() == null || !this.getGoalTarget().isAlive() || this.getGoalTarget().world != this.world || this.random.nextInt(1000) <= 10 || this.getGoalTarget().getBukkitEntity().getLocation().distanceSquared(this.getBukkitEntity().getLocation()) > 200.0) {
            this.setGoalTarget(null);
            return null;
        }
        final EntityHuman human = this.world.findNearbyPlayer(this, 32.0);
        System.out.println("Setting target");
        this.setGoalTarget((EntityLiving) human);
        return this.getGoalTarget();
    }
}
