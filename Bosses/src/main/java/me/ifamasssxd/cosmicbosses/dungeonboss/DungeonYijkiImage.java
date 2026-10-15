package me.ifamasssxd.cosmicbosses.dungeonboss;

import com.google.common.base.Predicate;
import de.inventivegames.particle.ParticleEffect;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.bosses.yijki.PathfinderGoalWitherBossArrowAttack;
import me.ifamasssxd.cosmicbosses.dungeonboss.abilities.UnstoppableForce;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import net.citizensnpcs.nms.v1_8_R3.entity.EntityHumanNPC;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class DungeonYijkiImage extends EntityMonster implements IRangedEntity {
    private float[] a = new float[2];
    private float[] b = new float[2];
    private float[] c = new float[2];
    private float[] bm = new float[2];
    private int[] nextShot;
    private int[] shootTimer;
    private int bv;
    private PathfinderGoalWitherBossArrowAttack arrowAttack;
    private Boss boss;
    public Predicate<Entity> bw;
    private int fails;
    private Vector direction;
    private long lastTargetCheck;
    private String lastTargetName;

    protected void h() {
        super.h();
        this.datawatcher.a(17, (Object) 0);
        this.datawatcher.a(18, (Object) 0);
        this.datawatcher.a(19, (Object) 0);
        this.datawatcher.a(20, (Object) 0);
    }

    public void b(final NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Invul", this.cl());
    }

    public void a(final NBTTagCompound nbttagcompound) {
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

    public DungeonYijkiImage(final World world) {
        super(world);
        this.a = new float[2];
        this.b = new float[2];
        this.c = new float[2];
        this.bm = new float[2];
        this.nextShot = new int[2];
        this.shootTimer = new int[2];
        this.fails = 0;
        this.lastTargetCheck = System.currentTimeMillis();
        this.lastTargetName = "";
        this.bw = (entity -> {
            if (entity instanceof EntityHuman && !(entity instanceof EntityHumanNPC)) {
                if (this.isTargettedByBossOrMinion((EntityLiving) entity) && this.fails < 3) {
                    ++this.fails;
                    return false;
                }
                this.fails = 0;
            }
            return true;
        });
        this.setHealth(this.getMaxHealth());
        this.a(0.9f, 4.0f);
        this.fireProof = true;
        ((Navigation) this.getNavigation()).e(true);
        this.goalSelector.a(0, (PathfinderGoal) new PathfinderGoalFloat((EntityInsentient) this));
        this.goalSelector.a(6, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 8.0f));
        this.goalSelector.a(7, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
        this.targetSelector.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget((EntityCreature) this, false));
        this.targetSelector.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) this, (Class) EntityHuman.class, 0, false, false, this.bw));
        this.arrowAttack = new PathfinderGoalWitherBossArrowAttack((IRangedEntity) this, 1.0, 20, 20, 20.0f);
        this.goalSelector.a(2, (PathfinderGoal) this.arrowAttack);
        this.b_ = 50;
    }

    public DungeonYijkiImage(final World world, final Boss boss) {
        this(world);
        this.boss = boss;
    }

    public void collide(final Entity entity) {
        if (entity instanceof EntityHuman && !(entity instanceof EntityHumanNPC) && this.isCharging() && entity.isAlive()) {
            ((EntityHuman) entity).lastDamager = (EntityLiving) this;
            final float newHealth = ((EntityHuman) entity).getHealth() * 0.25f;
            ((EntityHuman) entity).setHealth(newHealth);
            entity.getBukkitEntity().getWorld().playSound(entity.getBukkitEntity().getLocation(), Sound.ANVIL_BREAK, 1.0f, 1.1f);
            Bukkit.getLogger().info("Setting " + entity.getName() + " HP to " + newHealth + " from unstoppable force from Yijki Image!");
            return;
        }
        super.collide(entity);
    }

    private boolean isCharging() {
        return this.direction != null && this.ticksLived >= 140;
    }

    private void levelWithTarget(final double motionMultiplier) {
        if (this.getGoalTarget() != null) {
            final double targetY = this.getGoalTarget().locY;
            if (this.locY < targetY) {
                if (this.motY < 0.0) {
                    this.motY = 0.0;
                }
                this.motY += (0.25 - this.motY) * (motionMultiplier / 2.0);
            }
        }
    }

    private boolean isTargettedByBossOrMinion(final EntityLiving entityliving) {
        if (this.boss == null) {
            return false;
        }
        for (final org.bukkit.entity.Entity ent : this.boss.getMinions()) {
            if (((CraftEntity) ent).getHandle() instanceof DungeonYijkiImage) {
                final DungeonYijkiImage handle = (DungeonYijkiImage) ((CraftEntity) ent).getHandle();
                if (handle.getGoalTarget() != null && handle.getGoalTarget().equals((Object) entityliving)) {
                    return true;
                }
                continue;
            }
        }
        final Entity target = (Entity) ((DungeonYijki) this.boss).getBossEntity();
        return target != null && target.equals((Object) entityliving);
    }

    @SneakyThrows
    public void m() {
        if (this.boss.getEntity() == null || this.boss.getEntity().isDead()) {
            this.boss.removeMinion((org.bukkit.entity.Entity) this.getBukkitEntity());
            this.die();
            return;
        }
        if (this.direction == null && this.ticksLived >= 139) {
            Entity target = this.getGoalTarget();
            if (target == null) {
                target = (Entity) this.world.findNearbyPlayer(this, 32.0);
            }
            if (target == null) {
                target = ((DungeonYijki) this.boss).getBossEntity().getGoalTarget();
            }
            if (target != null) {
                this.direction = target.getBukkitEntity().getLocation().toVector().subtract(this.getBukkitEntity().getLocation().toVector());
                if (this.direction.length() != 0.0) {
                    this.direction = this.direction.normalize();
                    this.direction = this.direction.multiply(1.2);
                }
            } else {
                this.direction = this.getBukkitEntity().getLocation().getDirection().multiply(2);
            }
            Bukkit.getLogger().info("Image Charging Dir: " + this.direction);
        }
        final double motionMultiplier = 0.6000000238418579;
        if (this.isCharging()) {
            final List<Entity> ents = (List<Entity>) this.world.getEntities((Entity) this, this.getBoundingBox().grow(0.75, 1.0, 0.75));
            for (final Entity inBox : ents) {
                if (inBox instanceof EntityHuman && !(inBox instanceof EntityHumanNPC)) {
                    this.collide(inBox);
                    Bukkit.getLogger().info("Forcing collide with " + inBox.getName() + " for Yijki Image.");
                }
            }
            this.motX += this.direction.getX() * motionMultiplier;
            this.motZ += this.direction.getZ() * motionMultiplier;
            if (this.ticksLived % 2 == 0) {
                de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), this.getBukkitEntity().getLocation(), 0f, 0f, 0f, 0.09f, 25);
            }
            this.levelWithTarget(motionMultiplier);
        } else if (this.direction == null && this.ticksLived > 60) {
            this.arrowAttack.setSpeed(15);
            this.levelWithTarget(motionMultiplier);
            if (this.ticksLived % 10 == 0) {
                this.getBukkitEntity().getWorld().playSound(this.getBukkitEntity().getLocation(), Sound.NOTE_PLING, 15.0f, 1.1f);
            }
            if (this.ticksLived % 10 == 0) {
                ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), this.getBukkitEntity().getLocation(), 0f, 0f, 0f, 0.09f, 25);
            }
            if (this.ticksLived % 20 == 0) {
                UnstoppableForce.drawParticleLineInDirection((LivingEntity) this.getBukkitEntity());
            }
        } else if (!this.world.isClientSide && this.s(0) > 0) {
            final Entity entity = this.world.a(this.s(0));
            if (entity != null) {
                this.moveTowardsLocation(entity.locX, entity.locY, entity.locZ, motionMultiplier);
            }
        }
        if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806) {
            this.yaw = (float) Math.atan2(this.motZ, this.motX) * 57.295776f - 90.0f;
        }
        super.m();
        for (int i = 0; i < 2; ++i) {
            this.bm[i] = this.b[i];
            this.c[i] = this.a[i];
        }
        for (int i = 0; i < 2; ++i) {
            final int j = this.s(i + 1);
            Entity entity2 = null;
            if (j > 0) {
                entity2 = this.world.a(j);
            }
            if (entity2 != null && entity2 instanceof EntityHuman) {
                double d0 = t(i + 1);
                double d1 = u(i + 1);
                double d2 = v(i + 1);
                double d4 = entity2.locX - d0;
                double d5 = entity2.locY + entity2.getHeadHeight() - d1;
                double d6 = entity2.locZ - d2;
                double d7 = MathHelper.sqrt(d4 * d4 + d6 * d6);
                float f = (float) (MathHelper.b(d6, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                float f2 = (float) -(MathHelper.b(d5, d7) * 180.0D / 3.1415927410125732D);
                this.a[i] = this.b(this.a[i], f2, 40.0f);
                this.b[i] = this.b(this.b[i], f, 10.0f);
            } else {
                this.b[i] = this.b(this.b[i], this.aM, 10.0f);
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

    private void moveTowardsLocation(final double x, final double y, final double z, final double motionMultiplier) {
        final double height = 1.0;
        if (this.locY < y || this.locY < y + height) {
            if (this.motY < 0.0) {
                this.motY = 0.0;
            }
            this.motY += (0.5 - this.motY) * (motionMultiplier / 2.0);
        }
        final double distanceX = x - this.locX;
        final double distanceZ = z - this.locZ;
        final double distanceSqr = distanceX * distanceX + distanceZ * distanceZ;
        final double distanceFromPlayer = 5.0;
        final double speedMultiplier = 1.399999976158142;
        if (distanceSqr > distanceFromPlayer) {
            final double distanceSqrRoot = MathHelper.sqrt(distanceSqr);
            this.motX += (distanceX / distanceSqrRoot * 0.7 - this.motX) * (motionMultiplier * speedMultiplier);
            this.motZ += (distanceZ / distanceSqrRoot * 0.7 - this.motZ) * (motionMultiplier * speedMultiplier);
        }
    }

    protected void E() {
        if (this.cl() > 0) {
            final int invulnerableTicks = this.cl() - 1;
            if (invulnerableTicks <= 0) {
                final ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Entity) this.getBukkitEntity(), 7.0f, false);
                this.world.getServer().getPluginManager().callEvent((Event) event);
                if (!event.isCancelled()) {
                    this.world.createExplosion((Entity) this, this.locX, this.locY + this.getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                }
                this.world.createExplosion((Entity) this, this.locX, this.locY + this.getHeadHeight(), this.locZ, 7.0f, false, this.world.getGameRules().getBoolean("mobGriefing"));
                final int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
                for (Object player2 : this.world.players) {
                    EntityPlayer player = (EntityPlayer) player2;
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
                        player.playerConnection.sendPacket((Packet) new PacketPlayOutWorldEvent(1013, new BlockPosition((int) relativeX, (int) this.locY, (int) relativeZ), 0, true));
                    } else {
                        player.playerConnection.sendPacket((Packet) new PacketPlayOutWorldEvent(1013, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0, true));
                    }
                }
            }
            this.r(invulnerableTicks);
            if (this.ticksLived % 10 == 0) {
                this.heal(10.0f, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
            }
        } else {
            super.E();
            for (int targetID = 1; targetID < 3; ++targetID) {
                if (this.ticksLived >= this.nextShot[targetID - 1]) {
                    this.nextShot[targetID - 1] = this.ticksLived + 10 + this.random.nextInt(10);
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                        final int targetId = targetID - 1;
                        final int value = this.shootTimer[targetId];
                        this.shootTimer[targetId] = value + 1;
                        if (value > 15) {
                            final float width = 10.0f;
                            final float height = 5.0f;
                            final double x = MathHelper.a(this.random, this.locX - width, this.locX + width);
                            final double y = MathHelper.a(this.random, this.locY - height, this.locY + height);
                            final double z = MathHelper.a(this.random, this.locZ - width, this.locZ + width);
                            this.a(targetID + 1, x, y, z, true);
                            this.shootTimer[targetID - 1] = 0;
                        }
                    }
                    final int target = this.s(targetID);
                    if (target > 0 && this.random.nextInt(10) <= 2 && System.currentTimeMillis() - this.lastTargetCheck <= 15000L) {
                        final Entity entity = this.world.a(target);
                        if (entity instanceof EntityHuman) {
                            if (entity.isAlive() && this.h(entity) <= 900.0 && this.hasLineOfSight(entity)) {
                                this.a(targetID + 1, (EntityLiving) entity);
                                this.nextShot[targetID - 1] = this.ticksLived + 5 + this.random.nextInt(20);
                                this.shootTimer[targetID - 1] = 0;
                            } else {
                                this.b(targetID, 0);
                            }
                        }
                    } else {
                        final List<?> list = (List<?>) this.world.a((Class) EntityHuman.class, this.getBoundingBox().grow(50.0, 8.0, 50.0), this.bw);
                        int targetsFound = 0;
                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); ++i1) {
                            final EntityLiving entityliving = (EntityLiving) list.get(this.random.nextInt(list.size()));
                            if (entityliving != this && entityliving.isAlive() && this.hasLineOfSight((Entity) entityliving)) {
                                if (!(entityliving instanceof EntityHuman)) {
                                    break;
                                }
                                final EntityHuman human = (EntityHuman) entityliving;
                                if (!this.lastTargetName.equals(human.getName())) {
                                    if (!human.abilities.isInvulnerable) {
                                        ++targetsFound;
                                        this.b(targetID, entityliving.getId());
                                    }
                                    break;
                                }
                            } else {
                                list.remove(entityliving);
                            }
                        }
                        if (targetsFound > 0) {
                            this.lastTargetCheck = System.currentTimeMillis();
                        }
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
                    final int locY = MathHelper.floor(this.locY);
                    final int target = MathHelper.floor(this.locX);
                    final int j1 = MathHelper.floor(this.locZ);
                    boolean flag = false;
                    for (int k1 = -1; k1 <= 1; ++k1) {
                        for (int l1 = -1; l1 <= 1; ++l1) {
                            for (int i2 = 0; i2 <= 3; ++i2) {
                                final int j2 = target + k1;
                                final int k2 = locY + i2;
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
        this.r(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }

    public void as() {
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

    public int aV() {
        return 4;
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
        final EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8) {
            protected void a(final MovingObjectPosition movingobjectposition) {
                final Entity colliding = movingobjectposition.entity;
                if (colliding != null && !(colliding instanceof EntityHuman)) {
                    return;
                }
                super.a(movingobjectposition);
            }
        };
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
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0);
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

    public void mount(Entity entity) {
        this.vehicle = null;
    }
}
