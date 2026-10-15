package me.ifamasssxd.cosmicbosses.bosses.yijki;

import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijkiEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;

public class WitherBoss extends EntityMonster implements IRangedEntity {
    private Boss boss;
    private float[] bp;
    private float[] bq;
    private float[] br;
    private float[] bs;
    private int[] nextShot;
    private int[] shootTimer;
    private int bv;
    private IEntitySelector bw;
    int failedTimes;
    private long lastTargetCheck;
    private String lastTargetName;

    protected void h() {
        super.h();
        this.datawatcher.a(17, 0);
        this.datawatcher.a(18, 0);
        this.datawatcher.a(19, 0);
        this.datawatcher.a(20, 0);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Invul", this.cl());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.r(nbttagcompound.getInt("Invul"));
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

    public WitherBoss(final World world) {
        super(world);
        this.bp = new float[2];
        this.bq = new float[2];
        this.br = new float[2];
        this.bs = new float[2];
        this.nextShot = new int[2];
        this.shootTimer = new int[2];
        this.failedTimes = 100;
        this.lastTargetCheck = System.currentTimeMillis();
        this.lastTargetName = "";
        try {
            this.bw = (IEntitySelector) ReflectionUtils.getObject(EntityWither.class, this, "bw");
        } catch (Exception e) {
            e.printStackTrace();
            this.bw = null;
        }
        this.setHealth(this.getMaxHealth());
        this.a(0.9f, 4.0f);
        this.fireProof = true;
        ((Navigation) this.getNavigation()).e(true);
        this.goalSelector.a(0, (PathfinderGoal) new PathfinderGoalFloat((EntityInsentient) this));
        this.goalSelector.a(6, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 8.0f));
        this.goalSelector.a(7, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
        this.targetSelector.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget((EntityCreature) this, false));
        this.targetSelector.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) this, (Class) EntityHuman.class, 0, false, false, DungeonYijkiEntity.bq));
        this.b_ = 50;
    }

    public void setBoss(final Boss boss) {
        if (boss != null) {
            this.boss = boss;
        } else {
            --this.failedTimes;
        }
        if (boss != null) {
            this.goalSelector.a(2, (PathfinderGoal) new PathfinderGoalWitherBossArrowAttack(boss, (IRangedEntity) this, 1.0, 40, 40, 20.0f));
        }
        System.out.println("Given: " + boss);
    }

    public void m() {
        if (this.boss == null && this.failedTimes > 0) {
            if (this.ticksLived > 10) {
                this.setBoss(this.boss = Boss.bosses.get(this.getUniqueID()));
            }
            return;
        }
        if (this.failedTimes == 0) {
            this.die();
            return;
        }
        final double motionMultiplier = 0.6000000238418579;
        this.motY *= motionMultiplier;
        if (!this.world.isClientSide && this.s(0) > 0) {
            final Entity entity = this.world.a(this.s(0));
            if (entity != null) {
                double height = 7.0;
                if (this.boss != null) {
                    height = ((this.boss.getPhase() == Phase.TWO) ? 7.0 : 3.0);
                }
                if (this.locY < entity.locY || this.locY < entity.locY + height) {
                    if (this.motY < 0.0) {
                        this.motY = 0.0;
                    }
                    this.motY += (0.5 - this.motY) * (motionMultiplier / 2.0);
                }
                final double distanceX = entity.locX - this.locX;
                final double distanceZ = entity.locZ - this.locZ;
                final double distanceSqr = distanceX * distanceX + distanceZ * distanceZ;
                double distanceFromPlayer = 9.0;
                double speedMultiplier = 1.0;
                if (this.boss != null) {
                    distanceFromPlayer = ((this.boss.getPhase() == Phase.TWO) ? 12.0 : 4.0);
                    speedMultiplier = ((this.boss.getPhase() == Phase.TWO) ? 1.2 : 1.0);
                }
                if (distanceSqr > distanceFromPlayer) {
                    final double distanceSqrRoot = MathHelper.sqrt(distanceSqr);
                    this.motX += (distanceX / distanceSqrRoot * 0.7 - this.motX) * (motionMultiplier * speedMultiplier);
                    this.motZ += (distanceZ / distanceSqrRoot * 0.7 - this.motZ) * (motionMultiplier * speedMultiplier);
                }
            }
        }
        if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806) {
            this.yaw = (float) Math.atan2(this.motZ, this.motX) * 57.295776f - 90.0f;
        }
        super.m();
        for (int i = 0; i < 2; ++i) {
            this.bs[i] = this.bq[i];
            this.br[i] = this.bp[i];
        }
        for (int i = 0; i < 2; ++i) {
            final int j = this.s(i + 1);
            Entity entity2 = null;
            if (j > 0) {
                entity2 = this.world.a(j);
            }
            if (entity2 != null && entity2 instanceof EntityHuman) {
                final double distanceZ = this.t(i + 1);
                final double distanceSqr = this.u(i + 1);
                final double distanceSqrRoot = this.v(i + 1);
                final double d4 = entity2.locX - distanceZ;
                final double d5 = entity2.locY + entity2.getHeadHeight() - distanceSqr;
                final double d6 = entity2.locZ - distanceSqrRoot;
                final double d7 = MathHelper.sqrt(d4 * d4 + d6 * d6);
                final float f = (float) (Math.atan2(d6, d4) * 180.0 / 3.1415927410125732) - 90.0f;
                final float f2 = (float) (-(Math.atan2(d5, d7) * 180.0 / 3.1415927410125732));
                this.bp[i] = this.b(this.bp[i], f2, 40.0f);
                this.bq[i] = this.b(this.bq[i], f, 10.0f);
            } else {
                this.bq[i] = this.b(this.bq[i], this.aM, 10.0f);
            }
        }
        final boolean flag = this.cb();
        for (int j = 0; j < 3; ++j) {
            final double d8 = this.t(j);
            final double d9 = this.u(j);
            final double d10 = this.v(j);
            if (!this.isInvisible()) {
                this.world.addParticle(EnumParticle.SMOKE_NORMAL, d8 + this.random.nextGaussian() * 0.30000001192092896, d9 + this.random.nextGaussian() * 0.30000001192092896, d10 + this.random.nextGaussian() * 0.30000001192092896, 0.0, 0.0, 0.0);
            }
            if (flag && this.world.random.nextInt(4) == 0) {
                this.world.addParticle(EnumParticle.SPELL_MOB, d8 + this.random.nextGaussian() * 0.30000001192092896, d9 + this.random.nextGaussian() * 0.30000001192092896, d10 + this.random.nextGaussian() * 0.30000001192092896, 0.699999988079071, 0.699999988079071, 0.5);
            }
        }
        if (this.cl() > 0) {
            for (int j = 0; j < 3; ++j) {
                this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + this.random.nextGaussian() * 1.0, this.locY + this.random.nextFloat() * 3.3f, this.locZ + this.random.nextGaussian() * 1.0, 0.699999988079071, 0.699999988079071, 0.8999999761581421);
            }
        }
    }

    protected void E() {
        if (this.cl() > 0) {
            final int witherTargetId = this.cl() - 1;
            if (witherTargetId <= 0) {
                final ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Entity) this.getBukkitEntity(), 7.0f, false);
                this.world.getServer().getPluginManager().callEvent((Event) event);
                if (!event.isCancelled()) {
                    this.world.createExplosion((Entity) this, this.locX, this.locY + this.getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                }
                this.world.createExplosion((Entity) this, this.locX, this.locY + this.getHeadHeight(), this.locZ, 7.0f, false, this.world.getGameRules().getBoolean("mobGriefing"));
                final int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
                for (final EntityHuman player : (List<EntityHuman>) this.world.players) {
                    if (!(player instanceof EntityPlayer)) continue;

                    final double deltaX = this.locX - player.locX;
                    final double deltaZ = this.locZ - player.locZ;
                    final double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if (this.world.spigotConfig.witherSpawnSoundRadius > 0 && distanceSquared > this.world.spigotConfig.witherSpawnSoundRadius * this.world.spigotConfig.witherSpawnSoundRadius) {
                        continue;
                    }
                    if (distanceSquared > viewDistance * viewDistance) {
                        final double deltaLength = Math.sqrt(distanceSquared);
                        final double relativeX = player.locX + deltaX / deltaLength * viewDistance;
                        final double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
                        ((EntityPlayer) player).playerConnection.sendPacket((Packet) new PacketPlayOutWorldEvent(1013, new BlockPosition((int) relativeX, (int) this.locY, (int) relativeZ), 0, true));
                    } else {
                        ((EntityPlayer) player).playerConnection.sendPacket((Packet) new PacketPlayOutWorldEvent(1013, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0, true));
                    }
                }
            }
            this.s(witherTargetId);
            if (this.ticksLived % 10 == 0) {
                this.heal(10.0f, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
            }
        } else {
            super.E();
            for (int witherTargetId = 1; witherTargetId < 3; ++witherTargetId) {
                if (this.ticksLived >= this.nextShot[witherTargetId - 1]) {
                    this.nextShot[witherTargetId - 1] = this.ticksLived + 10 + this.random.nextInt(10);
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                        final int targetId = witherTargetId - 1;
                        final int value = this.shootTimer[targetId];
                        this.shootTimer[targetId] = value + 1;
                        if (value > 15) {
                            final float width = 10.0f;
                            final float height = 5.0f;
                            final double x = MathHelper.a(this.random, this.locX - width, this.locX + width);
                            final double y = MathHelper.a(this.random, this.locY - height, this.locY + height);
                            final double z = MathHelper.a(this.random, this.locZ - width, this.locZ + width);
                            this.a(witherTargetId + 1, x, y, z, true);
                            this.shootTimer[witherTargetId - 1] = 0;
                        }
                    }
                    final int target = this.s(witherTargetId);
                    if (target > 0 && this.random.nextInt(10) <= 2 && System.currentTimeMillis() - this.lastTargetCheck <= 15000L) {
                        final Entity entity = this.world.a(target);
                        if (entity instanceof EntityHuman) {
                            if (entity != null && entity.isAlive() && this.h(entity) <= 900.0 && this.hasLineOfSight(entity)) {
                                this.a(witherTargetId + 1, (EntityLiving) entity);
                                this.nextShot[witherTargetId - 1] = this.ticksLived + 5 + this.random.nextInt(20);
                                this.shootTimer[witherTargetId - 1] = 0;
                            } else {
                                this.b(witherTargetId, 0);
                            }
                        }
                    } else {
                        /*final List<?> list = (List<?>)this.world.a((Class)EntityHuman.class, this.getBoundingBox().grow(50.0, 8.0, 50.0), (IEntitySelector)ReflectionUtils.getObject(EntityWither.class, this, "bw"));
                        int targetsFound = 0;
                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); ++i1) {
                            final EntityLiving entityliving = (EntityLiving)list.get(this.random.nextInt(list.size()));
                            if (entityliving != this && entityliving.isAlive() && this.hasLineOfSight((Entity)entityliving)) {
                                if (!(entityliving instanceof EntityHuman)) {
                                    break;
                                }
                                final EntityHuman human = (EntityHuman)entityliving;
                                if (!this.lastTargetName.equals(human.getName())) {
                                    if (!human.abilities.isInvulnerable) {
                                        ++targetsFound;
                                        this.b(witherTargetId, entityliving.getId());
                                    }
                                    break;
                                }
                            }
                            else {
                                list.remove(entityliving);
                            }
                        }
                        if (targetsFound > 0) {
                            this.lastTargetCheck = System.currentTimeMillis();
                        }*/
                    }
                }
            }
            if (this.getGoalTarget() != null && this.getGoalTarget() instanceof EntityHuman) {
                this.b(0, this.getGoalTarget().getId());
            } else {
                this.b(0, 0);
            }
            if (this.bv > 0) {
                --this.bv;
                if (this.bv == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    final int witherTargetId = MathHelper.floor(this.locY);
                    final int target = MathHelper.floor(this.locX);
                    final int j1 = MathHelper.floor(this.locZ);
                    boolean flag = false;
                    for (int k1 = -1; k1 <= 1; ++k1) {
                        for (int l1 = -1; l1 <= 1; ++l1) {
                            for (int i2 = 0; i2 <= 3; ++i2) {
                                final int j2 = target + k1;
                                final int k2 = witherTargetId + i2;
                                final int l2 = j1 + l1;
                                final Block block = this.world.getType(new BlockPosition(j2, k2, l2)).getBlock();
                                if (block.getMaterial() != Material.AIR && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME && block != Blocks.COMMAND_BLOCK) {
                                    if (!CraftEventFactory.callEntityChangeBlockEvent((Entity) this, j2, k2, l2, Blocks.AIR, 0).isCancelled()) {
                                        flag = (this.world.setAir(new BlockPosition(j2, k2, l2), true) || flag);
                                    }
                                }
                            }
                        }
                    }
                    if (flag) {
                        this.world.a((EntityHuman) null, 1012, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0);
                    }
                }
            }
            if (this.ticksLived % 120 == 0) {
                this.heal(1.0f, EntityRegainHealthEvent.RegainReason.REGEN);
            }
        }
    }

    public void bZ() {
        this.s(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }

    public void as() {
    }

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

    private float b(final float f, final float f1, final float f2) {
        float f3 = MathHelper.g(f1 - f);
        if (f3 > f2) {
            f3 = f2;
        }
        if (f3 < -f2) {
            f3 = -f2;
        }
        return f + f3;
    }

    private void a(final int i, final EntityLiving entityliving) {
        this.a(i, entityliving.locX, entityliving.locY + entityliving.getHeadHeight() * 0.5, entityliving.locZ, i == 0 && this.random.nextFloat() < 0.001f);
    }

    private void a(final int entityID, final double spawnX, final double spawnY, final double spawnZ, final boolean flag) {
        this.world.a((EntityHuman) null, 1014, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0);
        final double x = this.t(entityID);
        final double y = this.u(entityID);
        final double z = this.v(entityID);
        final double d6 = spawnX - x;
        final double d7 = spawnY - y;
        final double d8 = spawnZ - z;
        final EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, (EntityLiving) this, d6, d7, d8);
        entitywitherskull.locY = y;
        entitywitherskull.locX = x;
        entitywitherskull.locZ = z;
        this.world.addEntity((Entity) entitywitherskull);
    }

    public void a(final EntityLiving entityliving, final float f) {
        this.a(0, entityliving);
    }

    public boolean damageEntity(final DamageSource damagesource, final float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        }
        if (damagesource == DamageSource.DROWN || damagesource == DamageSource.WITHER) {
            return false;
        }
        if (this.cl() > 0) {
            return false;
        }
        final Entity entity = damagesource.getEntity();
        if ((entity != null && !(entity instanceof EntityHuman) && entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == this.getMonsterType()) || entity instanceof EntityWitherSkull) {
            return false;
        }
        if (this.bv <= 0) {
            this.bv = 20;
        }
        for (int i = 0; i < this.shootTimer.length; ++i) {
            final int[] shootTimer = this.shootTimer;
            final int n = i;
            shootTimer[n] += 3;
        }
        return super.damageEntity(damagesource, f);
    }

    protected void dropDeathLoot(final boolean flag, final int i) {
    }

    protected void D() {
        this.ticksFarFromPlayer = 0;
    }

    protected void b(final float f) {
    }

    public void addEffect(final MobEffect mobeffect) {
    }

    protected boolean bk() {
        return true;
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(50.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0);
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

    public void mount(final Entity entity) {
        this.vehicle = null;
    }
}
