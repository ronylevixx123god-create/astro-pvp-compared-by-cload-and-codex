package me.ifamasssxd.cosmicbosses.bosses.dragon;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.itembuilder.ArmorSetItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.HeroicUpgradeBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.MaskBuilder;
import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.struct.EquipmentType;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.dragon.phases.PhaseList;
import me.ifamasssxd.cosmicbosses.struct.*;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.StringUtils;
import me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits;
import me.ifamasssxd.cosmicevolutionkits.structure.ekits.Lucky;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.SpigotWorldConfig;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EndDragon extends Boss implements GlobalBoss {
    private EndDragonEntity dragon;
    private int endSpawnActivation;
    private int endViewDistance;
    private DragonTNTBomb bomb;
    private DragonFireball fireball;
    private DragonPhase dragonPhase;
    private long lastPhaseChange;

    public EndDragon(final Location location, final BossType bossType) {
        super(location, bossType);
        this.bomb = new DragonTNTBomb(this);
        this.fireball = new DragonFireball(this);
        this.dragonPhase = DragonPhase.FIREBALL;
        this.modifier = new BlockModifier();
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public String getCustomName() {
        return CC.DarkRedB + "Timeless Dragon";
    }

    @Override
    public String getName() {
        return "Timeless Dragon";
    }

    @Override
    public void cleanup() {
        if (this.endSpawnActivation != 0) {
            ((CraftWorld) this.location.getWorld()).getHandle().spigotConfig.monsterActivationRange = this.endSpawnActivation;
        }
        if (this.endViewDistance != 0) {
            ((CraftWorld) this.location.getWorld()).getHandle().spigotConfig.viewDistance = this.endViewDistance;
        }
        this.removeMinions();
        this.clearScoreboard();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public LivingEntity spawnBoss() {
        final WorldServer server = ((CraftWorld) this.location.getWorld()).getHandle();
        (this.dragon = new EndDragonEntity((World) server, this)).setPosition(this.location.getX(), this.location.getY(), this.location.getZ());
        this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        (this.dragon.homeLocation = this.location.clone()).setY((double) (BossUtils.getHighestBlockYAt(this.dragon.homeLocation, true) + 3));
        server.addEntity((Entity) this.dragon, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final LivingEntity e = (LivingEntity) this.dragon.getBukkitEntity();
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
        EndDragon.bosses.put(e.getUniqueId(), this);
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(msg.getMessage());
        }
        this.setPhase(this.getNewPhase());
        this.endSpawnActivation = server.spigotConfig.monsterActivationRange;
        this.endViewDistance = server.spigotConfig.viewDistance;
        final SpigotWorldConfig spigotConfig = server.spigotConfig;
        ++spigotConfig.viewDistance;
        final SpigotWorldConfig spigotConfig2 = server.spigotConfig;
        spigotConfig2.monsterActivationRange *= 3;
        return e;
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        super.onUpdate();
        final long timeSinceChange = System.currentTimeMillis() - this.lastPhaseChange;
        if (timeSinceChange >= 30000L) {
            this.onPhaseChange(this.getNewPhase());
        }
        final EndDragonEntity entity = this.getNMSEntity();
        if (entity == null || !entity.isAlive()) {
            Bukkit.getLogger().info("EnderDragon Entity is not alive!");
            return;
        }
        if (entity.world.getChunkIfLoaded((int) Math.floor(entity.locX), (int) Math.floor(entity.locZ)) == null) {
            return;
        }
        if (this.bomb.canUse()) {
            this.bomb.onAbility();
            this.bomb.activateCooldown();
        }
        if (this.fireball.canUse()) {
            this.fireball.onAbility();
        }
        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final org.bukkit.entity.Entity damager) {
        if (!(damager instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        super.onDamageTaken(event, damager);
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
            return;
        }
        event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 3.0);
        super.onDamageTaken(event);
    }

    public EndDragonEntity getNMSEntity() {
        return this.dragon;
    }

    @Override
    public List<org.bukkit.entity.Entity> getMinions() {
        final List<org.bukkit.entity.Entity> minions = super.getMinions();
        minions.removeIf(ent -> !ent.isValid());
        return minions;
    }

    public List<org.bukkit.entity.Entity> getMinions(final EntityType type) {
        final List<org.bukkit.entity.Entity> found = Lists.newArrayList();
        final List<org.bukkit.entity.Entity> minions = super.getMinions();
        minions.removeIf(ent -> !ent.isValid());
        for (final org.bukkit.entity.Entity ent2 : minions) {
            if (ent2.getType() == type) {
                found.add(ent2);
            }
        }
        return found;
    }

    protected Phase getNewPhase() {
        final List<DragonPhase> available = Lists.newArrayList(DragonPhase.values());
        available.remove(this.dragonPhase);
        return Phase.getPhase(available.get(this.random.nextInt(available.size())).getPhase());
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null && Math.random() <= 0.1) {
            final ItemStack mask = new MaskBuilder("DRAGON_MASK").build();
            if (mask != null) {
                Bukkit.getLogger().info("[EndDragon] Dropping dragon mask!");
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), mask);
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ITEM_PICKUP, 10.0f, 1.1f);
            }
        }
        if (event != null && MinecraftServerUtils.isRunning() && CosmicUtils.get().isEvolutionKitEnabled()) {
            final List<ItemStack> items = this.addLuckyBoxItems(Lists.newArrayList());
            if (items != null && items.size() > 1) {
                items.remove(0);
                final int toGiveAmount = 10;
                while (items.size() < toGiveAmount) {
                    this.addLuckyBoxItems(items);
                }
                final Location loc = event.getEntity().getLocation();
                final List<ItemStack> toGive = items.subList(0, Math.min(items.size(), toGiveAmount));
                if (this.random.nextDouble() <= 0.33) {
                    final ItemStack armorsetItem = new ArmorSetItemBuilder("DRAGON_SLAYER", EquipmentType.getRandomArmor(), true).build();
                    if (armorsetItem != null) {
                        toGive.add(armorsetItem);
                    }
                }
                if (Math.random() <= 0.25) {
                    final ItemStack itemstack = new HeroicUpgradeBuilder("DRAGON_SLAYER", 50.0, 100.0).build();
                    if (itemstack != null) {
                        toGive.add(itemstack);
                    }
                }
                final List<JSONMessage> msgs = Lists.newArrayList();
                for (final ItemStack item : toGive) {
                    final JSONMessage msg2 = new JSONMessage("");
                    msg2.addItem(item, StringUtils.getCenteredMessage(CC.RedB + "* " + CC.White + ItemUtils.getDisplayName(item)), ChatColor.WHITE);
                    msgs.add(msg2);
                }
                final String header = StringUtils.getCenteredMessage(CC.RedB + "Timeless Dragon Bonus Rewards");
                for (final Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    pl.sendMessage(header);
                    msgs.forEach(msg -> msg.sendToPlayer(pl));
                }
                new BukkitRunnable() {
                    public void run() {
                        if (toGive.isEmpty()) {
                            this.cancel();
                            return;
                        }
                        final ItemStack item = toGive.remove(0);
                        final Location toSpawn = loc.clone();
                        toSpawn.add(EndDragon.this.random.nextGaussian(), 0.0, EndDragon.this.random.nextGaussian());
                        event.getEntity().getWorld().dropItemNaturally(toSpawn, item);
                        event.getEntity().getWorld().playSound(toSpawn, Sound.CHICKEN_EGG_POP, 10.0f, 1.4f);
                    }
                }.runTaskTimer((Plugin) CosmicBosses.get(), 20L, 5L);
            }
        }
    }

    private List<ItemStack> addLuckyBoxItems(final List<ItemStack> items) {
        final List<ItemStack> found = (List<ItemStack>) ((Lucky) EvolutionKits.LUCKY.getKit()).getLuckyBoxItems(6 + this.random.nextInt(5), 5000.0);
        found.remove(0);
        items.addAll(found);
        return items;
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
        final DragonPhase newDragonPhase = DragonPhase.getDragonPhase(newPhase.getPhase());
        if (newDragonPhase == null) {
            return;
        }
        this.lastPhaseChange = System.currentTimeMillis();
        this.dragonPhase = newDragonPhase;
    }

    @Override
    public void onMinionUpdate(final org.bukkit.entity.Entity minion, final Event e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (!event.getDamager().equals(minion)) {
                return;
            }
            if (!(event.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
            final LivingEntity entity = (LivingEntity) event.getEntity();
            if (minion instanceof TNTPrimed) {
                double percent = 0.0;
                if (minion.hasMetadata("mouth_bomb")) {
                    percent = 15.0;
                } else if (minion.hasMetadata("dragon_bomb")) {
                    percent = 30.0;
                }
                final Player player = (Player) entity;
                event.setDamage(0.0);
                if (percent > 0.0 && BossUtils.dealPercentMaxHealthDamage(player, percent, 20, true, true)) {
                    if (entity.isDead()) {
                        event.setCancelled(true);
                        event.setDamage(0.0);
                        Bukkit.getLogger().info("[EndDragon] Cancelling TNT damage for " + ((Player) event.getEntity()).getName() + " because they are dead!");
                        return;
                    }
                    entity.playEffect(EntityEffect.HURT);
                    //ParticleEffect.sendToLocation(ParticleEffect.LARGE_SMOKE, entity.getLocation(), 1.0f, 1.0f, 1.0f, 0.01f, 15);
                    try {
                        de.inventivegames.particle.ParticleEffect.LARGE_SMOKE.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.01f, 15);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    Bukkit.getLogger().info("[EnderDragon] Damaging " + entity.getHealth() + " by " + percent + " new health: " + entity.getHealth());
                }
            } else if (minion instanceof Blaze || minion instanceof Skeleton) {
                event.setDamage(0.01);
                if (!event.getEntity().isDead() && BossUtils.dealPercentMaxHealthDamage((Player) event.getEntity(), 10.0, 10, true, true)) {
                    if (!event.getEntity().isDead()) {
                        event.getEntity().playEffect(EntityEffect.HURT);
                    } else {
                        event.setCancelled(true);
                        event.setDamage(0.0);
                        Bukkit.getLogger().info("Cancelling damage for " + ((Player) event.getEntity()).getName());
                    }
                }
            }
        }
        super.onMinionUpdate(minion, e);
    }

    @Override
    public void onMiscUpdate(final Event event) {
        if (event instanceof ProjectileHitEvent) {
            final ProjectileHitEvent hitEvent = (ProjectileHitEvent) event;
            final org.bukkit.entity.Entity projectile = (org.bukkit.entity.Entity) hitEvent.getEntity();
            final Location loc = projectile.getLocation();
            for (int toSpawn = 1 + ThreadLocalRandom.current().nextInt(3), i = 0; i < toSpawn; ++i) {
                this.spawnFireballMinion(loc.clone().add(this.random.nextGaussian() / 2.0, this.random.nextDouble() / 2.0, this.random.nextGaussian() / 2.0), true);
            }
            loc.getWorld().playSound(loc, Sound.EXPLODE, 4.0f, 1.1f);
            //ParticleEffect.sendToLocation(ParticleEffect.HUGE_EXPLOSION, loc.add(0.0, 1.0, 0.0), 2.0f, 2.0f, 2.0f, 0.01f, 10);
            try {
                de.inventivegames.particle.ParticleEffect.HUGE_EXPLOSION.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.01f, 15);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            hitEvent.getEntity().getNearbyEntities(10.0, 10.0, 10.0).stream().filter(en -> en instanceof Player).map(e -> e).forEach(pl -> {
                if (!pl.isDead() && BossUtils.dealPercentMaxHealthDamage((Player) pl, 10.0, 10, true, true)) {
                    BossUtils.pushAwayPlayer((Player) pl, loc, 1.6);
                    pl.playEffect(EntityEffect.HURT);
                }
                return;
            });
            projectile.remove();
        }
    }

    private org.bukkit.entity.Entity spawnFireballMinion(final Location location, final boolean despawnOlds) {
        final EntityType toSpawn = this.random.nextBoolean() ? EntityType.SKELETON : EntityType.BLAZE;
        final List<org.bukkit.entity.Entity> minions = this.getMinions(toSpawn);
        int minionSize = minions.size();
        if (despawnOlds && minions.size() >= 15) {
            for (final org.bukkit.entity.Entity ent : minions) {
                if (ent.getTicksLived() >= 1200) {
                    ent.remove();
                    this.getMinions().remove(ent);
                    --minionSize;
                }
            }
        }
        if (minionSize >= 15) {
            return null;
        }
        final LivingEntity ent2 = (LivingEntity) this.getEntity().getWorld().spawnEntity(location, toSpawn);
        if (ent2 instanceof Skeleton) {
            ((Skeleton) ent2).setSkeletonType(Skeleton.SkeletonType.WITHER);
        }
        ent2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        ent2.setMaxHealth(ent2.getMaxHealth() * 5.0);
        ent2.setHealth(ent2.getMaxHealth());
        ent2.setCustomName(CC.LightPurpleB + "A Wandering Soul");
        ent2.setCustomNameVisible(true);
        ent2.setMetadata("no_drops", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.get(), (Object) this.getUuid()));
        ent2.setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.get(), (Object) this.getUuid()));
        ent2.getEquipment().setItemInHandDropChance(0.0f);
        ent2.getEquipment().setHelmetDropChance(0.0f);
        ent2.getEquipment().setChestplateDropChance(0.0f);
        ent2.getEquipment().setLeggingsDropChance(0.0f);
        ent2.getEquipment().setBootsDropChance(0.0f);
        ent2.getEquipment().setItemInHand(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.DAMAGE_ALL, 5).build());
        this.getMinions().add((org.bukkit.entity.Entity) ent2);
        return (org.bukkit.entity.Entity) ent2;
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (event.getDamager() instanceof LargeFireball) {
            this.spawnFireballMinion(event.getDamager().getLocation().add(0.0, 1.0, 0.0), false);
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.FIRE;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.END;
    }

    @Override
    public int getMaxHealth() {
        return 2000;
    }

    @Override
    public void sendMessage(final String msg) {
        super.sendMessage(msg);
    }

    @Override
    public int getMessageRadius() {
        return 48;
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("You dare to enter my realm and disturb my slumber?", false, -1), new BossMessage("I will burn your world to ash!", false, 0), new BossMessage("Greater warriors than you have fallen before me.", false, 0)});
    }

    enum PathStyle {
        NORMAL,
        DIVE_BOMB_ABOVE,
        DIVE_BOMB_BELOW;
    }
}
