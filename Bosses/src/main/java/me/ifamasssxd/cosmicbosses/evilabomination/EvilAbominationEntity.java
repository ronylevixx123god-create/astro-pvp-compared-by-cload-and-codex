package me.ifamasssxd.cosmicbosses.evilabomination;

import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.PathfinderGoalBossMeleeAttack;
import me.ifamasssxd.cosmicbosses.bosses.yijki.PathfinderGoalWitherBossArrowAttack;
import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;

import java.util.List;

public class EvilAbominationEntity extends EntityZombie implements IRangedEntity {
    private EvilAbomination boss;

    public EvilAbominationEntity(final EvilAbomination boss, final World world) {
        super(world);
        this.boss = boss;
    }

    @Override
    public void a(EntityLiving entityLiving, float v) {

    }

    public EvilAbominationEntity(final World world) {
        super(world);
        final List<?> list = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, this.goalSelector, "b");
        list.clear();
        this.goalSelector.a(0, (PathfinderGoal) new PathfinderGoalWitherBossArrowAttack((IRangedEntity) this, 1.0, 10, 25, 23.0f));
        this.goalSelector.a(0, (PathfinderGoal) new PathfinderGoalBossMeleeAttack((EntityCreature) this, EntityHuman.class, 1.2, false));
        this.goalSelector.a(1, (PathfinderGoal) new PathfinderGoalFloat((EntityInsentient) this));
        this.goalSelector.a(3, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 8.0f));
        this.goalSelector.a(4, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
        this.targetSelector.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget((EntityCreature) this, false));
        this.targetSelector.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) this, (Class) EntityHuman.class, false, true));
        /*if (world != null && !world.isClientSide) {
            this.bZ();
        }*/
    }

    /*
    protected Entity findTarget() {
        final EntityHuman human = this.world.findNearbyVulnerablePlayer((Entity)this, 16.0);
        if (human != null && !human.getBukkitEntity().hasMetadata("NPC")) {
            return (Entity)human;
        }
        return null;
    }
    
    public void shootNearby(final EntityLiving originalTarget) {
        if (this.boss == null || this.boss.getPhase() != Phase.TWO) {
            return;
        }
        int shots = 0;
        final int max = this.random.nextInt(5) + 6;
        for (final org.bukkit.entity.Entity entity : this.getBukkitEntity().getNearbyEntities(32.0, 32.0, 32.0)) {
            if (shots >= max) {
                break;
            }
            if (!(entity instanceof Player) || Math.random() > 0.9) {
                continue;
            }
            final Entity ent = ((CraftEntity)entity).getHandle();
            if (ent.equals((Object)originalTarget)) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)ent;
            if (player.getBukkitEntity().getGameMode() != GameMode.SURVIVAL) {
                continue;
            }
            ++shots;
            this.a((EntityLiving)ent, 1.0f);
        }
    }
    
    public void a(final EntityLiving entityliving, final float f) {
        final Location headLoc = this.getBukkitEntity().getLocation().add(0.0, (double)this.getHeadHeight(), 0.0);
        final LivingEntity ent = (LivingEntity)entityliving.getBukkitEntity();
        final Vector velocity = headLoc.toVector().subtract(ent.getLocation().add(0.0, 0.2, 0.0).toVector());
        if (velocity.length() != 0.0) {
            velocity.normalize();
        }
        velocity.multiply(-1);
        final Vector spawnVel = velocity.clone().multiply(3);
        final EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, spawnVel.getX(), spawnVel.getY(), spawnVel.getZ()) {
            protected void a(final MovingObjectPosition movingobjectposition) {
                final Entity colliding = movingobjectposition.entity;
                if (colliding != null && !(colliding instanceof EntityHuman)) {
                    return;
                }
                super.a(movingobjectposition);
            }
            
            protected float e() {
                return 0.95f;
            }
            
            public void setDirection(final double d0, final double d1, final double d2) {
                final double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                this.dirX = d0 / d3 * 0.1;
                this.dirY = d1 / d3 * 0.1;
                this.dirZ = d2 / d3 * 0.1;
            }
        };
        if (this.random.nextDouble() <= ((this.vehicle != null && this.vehicle.isAlive()) ? 0.13 : 0.18) && this.boss != null && this.boss.getMinions().size() < 10) {
            entitywitherskull.setCharged(true);
        }
        final Location spawnLoc = headLoc.add(velocity.getX(), 0.0, velocity.getZ());
        entitywitherskull.locY = spawnLoc.getY();
        entitywitherskull.locX = spawnLoc.getX();
        entitywitherskull.locZ = spawnLoc.getZ();
        this.world.addEntity((Entity)entitywitherskull);
        this.makeSound("random.bow", 1.0f, 1.0f / (this.aI().nextFloat() * 0.4f + 0.8f));
    }*/
}
