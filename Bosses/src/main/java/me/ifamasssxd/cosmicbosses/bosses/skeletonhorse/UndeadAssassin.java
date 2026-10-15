package me.ifamasssxd.cosmicbosses.bosses.skeletonhorse;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.CustomItem;
import me.ifamasssxd.cosmicbosses.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
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

public class UndeadAssassin extends Boss {
    Horse horse;
    EmpoweredArrow empoweredArrow;
    //Summoning summoning;
    public boolean invincible;
    int hits;
    long lastHit;
    long timeSinceInvincible;
    Entity lastTarget;
    int storedHealth;

    public UndeadAssassin(final Location location, final BossType bossType) {
        super(location, bossType);
        this.empoweredArrow = new EmpoweredArrow(this);
        //this.summoning = new Summoning(this);
        this.invincible = false;
        this.hits = 0;
        this.lastHit = System.currentTimeMillis();
        this.timeSinceInvincible = System.currentTimeMillis();
        this.storedHealth = this.getMaxHealth();
        ItemStack OPKey = CrateKeyItem.getItem(CrateType.valueOf("SEASON"));
        this.getLootPool().add(OPKey);
    }

    @Override
    public String getTitle() {
        return "Assassin";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }

    @Override
    public LivingEntity spawnBoss() {
        final LivingEntity e = (LivingEntity) BossUtils.createCustomEntity(CustomEntity.SKELETON_BOSS, this.location);
        this.uuid = e.getUniqueId();
        e.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMetadata("boss", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMaxHealth((double) this.getMaxHealth());
        e.setHealth(e.getMaxHealth());
        this.setEnt(e);
        e.setCustomName(this.getDifficulty().getColor() + ChatColor.BOLD.toString() + this.getCustomName());
        e.setCustomNameVisible(true);
        e.setRemoveWhenFarAway(false);
        this.entity = e;
        UndeadAssassin.bosses.put(e.getUniqueId(), this);
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(ChatColor.ITALIC.toString() + msg.getMessage());
        }
        return e;
    }

