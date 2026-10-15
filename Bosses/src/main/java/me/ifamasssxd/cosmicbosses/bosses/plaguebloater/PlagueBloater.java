package me.ifamasssxd.cosmicbosses.bosses.plaguebloater;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.tasks.BleedTask;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.CustomItem;
import me.ifamasssxd.cosmicbosses.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.List;

public class PlagueBloater extends Boss {
    private final PlagueBomb plagueBomb;
    //private final PlagueCarriers plagueCarrier;

    public PlagueBloater(final Location location, final BossType bossType) {
        super(location, bossType);
        this.plagueBomb = new PlagueBomb(this);
        //this.plagueCarrier = new PlagueCarriers(this);
        ItemStack OPKey = CrateKeyItem.getItem(CrateType.valueOf("SEASON"));
        this.getLootPool().add(OPKey);
    }

    @Override
    public String getTitle() {
        return "Plague";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.GIANT;
    }

    @Override
    public void onBossSpawn() {
        final Giant ent = (Giant) this.spawnBoss();
        ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        this.setHelmet(new CustomItem(new ItemStack(Material.DIAMOND_HELMET)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.PROTECTION_FIRE, 5).build());
        this.setChestplate(new CustomItem(new ItemStack(Material.DIAMOND_CHESTPLATE)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        this.setLeggings(new CustomItem(new ItemStack(Material.DIAMOND_LEGGINGS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.THORNS, 1).build());
        this.setBoots(new CustomItem(new ItemStack(Material.DIAMOND_BOOTS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        final BossMessage start = this.getStartingMessage();
        if (start != null) {
            this.sendMessage(ChatColor.ITALIC + start.getMessage());
        }
        this.alert();
    }

    @Override
    public String getCustomName() {
        return "Plague Bloater";
    }

    @Override
    public String getName() {
        return "Plague Bloater";
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.loops % 24 == 0 && this.getAntiFarm().checkStuck()) {
            this.getAntiFarm().unstickBoss();
        }
        if (this.random.nextBoolean() && this.plagueBomb.canUse() && this.loops >= 20) {
            this.plagueBomb.onAbility();
        } /*else if (this.random.nextBoolean() && this.plagueCarrier.canUse() && this.loops >= 4) {
            this.plagueCarrier.onAbility();
        }*/
        if (this.loops % 5 == 0) {
            final Iterator<Entity> ent = this.getMinions().iterator();
            while (ent.hasNext()) {
                final Entity e = ent.next();
                if (e == null || !e.isValid()) {
                    ent.remove();
                } else {
                    if (e.getLocation().distanceSquared(this.getEntity().getLocation()) < 1200.0) {
                        continue;
                    }
                    e.teleport(this.getEntity().getLocation().clone().add(0.0, 5.0, 0.0));
                }
            }
            /*for (Entity ent2 : (List<Entity>) Lists.newArrayList((Iterable) this.plagueCarrier.getEntities())) {
                if (ent2 == null || ent2.isDead()) {
                    //this.plagueCarrier.getEntities().remove(ent2);
                } else {
                    if (ent2.getLocation().distanceSquared(this.getEntity().getLocation()) < 1500.0) {
                        continue;
                    }
                    ent2.teleport(BossUtils.getRandomLocationNearby(this.getEntity().getLocation(), 20, 5, 20));
                }
            }*/
        }
        if (this.loops % 3 == 0 && this.getEntity().hasMetadata("heroicEnvoyBoss") && this.getEntity().getLocation().distance(this.getLocation()) > 30.0) {
            this.getEntity().teleport(this.getLocation());
        }
        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }
        super.onUpdate();
    }

    @Override
    public void cleanup() {
        this.plagueBomb.clearEntities();
        //this.plagueCarrier.clearEntities();
        this.removeMinions();
        this.clearScoreboard();
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (this.random.nextInt(10) <= 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1), false);
        }
        if (this.getEntity().hasMetadata("heroicEnvoyBoss")) {
            event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 1.5);
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            event.setCancelled(true);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.setCancelled(true);
            if (this.getAntiFarm().checkStuck()) {
                this.getAntiFarm().unstickBoss();
            }
        }
        super.onDamageTaken(event);
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager) {
        if (event.getDamager() instanceof Projectile) {
            event.setCancelled(true);
            if (damager instanceof Player) {
                final Player pl = (Player) damager;
                pl.playSound(pl.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.4f);
                this.sendMessage(pl, ChatColor.ITALIC + "Your projectiles cannot harm me. I am but an omen of your doom.");
            }
            return;
        }
        if (this.getEntity().hasMetadata("heroicEnvoyBoss")) {
            event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 1.5);
        }
        if (this.random.nextBoolean() && this.getMinions().size() < 4) {
            /*final Zombie minion = (Zombie) this.spawnMinion(EntityType.ZOMBIE, BossUtils.getRandomLocationNearby(this.getEntity().getLocation(), 10, 5, 2));
            minion.setMetadata("plague_ads", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
            minion.setMaxHealth(20.0);
            minion.setBaby(false);
            minion.setCanPickupItems(false);
            minion.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 2), true);
            minion.getEquipment().setItemInHand(ItemBuilder.buildArmor(Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT));
            Bukkit.getScheduler().runTaskLater(CosmicBosses.getInstance(), minion::remove, 20*300);*/

        } else if (this.getMinions().size() < 14) {
/*            final Zombie minion = (Zombie) this.spawnMinion(EntityType.ZOMBIE, BossUtils.getRandomLocationNearby(this.getEntity().getLocation(), 10, 5, 2));
            minion.setMaxHealth(10.0);
            minion.setBaby(true);
            minion.setCanPickupItems(false);
            minion.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1), true);
            minion.getEquipment().setHelmet(ItemBuilder.buildArmor(Material.DIAMOND_HELMET, Enchantment.DURABILITY, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS));
            minion.getEquipment().setChestplate(ItemBuilder.buildArmor(Material.DIAMOND_CHESTPLATE, Enchantment.DURABILITY, Enchantment.THORNS));
            minion.getEquipment().setLeggings(ItemBuilder.buildArmor(Material.DIAMOND_LEGGINGS, Enchantment.DURABILITY, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS));
            minion.getEquipment().setBoots(ItemBuilder.buildArmor(Material.DIAMOND_BOOTS, Enchantment.DURABILITY, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS));
            minion.getEquipment().setItemInHand(ItemBuilder.buildArmor(Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT));

            Bukkit.getScheduler().runTaskLater(CosmicBosses.getInstance(), minion::remove, 20*300);*/

        }
        super.onDamageTaken(event, damager);
    }

