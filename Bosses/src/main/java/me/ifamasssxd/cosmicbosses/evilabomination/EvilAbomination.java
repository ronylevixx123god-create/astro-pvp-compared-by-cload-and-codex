package me.ifamasssxd.cosmicbosses.evilabomination;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.CustomItem;
import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EvilAbomination extends Boss implements GuardBoss {
    private WeakReference<Entity> horseEntity;
    private int outsideTicks;
    private Set<UUID> lastDamaged;

    public EvilAbomination(final Location location, final BossType bossType) {
        super(location, bossType);
        this.outsideTicks = 0;
        //this.lastDamaged = Collections.newSetFromMap(new MapMaker().expireAfterWrite(400L, TimeUnit.MILLISECONDS).makeMap());
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public String getCustomName() {
        return CC.DarkGreenB + "Evil Abomination";
    }

    @Override
    public BossType getBossType() {
        return BossType.EVIL_ABOMINATION;
    }

    @Override
    public String getName() {
        return "Evil Abomination";
    }

    @Override
    public LivingEntity spawnBoss() {
        final WorldServer world = ((CraftWorld) this.location.getWorld()).getHandle();
        final net.minecraft.server.v1_8_R3.Entity entity = (net.minecraft.server.v1_8_R3.Entity) new EvilAbominationEntity(this, (World) world);
        entity.setPosition(this.location.getX(), this.location.getY(), this.location.getZ());
        world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final LivingEntity e = (LivingEntity) entity.getBukkitEntity();
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
        EvilAbomination.bosses.put(e.getUniqueId(), this);
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(ChatColor.ITALIC.toString() + msg.getMessage());
        }
        return e;
    }

    @Override
    public void onBossSpawn() {
        /*final LivingEntity skeleton = this.spawnBoss();
        skeleton.getEquipment().setItemInHand(ItemBuilder.buildArmor(Material.BOW, Enchantment.ARROW_DAMAGE, Enchantment.ARROW_FIRE));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        this.setHelmet(new CustomItem(new ItemStack(Material.DIAMOND_HELMET)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        this.setChestplate(new CustomItem(new ItemStack(Material.DIAMOND_CHESTPLATE)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        this.setLeggings(new CustomItem(new ItemStack(Material.DIAMOND_LEGGINGS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).addEnchant(Enchantment.THORNS, 1).build());
        this.setBoots(new CustomItem(new ItemStack(Material.DIAMOND_BOOTS)).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        ((CraftLivingEntity)skeleton).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.4300000071525574);
        final Horse horse = (Horse)this.spawnMinion(EntityType.HORSE, skeleton.getLocation());
        this.horseEntity = new WeakReference<Entity>((Entity)horse);
        final EntityHorse entHorse = ((CraftHorse)horse).getHandle();
        entHorse.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(entHorse.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * 1.190000057220459);
        horse.setMetadata("horse", (MetadataValue)new FixedMetadataValue((Plugin)CosmicBosses.getInstance(), (Object)""));
        horse.setMaxHealth((double)this.getMaxHealth());
        horse.setCustomName(CC.DarkGreenB + "Abomination");
        horse.setCustomNameVisible(true);
        horse.setHealth(horse.getMaxHealth());
        horse.setAdult();
        horse.setVariant(Horse.Variant.UNDEAD_HORSE);
        horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        horse.setDomestication(horse.getMaxDomestication());
        horse.setPassenger((Entity)skeleton);
        this.alert();*/
    }

    @Override
    public void cleanup() {
        final Entity horse = this.horseEntity.get();
        if (horse != null) {
            horse.remove();
            this.horseEntity.clear();
        }
        this.removeMinions();
        this.clearScoreboard();
    }

    @Override
    public void onUpdate() {
        if (this.loops % 12 == 0) {
            if (this.getAntiFarm().checkStuck()) {
                this.getAntiFarm().unstickBoss();
            }
            if (!this.entity.hasPotionEffect(PotionEffectType.SPEED)) {
                this.entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            }
        }
        final Iterator<Entity> ent = this.getMinions().iterator();
        while (ent.hasNext()) {
            final Entity e = ent.next();
            if (e == null || !e.isValid()) {
                ent.remove();
            } else {
                if (e.getLocation().distanceSquared(this.getEntity().getLocation()) < 1000.0) {
                    continue;
                }
                e.teleport(this.getEntity().getLocation().clone());
            }
        }
        final Horse horse = (Horse) this.horseEntity.get();
        if (horse != null && horse.isValid()) {
            if (this.getEntity().getHealth() < this.getEntity().getMaxHealth() * 0.75) {
                horse.damage(2.147483647E9);
            } else {
                if (horse.getPassenger() == null) {
                    horse.setPassenger((Entity) this.getEntity());
                }
                final Creature boss = (Creature) this.getEntity();
                if (boss.getTarget() != null && boss.getTarget().getWorld() == boss.getWorld()) {
                    final double distanceSquared = horse.getLocation().distanceSquared(boss.getTarget().getLocation());
                    if (distanceSquared > 100.0) {
                        final EntityInsentient insent = (EntityInsentient) ((CraftHorse) horse).getHandle();
                        final Location targetLoc = boss.getTarget().getLocation();
                        insent.getNavigation().a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), 1.8);
                    } else if (distanceSquared < 5.0) {
                        final EntityInsentient insent = (EntityInsentient) ((CraftHorse) horse).getHandle();
                        final Location targetLoc = BossUtils.getRandomLocationNearby(boss.getTarget().getLocation(), 7, 4, 0);
                        insent.getNavigation().a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), 1.65);
                    }
                }
            }
        }
        if (horse != null && horse.isDead() && this.getPhase() != Phase.TWO) {
            this.onPhaseChange(Phase.TWO);
            this.horseEntity.clear();
        }
        this.checkGuardLocation(this, this.location);
        this.outsideTicks = this.checkOutsideTicks(this, this.outsideTicks, this.location);
        if (this.loops % 4 == 0) {
            this.updateScoreboard();
        }
        super.onUpdate();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        super.onAttackPlayer(event, player);
        if (event.getDamager() instanceof Projectile) {
            event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 3.0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 3), true);
            if (this.random.nextDouble() <= 0.25) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0), false);
            }
            BossUtils.pushAwayPlayer(event.getDamager(), player, 1.2699999809265137);
        }
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager) {
        if (damager instanceof Projectile) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof Horse) {
            return;
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
        if (event.getCause() == EntityDamageEvent.DamageCause.POISON) {
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
        } else if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            final boolean isMinion = this.getMinions().contains(event.getEntity());
            if (isMinion && event.getDamager() instanceof Projectile && !(((Projectile) event.getDamager()).getShooter() instanceof Player)) {
                event.setCancelled(true);
            } else if (event.getEntity() instanceof Player && this.getMinions().contains(event.getDamager()) && !event.isCancelled()) {
                final Player damaged = (Player) event.getEntity();
                if (this.lastDamaged.contains(damaged.getUniqueId()) || EssentialsUtils.isInGodMode(damaged)) {
                    return;
                }
                final double damage = 4.0;
                if (damaged.getHealth() < damage && !damaged.isDead()) {
                    damaged.setHealth(0.0);
                } else if (damaged.getHealth() > damage) {
                    damaged.setHealth(damaged.getHealth() - damage);
                }
                this.lastDamaged.add(damaged.getUniqueId());
                event.setDamage(0.1);
            }
        } else if (e instanceof EntityDamageEvent) {
            final EntityDamageEvent event2 = (EntityDamageEvent) e;
            if (event2.getCause() == EntityDamageEvent.DamageCause.FIRE || event2.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event2.getCause() == EntityDamageEvent.DamageCause.LAVA || event2.getCause() == EntityDamageEvent.DamageCause.WITHER) {
                event2.setCancelled(true);
                event2.getEntity().setFireTicks(0);
            }
        }
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
        if (newPhase == Phase.TWO) {
            this.setPhase(newPhase);
            this.getEntity().removeMetadata("empower_arrow", (Plugin) CosmicBosses.getInstance());
            this.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            final Location loc = this.getEntity().getLocation();
            loc.getWorld().playSound(loc, Sound.WITHER_SPAWN, 10.0f, 1.3f);
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> {
                loc.getWorld().playSound(loc, Sound.WITHER_SPAWN, 10.0f, 1.8f);
                loc.getWorld().playSound(loc, Sound.ZOMBIE_INFECT, 5.0f, 1.8f);
                return;
            }, 5L);
            this.sendMessage(ChatColor.ITALIC + "You killed something that was already dead. Am I supposed to be impressed? Feel the power of the ROT!", Sound.GHAST_MOAN, 1.3f);
        }
    }

    @Override
    public void teleportToChest(final Boss boss, final Location chestLocation) {
        final Location spawn = chestLocation.clone().add(0.0, 1.0, 0.0);
        final Entity vehicle = this.getEntity().getVehicle();
        if (vehicle != null && vehicle.isValid()) {
            vehicle.eject();
            vehicle.teleport(spawn);
        }
        this.getEntity().teleport(spawn);
        if (vehicle != null && vehicle.isValid()) {
            vehicle.setPassenger((Entity) this.getEntity());
        }
    }

    @Override
    public int getDistance() {
        return 225;
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        final Entity horse = this.horseEntity.get();
        if (horse != null && horse.isValid()) {
            horse.remove();
            this.horseEntity.clear();
        }
        super.onDeath(event);
        if (event != null) {
            this.sendMessage(ChatColor.ITALIC + "Arghh... You may have defeated me, but you cannot defeat The Plague. This world is doomed.");
        }
    }

    @Override
    public void onMiscUpdate(final Event event) {
        if (event instanceof ProjectileHitEvent) {
            final ProjectileHitEvent e = (ProjectileHitEvent) event;
            if (e.getEntity() instanceof WitherSkull && ((WitherSkull) e.getEntity()).isCharged()) {
                final Location landed = e.getEntity().getLocation();
                Block block = landed.getBlock();
                if (block.getType().isSolid()) {
                    block = block.getRelative(BlockFace.UP);
                    if (block.getType().isSolid()) {
                        block = block.getRelative(BlockFace.UP);
                    }
                }
                this.createMinion(block.getLocation());
                landed.getWorld().playSound(landed, Sound.ZOMBIE_IDLE, 1.1f, 1.1f);
            }
        }
    }

    private LivingEntity createMinion(final Location spawn) {
        final LivingEntity entity = (LivingEntity) spawn.getWorld().spawnEntity(spawn, EntityType.ZOMBIE);
        entity.setMaxHealth(15.0);
        entity.setHealth(entity.getMaxHealth());
        entity.getEquipment().setItemInHand(new CustomItem(new ItemStack(Material.DIAMOND_SWORD)).addEnchant(Enchantment.DURABILITY, 5).addEnchant(Enchantment.FIRE_ASPECT, 1).build());
        entity.getEquipment().setItemInHandDropChance(0.0f);
        entity.getEquipment().setChestplate(new CustomItem(new ItemStack(Material.IRON_CHESTPLATE)).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.THORNS, 5).build());
        entity.getEquipment().setChestplateDropChance(0.0f);
        if (this.random.nextBoolean()) {
            entity.getEquipment().setLeggings(new CustomItem(new ItemStack(Material.IRON_LEGGINGS)).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).build());
            entity.getEquipment().setLeggingsDropChance(0.0f);
        }
        entity.setCustomName(CC.DarkGreen + "Plagued Minion");
        entity.setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.getEntity().getUniqueId()));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4), true);
        final EntityInsentient ent = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
        //ent.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.41999998688697815);
        final AttributeInstance instance = ent.getAttributeInstance((IAttribute) ReflectionUtils.getObject(EntityZombie.class, null, "bp"));
        if (instance != null) {
            instance.setValue(0.0);
        }
        this.getMinions().add((Entity) entity);
        return entity;
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.POISON;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.HEROIC;
    }

    @Override
    public int getMaxHealth() {
        return 1500;
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("Disease. Rot. Death. These are the pillars I exist by, and these are the pillars you will die animTime.", false, -1), new BossMessage("Feel the decay of the plague consume you!", false, 0), new BossMessage("You cannot escape my blight, this place was doomed long before you set foot here!", false, 0)});
    }
}