    @Override
    public void onBossSpawn() {
        final LivingEntity skeleton = this.spawnBoss();
        skeleton.getEquipment().setItemInHand(ItemBuilder.buildArmor(Material.BOW, Enchantment.ARROW_DAMAGE, Enchantment.ARROW_FIRE));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        this.setHelmet(new CustomItem(new ItemStack(Material.IRON_HELMET)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        this.setChestplate(new CustomItem(new ItemStack(Material.IRON_CHESTPLATE)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        this.setLeggings(new CustomItem(new ItemStack(Material.IRON_LEGGINGS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchant(Enchantment.THORNS, 1).build());
        this.setBoots(new CustomItem(new ItemStack(Material.IRON_BOOTS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        (this.horse = (Horse) this.spawnMinion(EntityType.HORSE, skeleton.getLocation())).setMetadata("horse", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        this.horse.setMaxHealth((double) this.getMaxHealth());
        this.horse.setHealth(this.horse.getMaxHealth());
        this.horse.setAdult();
        this.horse.setVariant(Horse.Variant.SKELETON_HORSE);
        this.horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        this.horse.setDomestication(this.horse.getMaxDomestication());
        this.horse.setPassenger(skeleton);
        this.alert();
    }

    @Override
    public String getCustomName() {
        return "Undead Assassin";
    }

    @Override
    public String getName() {
        return "Undead Assassin";
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        final LivingEntity entity = this.getEntity();
        if (this.invincible && this.random.nextInt(10) <= 2) {
            try {
                de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.09f, 50);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.09f, 30);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (this.loops % 12 == 0) {
            if (!this.invincible && this.getAntiFarm().checkStuck()) {
                this.getAntiFarm().unstickBoss();
            }
            if (!entity.hasPotionEffect(PotionEffectType.SPEED)) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            }
        }
        if (this.loops % 4 == 0 && this.invincible && entity.getHealth() < this.storedHealth) {
            entity.setHealth((double) this.storedHealth);
        }
/*        if (!this.invincible && this.getPhase() == Phase.TWO && this.summoning.canUse() && (this.hits >= 10 || System.currentTimeMillis() - this.timeSinceInvincible >= this.summoning.getCooldown() * 0.45)) {
            this.hits = 0;
            this.timeSinceInvincible = System.currentTimeMillis();
            this.storedHealth = (int) entity.getHealth();
            this.sendMessage(ChatColor.ITALIC + "Rise my minions!");
            this.sendMessage(ChatColor.ITALIC + "Have you not figured it out? I. AM. ALREADY. DEAD. Now come, join me in the underworld.");
            this.sendMessage(ChatColor.WHITE + "You must defeat the minions to damage the boss!");
            //this.summoning.onAbility();
        }*/
        if (this.getPhase() == Phase.ONE && this.empoweredArrow.canUse()) {
            this.empoweredArrow.onAbility();
        }
        if (this.horse != null && this.horse.isValid()) {
            if (this.getEntity().getHealth() < this.getEntity().getMaxHealth() * 0.75) {
                this.horse.damage(2.147483647E9);
            } else {
                if (this.horse.getPassenger() == null) {
                    this.horse.setPassenger(this.getEntity());
                }
                final Creature boss = (Creature) this.getEntity();
                if (boss.getTarget() != null && boss.getTarget().getWorld() == boss.getWorld()) {
                    final double distanceSquared = this.horse.getLocation().distanceSquared(boss.getTarget().getLocation());
                    if (distanceSquared > 100.0) {
                        final EntityInsentient insent = (EntityInsentient) ((CraftHorse) this.horse).getHandle();
                        final Location targetLoc = boss.getTarget().getLocation();
                        insent.getNavigation().a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), 2.0);
                    } else if (distanceSquared < 5.0) {
                        final EntityInsentient insent = (EntityInsentient) ((CraftHorse) this.horse).getHandle();
                        final Location targetLoc = BossUtils.getRandomLocationNearby(boss.getTarget().getLocation(), 7, 4, 0);
                        insent.getNavigation().a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), 2.1);
                    }
                }
            }
        } else if ((this.horse == null || this.horse.isDead()) && this.getPhase() != Phase.TWO) {
            this.onPhaseChange(Phase.TWO);
            this.horse = null;
        }
        final Iterator<Entity> ent = this.getMinions().iterator();
        while (ent.hasNext()) {
            final Entity e = ent.next();
            if (e == null || !e.isValid()) {
                ent.remove();
            } else {
                if (e.getLocation().distanceSquared(this.getEntity().getLocation()) < 1200.0) {
                    continue;
                }
                e.teleport(this.getEntity().getLocation().clone().add(0.0, 15.0, 0.0));
            }
        }
        if (this.invincible && this.getMinions().size() <= 0) {
            this.removeInvincibility();
        }
        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }
        if (!this.invincible && this.loops % 2 * 20 == 0) {
            final Creature boss = (Creature) this.getEntity();
            if (!boss.isInsideVehicle() && boss.getTarget() != null && boss.getTarget().isValid() && boss.getTarget() instanceof Player && boss.getTarget().getWorld().equals(this.getEntity().getWorld())) {
                this.lastTarget = (Entity) boss.getTarget();
                final double distance = this.lastTarget.getLocation().distanceSquared(this.getEntity().getLocation());
                if (this.lastTarget.isValid() && distance <= 200.0 && distance > 60.0) {
                    final EntityInsentient insent = (EntityInsentient) ((CraftSkeleton) this.getEntity()).getHandle();
                    final net.minecraft.server.v1_8_R3.Entity ent2 = ((CraftEntity) this.lastTarget).getHandle();
                    insent.getNavigation().a(ent2, 2.0);
                }
            } else if (this.lastTarget != null) {
                final double distance = this.getLocation().distanceSquared(this.getEntity().getLocation());
                if (this.lastTarget.getWorld().equals(this.getEntity().getWorld()) && !this.lastTarget.isDead() && this.getDamageTracker().containsKey(this.lastTarget.getUniqueId()) && distance <= 200.0) {
                    boss.setTarget((LivingEntity) this.lastTarget);
                }
            }
        }
        super.onUpdate();
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (this.getPhase() == Phase.TWO) {
            event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 3.0);
            player.setFireTicks(600);
            if (BossUtils.canAffectPlayer(player, true)) {
                BossUtils.dealPercentMaxHealthDamage(player, 10.0, 10);
            }
        }
        if (event.getDamager().hasMetadata("boss_empowered_arrow")) {
            final Entity arrow = event.getDamager();
            event.getDamager().removeMetadata("boss_empowered_arrow", (Plugin) CosmicBosses.getInstance());
            arrow.getWorld().playSound(arrow.getLocation(), Sound.EXPLODE, 10.0f, 1.1f);
            if (arrow.getPassenger() != null) {
                arrow.getPassenger().remove();
            }
            player.setFireTicks(200);
            if (BossUtils.canAffectPlayer(player, true)) {
                BossUtils.dealPercentMaxHealthDamage(player, 40.0, 10);
            }
            if (this.random.nextInt(10) == 0) {
                try {
                    de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), arrow.getLocation(), 1f, 1f, 1f, 0.09f, 50);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                try {
                    de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), arrow.getLocation(), 1f, 1f, 1f, 0.09f, 30);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            for (final Entity ent : player.getNearbyEntities(4.0, 4.0, 4.0)) {
                if (ent instanceof Player) {
                    final Player pl = (Player) ent;
                    if (!BossUtils.canAffectPlayer(pl, true)) {
                        continue;
                    }
                    BossUtils.dealPercentHealthDamage(pl, 10.0, 10);
                    pl.setFireTicks(100);
                }
            }
        } else if (event.getDamager() instanceof Arrow) {
            event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 5.0);
        }
        super.onAttackPlayer(event, player);
    }

    @SneakyThrows
    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager){
        if (event.getDamager() instanceof Projectile) {
            event.setCancelled(true);
            return;
        }
        if (this.invincible) {
            event.setCancelled(true);
            if (damager instanceof Player) {
                final Player pl = (Player) damager;
                BossUtils.pushAwayPlayer((Entity) this.getEntity(), pl, 2.299999952316284);
                this.sendMessage(ChatColor.WHITE + "You cannot kill what is already dead.");
            }
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.WITHER_IDLE, 3.0f, 1.8f);
            if (this.random.nextInt(10) <= 4) {
                de.inventivegames.particle.ParticleEffect.EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.09f, 30);
            }
            return;
        }
        if (System.currentTimeMillis() - this.lastHit <= 500L) {
            ++this.hits;
        } else {
            this.hits = 1;
        }
        this.lastHit = System.currentTimeMillis();
        if (damager instanceof Player && event.getDamager() instanceof Player) {
            final Player pl = (Player) damager;
            if (this.random.nextInt(100) <= 10) {
                if (BossUtils.canAffectPlayer(pl, true)) {
                    pl.setFireTicks(30);
                    BossUtils.pushAwayPlayer((Entity) this.getEntity(), pl, 10.0);
                    if (this.random.nextInt(3) == 0) {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 140, 3));
                    }
                    this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.WITHER_SHOOT, 4.0f, 0.8f);
                }
            } else if (this.getPhase() == Phase.TWO && this.random.nextInt(10) <= 4) {
                this.getEntity().teleport(BossUtils.getRandomLocationNearby(damager.getLocation(), 2, 1, 1));
                this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.PIG_DEATH, 10.0f, 1.4f);
                if (this.random.nextInt(3) > 1) {
                    this.sendMessage(ChatColor.ITALIC + "Don't blink, you might die.");
                }
                de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.09f, 30);
            }
        }
        super.onDamageTaken(event, damager);
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.setCancelled(true);
            if (this.getAntiFarm().checkStuck()) {
                this.getAntiFarm().unstickBoss();
            }
        }
        if (this.invincible) {
            event.setCancelled(true);
        }
        super.onDamageTaken(event);
    }

    @Override
    public void onMinionUpdate(final Entity minion, final Event e) {
        if (minion.hasMetadata("horse")) {
            if (e instanceof EntityDeathEvent) {
                this.getEntity().leaveVehicle();
                this.getMinions().remove(minion);
                this.onPhaseChange(Phase.TWO);
            } else if (e instanceof EntityDamageByEntityEvent) {
                final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                if (event.getDamager() instanceof Player) {
                    this.onDamageTaken(event, event.getDamager());
                }
            } else if (e instanceof EntityDamageEvent) {
                final EntityDamageEvent event2 = (EntityDamageEvent) e;
                this.onDamageTaken(event2);
            }
        } else if (e instanceof EntityDeathEvent) {
            this.getMinions().remove(minion);
            if (this.invincible && this.getMinions().size() == 0) {
                this.removeInvincibility();
            }
        } else if (e instanceof EntityDamageEvent) {
            final EntityDamageEvent event2 = (EntityDamageEvent) e;
            if (event2.getCause() == EntityDamageEvent.DamageCause.FIRE || event2.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event2.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                event2.setCancelled(true);
                event2.getEntity().setFireTicks(0);
            }
        }
    }

    @Override
    public void removeMinion(final Entity ent) {
    }

    public void removeInvincibility() {
        this.invincible = false;
        this.timeSinceInvincible = System.currentTimeMillis();
        //this.summoning.activateCooldown();
        this.getEntity().removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
        this.setPhase(newPhase);
        if (newPhase == Phase.TWO) {
            this.getEntity().removeMetadata("empower_arrow", (Plugin) CosmicBosses.getInstance());
            this.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            this.setHelmet(ItemBuilder.buildArmor(Material.DIAMOND_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL));
            this.setChestplate(ItemBuilder.buildArmor(Material.DIAMOND_CHESTPLATE, Enchantment.THORNS));
            this.setLeggings(ItemBuilder.buildArmor(Material.DIAMOND_LEGGINGS, Enchantment.PROTECTION_FIRE));
            this.setBoots(ItemBuilder.buildArmor(Material.DIAMOND_BOOTS, Enchantment.PROTECTION_PROJECTILE));
            final ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
            weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
            weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
            weapon.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            this.getEntity().getEquipment().setItemInHandDropChance(0.0f);
            this.getEntity().getEquipment().setItemInHand(weapon);
            final Location loc = this.getEntity().getLocation();
            loc.getWorld().playSound(loc, Sound.WITHER_SPAWN, 10.0f, 1.3f);
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> loc.getWorld().playSound(loc, Sound.ENDERMAN_SCREAM, 10.0f, 1.8f), 40L);
            this.sendMessage(ChatColor.ITALIC + "So you killed my pet? Well guess what, now I'm going to KILL ALL OF YOU!", Sound.GHAST_MOAN, 1.3f);
        }
    }

    @Override
    public void cleanup() {
        this.removeMinions();
        this.clearScoreboard();
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null) {
            this.sendMessage(ChatColor.ITALIC + "You should not have come here... you will never leave alive!");
        }
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
        return 2000;
    }

    @Override
    public void onMiscUpdate(final Event event) {
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return Lists.newArrayList();
    }
}
