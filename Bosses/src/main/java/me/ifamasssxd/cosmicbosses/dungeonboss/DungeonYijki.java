package me.ifamasssxd.cosmicbosses.dungeonboss;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import de.inventivegames.particle.ParticleEffect;
import lombok.Getter;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.yijki.Yijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.abilities.*;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.*;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DungeonYijki extends Boss implements DungeonBoss {
    protected Phase lastPhase;
    protected long lastPhaseSwitch;
    protected BossPhase bossPhase;
    protected DungeonYijkiEntity bossEntity;
    protected DungeonAbility activeAbility;
    private EndOfWorld endOfWorldAbility;
    protected Class<? extends DungeonAbility> lastPhaseClass;
    protected long abilityActivateTime;
    private List<Location> spawnLocations;
    protected List<Phase> phases;

    public DungeonYijki(final Location location, final BossType bossType) {
        super(location, bossType);
        this.bossPhase = BossPhase.PHASE_2;
        this.abilityActivateTime = System.currentTimeMillis();
        this.spawnLocations = Lists.newArrayList();
        this.phases = Lists.newArrayList(Phase.ONE, Phase.TWO, Phase.THREE, Phase.FOUR, Phase.FIVE, Phase.SIX);
        this.modifier = new BlockModifier();
        this.spawnLocations.add(location);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WITHER;
    }

    @Override
    public String getCustomName() {
        return "Mother of Yijki";
    }

    @Override
    public String getName() {
        return "Mother of Yijki";
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

    public Location getRandomSpawnLocation() {
        if (this.spawnLocations != null && this.spawnLocations.size() > 0) {
            final Location random = this.spawnLocations.get(ThreadLocalRandom.current().nextInt(this.spawnLocations.size()));
            return this.location = random;
        }
        return super.getLocation();
    }

    @Override
    public void onUpdate() {
        this.loops++;
        if (this.loops % this.getSeconds((this.getPhase() == Phase.FIVE) ? 28 : 45) == 0) {
            Phase next = this.getRandomPhase();
            this.onPhaseChange(next);
            this.lastPhaseSwitch = System.currentTimeMillis();
        }
        if (this.loops % this.getSeconds(1) == 0) {
            this.updateScoreboard();
        }
    }

    public void setBossPhase(final BossPhase phase) {
        this.bossPhase = phase;
        this.getEntity().setMaxHealth((double) phase.getMaxHealth());
        this.getEntity().setHealth((double) phase.getMaxHealth());
        this.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 36000, 1, true));
    }

    public void clearAbilities() {
        if (this.activeAbility != null) {
            this.activeAbility.onAbilityDisable();
            this.activeAbility = null;
        }
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (this.bossPhase != null && event.getEntity() instanceof Projectile) {
            event.setDamage((double) this.bossPhase.getProjectileDamage());
        }
        if (event.getEntity() instanceof WitherSkull && event.getEntity() instanceof Player) {
            final Player pl = (Player)event.getEntity();
            pl.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 0));
        }
        super.onAttackPlayer(event, player);
    }

    public List<Player> getWorldPlayers() {
        return (List<Player>) this.getEntity().getWorld().getPlayers();
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        super.onDamageTaken(event);
        if (!event.isCancelled() && event.getFinalDamage() >= this.getEntity().getHealth()) {
            Bukkit.getPluginManager().callEvent((Event) new PreBossDeathEvent((Entity) this.getEntity(), this, event));
        }
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null && event.getEntity() != null) {
            try {
                ParticleEffect.LARGE_SMOKE.sendToPlayers(Bukkit.getOnlinePlayers(), event.getEntity().getLocation(), 3f, 3f, 3f, 0.09f, 15);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager) {
        if (!(damager instanceof Player) && !(damager instanceof Arrow)) {
            event.setCancelled(true);
            return;
        }
        if (this.bossPhase != null) {
            event.setDamage(this.bossPhase.getDamageReduction() * event.getDamage(EntityDamageEvent.DamageModifier.BASE));
        }


        final Player p = (damager instanceof Arrow) ? (Player) ((Arrow) damager).getShooter() : ((Player) damager);
        if (this.activeAbility instanceof AfterImage && ((AfterImage) this.activeAbility).isAnyImagesAlive()) {
            event.setCancelled(true);
            event.setDamage(0.0);
            p.sendMessage(CC.RedB + "(!) " + CC.Red + "You must kill all After Images before you can deal more damage!");
            p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.1f);
            return;
        }
        Yijki.WitherPhase phase = null;
        if (this.getPhase() == Phase.TWO) {
            if (!(event.getDamager() instanceof Arrow)) {
                phase = Yijki.WitherPhase.BOW;
            }
        } else if (this.getPhase() == Phase.ONE || this.getPhase() == Phase.SIX) {
            if (event.getDamager() instanceof Projectile || (p.getItemInHand() != null && !p.getItemInHand().getType().name().endsWith("_SWORD"))) {
                phase = Yijki.WitherPhase.SWORDS;
            }
        } else if (this.getPhase() == Phase.THREE) {
            if (event.getDamager() instanceof Projectile || (p.getItemInHand() != null && !p.getItemInHand().getType().name().endsWith("_AXE"))) {
                phase = Yijki.WitherPhase.AXE;
            }
        } else if (this.getPhase() == Phase.FOUR) {
            if (!(event.getEntity() instanceof ThrownPotion)) {
                phase = Yijki.WitherPhase.POTIONS;
            }
        } else if (this.getPhase() == Phase.FIVE) {
            phase = Yijki.WitherPhase.NONE;
        }
        if (phase != null) {
            event.setCancelled(true);
            event.setDamage(0.0);
            p.sendMessage(String.format(phase.getMessage(), this.getColor(true) + this.getName()));
            p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.1f);
            return;
        }
        if (p.getWorld().getName().endsWith("yijki_a") && !WorldGuardUtils.isInRegionsFast(p.getLocation(), "bridge", "hanger")) {
            event.setCancelled(true);
            p.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot damage Yijki outside of the boss room!");
            return;
        }
        if (this.getEntity().getHealth() - event.getFinalDamage() <= 250.0 && this.endOfWorldAbility == null && this.bossPhase == BossPhase.PHASE_3) {
            if (this.getEntity().getHealth() <= event.getFinalDamage()) {
                event.setCancelled(true);
            }
            Bukkit.getLogger().info("Triggering EndOfWorld ability due to health being " + this.getEntity().getHealth() + " and damage being " + event.getFinalDamage());
            (this.endOfWorldAbility = new EndOfWorld(this)).onAbilityEnable();
        }
        super.onDamageTaken(event, damager);
        if (!event.isCancelled() && event.getFinalDamage() >= this.getEntity().getHealth()) {
            Bukkit.getPluginManager().callEvent((Event) new PreBossDeathEvent((Entity) this.getEntity(), this, (EntityDamageEvent) event));
        }
    }

    @Override
    public Entity spawnMinion(final EntityType type, final Location l) {
        Entity ent;
        if (type == EntityType.WITHER) {
            final CraftWorld craftWorld = (CraftWorld) l.getWorld();
            final DungeonYijkiImage image = new DungeonYijkiImage((World) craftWorld.getHandle(), this);
            image.setPosition(l.getX(), l.getY(), l.getZ());
            craftWorld.getHandle().addEntity((net.minecraft.server.v1_8_R3.Entity) image, CreatureSpawnEvent.SpawnReason.CUSTOM);
            ent = (Entity) image.getBukkitEntity();
        } else {
            ent = l.getWorld().spawnEntity(l, type);
        }
        ent.setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.getEntity().getUniqueId()));
        ent.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        this.getMinions().add(ent);
        if (ent instanceof LivingEntity) {
            final LivingEntity li = (LivingEntity) ent;
            li.setRemoveWhenFarAway(false);
            li.setCustomName(this.getColor(true) + this.getName() + "'s Minion");
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
    public LivingEntity spawnBoss() {
        final DungeonYijkiEntity boss = (DungeonYijkiEntity) BossUtils.createCustomEntity(CustomEntity.DUNGEON_YIJKI, this, this.location);
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
        DungeonYijki.bosses.put(e.getUniqueId(), this);
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(msg.getMessage());
        }
        this.setPhase(this.getRandomPhase());
        return e;
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
    public void setPhase(final Phase phase) {
        this.lastPhase = this.getPhase();
        super.setPhase(phase);
    }

    @Override
    public void onPhaseChange(Phase newPhase) {
        if (newPhase.getPhase() > 6) {
            newPhase = Phase.ONE;
        }
        this.setPhase(newPhase);
        String tip = "";
        final Yijki.WitherPhase ph = Yijki.WitherPhase.getPhase(newPhase);
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
        this.updateScoreboard();
        if (ph != Yijki.WitherPhase.NONE) {
            this.sendMessage(ChatColor.ITALIC + "I have reprogrammed my defenses against your attacks. You can no longer harm me as you have before.");
        }
    }

    public int getShootDelay() {
        return (this.activeAbility != null && this.activeAbility instanceof TemporalRift) ? 13 : ((this.activeAbility != null && this.activeAbility instanceof ChainReaction) ? 12 : ((int) this.bossPhase.getFireRateMultiplier()));
    }

    protected double getHealthPercent(final double health) {
        if (this.getEntity() == null || !this.getEntity().isValid()) {
            return 0.0;
        }
        return health / this.getEntity().getMaxHealth() * 100.0;
    }

    public void findNewAbility() {
        final List<Class<? extends DungeonAbility>> available = this.bossPhase.getAbilityClasses();
        if (this.activeAbility != null) {
            available.remove(this.activeAbility.getClass());
        }
        if (this.lastPhaseClass != null) {
            available.remove(this.lastPhaseClass);
        }
        if (this.getHealthPercent() > 90.0) {
            available.remove(UnstoppableForce.class);
        }
        final Class<? extends DungeonAbility> abilityFound = available.get(ThreadLocalRandom.current().nextInt(available.size()));
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

    protected Phase getRandomPhase() {
        final List<Phase> available = this.phases.stream().filter(ph -> ph != this.getPhase()).collect(Collectors.toList());
        if (this.entity == null) {
            available.remove(Phase.ONE);
            available.remove(Phase.THREE);
        } else {
            final double healthPercent = this.getHealthPercent();
            if (healthPercent > 35.0) {
                available.remove(Phase.ONE);
            }
            if (healthPercent > 50.0) {
                available.remove(Phase.THREE);
            }
            if (healthPercent >= 70.0) {
                if (ThreadLocalRandom.current().nextBoolean() && !this.getPhase().equals(Phase.THREE) && !available.contains(Phase.THREE)) {
                    available.add(Phase.THREE);
                } else if (ThreadLocalRandom.current().nextInt(3) <= 1 && !this.getPhase().equals(Phase.ONE) && !available.contains(Phase.ONE)) {
                    available.add(Phase.ONE);
                }
            }
        }
        available.remove(Phase.SIX);
        available.remove(Phase.FOUR);
        return available.get(this.random.nextInt(available.size()));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onMiscUpdate(final Event event) {
        if (this.activeAbility != null) {
            this.activeAbility.onAbilityEvent(event);
        }
    }

    @Override
    public void onMinionUpdate(final Entity minion, final Event e) {
        if (this.activeAbility != null) {
            this.activeAbility.onAbilityEvent(e);
        }
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
        return this.bossPhase.getMaxHealth();
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return Lists.newArrayList(new BossMessage("Now you will truly meet death!", false, -1));
    }

    @Override
    public net.minecraft.server.v1_8_R3.Entity getNMSEntity() {
        return this.bossEntity;
    }

    public BossPhase getBossPhase() {
        return this.bossPhase;
    }

    public DungeonYijkiEntity getBossEntity() {
        return this.bossEntity;
    }

    public DungeonAbility getActiveAbility() {
        return this.activeAbility;
    }

    public EndOfWorld getEndOfWorldAbility() {
        return this.endOfWorldAbility;
    }

    public long getAbilityActivateTime() {
        return this.abilityActivateTime;
    }

    public List<Location> getSpawnLocations() {
        return this.spawnLocations;
    }

    public enum BossPhase {
        PHASE_1(1000, 6, 0.25f, 15.0f, Lists.newArrayList(ChainReaction.class, TemporalRift.class, UnstoppableForce.class)),
        PHASE_2(1500, 10, 0.25f, 10.0f, Lists.newArrayList(AntiWither.class, AfterImage.class, UnstoppableForce.class, TemporalRift.class, ChainReaction.class)),
        PHASE_3(2000, 14, 0.1f, 6.0f, Lists.newArrayList(EndOfWorld.class, AfterImage.class, UnstoppableForce.class, TemporalRift.class, ChainReaction.class));

        private int maxHealth;
        private int projectileDamage;
        private float damageReduction;
        private float fireRateMultiplier;
        private List<Class<? extends DungeonAbility>> abilityClasses;

        public List<Class<? extends DungeonAbility>> getAbilityClasses() {
            return Lists.newArrayList(this.abilityClasses);
        }

        private BossPhase(final int maxHealth, final int projectileDamage, final float damageReduction, final float fireRateMultiplier, final List<Class<? extends DungeonAbility>> abilityClasses) {
            this.maxHealth = maxHealth;
            this.projectileDamage = projectileDamage;
            this.damageReduction = damageReduction;
            this.fireRateMultiplier = fireRateMultiplier;
            this.abilityClasses = abilityClasses;
        }

        public int getMaxHealth() {
            return this.maxHealth;
        }

        public int getProjectileDamage() {
            return this.projectileDamage;
        }

        public float getDamageReduction() {
            return this.damageReduction;
        }

        public float getFireRateMultiplier() {
            return this.fireRateMultiplier;
        }
    }
}
