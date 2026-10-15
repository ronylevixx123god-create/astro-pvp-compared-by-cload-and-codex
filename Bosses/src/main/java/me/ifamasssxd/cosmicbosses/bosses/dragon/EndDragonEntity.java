package me.ifamasssxd.cosmicbosses.bosses.dragon;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.dragon.phases.IPhase;
import me.ifamasssxd.cosmicbosses.bosses.dragon.phases.PhaseList;
import me.ifamasssxd.cosmicbosses.struct.BossPath;
import me.ifamasssxd.cosmicbosses.struct.BossPathHeap;
import me.ifamasssxd.cosmicbosses.struct.BossPathPoint;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.util.BlockStateListPopulator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EndDragonEntity extends EntityEnderDragon {
    public EndDragon bossInstance;
    public double[][] ringEffects;
    public int bo;
    public EntityComplexPart head;
    public EntityComplexPart body;
    public EntityComplexPart tail1;
    public float prevAnimTime;
    public float animTime;
    public boolean firstTargetSearch;
    public boolean bA;
    public Entity target;
    public int bB;
    public EntityEnderCrystal bC;
    private Explosion explosionSource;
    public IPhase phase;
    public DragonFightManager fightManager;
    public PhaseManager phaseManager;
    public Random random;
    public Location homeLocation;
    public long timeSinceLastApproach;
    private BossPathPoint[] bossPathPoints;
    private final int[] neighbors;
    private BossPathHeap pathQueue;
    private int sittingDamageReceived;

    public EndDragonEntity(final World world) {
        super(world);
        this.ringEffects = new double[64][3];
        this.bo = -1;
        this.explosionSource = new Explosion((World) null, (Entity) this, Double.NaN, Double.NaN, Double.NaN, Float.NaN, false, false);
        this.random = new Random();
        this.bossPathPoints = new BossPathPoint[24];
        this.neighbors = new int[24];
        this.pathQueue = new BossPathHeap();
        this.dead = true;
    }

    public EndDragonEntity(final World world, final EndDragon boss) {
        super(world);
        this.ringEffects = new double[64][3];
        this.bo = -1;
        this.explosionSource = new Explosion((World) null, (Entity) this, Double.NaN, Double.NaN, Double.NaN, Float.NaN, false, false);
        this.random = new Random();
        this.bossPathPoints = new BossPathPoint[24];
        this.neighbors = new int[24];
        this.pathQueue = new BossPathHeap();
        this.bossInstance = boss;
        this.head = this.children[0];
        this.body = this.children[1];
        this.tail1 = this.children[1];
        this.setHealth(this.getMaxHealth());
        this.persistent = true;
        this.a(16.0f, 8.0f);
        this.fireProof = true;
        this.ak = true;
        this.fightManager = new DragonFightManager(world);
        this.phaseManager = new PhaseManager(this);
        this.homeLocation = new Location(this.world.getWorld(), 62.0, 39.0, 69.0);
    }

    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }

    public DragonFightManager getFightManager() {
        return this.fightManager;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.maxHealth).setValue(200.0D);
    }

    protected void h() {
        super.h();
    }

    public double[] b(final int i, float f) {
        if (this.getHealth() <= 0.0f) {
            f = 0.0f;
        }
        f = 1.0f - f;
        final int j = this.bo - i * 1 & 0x3F;
        final int k = this.bo - i * 1 - 1 & 0x3F;
        final double[] adouble = new double[3];
        double d0 = this.ringEffects[j][0];
        double d2 = MathHelper.g(this.ringEffects[k][0] - d0);
        adouble[0] = d0 + d2 * f;
        d0 = this.ringEffects[j][1];
        d2 = this.ringEffects[k][1] - d0;
        adouble[1] = d0 + d2 * f;
        adouble[2] = this.ringEffects[j][2] + (this.ringEffects[k][2] - this.ringEffects[j][2]) * f;
        return adouble;
    }

    public void g(final double d0, final double d1, final double d2) {
        super.g(d0, d1, d2);
    }

    public void shootTNT() {
        final Vec3D vec3d = this.getHeadLookVec(1.0f).a();
        vec3d.b(-0.7853982f);
        final int toSpawn = (int) Math.min(1.0, Math.ceil(this.getHealth() / this.getMaxHealth() * 3.0f));
        final double d0 = this.head.locX;
        final double d2 = this.head.locY - this.head.getHeadHeight() / 2.0f - 2.5;
        final double d3 = this.head.locZ;
        for (int i = 0; i < toSpawn; ++i) {
            final EntityTNTPrimed primed = new EntityTNTPrimed(new org.bukkit.Location(this.world.getWorld(), d0 + this.random.nextGaussian() / 3.0 * i, d2 + this.random.nextGaussian() / 2.0, d3 + this.random.nextGaussian() / 3.0 * i), this.world, d0 + this.random.nextGaussian() / 3.0 * i, d2 + this.random.nextGaussian() / 2.0, d3 + this.random.nextGaussian() / 3.0 * i, (EntityLiving) this);
            primed.fuseTicks = 20 + this.random.nextInt(6);
            primed.getBukkitEntity().setMetadata("dragon_bomb", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.get(), (Object) ""));
            primed.getBukkitEntity().setMetadata("mouth_bomb", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.get(), (Object) this.getUniqueID()));
            primed.getBukkitEntity().setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.get(), (Object) this.getUniqueID()));
            this.world.addEntity((Entity) primed, CreatureSpawnEvent.SpawnReason.CUSTOM);
            final float offX = (float) (-vec3d.a * 0.07999999821186066 * i);
            final float offY = (float) (-vec3d.b * 0.6000000238418579);
            final float offZ = (float) (-vec3d.c * 0.07999999821186066 * i);
            primed.getBukkitEntity().setVelocity(new Vector(offX, offY, offZ));
            this.world.a((EntityHuman) null, 1009, new BlockPosition((int) this.head.locX, (int) this.head.locY, (int) this.head.locZ), 0);
        }
    }

    public boolean shootFireball() {
        if (this.target != null && this.head.isAlive() && this.target.world == this.world) {
            final Vec3D vec3d1 = new Vec3D(this.target.locX - this.locX, 0.0, this.target.locZ - this.locZ).a();
            final Vec3D vec3d2 = new Vec3D(Math.sin(this.yaw * 0.017453292f), 0.0, -Math.cos(this.yaw * 0.017453292f)).a();
            final float f1 = (float) vec3d2.b(vec3d1);
            float f2 = (float) (Math.acos(f1) * 57.29577951308232);
            f2 += 0.5f;
            final double d14 = 1.0;
            final Vec3D vec3d3 = BossUtils.oldJ(this, 1.0f);
            final double d15 = this.head.locX - vec3d3.a * 1.0;
            final double d16 = this.head.locY + this.head.getHeadHeight() / 2.0f + 0.5;
            final double d17 = this.head.locZ - vec3d3.c * 1.0;
            final double d18 = this.target.locX - d15;
            final double d19 = this.target.locY + this.target.getHeadHeight() / 2.0f - (d16 + this.head.getHeadHeight() / 2.0f);
            final double d20 = this.target.locZ - d17;
            final EntityLargeFireball fireball = new EntityLargeFireball(this.world, this, d18, d19, d20) {
                protected void a(final MovingObjectPosition movingobjectposition) {
                    if (movingobjectposition.entity != null && !(movingobjectposition.entity instanceof EntityHuman)) {
                        return;
                    }
                    super.a(movingobjectposition);
                }

                public boolean damageEntity(final DamageSource damagesource, final float f) {
                    return damagesource.getEntity() == null && super.damageEntity(damagesource, f);
                }
            };
            fireball.setPositionRotation(d15, d16, d17, 0.0f, 0.0f);
            fireball.yield = 0;
            fireball.isIncendiary = false;
            this.world.addEntity((Entity) fireball, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return true;
        }
        return false;
    }

    public float setRandomYawVelocity(final float val) {
        return this.bb = val;
    }

    public float getRandomYawVelocity() {
        return this.bb;
    }

    private void log(final String msg) {
        Bukkit.getLogger().info("[EndDragonEntity] " + msg);
    }

    public void e() {
        if (this.world.players.isEmpty()) {
            return;
        }
        if (this.bossInstance == null) {
            this.dead = true;
            Bukkit.getLogger().info("[EndDragonEntity] Removing found EndDragonEntity mob due to bossInstance == null!");
            return;
        }
        try {
            if (this.world.isClientSide) {
                float f = MathHelper.cos(this.bv * 3.1415927F * 2.0F);
                float f1 = MathHelper.cos(this.bu * 3.1415927F * 2.0F);
                if (f1 <= -0.3F && f >= -0.3F && !R())
                    this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
            }
            this.bu = this.bv;
            if (getHealth() <= 0.0F) {
                float f = (this.random.nextFloat() - 0.5F) * 8.0F;
                float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
                float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
                this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.locX + f, this.locY + 2.0D + f1, this.locZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
            } else {
                n();
                float f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
                f *= (float) Math.pow(2.0D, this.motY);
                if (this.bx) {
                    this.bv += f * 0.5F;
                } else {
                    this.bv += f;
                }
                this.yaw = MathHelper.g(this.yaw);
                if (ce()) {
                    this.bv = 0.5F;
                } else {
                    if (this.bl < 0)
                        for (int i = 0; i < this.bk.length; i++) {
                            this.bk[i][0] = this.yaw;
                            this.bk[i][1] = this.locY;
                        }
                    if (++this.bl == this.bk.length)
                        this.bl = 0;
                    this.bk[this.bl][0] = this.yaw;
                    this.bk[this.bl][1] = this.locY;
                    if (this.world.isClientSide) {
                        if (this.bc > 0) {
                            double d3 = this.locX + (this.bd - this.locX) / this.bc;
                            double d0 = this.locY + (this.be - this.locY) / this.bc;
                            double d1 = this.locZ + (this.bf - this.locZ) / this.bc;
                            double d2 = MathHelper.g(this.bg - this.yaw);
                            this.yaw = (float) (this.yaw + d2 / this.bc);
                            this.pitch = (float) (this.pitch + (this.bh - this.pitch) / this.bc);
                            this.bc--;
                            setPosition(d3, d0, d1);
                            setYawPitch(this.yaw, this.pitch);
                        }
                    } else {
                        double d3 = this.a - this.locX;
                        double d0 = this.b - this.locY;
                        double d1 = this.c - this.locZ;
                        double d2 = d3 * d3 + d0 * d0 + d1 * d1;
                        if (this.target != null) {
                            this.a = this.target.locX;
                            this.c = this.target.locZ;
                            double d5 = this.a - this.locX;
                            double d6 = this.c - this.locZ;
                            double d7 = Math.sqrt(d5 * d5 + d6 * d6);
                            double d11 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
                            if (d11 > 10.0D)
                                d11 = 10.0D;
                            this.b = (this.target.getBoundingBox()).b + d11;
                        } else {
                            this.a += this.random.nextGaussian() * 2.0D;
                            this.c += this.random.nextGaussian() * 2.0D;
                        }
                        /*if (this.bw || d2 < 100.0D || d2 > 22500.0D || this.positionChanged || this.E)
                            cf();*/
                        d0 /= MathHelper.sqrt(d3 * d3 + d1 * d1);
                        float f14 = 0.6F;
                        d0 = MathHelper.a(d0, -f14, f14);
                        this.motY += d0 * 0.10000000149011612D;
                        this.yaw = MathHelper.g(this.yaw);
                        double d8 = 180.0D - MathHelper.b(d3, d1) * 180.0D / 3.1415927410125732D;
                        double d9 = MathHelper.g(d8 - this.yaw);
                        if (d9 > 50.0D)
                            d9 = 50.0D;
                        if (d9 < -50.0D)
                            d9 = -50.0D;
                        Vec3D vec3d = (new Vec3D(this.a - this.locX, this.b - this.locY, this.c - this.locZ)).a();
                        double d4 = -MathHelper.cos(this.yaw * 3.1415927F / 180.0F);
                        Vec3D vec3d1 = (new Vec3D(MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, d4)).a();
                        float f4 = ((float) vec3d1.b(vec3d) + 0.5F) / 1.5F;
                        if (f4 < 0.0F)
                            f4 = 0.0F;
                        this.bb *= 0.8F;
                        float f5 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
                        double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
                        if (d10 > 40.0D)
                            d10 = 40.0D;
                        this.bb = (float) (this.bb + d9 * 0.699999988079071D / d10 / f5);
                        this.yaw += this.bb * 0.1F;
                        float f6 = (float) (2.0D / (d10 + 1.0D));
                        float f7 = 0.06F;
                        a(0.0F, -1.0F, f7 * (f4 * f6 + 1.0F - f6));
                        if (this.bx) {
                            move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                        } else {
                            move(this.motX, this.motY, this.motZ);
                        }
                        Vec3D vec3d2 = (new Vec3D(this.motX, this.motY, this.motZ)).a();
                        float f8 = ((float) vec3d2.b(vec3d1) + 1.0F) / 2.0F;
                        f8 = 0.8F + 0.15F * f8;
                        this.motX *= f8;
                        this.motZ *= f8;
                        this.motY *= 0.9100000262260437D;
                    }
                    this.aI = this.yaw;
                    this.bn.width = this.bn.length = 3.0F;
                    this.bp.width = this.bp.length = 2.0F;
                    this.bq.width = this.bq.length = 2.0F;
                    this.br.width = this.br.length = 2.0F;
                    /*this.bo.length = 3.0F;
                    this.bo.width = 5.0F;*/
                    this.bs.length = 2.0F;
                    this.bs.width = 4.0F;
                    this.bt.length = 3.0F;
                    this.bt.width = 4.0F;
                    float f1 = (float) (b(5, 1.0F)[1] - b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
                    float f2 = MathHelper.cos(f1);
                    float f9 = -MathHelper.sin(f1);
                    float f10 = this.yaw * 3.1415927F / 180.0F;
                    float f11 = MathHelper.sin(f10);
                    float f12 = MathHelper.cos(f10);
                    /*this.bo.t_();
                    this.bo.setPositionRotation(this.locX + (f11 * 0.5F), this.locY, this.locZ - (f12 * 0.5F), 0.0F, 0.0F);*/
                    this.bs.t_();
                    this.bs.setPositionRotation(this.locX + (f12 * 4.5F), this.locY + 2.0D, this.locZ + (f11 * 4.5F), 0.0F, 0.0F);
                    this.bt.t_();
                    this.bt.setPositionRotation(this.locX - (f12 * 4.5F), this.locY + 2.0D, this.locZ - (f11 * 4.5F), 0.0F, 0.0F);
                    if (!this.world.isClientSide && this.hurtTicks == 0) {
                        a(this.world.getEntities(this, this.bs.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
                        a(this.world.getEntities(this, this.bt.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
                        b(this.world.getEntities(this, this.bn.getBoundingBox().grow(1.0D, 1.0D, 1.0D)));
                    }
                    double[] adouble = b(5, 1.0F);
                    double[] adouble1 = b(0, 1.0F);
                    float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bb * 0.01F);
                    float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bb * 0.01F);
                    this.bn.t_();
                    this.bn.setPositionRotation(this.locX + (f3 * 5.5F * f2), this.locY + (adouble1[1] - adouble[1]) * 1.0D + (f9 * 5.5F), this.locZ - (f13 * 5.5F * f2), 0.0F, 0.0F);
                    for (int j = 0; j < 3; j++) {
                        EntityComplexPart entitycomplexpart = null;
                        if (j == 0)
                            entitycomplexpart = this.bp;
                        if (j == 1)
                            entitycomplexpart = this.bq;
                        if (j == 2)
                            entitycomplexpart = this.br;
                        double[] adouble2 = b(12 + j * 2, 1.0F);
                        float f14 = this.yaw * 3.1415927F / 180.0F + b(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
                        float f15 = MathHelper.sin(f14);
                        float f16 = MathHelper.cos(f14);
                        float f17 = 1.5F;
                        float f18 = (j + 1) * 2.0F;
                        entitycomplexpart.t_();
                        entitycomplexpart.setPositionRotation(this.locX - ((f11 * f17 + f15 * f18) * f2), this.locY + (adouble2[1] - adouble[1]) * 1.0D - ((f18 + f17) * f9) + 1.5D, this.locZ + ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
                    }
                    /*if (!this.world.isClientSide)
                        this.bx = b(this.bn.getBoundingBox()) | b(this.bo.getBoundingBox());*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getHeadYOffset(final float p_184662_1_) {
        double d0;
        if (this.phaseManager.getCurrentPhase().getIsStationary()) {
            d0 = -1.0;
        } else {
            final double[] adouble = this.b(5, 1.0f);
            final double[] adouble2 = this.b(0, 1.0f);
            d0 = adouble[1] - adouble2[1];
        }
        return (float) d0;
    }

    public Vec3D getHeadLookVec(final float p_184665_1_) {
        final IPhase iphase = this.phaseManager.getCurrentPhase();
        final PhaseList<? extends IPhase> phaselist = iphase.getType();
        Vec3D vec3d;
        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
            if (iphase.getIsStationary()) {
                final float f4 = this.pitch;
                final float f5 = 1.5f;
                this.pitch = -45.0f;
                vec3d = BossUtils.oldJ(this, p_184665_1_);
                this.pitch = f4;
            } else {
                vec3d = BossUtils.oldJ(this, p_184665_1_);
            }
        } else {
            final Location blockpos = this.homeLocation;
            final float f6 = Math.max(MathHelper.sqrt(this.distanceSqToCenter(blockpos.getX(), blockpos.getY(), blockpos.getZ())) / 4.0f, 1.0f);
            final float f7 = 6.0f / f6;
            final float f8 = this.pitch;
            final float f9 = 1.5f;
            this.pitch = -f7 * 1.5f * 5.0f;
            vec3d = BossUtils.oldJ(this, p_184665_1_);
            this.pitch = f8;
        }
        return vec3d;
    }

    private void n() {
        if (this.bC != null) {
            if (this.bC.dead) {
                if (!this.world.isClientSide) {
                    CraftEventFactory.entityDamage = (Entity) this.bC;
                    this.a(this.head, DamageSource.explosion((Explosion) null), 10.0f);
                    CraftEventFactory.entityDamage = null;
                }
                this.bC = null;
            } else if (this.ticksLived % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                final EntityRegainHealthEvent event = new EntityRegainHealthEvent((org.bukkit.entity.Entity) this.getBukkitEntity(), 1.0, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                this.world.getServer().getPluginManager().callEvent((Event) event);
                if (!event.isCancelled()) {
                    this.setHealth((float) (this.getHealth() + event.getAmount()));
                }
            }
        }
        if (this.random.nextInt(10) == 0) {
            final float f = 32.0f;
            final List list = this.world.a((Class) EntityEnderCrystal.class, this.getBoundingBox().grow((double) f, (double) f, (double) f));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            /*for (EntityEnderCrystal entityendercrystal2 : (List<EntityEnderCrystal>)list) {
                final double d2 = entityendercrystal2.f((Entity)this);
                if (d2 < d0) {
                    d0 = d2;
                    entityendercrystal = entityendercrystal2;
                }
            }
            this.bC = entityendercrystal;*/
        }
    }

    private void a(final List list) {
        final double d0 = (this.body.getBoundingBox().a + this.body.getBoundingBox().d) / 2.0;
        final double d2 = (this.body.getBoundingBox().c + this.body.getBoundingBox().f) / 2.0;
        for (Entity entity : (List<Entity>) list) {
            if (entity instanceof EntityLiving) {
                final double d3 = entity.locX - d0;
                final double d4 = entity.locZ - d2;
                final double d5 = d3 * d3 + d4 * d4;
                final Vector vect = new Vector(d3 / d5 * 4.0 * 0.3, 0.20000000298023224, d4 / d5 * 4.0 * 0.3);
                if (entity instanceof EntityHuman) {
                    final Player player = (Player) entity.getBukkitEntity();
                    vect.multiply(1.6);
                    player.setVelocity(vect);
                    //entity.al = true;
                } else {
                    entity.g(vect.getX(), vect.getY(), vect.getZ());
                }
            }
        }
    }

    private void b(final List list) {
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = (Entity) list.get(i);
            if (entity instanceof EntityLiving) {
                entity.damageEntity(DamageSource.mobAttack((EntityLiving) this), 30.0f);
            }
        }
    }

    private float b(final double d0) {
        return (float) MathHelper.g(d0);
    }

    public void moveRelative(float strafe, float up, float forward, final float friction) {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.sqrt((double) f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            up *= f;
            forward *= f;
            final float f2 = MathHelper.sin(this.yaw * 0.017453292f);
            final float f3 = MathHelper.cos(this.yaw * 0.017453292f);
            this.motX += strafe * f3 - forward * f2;
            this.motY += up;
            this.motZ += forward * f3 + strafe * f2;
        }
    }
    
    /*private boolean a(final AxisAlignedBB axisalignedbb) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.b);
        final int k = MathHelper.floor(axisalignedbb.c);
        final int l = MathHelper.floor(axisalignedbb.d);
        final int i2 = MathHelper.floor(axisalignedbb.e);
        final int j2 = MathHelper.floor(axisalignedbb.f);
        boolean flag = false;
        boolean flag2 = false;
        final List<Block> destroyedBlocks = new ArrayList<Block>();
        final CraftWorld craftWorld = this.world.getWorld();
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = j; l2 <= i2; ++l2) {
                for (int i3 = k; i3 <= j2; ++i3) {
                    final net.minecraft.server.v1_8_R3.Block block = this.world.getType(k2, l2, i3);
                    if (block.getMaterial() != Material.AIR) {
                        if (block != Blocks.OBSIDIAN && block != Blocks.WHITESTONE && block != Blocks.BEDROCK && this.world.getGameRules().getBoolean("mobGriefing")) {
                            flag2 = true;
                            destroyedBlocks.add(craftWorld.getBlockAt(k2, l2, i3));
                        }
                        else {
                            flag = true;
                        }
                    }
                }
            }
        }
        if (flag2) {
            final org.bukkit.entity.Entity bukkitEntity = (org.bukkit.entity.Entity)this.getBukkitEntity();
            final EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), (List)destroyedBlocks, 0.0f);
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return flag;
            }
            if (event.getYield() == 0.0f) {
                for (final Block block2 : event.blockList()) {
                    this.world.setAir(block2.getX(), block2.getY(), block2.getZ());
                }
            }
            else {
                for (final Block block2 : event.blockList()) {
                    final org.bukkit.Material blockId = block2.getType();
                    if (blockId != org.bukkit.Material.AIR) {
                        final int blockX = block2.getX();
                        final int blockY = block2.getY();
                        final int blockZ = block2.getZ();
                        final net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(blockId);
                        if (nmsBlock.a(this.explosionSource)) {
                            nmsBlock.dropNaturally(this.world, blockX, blockY, blockZ, (int)block2.getData(), event.getYield(), 0);
                        }
                        nmsBlock.wasExploded(this.world, blockX, blockY, blockZ, this.explosionSource);
                        this.world.setAir(blockX, blockY, blockZ);
                    }
                }
            }
            final double d0 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * this.random.nextFloat();
            final double d2 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * this.random.nextFloat();
            final double d3 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * this.random.nextFloat();
            this.world.addParticle("largeexplode", d0, d2, d3, 0.0, 0.0, 0.0);
        }
        return flag;
    }*/

    public boolean a(final EntityComplexPart entitycomplexpart, final DamageSource damagesource, float f) {
        if (entitycomplexpart != this.head) {
            f /= 3.0f;
        }
        f = this.phaseManager.getCurrentPhase().getAdjustedDamage(entitycomplexpart, damagesource, f);
        if (f < 0.01f) {
            return false;
        }
        this.damageEntity(damagesource, f);
        return true;
    }

    public boolean damageEntity(final DamageSource damagesource, final float f) {
        if (damagesource == DamageSource.STUCK || damagesource == DamageSource.CACTUS) {
            return false;
        }
        if (damagesource.getEntity() instanceof EndDragonEntity || damagesource.getEntity() instanceof EntityTNTPrimed || damagesource.isExplosion()) {
            return false;
        }
        this.target = null;
        boolean retr;
        if (damagesource.getEntity() instanceof EntityHuman) {
            final float health = this.getHealth();
            retr = super.damageEntity(damagesource, f);
            if (this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.sittingDamageReceived += (int) (health - this.getHealth());
                if (this.sittingDamageReceived > 0.1f * this.getMaxHealth()) {
                    this.sittingDamageReceived = 0;
                    this.phaseManager.setPhase(PhaseList.TAKEOFF);
                    final EntityLiving nearbyPlayer = (EntityLiving) BossUtils.findNearbyVulnerablePlayer(this.world, this, 100.0);
                    if (nearbyPlayer != null) {
                        this.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
                        this.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER).setTarget(new Vec3D(nearbyPlayer.locX, nearbyPlayer.locY, nearbyPlayer.locZ));
                    }
                }
            }
        } else {
            retr = super.damageEntity(damagesource, f);
        }
        return retr;
    }

    public boolean dealDamage(final DamageSource damagesource, final float f) {
        return this.damageEntity(damagesource, f);
    }

    protected void aF() {
        if (!this.dead) {
            ++this.bB;
            if (this.bB >= 180 && this.bB <= 200) {
                final float f = (this.random.nextFloat() - 0.5f) * 8.0f;
                final float f2 = (this.random.nextFloat() - 0.5f) * 4.0f;
                final float f3 = (this.random.nextFloat() - 0.5f) * 8.0f;
                this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.locX + f, this.locY + 2.0 + f2, this.locZ + f3, 0.0, 0.0, 0.0);
            }
            if (!this.world.isClientSide) {
                if (this.bB > 150 && this.bB % 5 == 0) {
                    int i = this.expToDrop / 12;
                    while (i > 0) {
                        final int j = EntityExperienceOrb.getOrbValue(i);
                        i -= j;
                        this.world.addEntity((Entity) new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                    }
                }
                if (this.bB == 1) {
                    final int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
                    for (final EntityHuman player : (List<EntityHuman>) this.world.players) {
                        final double deltaX = this.locX - player.locX;
                        final double deltaZ = this.locZ - player.locZ;
                        final double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                        if (this.world.spigotConfig.dragonDeathSoundRadius <= 0 || distanceSquared <= this.world.spigotConfig.dragonDeathSoundRadius * this.world.spigotConfig.dragonDeathSoundRadius) {
                            if (distanceSquared > viewDistance * viewDistance) {
                                final double deltaLength = Math.sqrt(distanceSquared);
                                final double relativeX = player.locX + deltaX / deltaLength * viewDistance;
                                final double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
                                ((EntityPlayer) player).playerConnection.sendPacket((Packet) new PacketPlayOutWorldEvent(1018, new BlockPosition((int) relativeX, (int) this.locY, (int) relativeZ), 0, true));
                            } else {
                                ((EntityPlayer) player).playerConnection.sendPacket((Packet) new PacketPlayOutWorldEvent(1018, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0, true));
                            }
                        }
                    }
                }
            }
            this.move(0.0, 0.10000000149011612, 0.0);
            final float n = this.yaw + 20.0f;
            this.yaw = n;
            this.aM = n;
            if (this.bB == 200 && !this.world.isClientSide) {
                int i = this.expToDrop - 10 * this.expToDrop / 12;
                while (i > 0) {
                    final int j = EntityExperienceOrb.getOrbValue(i);
                    i -= j;
                    this.world.addEntity((Entity) new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                }
                this.die();
            }
        }
    }

    private void b(final int i, final int j) {
        final byte b0 = 64;
        //BlockEnderPortal.a = true;
        final byte b2 = 4;
        final BlockStateListPopulator world = new BlockStateListPopulator((org.bukkit.World) this.world.getWorld());
        for (int k = b0 - 1; k <= b0 + 32; ++k) {
            for (int l = i - b2; l <= i + b2; ++l) {
                for (int i2 = j - b2; i2 <= j + b2; ++i2) {
                    final double d0 = l - i;
                    final double d2 = i2 - j;
                    final double d3 = d0 * d0 + d2 * d2;
                    if (d3 <= (b2 - 0.5) * (b2 - 0.5)) {
                        if (k < b0) {
                            if (d3 <= (b2 - 1 - 0.5) * (b2 - 1 - 0.5)) {
                                world.setTypeUpdate(l, k, i2, Blocks.BEDROCK);
                            }
                        } else if (k > b0) {
                            world.setTypeUpdate(l, k, i2, Blocks.AIR);
                        } else if (d3 > (b2 - 1 - 0.5) * (b2 - 1 - 0.5)) {
                            world.setTypeUpdate(l, k, i2, Blocks.BEDROCK);
                        } else {
                            world.setTypeUpdate(l, k, i2, Blocks.END_PORTAL);
                        }
                    }
                }
            }
        }
        world.setType(i, b0 + 0, j, Blocks.BEDROCK);
        world.setType(i, b0 + 1, j, Blocks.BEDROCK);
        world.setType(i, b0 + 2, j, Blocks.BEDROCK);
        world.setTypeAndData(i - 1, b0 + 2, j, Blocks.TORCH, 2, 0);
        world.setTypeAndData(i + 1, b0 + 2, j, Blocks.TORCH, 1, 0);
        world.setTypeAndData(i, b0 + 2, j - 1, Blocks.TORCH, 4, 0);
        world.setTypeAndData(i, b0 + 2, j + 1, Blocks.TORCH, 3, 0);
        world.setType(i, b0 + 3, j, Blocks.BEDROCK);
        world.setType(i, b0 + 4, j, Blocks.DRAGON_EGG);
        final EntityCreatePortalEvent event = new EntityCreatePortalEvent((LivingEntity) this.getBukkitEntity(), (List) Collections.unmodifiableList((List<?>) world.getList()), PortalType.ENDER);
        this.world.getServer().getPluginManager().callEvent((Event) event);
        if (!event.isCancelled()) {
            for (final BlockState state : event.getBlocks()) {
                state.update(true);
            }
        } else {
            for (final BlockState state : event.getBlocks()) {
                final PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(this.world, new BlockPosition(state.getX(), state.getY(), state.getZ()));
                for (EntityHuman entity : (List<EntityHuman>) this.world.players) {
                    if (entity instanceof EntityPlayer) {
                        ((EntityPlayer) entity).playerConnection.sendPacket((Packet) packet);
                    }
                }
            }
        }
        //BlockEnderPortal.a = false;
    }

    protected void D() {
    }

    public Entity[] aB() {
        return (Entity[]) this.children;
    }

    public boolean ad() {
        return false;
    }

    public World a() {
        return this.world;
    }

    protected String z() {
        return "mob.enderdragon.growl";
    }

    protected String bo() {
        return "mob.enderdragon.hit";
    }

    protected float bB() {
        return 5.0F;
    }

    public int getExpReward() {
        return 12000;
    }

    public double distanceSqToCenter(final double xIn, final double yIn, final double zIn) {
        final double d0 = this.locX + 0.5 - xIn;
        final double d2 = this.locY + 0.5 - yIn;
        final double d3 = this.locZ + 0.5 - zIn;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }

    public int initPathPoints() throws NoSuchFieldException {
        if (this.bossPathPoints[0] == null) {
            final int x = this.homeLocation.getBlockX();
            final int z = this.homeLocation.getBlockZ();
            for (int i = 0; i < 24; ++i) {
                int j = 5;
                int circleX;
                int circleZ;
                if (i < 12) {
                    circleX = (int) (40.0f * MathHelper.cos(2.0f * (-3.1415927f + 0.2617994f * i)));
                    circleZ = (int) (40.0f * MathHelper.sin(2.0f * (-3.1415927f + 0.2617994f * i)));
                } else if (i < 20) {
                    final int lvt_3_1_ = i - 12;
                    circleX = (int) (30.0f * MathHelper.cos(2.0f * (-3.1415927f + 0.3926991f * lvt_3_1_)));
                    circleZ = (int) (30.0f * MathHelper.sin(2.0f * (-3.1415927f + 0.3926991f * lvt_3_1_)));
                    j += 10;
                } else {
                    final int k1 = i - 20;
                    circleX = (int) (20.0f * MathHelper.cos(2.0f * (-3.1415927f + 0.7853982f * k1)));
                    circleZ = (int) (20.0f * MathHelper.sin(2.0f * (-3.1415927f + 0.7853982f * k1)));
                }
                circleX += x;
                circleZ += z;
                final int j2 = Math.max(40, BossUtils.getHighestBlockYAt((org.bukkit.World) this.world.getWorld(), circleX, circleZ, false) + j);
                this.bossPathPoints[i] = new BossPathPoint(circleX, j2, circleZ);
            }
            this.neighbors[0] = 6146;
            this.neighbors[1] = 8197;
            this.neighbors[2] = 8202;
            this.neighbors[3] = 16404;
            this.neighbors[4] = 32808;
            this.neighbors[5] = 32848;
            this.neighbors[6] = 65696;
            this.neighbors[7] = 131392;
            this.neighbors[8] = 131712;
            this.neighbors[9] = 263424;
            this.neighbors[10] = 526848;
            this.neighbors[11] = 525313;
            this.neighbors[12] = 1581057;
            this.neighbors[13] = 3166214;
            this.neighbors[14] = 2138120;
            this.neighbors[15] = 6373424;
            this.neighbors[16] = 4358208;
            this.neighbors[17] = 12910976;
            this.neighbors[18] = 9044480;
            this.neighbors[19] = 9706496;
            this.neighbors[20] = 15216640;
            this.neighbors[21] = 13688832;
            this.neighbors[22] = 11763712;
            this.neighbors[23] = 8257536;
        }
        return this.getNearestPpIdx(this.locX, this.locY, this.locZ);
    }

    public int getNearestPpIdx(final double x, final double y, final double z) {
        float f = 10000.0f;
        int i = 0;
        final PathPoint pathpoint = new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
        int j = 0;
        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            j = 12;
        }
        for (int k = j; k < 24; ++k) {
            if (this.bossPathPoints[k] != null) {
                final float f2 = this.bossPathPoints[k].distanceToSquared(pathpoint);
                if (f2 < f) {
                    f = f2;
                    i = k;
                }
            }
        }
        return i;
    }

    public BossPath findPath(final int startIdx, final int finishIdx, @Nullable final BossPathPoint andThen) {
        for (int i = 0; i < 24; ++i) {
            final BossPathPoint pathpoint = this.bossPathPoints[i];
            pathpoint.setVisited(false);
            pathpoint.setDistanceToTarget(0.0f);
            pathpoint.setTotalPathDistance(0.0f);
            pathpoint.setDistanceToNext(0.0f);
            pathpoint.setPrevious(null);
            pathpoint.setIndex(-1);
        }
        final BossPathPoint pathpoint2 = this.bossPathPoints[startIdx];
        BossPathPoint pathpoint3 = this.bossPathPoints[finishIdx];
        pathpoint2.setTotalPathDistance(0.0f);
        pathpoint2.setDistanceToNext(pathpoint2.distanceTo(pathpoint3));
        pathpoint2.setDistanceToTarget(pathpoint2.getDistanceToNext());
        this.pathQueue.clearPath();
        this.pathQueue.addPoint(pathpoint2);
        BossPathPoint pathpoint4 = pathpoint2;
        int j = 0;
        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            j = 12;
        }
        while (!this.pathQueue.isPathEmpty()) {
            final BossPathPoint pathpoint5 = this.pathQueue.dequeue();
            if (pathpoint5.equals((Object) pathpoint3)) {
                if (andThen != null) {
                    andThen.setPrevious(pathpoint3);
                    pathpoint3 = andThen;
                }
                return this.makePath(pathpoint2, pathpoint3);
            }
            if (pathpoint5.distanceTo(pathpoint3) < pathpoint4.distanceTo(pathpoint3)) {
                pathpoint4 = pathpoint5;
            }
            pathpoint5.setVisited(true);
            int k = 0;
            for (int l = 0; l < 24; ++l) {
                if (this.bossPathPoints[l] == pathpoint5) {
                    k = l;
                    break;
                }
            }
            for (int i2 = j; i2 < 24; ++i2) {
                if ((this.neighbors[k] & 1 << i2) > 0) {
                    final BossPathPoint pathpoint6 = this.bossPathPoints[i2];
                    if (!pathpoint6.isVisited()) {
                        final float f = pathpoint5.getTotalPathPointDistance() + pathpoint5.distanceTo(pathpoint6);
                        if (!pathpoint6.isAssigned() || f < pathpoint6.getTotalPathPointDistance()) {
                            pathpoint6.setPrevious(pathpoint5);
                            pathpoint6.setTotalPathDistance(f);
                            pathpoint6.setDistanceToNext(pathpoint6.distanceTo(pathpoint3));
                            if (pathpoint6.isAssigned()) {
                                this.pathQueue.changeDistance(pathpoint6, pathpoint6.getTotalPathPointDistance() + pathpoint6.getDistanceToNext());
                            } else {
                                pathpoint6.setDistanceToTarget(pathpoint6.getTotalPathPointDistance() + pathpoint6.getDistanceToNext());
                                this.pathQueue.addPoint(pathpoint6);
                            }
                        }
                    }
                }
            }
        }
        if (pathpoint4 == pathpoint2) {
            return null;
        }
        if (andThen != null) {
            andThen.setPrevious(pathpoint4);
            pathpoint4 = andThen;
        }
        return this.makePath(pathpoint2, pathpoint4);
    }

    private BossPath makePath(final BossPathPoint start, final BossPathPoint finish) {
        int i = 1;
        for (BossPathPoint pathpoint = finish; pathpoint.getPrevious() != null; pathpoint = pathpoint.getPrevious()) {
            ++i;
        }
        final BossPathPoint[] apathpoint = new BossPathPoint[i];
        BossPathPoint pathpoint2 = finish;
        --i;
        apathpoint[i] = finish;
        while (pathpoint2.getPrevious() != null) {
            pathpoint2 = pathpoint2.getPrevious();
            --i;
            apathpoint[i] = pathpoint2;
        }
        return new BossPath(apathpoint);
    }

    public Location getHomeLocation() {
        return this.homeLocation;
    }
}
