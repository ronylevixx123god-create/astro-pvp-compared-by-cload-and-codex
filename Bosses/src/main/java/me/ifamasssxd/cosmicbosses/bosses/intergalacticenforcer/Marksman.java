package me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.PathfinderGoalBossArrowAttack;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.CustomItem;
import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Marksman extends Boss implements GuardBoss {
    private int outsideTicks;

    public Marksman(final Location location, final BossType bossType) {
        super(location, bossType);
        this.outsideTicks = 0;
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.ULTIMATE));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.ULTIMATE));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }

    @Override
    public LivingEntity setupBoss(final LivingEntity e) {
        final LivingEntity entity = super.setupBoss(e);
        final EntityCreature creature = ((CraftCreature) entity).getHandle();
        creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0);
        creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.4000000059604645);
        creature.getAttributeInstance(GenericAttributes.c).setValue(0.6499999761581421);
        creature.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.0);
        final Skeleton skeleton = (Skeleton) entity;
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        return entity;
    }

    @Override
    public void onBossSpawn() {
        final Skeleton skeleton = (Skeleton) this.spawnBoss();
        final EntitySkeleton entity = ((CraftSkeleton) skeleton).getHandle();
        ReflectionUtils.setObject(EntitySkeleton.class, entity, new PathfinderGoalBossArrowAttack((IRangedEntity) entity, 1.0, 5, 20, 15.0f), "bp");
        BossUtils.clearGoalSelectors((EntityInsentient) entity);
        final PathfinderGoalSelector targetSelector = BossUtils.getSelector((EntityInsentient) entity, "targetSelector");
        final PathfinderGoalSelector goalSelector = BossUtils.getSelector((EntityInsentient) entity, "goalSelector");
        goalSelector.a(1, (PathfinderGoal) new PathfinderGoalFloat((EntityInsentient) entity));
        goalSelector.a(5, (PathfinderGoal) new PathfinderGoalMoveTowardsRestriction((EntityCreature) entity, 1.3));
        goalSelector.a(6, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) entity, (Class) EntityHuman.class, 8.0f));
        goalSelector.a(6, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) entity));
        targetSelector.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget((EntityCreature) entity, false));
        targetSelector.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) entity, (Class) EntityHuman.class, false, true));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        this.setHelmet(new CustomItem(new ItemStack(Material.GOLD_HELMET)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).addEnchant(Enchantment.PROTECTION_FIRE, 5).build());
        this.setChestplate(new CustomItem(new ItemStack(Material.GOLD_CHESTPLATE)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        this.setLeggings(new CustomItem(new ItemStack(Material.GOLD_LEGGINGS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).addEnchant(Enchantment.THORNS, 1).build());
        this.setBoots(new CustomItem(new ItemStack(Material.GOLD_BOOTS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        skeleton.getEquipment().setItemInHand(new CustomItem(new ItemStack(Material.BOW)).setUnbreakable(true).addEnchant(Enchantment.ARROW_KNOCKBACK, 5).addEnchant(Enchantment.ARROW_FIRE, 5).addEnchant(Enchantment.ARROW_DAMAGE, 10).build());
        skeleton.getEquipment().setItemInHandDropChance(0.0f);
        final BossMessage start = this.getStartingMessage();
        if (start != null) {
            this.sendMessage(ChatColor.ITALIC + start.getMessage());
        }
    }

    @Override
    public String getCustomName() {
        return ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Intergalactic Marksman";
    }

    @Override
    public String getName() {
        return "Intergalactic Marksman";
    }

    @Override
    public void cleanup() {
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
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 2.0);
        super.onDamageTaken(event);
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.loops % 20 == 0 && this.getAntiFarm().checkStuck()) {
            this.getAntiFarm().unstickBoss();
        }
        this.checkGuardLocation(this, this.location);
        if (this.loops % 3 == 0) {
            this.outsideTicks = this.checkOutsideTicks(this, this.outsideTicks, this.location);
        }
        super.onUpdate();
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager) {
        if (damager instanceof Projectile) {
            event.setCancelled(true);
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ZOMBIE_INFECT, 1.5f, 2.0f);
            damager.remove();
            return;
        }
        super.onDamageTaken(event, damager);
    }

    @Override
    public void onAttackPlayer(EntityDamageByEntityEvent event, Player player) {
        int percentDamage = ThreadLocalRandom.current().nextInt(15) + 10;
        if (this.random.nextInt(100) < 30)
            percentDamage += 5;
        BossUtils.dealPercentMaxHealthDamage(player, percentDamage, 25, false);
        player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 3.0F, 1.3F);
        if (player.isSprinting() && this.random.nextInt(100) < 20)
            player.setSprinting(false);
        super.onAttackPlayer(event, player);
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.FIRE;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.LEGENDARY;
    }

    @Override
    public int getMaxHealth() {
        return 600;
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("You will crumble under my arrow!", false, -1)});
    }
}
