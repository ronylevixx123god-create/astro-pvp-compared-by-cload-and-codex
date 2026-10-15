package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.Tuple;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonBoss;
import me.ifamasssxd.cosmicbosses.dungeonboss.abilities.EndOfWorld;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.BlockModifier;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CorruptCosmonaut extends Boss implements DungeonBoss {
    protected Phase lastPhase;
    protected long lastPhaseSwitch;
    private boolean hasFloorBroke;
    protected BossPhase bossPhase;
    protected CorruptCosmonautEntity bossEntity;
    protected DungeonAbility activeAbility;
    private EndOfWorld endOfWorldAbility;
    protected Class<? extends DungeonAbility> lastPhaseClass;
    protected long abilityActivateTime;
    private List<Location> spawnLocations;
    private org.bukkit.entity.Entity lastTarget;
    private boolean duplicateMinions;
    private Tuple<UUID, Integer> currentCombo;
    private List<Block> floorBlocksToBreak;
    private long lastHitTime = 0;
    public long spawnTime;
    private int ticks;

    public CorruptCosmonaut(final Location location, final BossType bossType) {
        super(location, bossType);
        this.bossPhase = BossPhase.PHASE_1;
        this.spawnTime = System.currentTimeMillis();
        this.abilityActivateTime = System.currentTimeMillis();
        this.spawnLocations = Lists.newArrayList();
        this.modifier = new BlockModifier();
        this.spawnLocations.add(location);
        this.duplicateMinions = false;
        this.currentCombo = null;
        this.floorBlocksToBreak = new ArrayList<>();
        this.ticks = 0;
    }

    public void setBossPhase(final BossPhase phase) {
        this.bossPhase = phase;
        this.getEntity().setMaxHealth((double) phase.getMaxHealth());
        this.getEntity().setHealth((double) phase.getMaxHealth());
        this.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 36000, 1, true));
    }

    public void findNewAbility() {
        this.currentCombo = null;

        final List<Class<? extends DungeonAbility>> available = this.bossPhase.getAbilityClasses();
        if (this.activeAbility != null) {
            available.remove(this.activeAbility.getClass());
        }
        if (this.lastPhaseClass != null) {
            available.remove(this.lastPhaseClass);
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

    @Override
    public LivingEntity spawnBoss() {
        final CorruptCosmonautEntity boss = (CorruptCosmonautEntity) BossUtils.createCustomEntity(CustomEntity.CORRUPT_COSMONAUT, this, this.location);
        (this.bossEntity = boss).setBoss(this);
        final LivingEntity e = (LivingEntity) boss.getBukkitEntity();
        e.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMetadata("boss", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMaxHealth((double) this.getMaxHealth());
        e.setHealth(e.getMaxHealth());
        e.getEquipment().setHelmet(SkinType.valueOf("CORRUPT_HELMET").apply(new ItemStack(Material.DIAMOND_HELMET)));
        ItemStack fillerSword = new ItemStack(Material.DIAMOND_SWORD);
        fillerSword.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        fillerSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        fillerSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        e.getEquipment().setItemInHandDropChance(0.0f);
        fillerSword = SkinType.valueOf("CORRUPT_SWORD").apply(fillerSword);
        e.getEquipment().setItemInHand(fillerSword);
        e.setRemoveWhenFarAway(false);
        e.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + this.getCustomName());
        e.setCustomNameVisible(true);

        e.setMetadata("corrupt_boss", new FixedMetadataValue(CosmicBosses.get(), true));

        ((Skeleton) e).setSkeletonType(Skeleton.SkeletonType.WITHER);
        e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        this.setEnt(e);
        this.setupBoss(e);

        return e;
    }

    @Override
    public void onBossSpawn() {
        super.onBossSpawn();
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }

    @Override
    public String getCustomName() {
        return "The Corrupt Cosmonaut";
    }

    @Override
    public String getName() {
        return this.getCustomName();
    }

    @Override
    public void cleanup() {
        this.removeMinions();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onTick() {
        ++this.ticks;

        this.getLocation().getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().getBlock().getType() == Material.LAVA || p.getLocation().getBlock().getType() == Material.STATIONARY_LAVA) {
                if (p.getGameMode() != GameMode.CREATIVE && p.getHealth() != 0.0) {
                    p.setHealth(0.0);
                    p.sendMessage(CC.translate("&c&l(!) &cYou fell into the scorching lava and were incinerated instantly."));
                }
            }
        });

        if (this.ticks % 2 == 0) {
            if (!floorBlocksToBreak.isEmpty()) {
                Location breakLocation = floorBlocksToBreak.get(0).getLocation();

                breakLocation.getWorld().getBlockAt(breakLocation.getBlockX(), 83, breakLocation.getBlockZ()).setType(Material.STATIONARY_LAVA);
                breakLocation.getWorld().getBlockAt(breakLocation.getBlockX(), 84, breakLocation.getBlockZ()).setType(Material.AIR);
                breakLocation.getWorld().getBlockAt(breakLocation.getBlockX(), 85, breakLocation.getBlockZ()).setType(Material.AIR);
                breakLocation.getWorld().getBlockAt(breakLocation.getBlockX(), 86, breakLocation.getBlockZ()).setType(Material.AIR);
                breakLocation.getWorld().getBlockAt(breakLocation.getBlockX(), 87, breakLocation.getBlockZ()).setType(Material.AIR);

                floorBlocksToBreak.remove(0);
            }
        }
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        if (!this.hasFloorBroke) {
            this.breakFloor();
        }

        super.onDeath(event);
    }

    @Override
    public void onUpdate() {
        ++this.loops;

        if (this.getEntity().getLocation().getY() < 113 && !this.hasFloorBroke) {
            this.getEntity().teleport(new Location(this.getEntity().getLocation().getWorld(), 782, 120, 729), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }

        if ((this.getEntity().getLocation().getBlock().getType() == Material.LAVA || this.getEntity().getLocation().getBlock().getType() == Material.STATIONARY_LAVA)) {
            this.sendMessage("Ahh yes, glorious fire - lend me strength!");
            this.getEntity().teleport(new Location(this.getEntity().getLocation().getWorld(), 782, 91, 729), PlayerTeleportEvent.TeleportCause.PLUGIN);
            this.onHeal(100 + new Random().nextInt(100));
        }

        if (this.getHealthPercent() <= 32.5 && !this.hasFloorBroke && !(this.getActiveAbility() instanceof ArmorMagnetAbility)) {
            this.breakFloor();

            for (int first = 0; first < 26; first++) {
                Location pos1 = new Location(this.getLocation().getWorld(), 747 + first, 83, 694 + first);
                Location pos2 = new Location(this.getLocation().getWorld(), 817 - first, 83, 764 - first);

                int size = Math.abs(pos1.getBlockX() - pos2.getBlockX());

                int x, z;

                for (int i = 0; i < size; ++i) {
                    x = pos1.getBlockX() + i;
                    z = pos1.getBlockZ();

                    floorBlocksToBreak.add(this.getLocation().getWorld().getBlockAt(x, 83, z));
                }

                for (int i = 0; i < size; ++i) {
                    x = pos1.getBlockX() + size;
                    z = pos1.getBlockZ() + i;

                    floorBlocksToBreak.add(this.getLocation().getWorld().getBlockAt(x, 83, z));
                }

                for (int i = 0; i < size; ++i) {
                    x = pos1.getBlockX() + (size - i);
                    z = pos1.getBlockZ() + size;

                    floorBlocksToBreak.add(this.getLocation().getWorld().getBlockAt(x, 83, z));
                }

                for (int i = 0; i < size; ++i) {
                    x = pos1.getBlockX();
                    z = pos1.getBlockZ() + (size - i);

                    floorBlocksToBreak.add(this.getLocation().getWorld().getBlockAt(x, 83, z));
                }
            }
        }

        if (this.loops % 12 == 0 && !(this.getActiveAbility() instanceof CloneWarAbility)) {
            if (System.currentTimeMillis() - this.lastHitTime >= 1500) {
                this.onHeal(40);
            }
        }

        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }

        if (this.loops % 10 == 0) {
            final Creature boss = (Creature) this.getEntity();

            if (boss.getTarget() != null && boss.getTarget().getWorld().getName().equals(boss.getLocation().getWorld().getName())) {
                boss.setTarget(null);
            }

            boss.getNearbyEntities(6.0, 6.0, 6.0).stream().filter(e -> e instanceof Player).map(e -> (Player) e).forEach(p -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 3, 0));
            });

            for (org.bukkit.entity.Entity p : boss.getNearbyEntities(300.0, 300.0, 300.0)) {
                if (p instanceof Player) {
                    if (p.getLocation().distance(boss.getLocation()) <= 15) {
                        ((Player) p).setWalkSpeed(0.12F);
                    } else {
                        ((Player) p).setWalkSpeed(0.2F);
                    }
                }
            }

            if (!boss.isInsideVehicle() && boss.getTarget() != null && boss.getTarget().isValid() && boss.getTarget() instanceof Player && boss.getTarget().getWorld().equals(this.getEntity().getWorld())) {
                this.lastTarget = (org.bukkit.entity.Entity) boss.getTarget();
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

    public void doTrickster() {
        this.getEntity().getNearbyEntities(14.0, 14.0, 14.0).stream().filter(e -> e instanceof Player).findFirst().ifPresent(t -> {
            Player target = (Player) t;

            final Location l1 = this.getLocation().add(0.0, 1.0, 0.0);
            final Location l2 = this.getEntity().getTargetBlock((HashSet<Byte>) null, 4).getLocation().add(0.0, 1.0, 0.0).setDirection(target.getLocation().getDirection());

            if (l2.getBlock().getType() == Material.AIR && l2.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR) {
                if (Math.random() < 0.5) {
                    this.getEntity().teleport(l2, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    this.getLocation().getWorld().playSound(l1, Sound.PORTAL_TRIGGER, 0.8f, 1.4f);
                    this.sendMessage("Oh? You think you can trick me? - I'll trick you right back, scum!");
                    target.setHealth(target.getHealth() - target.getHealth() * 0.8);
                }
            }
        });
    }

    @Override
    public void onPhaseChange(Phase p0) {

    }

    @Override
    public void onMiscUpdate(Event event) {

    }


    @Override
    public void onMinionUpdate(org.bukkit.entity.Entity minion, Event e) {
        if (minion instanceof Skeleton && e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;

            if (event.getEntity() instanceof Player || event.getDamager() instanceof Player) {

                event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 3.0);
                if (event.getEntity() instanceof Player)
                    BossUtils.dealPercentMaxHealthDamage((Player) event.getEntity(), 15.0, 10);

                if (this.getMinions().size() < 39 && this.duplicateMinions()) {
                    this.spawnMinion(EntityType.SKELETON, minion.getLocation());
                    this.spawnMinion(EntityType.SKELETON, minion.getLocation());
                    this.spawnMinion(EntityType.SKELETON, minion.getLocation());
                }
            }
        }
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.FIRE;
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
    public List<BossMessage> getGeneralMessages() {
        return new ArrayList<>(Collections.singletonList(
                new BossMessage("Do you know who I am, what I am? Of course not, insolent slave.", false, 1)
        ));
    }

    @Override
    public Entity getNMSEntity() {
        return this.bossEntity;
    }

    public DungeonAbility getActiveAbility() {
        return activeAbility;
    }

    public void clearAbilities() {
        if (this.activeAbility != null) {
            try {
                this.activeAbility.onAbilityDisable();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            this.activeAbility = null;
        }
    }

    public void breakFloor() {
        this.setFloorBroke(true);

        this.sendMessage("Hahaha, not bad for a group of filthy Cosmonauts! I wonder though, can you handle the HEAT?!");

        World bukkitWorld = Bukkit.getWorld("dungeon_planet_nul");
        com.sk89q.worldedit.world.World weWorld = new BukkitWorld(bukkitWorld);
        ;


        ProtectedRegion wgRegion = getWorldGuard().getGlobalRegionManager().get(bukkitWorld).getRegion("boss_blocks_remove");
        Polygonal2DRegion weRegion = new Polygonal2DRegion(weWorld, wgRegion.getPoints(), wgRegion.getMinimumPoint().getBlockY(), wgRegion.getMaximumPoint().getBlockY());

        for (BlockVector block : weRegion) {
            Block bukkitBlock = bukkitWorld.getBlockAt(block.getBlockX(), block.getBlockY(), block.getBlockZ());
            bukkitBlock.setType(Material.AIR);
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public boolean hasFloorBroke() {
        return hasFloorBroke;
    }

    public void setFloorBroke(boolean hasFloorBroke) {
        this.hasFloorBroke = hasFloorBroke;
    }

    public long getAbilityActivateTime() {
        return this.abilityActivateTime;
    }

    @Override
    public void onDamageTaken(EntityDamageByEntityEvent event, org.bukkit.entity.Entity damager) {
        if (event.getDamager() instanceof Player) {// && CosmicBosses.getInstance().isFactionUpgradesEnabled()) {
            /*final int heroicLevel = UpgradeUtils.getUpgradeLevel((Player)event.getDamager(), "HEROIC_BOSS_MASTERY");
            double mult = 1.0;
            if (heroicLevel > 0) {
                mult = ((heroicLevel == 1) ? 2.05 : ((heroicLevel == 2) ? 2.15 : ((heroicLevel == 3) ? 2.25 : 1.0)));
            }
            else {
                final int level = FactionUpgradeAPI.getPerkLevel((Player)event.getDamager(), FactionUpgrade.BOSS_MASTERY);
                mult = ((level == 1) ? 1.25 : ((level == 2) ? 1.5 : ((level == 3) ? 1.75 : ((level == 4) ? 2.0 : 1.0))));
            }

            if (DungeonsAPI.get().getPlayerDungeonData((Player)damager).getCurrentDungeon() != null && DungeonsAPI.get().getPlayerDungeonData((Player)damager).getCurrentDungeon().getBossSlayerTrinket()) {
                mult += 0.50;
            }*/

            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager() instanceof Arrow) {
                event.setCancelled(true);
                return;
            }

            event.setDamage(event.getFinalDamage() * 0.2);//Math.min(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * (0.03), 3 + new Random().nextInt(5)));

            if (this.hasFloorBroke()) {
                if (!event.getDamager().isOnGround() && !event.getDamager().hasMetadata("no_knockback"))
                    GeneralUtil.pushAwayEntity(this.getEntity(), event.getDamager(), 1.7000000476837158);
            }

            this.lastHitTime = System.currentTimeMillis();
        }

        super.onDamageTaken(event, damager);
    }

    @Override
    public void onAttackPlayer(EntityDamageByEntityEvent event, Player player) {
        if (event.isCancelled()) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager() instanceof Arrow) {
            event.setCancelled(true);
            return;
        }

        BossUtils.dealPercentMaxHealthDamage(player, 25.0, 25);
        event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 6.0);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 3, 0));

        if (this.getMinions().size() < 39 && this.duplicateMinions()) {
            this.spawnMinion(EntityType.SKELETON, this.getLocation());
            this.spawnMinion(EntityType.SKELETON, this.getLocation());
            this.spawnMinion(EntityType.SKELETON, this.getLocation());
        }

        if (this.currentCombo == null) {
            this.currentCombo = new Tuple<>(player.getUniqueId(), 0);
        } else if (this.currentCombo.getLeft().toString().equals(player.getUniqueId().toString())) {
            int comboNum = this.currentCombo.getRight() + 1;
            this.currentCombo = new Tuple<>(player.getUniqueId(), comboNum);

            this.sendMessage(player, CC.translate("&5&lCOMBO! " + comboNum + "x hits. &c&l+" + (comboNum * 2) + " DMG!"));
            player.setHealth(Math.max(player.getHealth() - comboNum * 2, 0));
        }

        super.onAttackPlayer(event, player);
    }

    public boolean duplicateMinions() {
        return this.duplicateMinions;
    }

    public void setDuplicateMinions(boolean duplicateMinions) {
        this.duplicateMinions = duplicateMinions;
    }

    @Override
    public org.bukkit.entity.Entity spawnMinion(EntityType type, Location l) {
        if (type != EntityType.SKELETON) return super.spawnMinion(type, l);

        Skeleton skeleton = (Skeleton) super.spawnMinion(type, l);
        skeleton.setMaxHealth(125);
        skeleton.setHealth(skeleton.getMaxHealth());
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));

        skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);

        ItemStack fillerSword = new ItemStack(Material.DIAMOND_SWORD);
        fillerSword.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        fillerSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        fillerSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        skeleton.getEquipment().setItemInHandDropChance(0.0f);
        fillerSword = SkinType.valueOf("CORRUPT_SWORD").apply(fillerSword);
        skeleton.getEquipment().setItemInHand(fillerSword);
        skeleton.setRemoveWhenFarAway(false);
        skeleton.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "The Corrupt Cosmonaut");
        skeleton.setCustomNameVisible(true);
        skeleton.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        skeleton.setMetadata("no_drops", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        skeleton.setMetadata("cosmonaut_minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        skeleton.getEquipment().setHelmet(SkinType.valueOf("CORRUPT_HELMET").apply(new ItemStack(Material.DIAMOND_HELMET)));
        skeleton.getEquipment().setChestplate(null);
        skeleton.getEquipment().setLeggings(null);
        skeleton.getEquipment().setBoots(null);

        return skeleton;
    }

    public enum BossPhase {
        PHASE_1(2000, 6, 0.25f, 15.0f, Lists.newArrayList(SoulReaperAbility.class, CloneWarAbility.class,ArmorMagnetAbility.class, BloodMagicAbility.class, RobotSummonerAbility.class, ComboBreakerAbility.class));
        //PHASE_1(2000, 6, 0.25f, 15.0f, Lists.newArrayList(SilencerAbility.class, RobotSummonerAbility.class));

        private int maxHealth;
        private int projectileDamage;
        private float damageReduction;
        private float fireRateMultiplier;
        private List<Class<? extends DungeonAbility>> abilityClasses;

        public List<Class<? extends DungeonAbility>> getAbilityClasses() {
            return (List<Class<? extends DungeonAbility>>) Lists.newArrayList((Iterable) this.abilityClasses);
        }

        BossPhase(final int maxHealth, final int projectileDamage, final float damageReduction, final float fireRateMultiplier, final List<Class<? extends DungeonAbility>> abilityClasses) {
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