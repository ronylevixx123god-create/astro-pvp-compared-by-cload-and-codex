package me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.CustomItem;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Enforcer extends Boss implements GuardBoss {
    private long lastTargetChange;
    private long lastCheck;
    private int outsideTicks;

    public Enforcer(final Location location, final BossType bossType) {
        super(location, bossType);
        this.lastTargetChange = 0L;
        this.lastCheck = 0L;
        this.outsideTicks = 0;
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String getCustomName() {
        return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Cosmic Enforcer";
    }

    @Override
    public String getName() {
        return "Cosmic Enforcer";
    }

    @Override
    public LivingEntity setupBoss(final LivingEntity e) {
        final LivingEntity entity = super.setupBoss(e);
        final EntityCreature creature = ((CraftCreature) entity).getHandle();
        creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(24.0);
        //creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.550000011920929);
        creature.getAttributeInstance(GenericAttributes.c).setValue(0.8500000238418579);
        creature.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(3.0);
        final Zombie zombie = this.getPigZombie();
        zombie.setBaby(false);
        zombie.setVillager(false);
        return entity;
    }

    @Override
    public void onBossSpawn() {
        final LivingEntity ent = this.spawnBoss();
        final EntityInsentient insent = (EntityInsentient) ((CraftCreature) ent).getHandle();
        final EntityCreature creature = ((CraftCreature) ent).getHandle();
        BossUtils.clearGoalSelectors(insent);
        final PathfinderGoalSelector targetSelector = BossUtils.getSelector(insent, "targetSelector");
        final PathfinderGoalSelector goalSelector = BossUtils.getSelector(insent, "goalSelector");
        goalSelector.a(0, (PathfinderGoal) new PathfinderGoalFloat(insent));
        goalSelector.a(2, (PathfinderGoal) new PathfinderGoalMeleeAttack(creature, (Class) EntityHuman.class, 1.2, false));
        goalSelector.a(5, (PathfinderGoal) new PathfinderGoalMoveTowardsRestriction(creature, 1.3));
        goalSelector.a(8, (PathfinderGoal) new PathfinderGoalLookAtPlayer(insent, (Class) EntityHuman.class, 8.0f));
        goalSelector.a(8, (PathfinderGoal) new PathfinderGoalRandomLookaround(insent));
        targetSelector.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget(creature, true));
        targetSelector.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget(creature, (Class) EntityHuman.class, false, true));
        this.setHelmet(new CustomItem(new ItemStack(Material.DIAMOND_HELMET)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.PROTECTION_FIRE, 5).build());
        this.setChestplate(new CustomItem(new ItemStack(Material.DIAMOND_CHESTPLATE)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        this.setLeggings(new CustomItem(new ItemStack(Material.DIAMOND_LEGGINGS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.THORNS, 1).build());
        this.setBoots(new CustomItem(new ItemStack(Material.DIAMOND_BOOTS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        ent.getEquipment().setItemInHand(new CustomItem(new ItemStack(Material.DIAMOND_SWORD)).setUnbreakable(true).addEnchant(Enchantment.KNOCKBACK, 1).addEnchant(Enchantment.DAMAGE_ALL, 5).build());
        ent.getEquipment().setItemInHandDropChance(0.0f);
    }

    @Override
    public void cleanup() {
        this.clearScoreboard();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
    }

    @Override
    public void onMiscUpdate(final Event event) {
        if (event instanceof EntityTargetEvent) {
            final EntityTargetEvent eve = (EntityTargetEvent) event;
            if (!(eve.getTarget() instanceof Player)) {
                eve.setCancelled(true);
                return;
            }
            if (eve.getReason() == EntityTargetEvent.TargetReason.CUSTOM || eve.getReason() == EntityTargetEvent.TargetReason.CLOSEST_PLAYER) {
                return;
            }
            eve.setCancelled(true);
        }
    }

    public Zombie getPigZombie() {
        return (Zombie) this.getEntity();
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }
        if (this.loops % 10 == 0 && this.getAntiFarm().checkStuck()) {
            this.getAntiFarm().unstickBoss();
        }
        this.checkGuardLocation(this, this.location);
        if (this.lastTargetChange == 0L || System.currentTimeMillis() - this.lastTargetChange > 600L || this.getPigZombie().getTarget() == null || !this.getPigZombie().getTarget().isValid()) {
            if (this.lastCheck == 0L || System.currentTimeMillis() - this.lastCheck > 500L) {
                if (this.getEntity().getWorld().equals(this.location.getWorld())) {
                    final double distanceFromChest = this.location.distanceSquared(this.getEntity().getLocation());
                    if (distanceFromChest >= 325.0) {
                        this.getEntity().teleport(this.location.clone().add(0.0, 1.0, 0.0));
                        final Player closest = this.getClosestTarget(64.0);
                        if (closest != null) {
                            this.setTarget((LivingEntity) closest);
                            return;
                        }
                        this.setTarget(null);
                    }
                    final Player closest = this.getClosestTarget(64.0);
                    this.outsideTicks = this.checkOutsideTicks(this, this.outsideTicks, this.location);
                    this.lastCheck = System.currentTimeMillis();
                    if (closest != null) {
                        this.lastTargetChange = System.currentTimeMillis();
                        this.setTarget(null);
                        this.setTarget((LivingEntity) closest);
                        this.outsideTicks = 0;
                    }
                }
            }
        }
        super.onUpdate();
    }

    public Player getClosestTarget(final double range) {
        double closest = -1.0;
        Player closestPlayer = null;
        double radius = 30.0;
        radius *= radius;
        for (final Entity ent : this.getEntity().getNearbyEntities(range, range / 2.0, range)) {
            if (!(ent instanceof Player)) {
                continue;
            }
            final Player pl = (Player) ent;
            if (!pl.getWorld().equals(this.location.getWorld()) || pl.getGameMode() != GameMode.SURVIVAL) {
                continue;
            }
            final double distance = pl.getLocation().distanceSquared(this.location);
            if ((closest != -1.0 || distance > radius) && distance > closest) {
                continue;
            }
            closestPlayer = pl;
            closest = distance;
        }
        if (closestPlayer != null && closest <= radius) {
            return closestPlayer;
        }
        return null;
    }

    public void setTarget(final LivingEntity target) {
        final EntityCreature entity = ((CraftCreature) this.getPigZombie()).getHandle();
        if (target == null) {
            entity.setGoalTarget(null);
        } else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity) target).getHandle());
            entity.setGoalTarget(((CraftLivingEntity) target).getHandle());
        }
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        int percentDamage = ThreadLocalRandom.current().nextInt(11) + 7;
        if (this.random.nextInt(100) < 30) {
            percentDamage += 5;
            player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 3.0f, 1.3f);
        }
        BossUtils.dealPercentMaxHealthDamage(player, percentDamage, 25, false);
        if (player.isSprinting() && this.random.nextInt(100) < 20) {
            player.setSprinting(false);
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.setCancelled(true);
            if (this.getAntiFarm().checkStuck()) {
                this.getAntiFarm().unstickBoss();
            }
        }
        event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 2.0);
        super.onDamageTaken(event);
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.POISON;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.LEGENDARY_PLUS;
    }

    @Override
    public int getMaxHealth() {
        return 1500;
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return Lists.newArrayList(new BossMessage[]{new BossMessage("This treasure doesnt belong to you!", false, -1)});
    }
}
