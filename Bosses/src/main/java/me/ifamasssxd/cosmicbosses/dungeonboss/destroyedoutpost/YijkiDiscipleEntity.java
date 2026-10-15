package me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost;

import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.PathfinderGoalBossArrowAttack;
import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.PathfinderGoalBossMeleeAttack;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;

public class YijkiDiscipleEntity extends EntitySkeleton {
    YijkiDisciple boss;

    public YijkiDiscipleEntity(final World world) {
        super(world);
        ReflectionUtils.setObject(EntitySkeleton.class, this, new PathfinderGoalBossArrowAttack((IRangedEntity) this, 1.0, 20, 60, 15.0f), "a");
        ReflectionUtils.setObject(EntitySkeleton.class, this, new PathfinderGoalBossMeleeAttack((EntityCreature) this, EntityHuman.class, 1.2, false), "b");
        final List<?> list = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, this.goalSelector, "b");
        list.clear();
        this.goalSelector.a(1, (PathfinderGoal) new PathfinderGoalFloat((EntityInsentient) this));
        this.goalSelector.a(2, (PathfinderGoal) new PathfinderGoalMeleeAttack((EntityCreature) this, (Class) EntityHuman.class, 1.0, false));
        this.goalSelector.a(3, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 8.0f));
        this.goalSelector.a(4, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
        this.targetSelector.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget((EntityCreature) this, false));
        this.targetSelector.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) this, (Class) EntityHuman.class, false, true));
        if (world != null && !world.isClientSide) {
            this.n();
        }
    }

    protected Entity findTarget() {
        final EntityHuman human = this.world.findNearbyPlayer((Entity) this, 16.0);
        if (human != null && !human.getBukkitEntity().hasMetadata("NPC")) {
            return (Entity) human;
        }
        return null;
    }

    public void m() {
        if (this.boss == null) {
            this.boss = (YijkiDisciple) Boss.bosses.get(this.getUniqueID());
        }
        if (this.boss != null && this.boss.invincible) {
            return;
        }

        if (this.getGoalTarget() == null) {
            final Entity target = this.findTarget();
            if (target != null) {
                final EntityTargetEvent event = new EntityTargetEvent((org.bukkit.entity.Entity) this.getBukkitEntity(), (org.bukkit.entity.Entity) target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.world.getServer().getPluginManager().callEvent((Event) event);
                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.setGoalTarget(null);
                    } else {
                        this.setGoalTarget((EntityLiving) ((CraftEntity) event.getTarget()).getHandle());
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

    public void a(final EntityLiving entityliving, final float f) {
        final EntityArrow entityarrow = new EntityArrow(this.world, (EntityLiving) this, entityliving, 1.6f, (float) (14 - this.world.getDifficulty().a() * 4));
        final int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, this.bA());
        final int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, this.bA());
        entityarrow.b(2.0);
        if (i > 0) {
            entityarrow.b(entityarrow.j() + i * 0.5 + 0.5);
        }
        if (j > 0) {
            entityarrow.setKnockbackStrength(j);
        }
        if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, this.bA()) > 0 || this.getSkeletonType() == 1) {
            final EntityCombustEvent event = new EntityCombustEvent((org.bukkit.entity.Entity) entityarrow.getBukkitEntity(), 100);
            this.world.getServer().getPluginManager().callEvent((Event) event);
            if (!event.isCancelled()) {
                entityarrow.setOnFire(event.getDuration());
            }
        }
        final EntityShootBowEvent event2 = CraftEventFactory.callEntityShootBowEvent((EntityLiving) this, this.bA(), entityarrow, 0.8f);
        if (event2.isCancelled()) {
            event2.getProjectile().remove();
            return;
        }
        if (event2.getProjectile() == entityarrow.getBukkitEntity()) {
            this.world.addEntity((Entity) entityarrow);
        }
        this.makeSound("random.bow", 1.0f, 1.0f / (this.bc().nextFloat() * 0.4f + 0.8f));
    }
}
