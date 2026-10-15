package me.ifamasssxd.cosmicbosses.dungeonboss;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;

import de.inventivegames.particle.ParticleEffect;
import me.ifamasssxd.cosmicbosses.dungeonboss.abilities.UnstoppableForce;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.EnumMonsterType;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IEntitySelector;
import net.minecraft.server.v1_8_R3.IRangedEntity;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class DungeonYijkiEntity extends EntityMonster implements IRangedEntity {
    private DungeonYijki boss;

    private float[] a = new float[2];

    private float[] b = new float[2];

    private float[] c = new float[2];

    private float[] bm = new float[2];

    private int[] nextShot;

    private int[] shootTimer;

    private int bv;

    int failedTimes;

    private long lastTargetCheck;

    private String lastTargetName;

    public static final Predicate<Entity> bq = new Predicate() {
        public boolean a(Entity entity) {
            return (entity instanceof EntityLiving && ((EntityLiving)entity).getMonsterType() != EnumMonsterType.UNDEAD);
        }

        public boolean apply(Object object) {
            return a((Entity)object);
        }
    };

    protected void h() {
        super.h();
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(0));
        this.datawatcher.a(19, new Integer(0));
        this.datawatcher.a(20, new Integer(0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Invul", cl());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        r(nbttagcompound.getInt("Invul"));
    }

    protected String z() {
        return "mob.wither.idle";
    }

    protected String bo() {
        return "mob.wither.hurt";
    }

    protected String bp() {
        return "mob.wither.death";
    }

    public DungeonYijkiEntity(World world) {
        super(world);
        final DungeonYijkiEntity instance = this;
        Predicate<Entity> entSelectorPredicate = new Predicate() {
            public boolean a(Entity entity) {
                return ((entity instanceof EntityHuman && instance instanceof me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijkiEntity) || WorldGuardUtils.isInRegionsFast(new Location(entity.world.getWorld(), entity.locX, entity.locY, entity.locZ), "hanger", "bridge"));
            }

            public boolean apply(Object object) {
                return a((Entity)object);
            }
        };
        this.nextShot = new int[2];
        this.shootTimer = new int[2];
        this.failedTimes = 100;
        this.lastTargetCheck = System.currentTimeMillis();
        this.lastTargetName = "";
        setHealth(getMaxHealth());
        a(new float[] { 0.9F, 4.0F });
        this.fireProof = true;
        ((Navigation)getNavigation()).d(true);
        this.goalSelector.a(0, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(6, (PathfinderGoal)new PathfinderGoalLookAtPlayer((EntityInsentient)this, EntityHuman.class, 8.0F));
        this.goalSelector.a(7, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
        this.targetSelector.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetSelector.a(2, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, EntityHuman.class, 0, false, false, entSelectorPredicate));
        this.b_ = 50;
    }

    public void setBoss(DungeonYijki boss) {
        if (boss != null) {
            this.boss = boss;
        } else {
            this.failedTimes--;
        }
        if (boss != null) {
            boolean isMOY = boss instanceof me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
            this.goalSelector.a(2, new PathfinderGoalDungeonWitherBossAttack(boss, this, 1.0D, isMOY ? 25 : 40, isMOY ? 50 : 70, isMOY ? 30.0F : 20.0F));
        }
    }

    public void collide(Entity entity) {
        if (this.boss != null && this.boss.getActiveAbility() instanceof UnstoppableForce && entity instanceof EntityHuman && ((UnstoppableForce)this.boss.getActiveAbility()).shouldCharge() && entity.isAlive() && !BossUtils.isInGodMode((Player)entity.getBukkitEntity())) {
            if (Math.abs(this.boss.getLocation().getBlockY() - entity.getBukkitEntity().getLocation().getBlockY()) < 4) {
                if (WorldGuardUtils.getRegionName(entity.getBukkitEntity().getLocation()).equals("start_pearlok") || WorldGuardUtils.getRegionName(entity.getBukkitEntity().getLocation()).equals("bridge_start"))
                    return;
                ((EntityHuman)entity).lastDamager = this;
                ((EntityHuman)entity).setHealth(0.0F);
                Bukkit.getLogger().info("Killing " + entity.getName() + " with unstoppable force!");
            }
            return;
        }
        super.collide(entity);
    }

    public void die() {
        super.die();
    }

    public void setGoalTarget(EntityLiving entityliving) {
        if (getGoalTarget() != null && entityliving == null) {
            this.boss.location = this.boss.getRandomSpawnLocation();
            Bukkit.getLogger().info("Setting new Yijki spawn to " + this.boss.location.getX() + "x, " + this.boss.location.getY() + "y, " + this.boss.location.getZ() + "z");
        }
        super.setGoalTarget(entityliving);
    }

    public void m() {
        if (this.boss != null) {
            if (this.boss.getActiveAbility() != null) {
                if (this.boss.getActiveAbility().shouldStopTicking()) {
                    try {
                        this.boss.clearAbilities();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    try {
                        this.boss.getActiveAbility().onTick();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            } else {
                long lastAbilityTime = this.boss.getAbilityActivateTime();
                if (this.boss.getActiveAbility() == null && lastAbilityTime >= 8000L) {
                    this.boss.findNewAbility();
                    Bukkit.getLogger().info("Finding new ability for YIjki: " + this.boss.getActiveAbility());
                }
            }
            if (this.boss.getEndOfWorldAbility() != null && !this.boss.getEndOfWorldAbility().shouldStopTicking()) {
                try {
                    this.boss.getEndOfWorldAbility().onTick();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        double motionMultiplier = 0.6000000238418579D;
        if (this.boss != null && this.boss.getActiveAbility() instanceof UnstoppableForce) {
            UnstoppableForce force = (UnstoppableForce)this.boss.getActiveAbility();
            Vector dir = force.getDirection();
            if (force.shouldCharge()) {
                List<EntityLiving> ents = this.world.a(EntityLiving.class, getBoundingBox().grow(1.0D, 1.0D, 1.0D));
                for (Entity inBox : ents) {
                    if (inBox instanceof EntityHuman) {
                        collide(inBox);
                        Bukkit.getLogger().info("Colliding with " + inBox.getName() + " manually due to collide() being missed.");
                    }
                }
                this.motX += dir.getX();
                this.motZ += dir.getZ();
                if (this.ticksLived % 2 == 0) {
                    try {
                        ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), getBukkitEntity().getLocation(), 0.01f, 0.01f, 0.01f, 1, 20);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            if (getGoalTarget() != null) {
                double targetY = (getGoalTarget()).locY;
                if (this.locY < targetY && this.motY < 0.0D)
                    this.motY = 0.0D;
            }
        } else {
            this.motY *= 0.6000000238418579D;
            if (s(0) > 0) {
                Entity entity = this.world.a(s(0));
                if (entity != null) {
                    Location entLoc = entity.getBukkitEntity().getLocation();
                    if (this.ticksLived % 10 == 0 && entLoc.getWorld().getName().endsWith("yijki_a") && !WorldGuardUtils.isInRegionsFast(entLoc, "bridge", "hanger")) {
                        Bukkit.getLogger().info("Untargetting " + entity.getName() + " due to leaving hanger or bridge region!");
                        getBukkitEntity().getLocation().getWorld().playSound(getBukkitEntity().getLocation(), Sound.ITEM_BREAK, 5.0F, 2.5F);
                        setGoalTarget((EntityLiving)null);
                        b(0, 0);
                        return;
                    }
                    moveTowardsLocation(entity.locX, entity.locY, entity.locZ, 0.6000000238418579D);
                }
            } else if (this.boss != null) {
                Location spawn = this.boss.getLocation();
                if (f(spawn.getX(), spawn.getY(), spawn.getZ()) > 10.0D)
                    moveTowardsLocation(spawn.getX(), spawn.getY(), spawn.getZ(), 0.6000000238418579D);
            }
        }
        if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806D)
            this.yaw = (float)Math.atan2(this.motZ, this.motX) * 57.295776F - 90.0F;
        super.m();
        int i;
        for (i = 0; i < 2; i++) {
            this.bm[i] = this.b[i];
            this.c[i] = this.a[i];
        }
        for (i = 0; i < 2; i++) {
            int k = s(i + 1);
            Entity entity2 = null;
            if (k > 0)
                entity2 = this.world.a(k);
            if (entity2 != null && entity2 instanceof EntityHuman) {
                double d0 = t(i + 1);
                double d1 = u(i + 1);
                double d2 = v(i + 1);
                double d4 = entity2.locX - d0;
                double d5 = entity2.locY + entity2.getHeadHeight() - d1;
                double d6 = entity2.locZ - d2;
                double d7 = MathHelper.sqrt(d4 * d4 + d6 * d6);
                float f = (float)(MathHelper.b(d6, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                float f2 = (float)-(MathHelper.b(d5, d7) * 180.0D / 3.1415927410125732D);
                this.a[i] = b(this.a[i], f2, 40.0F);
                this.b[i] = b(this.b[i], f, 10.0F);
            } else {
                this.b[i] = b(this.b[i], this.aM, 10.0F);
            }
        }
        boolean flag = cm();
        for (int j = 0; j < 3; j++) {
            double d8 = t(j);
            double d9 = u(j);
            double d10 = v(j);
            this.world.addParticle(EnumParticle.SMOKE_NORMAL, d8 + this.random.nextGaussian() * 0.30000001192092896D, d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
            if (flag && this.world.random.nextInt(4) == 0)
                this.world.addParticle(EnumParticle.SPELL_MOB, d8 + this.random.nextGaussian() * 0.30000001192092896D, d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
        }
        if (cl() > 0)
            for (this.j = 0.0D; this.j < 3.0D; this.j++)
                this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + this.random.nextGaussian() * 1.0D, this.locY + (this.random.nextFloat() * 3.3F), this.locZ + this.random.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
    }

    private void moveTowardsLocation(double x, double y, double z, double motionMultiplier) {
        double height = 7.0D;
        if (this.boss != null)
            if (this.boss.getActiveAbility() instanceof UnstoppableForce) {
                height = 0.0D;
            } else {
                height = (this.boss.getPhase() == Phase.TWO) ? 7.0D : 3.0D;
            }
        if (this.locY < y || this.locY < y + height) {
            if (this.motY < 0.0D)
                this.motY = 0.0D;
            this.motY += (0.5D - this.motY) * motionMultiplier / 2.0D;
        }
        double distanceX = x - this.locX;
        double distanceZ = z - this.locZ;
        double distanceSqr = distanceX * distanceX + distanceZ * distanceZ;
        double distanceFromPlayer = 9.0D;
        double speedMultiplier = 1.0D;
        if (this.boss != null) {
            distanceFromPlayer = (this.boss.getPhase() == Phase.TWO) ? 12.0D : 4.0D;
            speedMultiplier = (this.boss.getPhase() == Phase.TWO) ? 1.2D : 1.0D;
        }
        if (distanceSqr > distanceFromPlayer) {
            double distanceSqrRoot = MathHelper.sqrt(distanceSqr);
            this.motX += (distanceX / distanceSqrRoot * 0.7D - this.motX) * motionMultiplier * speedMultiplier;
            this.motZ += (distanceZ / distanceSqrRoot * 0.7D - this.motZ) * motionMultiplier * speedMultiplier;
        }
    }

    protected void E() {
        if (cl() > 0) {
            int witherTargetId = cl() - 1;
            if (witherTargetId <= 0) {
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 7.0F, false);
                this.world.getServer().getPluginManager().callEvent((Event)event);
                if (!event.isCancelled())
                    this.world.createExplosion((Entity)this, this.locX, this.locY + getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                this.world.createExplosion((Entity)this, this.locX, this.locY + getHeadHeight(), this.locZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
                int viewDistance = ((WorldServer)this.world).getServer().getViewDistance() * 16;
                for (Object playerp : this.world.players) {
                    EntityPlayer player = (EntityPlayer)playerp;
                    double deltaX = this.locX - player.locX;
                    double deltaZ = this.locZ - player.locZ;
                    double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if (this.world.spigotConfig.witherSpawnSoundRadius > 0 && distanceSquared > (this.world.spigotConfig.witherSpawnSoundRadius * this.world.spigotConfig.witherSpawnSoundRadius))
                        continue;
                    if (distanceSquared > (viewDistance * viewDistance)) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.locX + deltaX / deltaLength * viewDistance;
                        double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
                        player.playerConnection.sendPacket((Packet)new PacketPlayOutWorldEvent(1013, new BlockPosition((int)relativeX, (int)this.locY, (int)relativeZ), 0, true));
                        continue;
                    }
                    player.playerConnection.sendPacket((Packet)new PacketPlayOutWorldEvent(1013, new BlockPosition((int)this.locX, (int)this.locY, (int)this.locZ), 0, true));
                }
            }
            r(witherTargetId);
            if (this.ticksLived % 10 == 0)
                heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
        } else {
            super.E();
            int witherTargetId;
            for (witherTargetId = 1; witherTargetId < 3; witherTargetId++) {
                if (this.ticksLived >= this.nextShot[witherTargetId - 1]) {
                    this.nextShot[witherTargetId - 1] = this.ticksLived + 10 + this.random.nextInt(10);
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                        int targetId = witherTargetId - 1;
                        int value = this.shootTimer[targetId];
                        this.shootTimer[targetId] = value + 1;
                        if (value > 15) {
                            float width = 10.0F;
                            float height = 5.0F;
                            double x = MathHelper.a(this.random, this.locX - 10.0D, this.locX + 10.0D);
                            double y = MathHelper.a(this.random, this.locY - 5.0D, this.locY + 5.0D);
                            double z = MathHelper.a(this.random, this.locZ - 10.0D, this.locZ + 10.0D);
                            a(witherTargetId + 1, x, y, z, true);
                            this.shootTimer[witherTargetId - 1] = 0;
                        }
                    }
                    int target = s(witherTargetId);
                    if (target > 0) {
                        Entity entity = this.world.a(target);
                        if (entity instanceof EntityHuman && entity.isAlive() && h(entity) <= 900.0D) {
                            a(witherTargetId + 1, (EntityLiving)entity);
                            this.nextShot[witherTargetId - 1] = this.ticksLived + 5 + this.random.nextInt(20);
                            this.shootTimer[witherTargetId - 1] = 0;
                        } else {
                            b(witherTargetId, 0);
                        }
                    } else {
                        List<EntityLiving> list = this.world.a(EntityLiving.class, getBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(bq, IEntitySelector.d));
                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); i1++) {
                            EntityLiving entityliving = list.get(this.random.nextInt(list.size()));
                            if (entityliving != this && entityliving.isAlive() && hasLineOfSight((Entity)entityliving)) {
                                if (entityliving instanceof EntityHuman) {
                                    if (!((EntityHuman)entityliving).abilities.isInvulnerable)
                                        b(witherTargetId, entityliving.getId());
                                    break;
                                }
                                b(witherTargetId, entityliving.getId());
                                break;
                            }
                            list.remove(entityliving);
                        }
                    }
                }
            }
            if (getGoalTarget() != null && getGoalTarget() instanceof EntityHuman && getGoalTarget().isAlive()) {
                b(0, getGoalTarget().getId());
            } else {
                b(0, 0);
            }
            if (this.bv > 0) {
                this.bv--;
                if (this.bv == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    witherTargetId = MathHelper.floor(this.locY);
                    int target = MathHelper.floor(this.locX);
                    int j1 = MathHelper.floor(this.locZ);
                    boolean flag = false;
                    for (int k1 = -1; k1 <= 1; k1++) {
                        for (int l1 = -1; l1 <= 1; l1++) {
                            for (int i2 = 0; i2 <= 3; i2++) {
                                int j2 = target + k1;
                                int k2 = witherTargetId + i2;
                                int l2 = j1 + l1;
                                Block block = this.world.getType(new BlockPosition(j2, k2, l2)).getBlock();
                                if (block.getMaterial() != Material.AIR && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME && block != Blocks.COMMAND_BLOCK &&
                                        !CraftEventFactory.callEntityChangeBlockEvent((Entity)this, j2, k2, l2, Blocks.AIR, 0).isCancelled())
                                    flag = (this.world.setAir(new BlockPosition(j2, k2, l2), true) || flag);
                            }
                        }
                    }
                    if (flag)
                        this.world.a((EntityHuman)null, 1012, new BlockPosition((Entity)this), 0);
                }
            }
            if (this.ticksLived % 120 == 0)
                heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
        }
    }

    public void bZ() {
        r(220);
        setHealth(getMaxHealth() / 3.0F);
    }

    public void as() {}

    public int aV() {
        return 4;
    }

    private double t(int i) {
        if (i <= 0)
            return this.locX;
        float f = (this.aI + (180 * (i - 1))) / 180.0F * 3.1415927F;
        float f1 = MathHelper.cos(f);
        return this.locX + f1 * 1.3D;
    }

    private double u(int i) {
        return (i <= 0) ? (this.locY + 3.0D) : (this.locY + 2.2D);
    }

    private double v(int i) {
        if (i <= 0)
            return this.locZ;
        float f = (this.aI + (180 * (i - 1))) / 180.0F * 3.1415927F;
        float f1 = MathHelper.sin(f);
        return this.locZ + f1 * 1.3D;
    }

    private float b(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);
        if (f3 > f2)
            f3 = f2;
        if (f3 < -f2)
            f3 = -f2;
        return f + f3;
    }

    private void a(int i, EntityLiving entityliving) {
        a(i, entityliving.locX, entityliving.locY + entityliving.getHeadHeight() * 0.5D, entityliving.locZ, (i == 0 && this.random.nextFloat() < 0.001F));
    }

    private void a(int entityID, double spawnX, double spawnY, double spawnZ, boolean flag) {
        this.world.a((EntityHuman)null, 1014, new BlockPosition((Entity)this), 0);
        double x = t(entityID);
        double y = u(entityID);
        double z = v(entityID);
        double d6 = spawnX - x;
        double d7 = spawnY - y;
        double d8 = spawnZ - z;
        final boolean fromMOY = this instanceof me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijkiEntity;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, (EntityLiving)this, d6 * 1.2D, d7, d8 * 1.2D) {
            protected void a(MovingObjectPosition movingobjectposition) {
                Entity colliding = movingobjectposition.entity;
                if (colliding != null && !(colliding instanceof EntityHuman))
                    return;
                super.a(movingobjectposition);
            }

            public void setDirection(double d0, double d1, double d2) {
                if (!fromMOY) {
                    d0 += this.random.nextGaussian() * 0.4D;
                    d1 += this.random.nextGaussian() * 0.4D;
                    d2 += this.random.nextGaussian() * 0.4D;
                }
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                this.dirX = d0 / d3 * 0.1D;
                this.dirY = d1 / d3 * 0.1D;
                this.dirZ = d2 / d3 * 0.1D;
            }
        };
        entitywitherskull.locY = y;
        entitywitherskull.locX = x;
        entitywitherskull.locZ = z;
        ProjectileLaunchEvent event = new ProjectileLaunchEvent(entitywitherskull.getBukkitEntity());
        Bukkit.getPluginManager().callEvent((Event)event);
        this.world.addEntity((Entity)entitywitherskull);
    }

    public void a(EntityLiving entityliving, float f) {
        a(0, entityliving);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (isInvulnerable(damagesource))
            return false;
        if (damagesource == DamageSource.DROWN || damagesource == DamageSource.WITHER)
            return false;
        if (cl() > 0)
            return false;
        Entity entity = damagesource.getEntity();
        if ((entity != null && !(entity instanceof EntityHuman) && entity instanceof EntityLiving && ((EntityLiving)entity).getMonsterType() == getMonsterType()) || entity instanceof EntityWitherSkull)
            return false;
        if (this.bv <= 0)
            this.bv = 20;
        for (int i = 0; i < this.shootTimer.length; i++) {
            int[] shootTimer = this.shootTimer;
            int n = i;
            shootTimer[n] = shootTimer[n] + 3;
        }
        return super.damageEntity(damagesource, f);
    }

    protected void dropDeathLoot(boolean flag, int i) {}

    protected void D() {
        this.ticksFarFromPlayer = 0;
    }

    protected void b(float f) {}

    public void addEffect(MobEffect mobeffect) {}

    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579D);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0D);
    }

    public int cl() {
        return this.datawatcher.getInt(20);
    }

    public void r(int i) {
        this.datawatcher.watch(20, Integer.valueOf(i));
    }

    public int s(int i) {
        return this.datawatcher.getInt(17 + i);
    }

    public void b(int i, int j) {
        this.datawatcher.watch(17 + i, Integer.valueOf(j));
    }

    public boolean cm() {
        return (getHealth() <= getMaxHealth() / 2.0F);
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    public void mount(Entity entity) {
        this.vehicle = null;
    }
}
