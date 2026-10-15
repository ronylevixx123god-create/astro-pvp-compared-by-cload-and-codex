package me.ifamasssxd.cosmicbosses.entities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import java.util.UUID;

public class SoulReaperEnderman extends EntityMonster {
    private static final UUID bp;
    private static final AttributeModifier bq;
    private int teleportTick;
    private int bt;
    private Entity bu;
    private boolean angry;
    private boolean teleportBehind;
    public boolean shooting;
    private double d;
    private double flagTimer;
    private float newF;
    private float newJ;

    public SoulReaperEnderman(final World world, final boolean teleportBehind) {
        super(world);
        this.teleportBehind = false;
        this.shooting = false;
        this.d = 0.0;
        this.flagTimer = 0.0;
        this.newF = 10.0f;
        this.newJ = this.newF * this.newF;
        this.a(0.6f, 2.9f);
        this.teleportBehind = teleportBehind;
        //this.W = 1.0f;
    }

    public SoulReaperEnderman(final World world) {
        super(world);
        this.teleportBehind = false;
        this.shooting = false;
        this.d = 0.0;
        this.flagTimer = 0.0;
        this.newF = 10.0f;
        this.newJ = this.newF * this.newF;
        this.a(0.6f, 2.9f);
        //this.W = 1.0f;
    }

    /*
    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(40.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(6.0);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(21.0);
    }
    
    protected void c() {
        super.c();
        this.datawatcher.a(16, (Object)new ProtocolData.ByteShort((short)0));
        this.datawatcher.a(17, (Object)new Byte((byte)0));
        this.datawatcher.a(18, (Object)new Byte((byte)0));
    }
    
    public void b(final NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("carried", (short)Block.getId(this.getCarried()));
        nbttagcompound.setShort("carriedData", (short)this.getCarriedData());
    }
    
    public void a(final NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCarried(Block.getById((int)nbttagcompound.getShort("carried")));
        this.setCarriedData(nbttagcompound.getShort("carriedData"));
    }
    
    protected Entity findTarget() {
        final EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer((Entity)this, 64.0);
        if (entityhuman != null) {
            this.angry = true;
            if (this.bt == 0) {
                this.world.makeSound(entityhuman.locX, entityhuman.locY, entityhuman.locZ, "mob.endermen.stare", 1.0f, 1.0f);
            }
            if (this.bt++ == 5) {
                this.bt = 0;
                this.a(true);
                return (Entity)entityhuman;
            }
        }
        return null;
    }
    */

    private boolean f(final EntityHuman entityhuman) {
        /*final ItemStack itemstack = entityhuman.me.astropvp.cratekeys.inventory.armor[3];
        if (itemstack != null && itemstack.getItem() == Item.getItemOf(Blocks.PUMPKIN)) {
            return false;
        }
        final Vec3D vec3d = entityhuman.j(1.0f).a();
        Vec3D vec3d2 = new Vec3D(this.locX - entityhuman.locX, this.getBoundingBox().b + this.length / 2.0f - (entityhuman.locY + entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
        final double d0 = vec3d2.b();
        vec3d2 = vec3d2.a();
        final double d2 = vec3d.b(vec3d2);
        return d2 > 1.0 - 0.025 / d0 && entityhuman.hasLineOfSight((Entity)this);*/
        return false;
    }

