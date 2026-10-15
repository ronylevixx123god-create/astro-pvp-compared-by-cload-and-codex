package me.ifamasssxd.cosmicbosses.bosses.yijki;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import de.inventivegames.particle.ParticleEffect;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.commands.CommandBoss;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.*;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.struct.enums.WeaponType;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Yijki extends Boss implements GlobalBoss {
    Cage cage;
    long lastPhaseSwitch;
    private Phase lastPhase;
    List<Phase> phases;
    private List<Wither> clones;

    public Yijki(final Location location, final BossType bossType) {
        super(location, bossType);
        this.lastPhaseSwitch = 0L;
        this.lastPhase = this.getPhase();
        this.phases = (List<Phase>) Lists.newArrayList(new Phase[]{Phase.ONE, Phase.TWO, Phase.THREE, Phase.FOUR, Phase.FIVE, Phase.SIX});
        this.clones = Lists.newArrayList();
        this.modifier = new BlockModifier();
        this.cage = new Cage(this, this.getModifier());
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
    }

    @Override
    public String getTitle() {
        return "Yijki";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WITHER;
    }

    @Override
    public LivingEntity spawnBoss() {
        final WitherBoss boss = (WitherBoss) BossUtils.createCustomEntity(CustomEntity.WITHER, this, this.location);
        boss.setBoss(this);
        final LivingEntity e = (LivingEntity) boss.getBukkitEntity();
        this.uuid = e.getUniqueId();
        e.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMetadata("boss", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMaxHealth((double) this.getMaxHealth());
        e.setHealth(e.getMaxHealth());
        this.setEnt(e);
        e.setRemoveWhenFarAway(false);
        e.setCustomName(this.getColor(true) + this.getCustomName());
        e.setCustomNameVisible(true);
        this.entity = e;
        Yijki.bosses.put(e.getUniqueId(), this);
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(msg.getMessage());
        }
        return e;
    }

    @Override
    public String getCustomName() {
        return "Yijki, World Destroyer";
    }

    @Override
    public String getName() {
        return "Yijki, World Destroyer";
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.loops % 5 == 0 && this.getAntiFarm().checkStuck()) {
            this.getAntiFarm().unstickBoss();
        }
        if (this.loops % this.getSeconds(30) == 0) {
            final Phase next = this.getRandomPhase();
            this.lastPhase = this.getPhase();
            this.onPhaseChange(next);
            this.lastPhaseSwitch = System.currentTimeMillis();
        }
        if (this.loops % this.getSeconds(5) == 0 && this.getMinions().size() < 20) {
            for (int toSpawn = this.random.nextInt(3) + 2, i = 0; i < toSpawn; ++i) {
                if (this.getMinions().size() >= 10) {
                    break;
                }
                this.spawnMinion();
            }
        }
        if (this.cage.canUse() && System.currentTimeMillis() - this.lastPhaseSwitch >= 5000L && this.getEntity().getTicksLived() >= 200) {
            this.cage.onAbility();
            this.sendMessage(ChatColor.ITALIC + "You are dead. I have already killed you. You have just not yet witnessed it for yourself.");
            for (int j = 0; j < this.random.nextInt(3) + 3 && this.getMinions().size() < 20; ++j) {
                this.spawnMinion();
            }
            for (final Entity ent : this.getMinions()) {
                if (ent instanceof Skeleton) {
                    ent.setMetadata("berserk", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10L))));
                }
            }
        }
        for (final Wither wither : this.clones) {
            if (wither.isValid() && wither.getWorld() == this.getEntity().getWorld()) {
                final double distance = wither.getLocation().distanceSquared(this.getEntity().getLocation());
                if (distance > 800.0) {
                    wither.teleport(this.getEntity().getLocation());
                } else {
                    if (wither.getTarget() != null) {
                        continue;
                    }
                    final Creature boss = (Creature) this.getEntity();
                    if (boss.getTarget() == null || !(boss.getTarget() instanceof Player)) {
                        continue;
                    }
                    wither.setTarget(boss.getTarget());
                }
            }
        }
        final Iterator<Entity> iter = this.getMinions().iterator();
        while (iter.hasNext()) {
            final Entity ent = iter.next();
            if (ent == null || ent.isDead()) {
                iter.remove();
            } else {
                final double distance = ent.getLocation().distanceSquared(this.getEntity().getLocation());
                if (ent.getWorld() != this.getEntity().getWorld() || distance > 750.0) {
                    final Creature wither2 = (Creature) this.getEntity();
                    final LivingEntity target = wither2.getTarget();
                    if (target != null && target instanceof Player && target.getWorld() == wither2.getWorld() && target.getLocation().distanceSquared(wither2.getLocation()) < 1000.0 && BossUtils.canAffectPlayer((Player) target, true)) {
                        ent.teleport(BossUtils.getRandomLocationNearby(wither2.getLocation(), 3, 2, 1));
                    }
                }
                if (!ent.hasMetadata("berserk") || this.loops % this.getSeconds(1) != 0 || this.random.nextInt(10) != 0) {
                    continue;
                }
                final long expire = ent.getMetadata("berserk").get(0).asLong();
                if (expire < System.currentTimeMillis()) {
                    ent.removeMetadata("berserk", (Plugin) CosmicBosses.getInstance());
                    return;
                }
                try {
                    ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation(), 1f, 1f, 1f, 0.09f, 14);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        if (this.loops % this.getSeconds(4) == 0) {
            this.updateScoreboard();
        }
        super.onUpdate();
    }

    private Skeleton spawnMinion() {
        final Skeleton wither = (Skeleton) this.spawnMinion(EntityType.SKELETON, BossUtils.getRandomLocationNearby(this.getEntity().getLocation(), 10, 4, 2));
        wither.setSkeletonType(Skeleton.SkeletonType.WITHER);
        wither.setMetadata("no_drops", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        wither.setMaxHealth(55.0);
        wither.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        wither.setHealth(wither.getMaxHealth());
        wither.getEquipment().setItemInHand(ItemBuilder.buildArmor(Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT));
        wither.getEquipment().setItemInHandDropChance(0.0f);
        if (this.random.nextInt(100) <= 60) {
            if (this.random.nextBoolean()) {
                wither.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            }
            if (this.random.nextBoolean()) {
                wither.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            }
            if (this.random.nextBoolean()) {
                wither.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            }
            if (this.random.nextBoolean()) {
                wither.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            }
        }
        return wither;
    }

    private Phase getRandomPhase() {
        final List<Phase> available = this.phases.stream().filter(ph -> ph != this.lastPhase).collect(Collectors.toList());
        return available.get(this.random.nextInt(available.size()));
    }

    @Override
    public void onMinionUpdate(final Entity minion, final Event e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager().equals(minion) && !event.isCancelled()) {
                event.getEntity().setFireTicks(60);
                if (event.getEntity() instanceof Player) {
                    final Player pl = (Player) event.getEntity();
                    if (minion.getType() == EntityType.WITHER) {
                        if (BossUtils.canAffectPlayer(pl, true)) {
                            final double maxHealth = this.getMaxHealth();
                            final int bossMissingHealth = (int) (this.getMaxHealth() / this.getEntity().getHealth());
                            final double percentHealthLost = bossMissingHealth / maxHealth * 100.0;
                            final int extraDamage = (int) Math.ceil(percentHealthLost / 5.0);
                            int percentDamage = Math.min(25, 5 + extraDamage);
                            if (this.random.nextInt(100) < 25) {
                                percentDamage += 5;
                            }
                            BossUtils.dealPercentMaxHealthDamage(pl, percentDamage, 20);
                            pl.playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 3.0f, 1.3f);
                            if (pl.isSprinting() && this.random.nextInt(100) < 20) {
                                pl.setSprinting(false);
                                pl.playSound(pl.getLocation(), Sound.WITHER_HURT, 1.0f, 1.4f);
                            }
                        }
                    } else {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                        if (this.random.nextInt(10) <= 7) {
                            pl.removePotionEffect(PotionEffectType.ABSORPTION);
                            pl.removePotionEffect(PotionEffectType.REGENERATION);
                        }
                        if (minion.hasMetadata("berserk")) {
                            final long expire = minion.getMetadata("berserk").get(0).asLong();
                            if (expire < System.currentTimeMillis()) {
                                minion.removeMetadata("berserk", (Plugin) CosmicBosses.getInstance());
                                return;
                            }
                            event.setDamage(0.0);
                            BossUtils.dealPercentMaxHealthDamage(pl, 20.0, 30);
                        }
                    }
                }
            } else if (event.getDamager() instanceof WitherSkull || event.getDamager().getType() == EntityType.UNKNOWN) {
                event.setCancelled(true);
            }
        } else if (e instanceof EntityDamageEvent) {
            final EntityDamageEvent event2 = (EntityDamageEvent) e;
            if (event2.getCause() == EntityDamageEvent.DamageCause.FALL || event2.getCause() == EntityDamageEvent.DamageCause.FIRE || event2.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event2.getCause() == EntityDamageEvent.DamageCause.WITHER) {
                event2.setCancelled(true);
            }
        } else if (e instanceof EntityTargetEvent) {
            final EntityTargetEvent event3 = (EntityTargetEvent) e;
            if (event3.getEntity() instanceof Wither && !(event3.getTarget() instanceof Player)) {
                event3.setCancelled(true);
                event3.setTarget((Entity) null);
                final Wither wither = (Wither) event3.getEntity();
                final Creature boss = (Creature) this.getEntity();
                if (boss.getTarget() != null) {
                    wither.setTarget(boss.getTarget());
                }
            }
        }
    }

    @Override
    public void onPhaseChange(Phase newPhase) {
        if (newPhase.getPhase() > 6) {
            newPhase = Phase.ONE;
        }
        this.setPhase(newPhase);
        String tip = "";
        final WitherPhase ph = WitherPhase.getPhase(newPhase);
        this.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + ph.getPhaseName() + " Phase");
        if (newPhase != Phase.SIX) {
            tip = ChatColor.YELLOW + "IMMUNE to " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "ALL" + ChatColor.YELLOW + " damage except for " + ChatColor.YELLOW + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString();
            switch (ph) {
                case SWORDS: {
                    tip += "SWORDS";
                    break;
                }
                case AXE: {
                    tip += "AXES";
                    break;
                }
                case BOW: {
                    tip += "BOWS";
                    break;
                }
                case POTIONS: {
                    tip += "POTION";
                    break;
                }
                case NONE: {
                    tip = ChatColor.YELLOW + ChatColor.BOLD.toString() + "INVINCIBLE" + ChatColor.YELLOW + " to " + ChatColor.YELLOW + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "ALL" + ChatColor.YELLOW + " damage!";
                    break;
                }
            }
            this.sendMessage(this.getColor(false) + tip, Sound.WITHER_IDLE, 0.8f);
        }
        if (ph == WitherPhase.MIRROR) {
            this.cleanupClones();
            final Location spawn = this.getEntity().getLocation();
            for (int i = 0; i < 2; ++i) {
                final Wither wither = (Wither) spawn.getWorld().spawnEntity(spawn, EntityType.WITHER);
                this.clones.add(wither);
                wither.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Yijki, World Destroyer");
                wither.setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.getUuid()));
                wither.setMaxHealth((double) (this.getMaxHealth() / 5));
                wither.setHealth(wither.getMaxHealth());
                final Creature boss = (Creature) this.getEntity();
                if (boss.getTarget() != null) {
                    wither.setTarget(boss.getTarget());
                }
            }
        } else {
            this.cleanupClones();
        }
        this.updateScoreboard();
        if (ph != WitherPhase.NONE) {
            this.sendMessage(ChatColor.ITALIC + "I have reprogrammed my defenses against your attacks. You can no longer harm me as you have before.");
        }
    }

    @Override
    public void cleanup() {
        this.removeMinions();
        this.cleanupClones();
        this.getModifier().removeAllBlocks(true);
        this.clearScoreboard();
    }

    private void cleanupClones() {
        if (this.clones.size() > 0) {
            this.clones.forEach(Entity::remove);
            this.clones.clear();
        }
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (BossUtils.canAffectPlayer(player, true)) {
            final double maxHealth = this.getMaxHealth();
            final int bossMissingHealth = (int) (this.getMaxHealth() / this.getEntity().getHealth());
            final double percentHealthLost = bossMissingHealth / maxHealth * 100.0;
            final int extraDamage = (int) Math.ceil(percentHealthLost / 5.0);
            int percentDamage = Math.min(25, 5 + extraDamage);
            if (this.random.nextInt(100) < 40) {
                percentDamage += 5;
            }
            BossUtils.dealPercentMaxHealthDamage(player, percentDamage, 25);
            player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 3.0f, 1.3f);
            if (player.isSprinting() && this.random.nextInt(100) < 20) {
                player.setSprinting(false);
                player.playSound(player.getLocation(), Sound.WITHER_HURT, 1.0f, 1.4f);
            }
            if (this.random.nextInt(100) <= 60) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
            }
            if (player.isOnGround()) {
                player.setVelocity(new Vector(0, 1, 0));
            }
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager) {
        if (damager instanceof Player || damager instanceof Arrow) {
            final Player p = (damager instanceof Arrow) ? (Player) ((Arrow) damager).getShooter() : ((Player) damager);
            WitherPhase phase = null;
            if (this.getPhase() == Phase.ONE || this.getPhase() == Phase.SIX) {
                if (p.getItemInHand() != null && !p.getItemInHand().getType().name().endsWith("_SWORD")) {
                    phase = WitherPhase.SWORDS;
                }
            } else if (this.getPhase() == Phase.TWO) {
                if (!(event.getDamager() instanceof Arrow)) {
                    phase = WitherPhase.BOW;
                }
            } else if (this.getPhase() == Phase.THREE) {
                if (p.getItemInHand() != null && !p.getItemInHand().getType().name().endsWith("_AXE")) {
                    phase = WitherPhase.AXE;
                }
            } else if (this.getPhase() == Phase.FOUR) {
                if (!(event.getEntity() instanceof ThrownPotion)) {
                    phase = WitherPhase.POTIONS;
                }
            } else if (this.getPhase() == Phase.FIVE) {
                phase = WitherPhase.NONE;
            }
            if (phase != null) {
                event.setCancelled(true);
                event.setDamage(0.0);
                p.sendMessage(String.format(phase.getMessage(), this.getColor(true) + this.getName()));
                p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.1f);
                return;
            }
        }
        event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 3.0);
        final double missingHealth = this.getMaxHealth() - this.getEntity().getHealth();
        final double damageReduction = missingHealth / this.getMaxHealth();
        final double newDamage = event.getDamage(EntityDamageEvent.DamageModifier.BASE) - event.getDamage(EntityDamageEvent.DamageModifier.BASE) * damageReduction;
        event.setDamage(newDamage);
        this.getEntity().setCustomName(this.getColor(true) + this.getCustomName());
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
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            event.setCancelled(true);
        }
        super.onDamageTaken(event);
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null) {
            this.sendMessage(ChatColor.ITALIC + "I-Impossible...", Sound.ENDERDRAGON_GROWL, 0.8f);
            if (Math.random() <= 0.1) {
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), CommandBoss.getDimensionalCore());
                Bukkit.getLogger().info("Dropping Interdimensional Core!");
            }
        }
        CosmicBosses.getInstance().getBlockChangeManager().restoreAllBlocks(this.getEntity().getEntityId());
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("There is no stopping me. No defeating me. You cannot win.", false, 0), new BossMessage("I am Yijki, destroyer of worlds. I'm afraid your time has come.", false, -1), new BossMessage("Your fate is inevitable, give up now.", false, 0), new BossMessage("Feel the power of absolute darkness.", false, 0)});
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.WITHER;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.LEGENDARY_PLUS;
    }

    @Override
    public int getMaxHealth() {
        return 2000;
    }

    @Override
    public void onMiscUpdate(final Event event) {
        if (event instanceof ProjectileLaunchEvent) {
            final Creature wither = (Creature) this.getEntity();
            if (wither.getTarget() != null && !(wither.getTarget() instanceof Player)) {
                wither.setTarget((LivingEntity) null);
                ((ProjectileLaunchEvent) event).setCancelled(true);
            }
        }
    }

    public enum WitherPhase {
        SWORDS(Phase.ONE, "Sword", WeaponType.SWORD),
        BOW(Phase.TWO, "Bow", WeaponType.BOW),
        AXE(Phase.THREE, "Axe", WeaponType.AXE),
        POTIONS(Phase.FOUR, "Potion", (WeaponType) null),
        NONE(Phase.FIVE, "Invincible", (WeaponType) null, ChatColor.RED + "Nothing can damage this creature!"),
        MIRROR(Phase.SIX, "Mirror", WeaponType.SWORD, ChatColor.RED + "Clones have been spawned!");

        Phase phase;
        String phaseName;
        WeaponType type;
        String msg;

        private WitherPhase(final Phase phase, final String name, final WeaponType type) {
            this.phase = phase;
            this.phaseName = name;
            this.type = type;
        }

        private WitherPhase(final Phase phase, final String name, final WeaponType type, final String msg) {
            this.phase = phase;
            this.phaseName = name;
            this.type = type;
            this.msg = msg;
        }

        public String getMessage() {
            if (this.msg != null) {
                return this.msg;
            }
            return ChatColor.RED + "You must use a " + ChatColor.RED + ChatColor.BOLD.toString() + this.getPhaseName().toUpperCase() + ChatColor.RED + " to damage %s!";
        }

        public static WitherPhase getPhase(final Phase phase) {
            for (final WitherPhase ph : values()) {
                if (ph.getPhase().equals(phase)) {
                    return ph;
                }
            }
            return values()[ThreadLocalRandom.current().nextInt(values().length)];
        }

        public Phase getPhase() {
            return this.phase;
        }

        public String getPhaseName() {
            return this.phaseName;
        }

        public WeaponType getType() {
            return this.type;
        }

        public String getMsg() {
            return this.msg;
        }
    }
}
