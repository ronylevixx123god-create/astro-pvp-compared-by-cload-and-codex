package me.ifamasssxd.cosmicbosses.bosses.soulreaper;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.entities.SoulReaperEnderman;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.ItemBuilder;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderman;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SoulReaper extends Boss {
    Random random;
    private long lastCloneSpawn;

    public SoulReaper(final Location location, final BossType bossType) {
        super(location, bossType);
        this.random = new Random();
        this.lastCloneSpawn = 0L;
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.ULTIMATE));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.ULTIMATE));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ENDERMAN;
    }

    @Override
    public void onBossSpawn() {
        super.onBossSpawn();
    }

    @Override
    public LivingEntity spawnBoss() {
        final CraftWorld world = (CraftWorld) this.location.getWorld();
        CustomEntity.ENDERMAN.registerEntity();
        final SoulReaperEnderman ender = new SoulReaperEnderman((World) world.getHandle());
        ender.setPosition(this.location.getX(), this.location.getY(), this.location.getZ());
        world.getHandle().addEntity((Entity) ender, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final LivingEntity ent = (LivingEntity) ender.getBukkitEntity();
        ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        this.setupBoss(ent);
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
        return ent;
    }

    @Override
    public LivingEntity setupBoss(final LivingEntity e) {
        final LivingEntity ent = super.setupBoss(e);
        return ent;
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null) {
            this.sendMessage(ChatColor.LIGHT_PURPLE + "You can never truly defeat me, I am the reaper of souls, and I shall return.", Sound.ENDERDRAGON_DEATH, 1.3f);
            final Location death = event.getEntity().getLocation();
            for (int i = 0; i < 10; ++i) {
                death.getWorld().dropItemNaturally(death, EnchantmentBook.getSecretDust(ItemRarity.SOUL, this.random.nextInt(70) + 10));
            }
            for (int i = 0; i < 10; ++i) {
                death.getWorld().dropItemNaturally(death, KillTrackerAPI.createSoulShard(50 + this.random.nextInt(51)));
            }
        }
    }

    @Override
    public String getCustomName() {
        return ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + this.getName();
    }

    @Override
    public String getName() {
        return "Soul Reaper";
    }

    @Override
    public void cleanup() {
        this.clearMinions();
    }

    private void clearMinions() {
        for (final org.bukkit.entity.Entity m : this.getMinions()) {
            m.remove();
        }
    }

    @Override
    public String getTitle() {
        return "Reaper";
    }

    public CraftEnderman getEnderman() {
        return (CraftEnderman) this.entity;
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.loops % 20 == 0 && this.getMinions().size() < 3 && this.lastCloneSpawn < System.currentTimeMillis()) {
            final LivingEntity ent = (LivingEntity) this.spawnMinion(EntityType.ENDERMAN, BossUtils.getRandomLocationNearby(this.entity.getLocation(), 3, 1));
            this.sendMessage(ChatColor.LIGHT_PURPLE + "Rise my children of the ender, devour them!");
            if (this.random.nextBoolean()) {
                ent.setMetadata("explode_death", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
            }
            this.lastCloneSpawn = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5L);
        }
        if (this.loops % 120 == 0 || this.loops % 90 == 0) {
            this.sendMessage(ChatColor.LIGHT_PURPLE + "Your end has come, behold the power of the ender.");
            for (final org.bukkit.entity.Entity minion : this.getMinions()) {
                if (minion.isValid()) {
                    final SoulReaperEnderman ender = this.getSoulReaperEnderman(minion);
                    if (ender == null) {
                        continue;
                    }
                    if (this.loops % 120 == 0) {
                        ender.shooting = true;
                    } else {
                        ender.shooting = false;
                    }
                }
            }
        }
        if (this.loops % 4 == 0) {
            this.updateScoreboard();
        }
        super.onUpdate();
    }

    @Override
    public org.bukkit.entity.Entity spawnMinion(final EntityType type, final Location l) {
        final CraftWorld world = (CraftWorld) l.getWorld();
        final SoulReaperEnderman clone = new SoulReaperEnderman((World) world.getHandle(), true);
        clone.setPosition(l.getX(), l.getY(), l.getZ());
        world.getHandle().addEntity((Entity) clone, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final LivingEntity entity = (LivingEntity) clone.getBukkitEntity();
        entity.setMaxHealth(200.0);
        entity.setHealth(200.0);
        final org.bukkit.entity.Entity ent = (org.bukkit.entity.Entity) clone.getBukkitEntity();
        ent.setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.getEntity().getUniqueId()));
        ent.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        this.getMinions().add(ent);
        if (ent instanceof LivingEntity) {
            final LivingEntity li = (LivingEntity) ent;
            li.setCustomName(this.getCustomName());
            li.setCustomNameVisible(true);
            if (li.getEquipment() != null) {
                li.getEquipment().setItemInHandDropChance(0.0f);
                li.getEquipment().setHelmetDropChance(0.0f);
                li.getEquipment().setChestplateDropChance(0.0f);
                li.getEquipment().setLeggingsDropChance(0.0f);
                li.getEquipment().setBootsDropChance(0.0f);
            }
        }
        return ent;
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            event.setCancelled(true);
        }
        super.onDamageTaken(event);
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        final AtomicBoolean performedAOE = new AtomicBoolean(false);
        if (this.random.nextInt(100) <= 30) {
            player.setVelocity(player.getLocation().getDirection().multiply(-1).setY(0.7));
        }
        if (this.random.nextInt(10) <= 4 && BossUtils.canAffectPlayer(player, true)) {
            BossUtils.dealPercentMaxHealthDamage(player, 5.0, 10);
        }
        final AtomicInteger affected = new AtomicInteger(0);
        final AtomicBoolean atomicBoolean;
        player.getNearbyEntities(1.0, 2.0, 1.0).stream().filter(entity -> entity != event.getEntity()).filter(entity -> entity instanceof Player && BossUtils.canAffectPlayer((Player) entity, true)).forEach(entity -> {
            if (affected.get() >= 10) {
                return;
            } else {
                BossUtils.dealPercentHealthDamage(player, 10.0, 5);
                ((Player) entity).damage(0.0);
                affected.incrementAndGet();
                player.setVelocity(player.getVelocity().multiply(-1).setY(1));
                try {
                    de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.09f, 30);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                performedAOE.set(true);
                return;
            }
        });
        if (performedAOE.get()) {
            player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_HIT, 1.0f, 1.3f);
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onEntityTeleport(final EntityTeleportEvent event, final org.bukkit.entity.Entity boss) {
    }

    public SoulReaperEnderman getSoulReaperEnderman(final org.bukkit.entity.Entity entity) {
        if (entity instanceof CraftCreature) {
            final CraftCreature creature = (CraftCreature) entity;
            if (creature.getHandle() instanceof SoulReaperEnderman) {
                return (SoulReaperEnderman) creature.getHandle();
            }
        }
        return null;
    }

    @Override
    public void onMinionUpdate(final org.bukkit.entity.Entity minion, final Event e) {
        super.onMinionUpdate(minion, e);
        if (e instanceof EntityDeathEvent) {
            if (minion.hasMetadata("explode_death")) {
                try {
                    de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), minion.getLocation(), 1f, 1f, 1f, 0.01f, 15);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                minion.getWorld().playSound(minion.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                for (final org.bukkit.entity.Entity ent : minion.getNearbyEntities(10.0, 10.0, 10.0)) {
                    if (ent instanceof Player) {
                        final Player player = (Player) ent;
                        if (!BossUtils.canAffectPlayer(player, true)) {
                            continue;
                        }
                        BossUtils.dealPercentHealthDamage(player, 25.0, 15);
                    }
                }
            }
        } else if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager() instanceof Projectile) {
                if (event.getEntity().getType() != EntityType.PLAYER) {
                    event.setCancelled(true);
                } else {
                    final double dmg = 30.0;
                    final double missingHealth = this.getEntity().getHealth() / this.getMaxHealth();
                    final double missing = 1.0 - missingHealth;
                    event.setDamage(dmg * Math.min(2.0, 1.0 + missing));
                }
            } else if (event.getDamager().equals(minion)) {
                if (!(event.getEntity() instanceof Player)) {
                    event.setCancelled(true);
                }
            } else if (event.getEntity().hasMetadata("minion") && event.getDamager().getType() != EntityType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onMiscUpdate(final Event event) {
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.POISON;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.OUTPOST;
    }

    @Override
    public int getMaxHealth() {
        return 1500;
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage(ChatColor.LIGHT_PURPLE + "Your soul is mine now [NAME]!", false, 0)});
    }
}