    public void e() {
        /*if (!(this.target instanceof EntityHuman)) {
            this.target = null;
        }
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(5.0);
        if (this.bu != this.target) {
            final AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
            attributeinstance.b(SoulReaperEnderman.bq);
            if (this.target != null) {
                attributeinstance.a(SoulReaperEnderman.bq);
            }
        }
        this.bu = this.target;
        if (!this.world.isClientSide && this.world.getGameRules().getBoolean("mobGriefing")) {
            if (this.getCarried().getMaterial() == Material.AIR) {
                if (this.random.nextInt(20) == 0) {
                    final int i = MathHelper.floor(this.locX - 2.0 + this.random.nextDouble() * 4.0);
                    final int j = MathHelper.floor(this.locY + this.random.nextDouble() * 3.0);
                    final int k = MathHelper.floor(this.locZ - 2.0 + this.random.nextDouble() * 4.0);
                    this.world.getType(i, j, k);
                }
            }
            else if (this.random.nextInt(2000) == 0) {
                final int i = MathHelper.floor(this.locX - 1.0 + this.random.nextDouble() * 2.0);
                final int j = MathHelper.floor(this.locY + this.random.nextDouble() * 2.0);
                final int k = MathHelper.floor(this.locZ - 1.0 + this.random.nextDouble() * 2.0);
                final Block block = this.world.getType(i, j, k);
                final Block block2 = this.world.getType(i, j - 1, k);
                if (block.getMaterial() == Material.AIR && block2.getMaterial() != Material.AIR && block2.d() && !CraftEventFactory.callEntityChangeBlockEvent((Entity)this, i, j, k, this.getCarried(), this.getCarriedData()).isCancelled()) {
                    this.world.setTypeAndData(i, j, k, this.getCarried(), this.getCarriedData(), 3);
                    this.setCarried(Blocks.AIR);
                }
            }
        }
        for (int i = 0; i < 2; ++i) {
            this.world.addParticle("portal", this.locX + (this.random.nextDouble() - 0.5) * this.width, this.locY + this.random.nextDouble() * this.length - 0.25, this.locZ + (this.random.nextDouble() - 0.5) * this.width, (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
        }
        if (this.ticksLived % 20 == 0) {}
        if (!this.world.w() || !this.world.isClientSide) {}
        this.bc = false;
        if (this.target != null) {
            this.a(this.target, 100.0f, 100.0f);
        }
        if (!this.world.isClientSide && this.isAlive()) {
            if (this.target != null) {
                final double distance = this.target.f((Entity)this);
                if (this.target instanceof EntityHuman && this.f((EntityHuman)this.target)) {
                    if (this.target.f((Entity)this) < 16.0 && this.random.nextInt(100) <= 20) {
                        this.c(this.target);
                    }
                    this.teleportTick = 0;
                }
                else if (distance > 80.0 && distance < 1000.0 && this.teleportTick++ >= 5 && WorldGuardUtils.isPvPEnabled(this.target.getBukkitEntity().getLocation()) && this.c(this.target)) {
                    this.teleportTick = 0;
                }
            }
            else {
                this.a(false);
                this.teleportTick = 0;
            }
        }
        if (this.target != null) {
            this.shootTick();
        }
        super.e();*/
    }

    public CraftEntity getBukkitEntity() {
        return super.getBukkitEntity();
    }

    protected boolean bZ() {
        final double d0 = this.locX + (this.random.nextDouble() - 0.5) * 3.0;
        final double d2 = this.locY + (this.random.nextInt(64) - 1);
        final double d3 = this.locZ + (this.random.nextDouble() - 0.5) * 3.0;
        return this.k(d0, d2, d3);
    }

