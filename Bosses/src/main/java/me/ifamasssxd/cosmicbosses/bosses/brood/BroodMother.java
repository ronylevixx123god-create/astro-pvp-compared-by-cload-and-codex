package me.ifamasssxd.cosmicbosses.bosses.brood;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.entities.SpiderBoss;
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
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSpider;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;

public class BroodMother extends Boss {
    WebAttack attack;
    SpiderSpawn spawn;
    int walkFails;

    public BroodMother(final Location location, final BossType bossType) {
        super(location, bossType);
        this.spawn = new SpiderSpawn(this);
        this.walkFails = 0;
        this.modifier = new BlockModifier();
        this.attack = new WebAttack(this, this.modifier);
        ItemStack OPKey = CrateKeyItem.getItem(CrateType.valueOf("SEASON"));
        this.getLootPool().add(OPKey);

    }

    @Override
    public String getTitle() {
        return "Brood";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SPIDER;
    }

    @Override
    public void onBossSpawn() {
        super.onBossSpawn();
        final BossMessage start = this.getStartingMessage();
        if (start != null) {
            this.sendMessage(ChatColor.ITALIC + start.getMessage());
        }
    }

    @Override
    public LivingEntity spawnBoss() {
        final CraftWorld world = (CraftWorld) this.location.getWorld();
        final SpiderBoss boss = new SpiderBoss((World) world.getHandle());
        boss.setPosition(this.location.getX(), this.location.getY(), this.location.getZ());
        world.getHandle().addEntity((Entity) boss, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final LivingEntity e = (LivingEntity) boss.getBukkitEntity();
        this.uuid = e.getUniqueId();
        e.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMetadata("boss", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        e.setMaxHealth((double) this.getMaxHealth());
        e.setHealth(e.getMaxHealth());
        this.setEnt(e);
        e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 50));
        e.setCustomName(this.getDifficulty().getColor() + ChatColor.BOLD.toString() + this.getCustomName());
        e.setCustomNameVisible(true);
        this.entity = e;
        BroodMother.bosses.put(e.getUniqueId(), this);
        return e;
    }

    @Override
    public String getCustomName() {
        return "Brood Mother";
    }

