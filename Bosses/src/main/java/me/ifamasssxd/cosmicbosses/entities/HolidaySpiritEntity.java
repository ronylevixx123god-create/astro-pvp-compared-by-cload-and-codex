package me.ifamasssxd.cosmicbosses.entities;

import me.ifamasssxd.cosmicbosses.bosses.EntityHolidayGolem;
import me.ifamasssxd.cosmicbosses.bosses.holidayspirit.HolidaySpirit;
import me.ifamasssxd.cosmicbosses.bosses.holidayspirit.PathfinderGoalArrowAttackSnowman;
import net.citizensnpcs.nms.v1_8_R3.entity.EntityHumanNPC;
import net.minecraft.server.v1_8_R3.*;

public class HolidaySpiritEntity extends EntityHolidayGolem {
    public PathfinderGoalArrowAttackSnowman snowmanAttack;
    private HolidaySpirit spiritBoss;

    public HolidaySpiritEntity(final World world) {
        super(world);
    }

    public HolidaySpiritEntity(final World world, final HolidaySpirit spiritBoss) {
        super(world);
        this.spiritBoss = spiritBoss;
        this.a(0.4f, 1.8f);
        //this.getNavigation().a(true);
        this.snowmanAttack = new PathfinderGoalArrowAttackSnowman(this, 1.25, 20, 10.0f);
        this.goalSelector.a(1, (PathfinderGoal) this.snowmanAttack);
        this.goalSelector.a(3, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 6.0f));
        this.goalSelector.a(4, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
        this.targetSelector.a(1, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) this, (Class) EntityHuman.class, 0, true, false, entity -> entity instanceof EntityHuman && !(entity instanceof EntityHumanNPC)));
    }
    
    /*public boolean bk() {
        return true;
    }
    
    public CraftEntity getBukkitEntity() {
        return (CraftEntity)new CraftCreature(this.world.getServer(), (EntityCreature)this);
    }
    
    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(4.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6);
    }
    
    public void e() {
        super.e();
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);
        if (this.spiritBoss != null && this.spiritBoss.isBossSpawned()) {
            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.locX + (l % 2 * 2 - 1) * 0.25f);
                j = MathHelper.floor(this.locY);
                k = MathHelper.floor(this.locZ + (l / 2 % 2 * 2 - 1) * 0.25f);
                if (this.random.nextInt(100) == 0 && this.world.getType(i, j, k).getMaterial() == Material.AIR && this.world.getType(i, j - 1, k).getMaterial().isSolid()) {
                    final BlockChangeManager blockManager = CosmicBosses.getInstance().getBlockChangeManager();
                    final Location location = new Location((org.bukkit.World)this.world.getWorld(), (double)i, (double)j, (double)k);
                    blockManager.replaceBlock(location.getBlock(), this.getId(), -1, org.bukkit.Material.FIRE, (byte)0);
                }
                if (this.world.getType(i, j - 1, k).getMaterial().isSolid()) {
                    final Location location2 = new Location((org.bukkit.World)this.world.getWorld(), (double)i, (double)(j - 1), (double)k);
                    if (location2.getBlock().getType() != org.bukkit.Material.NETHERRACK) {
                        final BlockChangeManager blockManager2 = CosmicBosses.getInstance().getBlockChangeManager();
                        blockManager2.replaceBlock(location2.getBlock(), this.getId(), -1, org.bukkit.Material.NETHERRACK, (byte)0);
                    }
                }
            }
        }
        else if (!this.dead) {
            this.die();
        }
    }
    
    protected Item getLoot() {
        return Items.SNOW_BALL;
    }
    
    protected void dropDeathLoot(final boolean flag, final int i) {
    }*/
}