    protected boolean c(final Entity entity) {
        /*final CraftWorld wrld = this.world.getWorld();
        if (this.teleportBehind) {
            final Location loc = entity.getBukkitEntity().getLocation();
            org.bukkit.block.Block behind = loc.getBlock();
            int direction = (int)loc.getYaw();
            if (direction < 0) {
                direction += 360;
                direction = (direction + 45) / 90;
            }
            else {
                direction = (direction + 45) / 90;
            }
            switch (direction) {
                case 1: {
                    behind = wrld.getBlockAt((int)loc.getX() + 1, (int)loc.getY(), (int)loc.getZ());
                    break;
                }
                case 2: {
                    behind = wrld.getBlockAt((int)loc.getX(), (int)loc.getY(), (int)loc.getZ() + 1);
                    break;
                }
                case 3: {
                    behind = wrld.getBlockAt((int)loc.getX() - 1, (int)loc.getY(), (int)loc.getZ());
                    break;
                }
                case 4: {
                    behind = wrld.getBlockAt((int)loc.getX(), (int)loc.getY(), (int)loc.getZ() - 1);
                    break;
                }
                case 0: {
                    behind = wrld.getBlockAt((int)loc.getX(), (int)loc.getY(), (int)loc.getZ() - 1);
                    break;
                }
            }
            final double d1 = behind.getX() + (this.random.nextDouble() - 0.5) * 10.0;
            final double d2 = behind.getY() + (this.random.nextInt(15) - 1);
            final double d3 = behind.getZ() + (this.random.nextDouble() - 0.5) * 10.0;
            return this.k(d1, d2, d3);
        }
        final double d4 = entity.locX + (this.random.nextDouble() - 0.5) * 10.0;
        final double d5 = entity.locY + (this.random.nextInt(15) - 1);
        final double d6 = entity.locZ + (this.random.nextDouble() - 0.5) * 10.0;
        return this.k(d4, d5, d6);*/
        return false;
    }

    protected boolean k(final double d0, final double d1, final double d2) {
        /*final double d3 = this.locX;
        final double d4 = this.locY;
        final double d5 = this.locZ;
        final double y = d4;
        boolean flag = false;
        final int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        final int k = MathHelper.floor(d2);
        if (this.world.isLoaded(i, j, k)) {
            boolean flag2 = false;
            while (!flag2 && j > 0) {
                final Block block = this.world.getType(i, j - 1, k);
                if (block.getMaterial().isSolid()) {
                    flag2 = true;
                }
                else {
                    --j;
                }
            }
            if (flag2) {
                final EntityTeleportEvent teleport = new EntityTeleportEvent((org.bukkit.entity.Entity)this.getBukkitEntity(), new Location((org.bukkit.World)this.world.getWorld(), d3, (double)j, d5), new Location((org.bukkit.World)this.world.getWorld(), this.locX, this.locY, this.locZ));
                this.world.getServer().getPluginManager().callEvent((Event)teleport);
                if (teleport.isCancelled()) {
                    return false;
                }
                final Location to = teleport.getTo();
                this.setPosition(to.getX(), to.getY(), to.getZ());
                if (this.world.getCubes((Entity)this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox())) {
                    flag = true;
                }
            }
        }
        this.locX = d0;
        this.locY = j;
        this.locZ = d2;
        if (!flag) {
            this.setPosition(d3, d4, d5);
            return false;
        }
        final short short1 = 128;
        for (int l = 0; l < short1; ++l) {
            final double d6 = l / (short1 - 1.0);
            final float f = (this.random.nextFloat() - 0.5f) * 0.2f;
            final float f2 = (this.random.nextFloat() - 0.5f) * 0.2f;
            final float f3 = (this.random.nextFloat() - 0.5f) * 0.2f;
            final double d7 = d3 + (this.locX - d3) * d6 + (this.random.nextDouble() - 0.5) * this.width * 2.0;
            final double d8 = d4 + (this.locY - d4) * d6 + this.random.nextDouble() * this.length;
            final double d9 = d5 + (this.locZ - d5) * d6 + (this.random.nextDouble() - 0.5) * this.width * 2.0;
            this.world.addParticle("portal", d7, d8, d9, (double)f, (double)f2, (double)f3);
        }
        this.world.makeSound(d3, d4, d5, "mob.endermen.portal", 1.0f, 1.1f);
        this.makeSound("mob.endermen.portal", 1.0f, 1.1f);*/
        return true;
    }
    
    /*protected String t() {
        return this.cd() ? "mob.endermen.scream" : "mob.endermen.idle";
    }
    
    protected String aT() {
        return "mob.endermen.hit";
    }
    
    protected String aU() {
        return "mob.endermen.death";
    }
    
    protected Item getLoot() {
        return Items.END_PEARL;
    }*/

