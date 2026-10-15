package me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.yijki.Yijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.abilities.*;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MotherOfYijki extends DungeonYijki {
    private final double DIMENSIONAL_SHIFT_HP_GAP = 20.0;
    private long lastDimensionalShift;
    private long lastSpawnedMinions;
    public boolean inDifferentDimension;
    private Location originalSpawn;
    private double healthBeforeTeleporting;
    private MinionsAbility minionsAbility;
    private double lastDimensionalShiftHealth;
    private boolean inMainWorld;

    public MotherOfYijki(final Location location, final BossType bossType) {
        super(location, bossType);
        this.inDifferentDimension = false;
        this.healthBeforeTeleporting = this.getMaxHealth();
        this.lastDimensionalShiftHealth = 100.0;
        this.originalSpawn = location;
        this.minionsAbility = new MinionsAbility(this);
        this.lastSpawnedMinions = System.currentTimeMillis() - 5000L;
        this.inMainWorld = location.getWorld().equals(Bukkit.getWorlds().get(0));
    }

    @Override
    public BossType getBossType() {
        return BossType.MOTHER_OF_YIJKI;
    }

    @Override
    public BossPhase getBossPhase() {
        return super.getBossPhase();
    }

    public void teleportBackToSpawn() {
        this.teleportBossInstance(this.originalSpawn, false);
        if (this.getEntity().getHealth() < this.healthBeforeTeleporting) {
            this.getEntity().setHealth(this.healthBeforeTeleporting);
        }
        if (this.minionsAbility != null) {
            this.minionsAbility.clearEntities();
        }
    }

    public void teleportBossInstance(final Location newLocation, final boolean different) {
        this.inDifferentDimension = different;
        this.healthBeforeTeleporting = this.getEntity().getHealth();
        this.lastDimensionalShiftHealth = this.getHealthPercent();
        this.location = newLocation;
        this.getEntity().teleport(this.location);
        if (!different) {
            this.lastDimensionalShift = System.currentTimeMillis();
        }
    }

    @Override
    public void findNewAbility() {
        final List<Class<? extends DungeonAbility>> available = this.bossPhase.getAbilityClasses();
        if (this.activeAbility != null) {
            available.remove(this.activeAbility.getClass());
        }
        if (this.lastPhaseClass != null) {
            available.remove(this.lastPhaseClass);
        }
        if (this.inDifferentDimension) {
            available.remove(AfterImage.class);
        }
        available.remove(EndOfWorld.class);
        final double healthPercent = this.getHealthPercent();
        if (healthPercent > 90.0) {
            available.remove(UnstoppableForce.class);
        }
        if (this.inDifferentDimension || (this.lastDimensionalShift != 0L && System.currentTimeMillis() - this.lastDimensionalShift <= TimeUnit.SECONDS.toMillis(10L))) {
            available.remove(DimensionTravelAbility.class);
        }
        if (healthPercent < 50.0 && Math.random() <= 0.25) {
            available.remove(UnstoppableForce.class);
        }
        this.activateAbility(available.get(ThreadLocalRandom.current().nextInt(available.size())));
    }

    public void activateAbility(final Class<? extends DungeonAbility> abilityFound) {
        try {
            final Constructor constr = abilityFound.getDeclaredConstructor(Boss.class);
            final Object object = constr.newInstance(this);
            (this.activeAbility = (DungeonAbility) object).onAbilityEnable();
            this.lastPhaseClass = this.activeAbility.getClass();
            this.abilityActivateTime = System.currentTimeMillis();
            Bukkit.getLogger().info("Activating ability: " + this.activeAbility.getClass().getName());
            this.updateScoreboard();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex2) {
            ex2.printStackTrace();
        }
    }

    @Override
    public void onEntityChangeBlock(final EntityChangeBlockEvent event, final Entity boss) {
        if (event.getEntity().hasMetadata("chainReactionBlock")) {
            final FallingBlock block = (FallingBlock) event.getEntity();
            event.setCancelled(true);
            if (ThreadLocalRandom.current().nextBoolean()) {
                event.getEntity().getWorld().playEffect(event.getEntity().getLocation(), Effect.STEP_SOUND, block.getMaterial().getId());
            }
        }
    }

    @Override
    public LivingEntity spawnBoss() {
        final MotherOfYijkiEntity boss = (MotherOfYijkiEntity) BossUtils.createCustomEntity(CustomEntity.DUNGEON_MOTHER_OF_YIJKI, this, this.location);
        (this.bossEntity = boss).setBoss(this);
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
        MotherOfYijki.bosses.put(e.getUniqueId(), this);
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(msg.getMessage());
        }
        this.setPhase(this.getRandomPhase());
        return e;
    }

    @Override
    public void onUpdate() {
        if (this.getEntity().getLocation().getY() <= 70.0 && !this.inDifferentDimension && !this.inMainWorld) {
            this.getAntiFarm().unstickBoss();
        }
        if (this.inDifferentDimension && this.getEntity().getHealth() < this.healthBeforeTeleporting) {
            this.getEntity().setHealth(this.healthBeforeTeleporting);
            Bukkit.getLogger().info("[CosmicBosses] Setting MotherOfYijki health to " + this.healthBeforeTeleporting + " since it wasnt >=!");
        }
        ++this.loops;
        if (this.loops % this.getSeconds((this.getPhase() == Phase.FIVE) ? 25 : 45) == 0 && !this.inDifferentDimension) {
            final Phase next = this.getRandomPhase();
            this.onPhaseChange(next);
            this.lastPhaseSwitch = System.currentTimeMillis();
        }
        if (this.loops % this.getSeconds(1) == 0) {
            this.updateScoreboard();
        }
        if (System.currentTimeMillis() - this.lastSpawnedMinions >= 100000L && this.minionsAbility.getEntities().size() < 5 && !this.inDifferentDimension && (this.lastDimensionalShift == 0L || System.currentTimeMillis() - this.lastDimensionalShift >= TimeUnit.SECONDS.toMillis(90L))) {
            this.getEntity().getWorld().getPlayers().forEach(pl -> this.sendMessage(pl, "Go my children! Siphon from the life force of these pitiful creatures for your mother - heal me!"));
            this.lastSpawnedMinions = System.currentTimeMillis();
            this.minionsAbility.onAbility();
        }
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (this.inDifferentDimension) {
            event.setCancelled(true);
            return;
        }
        super.onDamageTaken(event);
        if (this.inMainWorld) {
            return;
        }
        final double newHealth = this.getEntity().getHealth();
        final double newHealthPercent = this.getHealthPercent(newHealth - event.getFinalDamage());
        if (newHealth - event.getFinalDamage() <= 0.0 || newHealthPercent <= 0.0) {
            return;
        }
        final double healthDifferenceSinceLast = this.lastDimensionalShiftHealth - newHealthPercent;
        Bukkit.getLogger().info("New Health: " + newHealth + " Percent: " + newHealthPercent + " heal difference: " + healthDifferenceSinceLast);
        if (healthDifferenceSinceLast >= 20.0 && !this.inDifferentDimension && (this.lastDimensionalShift == 0L || System.currentTimeMillis() - this.lastDimensionalShift >= TimeUnit.SECONDS.toMillis(10L))) {
            Bukkit.getLogger().info("Activating Dimension Rift due to health difference: " + newHealthPercent + " from: " + this.lastDimensionalShiftHealth);
            this.lastDimensionalShiftHealth = newHealthPercent;
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> {
                try {
                    this.clearAbilities();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.activateAbility(DimensionTravelAbility.class);
            }, 1L);
        }
    }

    @Override
    protected Phase getRandomPhase() {
        final List<Phase> available = this.phases.stream().filter(ph -> ph != this.getPhase()).collect(Collectors.toList());
        if (this.entity == null) {
            available.remove(Phase.ONE);
            available.remove(Phase.THREE);
        } else {
            final double healthPercent = this.getHealthPercent();
            if (healthPercent > 50.0) {
                available.remove(Yijki.WitherPhase.BOW.getPhase());
            }
            if (healthPercent > 25.0) {
                available.remove(Yijki.WitherPhase.NONE.getPhase());
            }
        }
        available.remove(Yijki.WitherPhase.MIRROR.getPhase());
        available.remove(Yijki.WitherPhase.POTIONS.getPhase());
        return available.get(this.random.nextInt(available.size()));
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final org.bukkit.entity.Entity damager) {
        if (this.inDifferentDimension) {
            event.setCancelled(true);
            if (damager instanceof Player) {
                ((Player) damager).sendMessage(CC.RedB + "(!) " + CC.Red + "You must escape this dimension before you can damage the Mother of Yijki!");
            }
            return;
        }
        super.onDamageTaken(event, damager);
    }

    @Override
    public String getCustomName() {
        return CC.LightPurpleB + "Mother Of Yijki";
    }

    @Override
    public int getShootDelay() {
        return (this.activeAbility instanceof TemporalRift) ? 10 : ((int) ((this.activeAbility instanceof ChainReaction) ? 10.0 : ((int) this.bossPhase.getFireRateMultiplier() * 0.9)));
    }

    @Override
    public void onMinionUpdate(final Entity minion, final Event e) {
        super.onMinionUpdate(minion, e);
        if (minion instanceof Blaze && e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager() instanceof SmallFireball) {
                if (event.getEntity() instanceof Player) {
                    event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 2.4);
                } else {
                    event.setCancelled(true);
                }
            } else if (event.getEntity().equals(minion) && event.getDamager() instanceof Player) {
                try {
                    de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), event.getEntity().getLocation(), 3f, 3f, 3f, 0.09f, 15);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                for (final Entity ent : event.getEntity().getNearbyEntities(8.0, 8.0, 8.0)) {
                    if (ent instanceof Player) {
                        BossUtils.pushAwayPlayer(minion, (Player) ent, 0.6000000238418579);
                    }
                }
            }
        }
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null && event.getEntity() != null) {
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENDERDRAGON_DEATH, 10.0f, 1.1f);
        }
    }

    @Override
    public void cleanup() {
        if (!this.getLocation().getWorld().getName().toLowerCase().startsWith("dungeon")) {
            this.modifier.replaceAll();
            Bukkit.getLogger().info("Removing all blocks modified by Dungeon Yijki due to not being in dungeon world!");
        } else {
            Bukkit.getLogger().info("Skipping block replacement from Dungeon Yijki due to being in dungeon world!");
        }
        this.removeMinions();
    }

    @Override
    public String getName() {
        return this.getCustomName();
    }

    public boolean isInDifferentDimension() {
        return this.inDifferentDimension;
    }

    public void setLastDimensionalShiftHealth(final double lastDimensionalShiftHealth) {
        this.lastDimensionalShiftHealth = lastDimensionalShiftHealth;
    }

    public double getLastDimensionalShiftHealth() {
        return this.lastDimensionalShiftHealth;
    }
}
