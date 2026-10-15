package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;

public class CorruptCosmonautEntity extends EntitySkeleton {
    private CorruptCosmonaut boss;

    public CorruptCosmonautEntity(World world) {
        super(world);
    }

    public void setBoss(final CorruptCosmonaut boss) {
        this.boss = boss;
    }

    protected Entity findTarget() {
        final EntityHuman human = this.world.findNearbyPlayer((Entity) this, 16.0);
        if (human != null && !human.getBukkitEntity().hasMetadata("NPC")) {
            return (Entity) human;
        }
        return null;
    }

    @SneakyThrows
    public void m() {
        if (this.boss != null) {
            if (this.boss.getActiveAbility() != null) {
                if (this.boss.getActiveAbility().shouldStopTicking()) {
                    this.boss.clearAbilities();
                } else {
                    this.boss.getActiveAbility().onTick();
                }
            } else {
                final long lastAbilityTime = this.boss.getAbilityActivateTime();
                if (System.currentTimeMillis() - this.boss.spawnTime >= 4000L) {
                    if (this.boss.getActiveAbility() == null && lastAbilityTime >= 8000L) {
                        this.boss.findNewAbility();
                    }
                }
            }
        }

        this.world.methodProfiler.a("ai");

        final float f11 = 16.0f;
        if (this.getGoalTarget() == null) {
            final Entity target = this.findTarget();
            if (target != null) {
                final EntityTargetEvent event = new EntityTargetEvent((org.bukkit.entity.Entity) this.getBukkitEntity(), (org.bukkit.entity.Entity) target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.world.getServer().getPluginManager().callEvent((Event) event);
                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.setGoalTarget(null);
                    } else {
                        try {
                            this.setGoalTarget((EntityLiving) ((CraftEntity) event.getTarget()).getHandle());

                        } catch (Exception e) {

                        }
                    }
                }
            }
            if (this.getGoalTarget() != null) {
                //this.getNavigation().d = this.getNavigation().a(this.getGoalTarget());
            }
        } else if (this.getGoalTarget().isAlive()) {

        } else {
            final EntityTargetEvent event2 = new EntityTargetEvent((org.bukkit.entity.Entity) this.getBukkitEntity(), (org.bukkit.entity.Entity) null, EntityTargetEvent.TargetReason.TARGET_DIED);
            this.world.getServer().getPluginManager().callEvent((Event) event2);
            if (!event2.isCancelled()) {
                if (event2.getTarget() == null) {
                    System.out.println("target dead");
                    this.setGoalTarget(null);
                } else {
                    this.setGoalTarget((EntityLiving) ((CraftEntity) event2.getTarget()).getHandle());
                }
            }
        }
        if (this.getGoalTarget() instanceof EntityPlayer && ((EntityPlayer) this.getGoalTarget()).playerInteractManager.isCreative()) {
            this.setGoalTarget(null);
        }
        this.world.methodProfiler.b();
        //if (this.getGoalTarget() != null && (this.getNavigation().d == null || this.random.nextInt(20) == 0)) {
            //this.getNavigation().d = this.getNavigation().a(this.getGoalTarget());
        //}

        super.m();
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(20.0D);
        this.getAttributeInstance(GenericAttributes.c).setValue(2.0D);
    }
}