    protected void dropDeathLoot(final boolean flag, final int i) {
    }

    public void setCarried(final Block block) {
        //this.datawatcher.watch(16, (Object)new ProtocolData.ByteShort((short)Block.getId(block)));
    }

    public Block getCarried() {
        return Block.getById((int) this.datawatcher.getShort(16));
    }

    public void setCarriedData(final int i) {
        this.datawatcher.watch(17, (Object) (byte) (i & 0xFF));
    }

    public int getCarriedData() {
        return this.datawatcher.getByte(17);
    }

    public boolean damageEntity(final DamageSource damagesource, final float f) {
        /*if (this.isInvulnerable()) {
            return false;
        }
        this.a(true);
        if (damagesource instanceof EntityDamageSource) {
            if (damagesource.getEntity() instanceof EntityHuman) {
                this.angry = true;
            }
            final EntityDamageSource source = (EntityDamageSource)damagesource;
            if (!(source.getEntity() instanceof EntityHuman)) {
                return false;
            }
        }
        final boolean happens = super.damageEntity(damagesource, f);
        if (happens && damagesource instanceof EntityDamageSource && damagesource.getEntity() instanceof EntityHuman && damagesource.getEntity() != this.target && this.random.nextInt(30) == 0) {
            this.target = damagesource.getEntity();
            this.setGoalTarget((EntityLiving)damagesource.getEntity());
        }
        return happens;*/
        return true;
    }

    public boolean cd() {
        return this.datawatcher.getByte(18) > 0;
    }

    public void a(final boolean flag) {
        this.datawatcher.watch(18, (Object) (byte) (flag ? 1 : 0));
    }

    public void die() {
        super.die();
    }

    public void shootTick() {
        /*if (!this.shooting) {
            return;
        }
        final double d0 = this.e(this.target.locX, this.target.getBoundingBox().b, this.target.locZ);
        final boolean flag = this.getEntitySenses().a(this.target);
        if (flag) {
            ++this.flagTimer;
        }
        else {
            this.flagTimer = 0.0;
        }
        if (d0 <= this.newJ && this.flagTimer >= 20.0) {
            this.getNavigation().h();
        }
        else {
            this.getNavigation().a(this.target, 1.25);
        }
        this.getControllerLook().a(this.target, 30.0f, 30.0f);
        final double d2 = this.d - 1.0;
        this.d = d2;
        if (d2 == 0.0) {
            if (d0 > this.newJ || !flag) {
                return;
            }
            float f2;
            final float f = f2 = MathHelper.sqrt(d0) / this.newF;
            if (f < 0.1f) {
                f2 = 0.1f;
            }
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            if (this.target instanceof EntityHuman) {
                this.shootEnderpearl((EntityLiving)this.target, f2);
            }
            this.d = this.random.nextInt(5) + 5;
        }
        else if (this.d < 0.0) {
            this.d = this.random.nextInt(5) + 5;
        }*/
    }

    private void shootEnderpearl(final EntityLiving entityLiving, final float v) {
        /*final EntityEnderPearl entitysnowball = new EntityEnderPearl(this.world, (EntityLiving)this);
        final double d0 = entityLiving.locX - this.locX;
        final double d2 = entityLiving.locY + entityLiving.getHeadHeight() - 1.600000023841858 - entitysnowball.locY;
        final double d3 = entityLiving.locZ - this.locZ;
        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
        entitysnowball.getBukkitEntity().setMetadata("exploding", (MetadataValue)new FixedMetadataValue((Plugin)CosmicBosses.getInstance(), (Object)""));
        entitysnowball.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
        this.makeSound("random.bow", 1.0f, 1.0f / (this.aI().nextFloat() * 0.4f + 0.8f));
        this.world.addEntity((Entity)entitysnowball);*/
    }

    static {
        bp = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
        bq = new AttributeModifier(SoulReaperEnderman.bp, "Attacking speed boost", 6.199999809265137, 0).a(false);
    }
}
