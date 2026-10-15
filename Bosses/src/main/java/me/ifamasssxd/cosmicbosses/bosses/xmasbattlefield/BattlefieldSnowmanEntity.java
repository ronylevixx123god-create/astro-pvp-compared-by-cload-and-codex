package me.ifamasssxd.cosmicbosses.bosses.xmasbattlefield;

import me.ifamasssxd.cosmicbosses.bosses.EntityHolidayGolem;
import me.ifamasssxd.cosmicbosses.bosses.holidayspirit.PathfinderGoalArrowAttackSnowman;
import net.citizensnpcs.nms.v1_8_R3.entity.EntityHumanNPC;
import net.minecraft.server.v1_8_R3.*;

public class BattlefieldSnowmanEntity extends EntityHolidayGolem {
    private BattlefieldSnowman boss;

    public BattlefieldSnowmanEntity(final World world) {
        this(world, null);
        this.dead = true;
    }

    public BattlefieldSnowmanEntity(final World world, final BattlefieldSnowman spiritBoss) {
        super(world);
        this.a(0.4f, 1.8f);
        //this.getNavigation().a(true);
        this.boss = spiritBoss;
        this.goalSelector.a(1, (PathfinderGoal) new PathfinderGoalArrowAttackSnowman(this, 1.25, 20, 10.0f));
        this.goalSelector.a(1, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 6.0f));
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
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(32.0);
    }
    
    public void e() {
        if (this.boss == null) {
            this.die();
            Bukkit.getLogger().info("[BattlefieldSnowman] Unable to find boss.");
            return;
        }
        super.e();
        for (int l = 0; l < 4; ++l) {
            final int i = MathHelper.floor(this.locX + (l % 2 * 2 - 1) * 0.25f);
            final int j = MathHelper.floor(this.locY);
            final int k = MathHelper.floor(this.locZ + (l / 2 % 2 * 2 - 1) * 0.25f);
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
    }*/
}