    @Override
    public void onMinionUpdate(final Entity minion, final Event e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (minion.hasMetadata("plague_ads") && event.getEntity() instanceof Player) {
                final Player pl = (Player) event.getEntity();
                BossUtils.dealPercentMaxHealthDamage(pl, 20.0, 30);
            }
        } else if (e instanceof EntityDamageEvent && ((EntityDamageEvent) e).getCause() == EntityDamageEvent.DamageCause.FALL) {
            ((EntityDamageEvent) e).setCancelled(true);
        }
        super.onMinionUpdate(minion, e);
    }

    public void onTNTExplode(final Entity ent) {
        try {
            de.inventivegames.particle.ParticleEffect.LARGE_SMOKE.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation(), 1f, 1f, 1f, 0.09f, 20);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ent.getWorld().playSound(ent.getLocation(), Sound.EXPLODE, 4.0f, 1.0f);
        for (final Entity e : ent.getNearbyEntities(10.0, 10.0, 10.0)) {
            if (e instanceof Player) {
                final Player pl = (Player) e;
                if (!BossUtils.canAffectPlayer(pl, true)) {
                    continue;
                }
                if (BleedTask.isBleeding(pl)) {
                    continue;
                }
                final int duration = 300;
                pl.removePotionEffect(PotionEffectType.REGENERATION);
                pl.removePotionEffect(PotionEffectType.ABSORPTION);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 1));
                pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 2));
                pl.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 1));
                BleedTask.bleedPlayer(pl, 5);
                this.sendMessage(pl, "You've been infected!");
                pl.playSound(pl.getLocation(), Sound.ZOMBIE_REMEDY, 1.0f, 1.4f);
            }
        }
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        this.sendMessage(ChatColor.ITALIC + "Arggh.. I suppose this planets time has not yet come... I will.. return.", Sound.WITHER_DEATH, 1.5f);
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.POISON;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.LEGIT;
    }

    @Override
    public int getMaxHealth() {
        return 2500;
    }

    @Override
    public void onMiscUpdate(final Event event) {
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("If you had not committed great offenses, the galaxy would not have sent a curse as vile as me upon you.", false, 0), new BossMessage("I. AM. DEATH", false, 0), new BossMessage("Come, breathe in my black fog and see your body crumble", false, 0), new BossMessage("You cannot kill a plague.", false, 0)});
    }

    public PlagueBomb getPlagueBomb() {
        return this.plagueBomb;
    }
/*
    public PlagueCarriers getPlagueCarrier() {
        return this.plagueCarrier;
    }*/
}