    @Override
    public String getName() {
        return "Brood Mother";
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.attack.canUse() && this.random.nextBoolean() && this.loops >= 20) {
            this.attack.onAbility();
        } else if (this.spawn.canUse() && this.random.nextInt(10) <= 7) {
            this.spawn.onAbility();
        }
        if (this.getAntiFarm().checkStuck()) {
            this.getAntiFarm().unstickBoss();
        }
        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }
        final Iterator<org.bukkit.entity.Entity> ent = this.getMinions().iterator();
        while (ent.hasNext()) {
            final org.bukkit.entity.Entity e = ent.next();
            if (e == null || !e.isValid()) {
                ent.remove();
            } else if (e.getTicksLived() >= 140 && this.getMinions().size() > 7 && this.random.nextInt(100) < 25) {
                final org.bukkit.entity.Entity entity;
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> {
                    if (e != null && e.isValid()) {
                        e.remove();
                    }
                    return;
                }, 1L);
            } else {
                if (e.getLocation().distanceSquared(this.getEntity().getLocation()) >= 750.0) {
                    e.teleport(this.getEntity().getLocation().clone());
                }
                if (this.loops % 10 != 0) {
                    continue;
                }
                this.spiderJump(e, 5.0f);
            }
        }
        super.onUpdate();
    }

    @Override
    public void cleanup() {
        this.getModifier().removeAllBlocks(true);
        this.removeMinions();
        this.clearScoreboard();
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (event.isCancelled()) {
            return;
        }
        final boolean canAffect = BossUtils.canAffectPlayer(player, false);
        if (this.random.nextInt(10) <= 2) {
            this.getEntity().getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.9f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 5), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1), true);
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "* BITTEN *");
            this.sendMessage(player, "You have been poisoned!");
            if (canAffect) {
                BossUtils.dealPercentMaxHealthDamage(player, 10.0, 10);
            }
            float speed = player.getWalkSpeed() - 0.05f;
            if (speed <= 0.0f) {
                speed = 0.02f;
            }
            player.setWalkSpeed(speed);
            new BukkitRunnable() {
                public void run() {
                    float speed = player.getWalkSpeed() + 0.02f;
                    if (speed > 0.2) {
                        speed = 0.2f;
                    }
                    player.setWalkSpeed(speed);
                }
            }.runTaskLater((Plugin) CosmicBosses.getInstance(), 60L);
        }
        if (canAffect) {
            BossUtils.dealPercentHealthDamage(player, 5.0, 4, 5);
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onMinionUpdate(final org.bukkit.entity.Entity minion, final Event e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getEntity().hasMetadata("minion")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> minion.setVelocity(new Vector()));
            }
            if (event.getEntity() instanceof Player) {
                final Player pl = (Player) event.getEntity();
                if (!event.isCancelled() && event.getFinalDamage() < pl.getHealth()) {
                    pl.playSound(pl.getLocation(), Sound.ZOMBIE_METAL, 1.0f, 1.4f);
                    BossUtils.dealPercentMaxHealthDamage(pl, 5.0, 8);
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 4));
                }
            } else if (event.getEntity().hasMetadata("minion") && event.getDamager() instanceof Projectile) {
                event.setCancelled(true);
                minion.getWorld().playSound(minion.getLocation(), Sound.ITEM_BREAK, 4.0f, 1.4f);
            }
        } else if (e instanceof EntityDamageEvent) {
            final EntityDamageEvent event2 = (EntityDamageEvent) e;
            if (event2.getCause() == EntityDamageEvent.DamageCause.FIRE || event2.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event2.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                event2.setCancelled(true);
            }
        }
    }

    private void spiderJump(final org.bukkit.entity.Entity ent, final float strength) {
        final Spider spider = (Spider) ent;
        final org.bukkit.entity.Entity target = (org.bukkit.entity.Entity) spider.getTarget();
        final Location spiderLoc = spider.getLocation();
        if (target != null && strength > 2.0f && strength < 6.0f && this.random.nextInt(10) == 0 && spider.isOnGround()) {
            final Location targetLoc = target.getLocation();
            final double distX = targetLoc.getX() - spiderLoc.getX();
            final double distZ = targetLoc.getZ() - spiderLoc.getZ();
            final float distanceSquared = MathHelper.sqrt(distX * distX + distZ * distZ);
            final Vector velocity = spider.getVelocity();
            final double motX = distX / distanceSquared * 0.5 * 0.800000011920929 + velocity.getX() * 0.20000000298023224;
            final double motZ = distZ / distanceSquared * 0.5 * 0.800000011920929 + velocity.getZ() * 0.20000000298023224;
            final double motY = 0.4000000059604645;
            velocity.setX(motX);
            velocity.setZ(motZ);
            velocity.setY(motY);
            spider.setVelocity(velocity);
            System.out.println("Leaping");
        }
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final org.bukkit.entity.Entity damager) {
        if (!event.isCancelled()) {
            final Creature ent = (Creature) this.getEntity();
            event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 2.0);
            if (damager instanceof Player) {
                final Player pl = (Player) damager;
                final double distance = (ent.getTarget() != null) ? ent.getTarget().getLocation().distanceSquared(ent.getLocation()) : -1.0;
                if (ent.getTarget() == null || distance > 100.0) {
                    ent.setTarget((LivingEntity) pl);
                    final EntityInsentient spider = (EntityInsentient) ((CraftSpider) this.getEntity()).getHandle();
                    final boolean walked = spider.getNavigation().a(pl.getLocation().getX(), pl.getLocation().getY(), pl.getLocation().getZ(), 1.4);
                    if (walked) {
                        this.walkFails = 0;
                    } else {
                        ++this.walkFails;
                    }
                    if (this.walkFails >= 5) {
                        final Location loc = BossUtils.getRandomLocationNearby(pl.getLocation(), 5, 3);
                        ent.teleport(loc);
                        this.sendMessage(ChatColor.RED + "Get over here!");
                    }
                }
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
        super.onDamageTaken(event);
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null) {
            this.sendMessage(ChatColor.ITALIC + "HOW IS THIS POSSIBLE? The Brood Mother, ME, defeated by infidels such as yourselves?!", Sound.SPIDER_DEATH, 0.7f);
            if (event.getEntity() != null) {
                for (final org.bukkit.entity.Entity near : event.getEntity().getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (near instanceof Player) {
                        final Player player = (Player) near;
                        if (!player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                            continue;
                        }
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                }
            }
        }
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.POISON;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.ULTIMATE;
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
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("Have you ever wondered why you're afraid of spiders?", false, -1), new BossMessage("One bite, and you'll be mine.", false, 0), new BossMessage("My babies.. they hunger for Cosmonauts!", true, 0)});
    }
}
