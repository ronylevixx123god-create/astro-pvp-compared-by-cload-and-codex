package me.ifamasssxd.cosmicbosses.bosses.kingslime;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.BlockModifier;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSlime;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.List;

public class KingSlime extends Boss {
    WaterTrap waterTrap;
    boolean inAir;
    double health;

    public KingSlime(final Location location, final BossType bossType) {
        super(location, bossType);
        this.inAir = false;
        this.health = 0.0;
        this.modifier = new BlockModifier();
        this.waterTrap = new WaterTrap(this, this.modifier);
        ItemStack OPKey = CrateKeyItem.getItem(CrateType.valueOf("SEASON"));
        OPKey.setAmount(1);
        this.getLootPool().add(OPKey);

    }

    @Override
    public String getTitle() {
        return "Slimey";
    }

    @Override
    public EntityType getEntityType() {
        return (this.getPhase() == Phase.TWO) ? EntityType.MAGMA_CUBE : EntityType.SLIME;
    }

    @Override
    public void onBossSpawn() {
        final LivingEntity ent = this.spawnBoss();
        this.getSlime().setSize(15);
        ent.setMaxHealth((double) this.getMaxHealth());
        ent.setHealth((double) this.getMaxHealth());
        this.alert();
        final BossMessage msg = this.getStartingMessage();
        if (msg != null) {
            this.sendMessage(ChatColor.ITALIC.toString() + msg.getMessage());
        }
    }

    @Override
    public int getMaxHealth() {
        return 1500;
    }

    @Override
    public String getCustomName() {
        return "King Slime";
    }

    @Override
    public String getName() {
        return "King Slime";
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.ULTIMATE_PLUS;
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        if (this.loops % 80 == 0) {
            this.onPhaseChange((this.getPhase() == Phase.ONE) ? Phase.TWO : ((this.getPhase() == Phase.TWO) ? Phase.ONE : Phase.ONE));
        }
        if (this.loops % 4 == 0 && this.getAntiFarm().checkStuck()) {
            this.getAntiFarm().unstickBoss();
        }
/*        if (this.waterTrap.canUse() && this.random.nextBoolean() && this.loops >= 20) {
            this.waterTrap.onAbility();
            this.sendMessage(ChatColor.ITALIC + "Breath it in!");
        }*/
        final Location entityLocation = this.getEntity().getLocation();
        /*if (this.getPhase() == Phase.ONE && this.loops % 4 == 0 && this.getMinions().size() < 6 && this.random.nextInt(3) <= 1) {
            for (final Entity ent : this.getNearbyEntities(20.0, EntityType.PLAYER)) {
                if (this.getMinions().size() >= 8) {
                    break;
                }
                final Player p = (Player) ent;
                if (!BossUtils.canAffectPlayer(p, false)) {
                    continue;
                }
                final Location loc = BossUtils.getRandomLocationNearby(p.getLocation(), 10, 3, 0);
                final Slime minion = (Slime) this.spawnMinion(EntityType.SLIME, loc);
                this.applyNMS(minion);
                minion.setCustomName(this.getColor(true) + this.getName() + "'s Minion");
                minion.setCustomNameVisible(true);
                minion.setMetadata("no_slime_split", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
                minion.setSize(2);
                minion.setMaxHealth(100.0);
                minion.setHealth(minion.getMaxHealth());
                Bukkit.getScheduler().runTaskLater(CosmicBosses.getInstance(), minion::remove, 20*300);
            }
        }
        this.updateMinionPath();*/
        this.updateSize();
        if (this.loops % 16 == 0) {
            this.updateScoreboard();
        }
        if (this.getEntity().isOnGround() && this.inAir) {
            this.inAir = false;
            final Location loc2 = entityLocation.clone();
            final Block b = loc2.getBlock();
            Location under;
            if (this.getModifier().containsLocation(this.waterTrap, b.getLocation())) {
                under = b.getLocation();
            } else {
                under = b.getRelative(BlockFace.DOWN).getLocation();
            }
            final Location resuable = under.clone();
            if (this.getModifier().containsLocation(this.waterTrap, under)) {
                for (int x = under.getBlockX() - 2; x < under.getBlockX() + 2; ++x) {
                    for (int z = under.getBlockZ() - 2; z < under.getBlockZ() + 2; ++z) {
                        resuable.setX((double) x);
                        resuable.setZ((double) z);
                        if (this.getModifier().containsLocation(this.waterTrap, resuable)) {
                            this.getModifier().removeBlock(this.waterTrap, resuable);
                            b.getWorld().playSound(b.getLocation(), Sound.ZOMBIE_WOODBREAK, 5.0f, 0.7f);
                        }
                    }
                }
            }
        } else {
            this.inAir = true;
        }
        super.onUpdate();
    }

    private void updateMinionPath() {
        final Iterator<Entity> iter = this.getMinions().iterator();
        while (iter.hasNext()) {
            final LivingEntity e = (LivingEntity) iter.next();
            if (e == null || !e.isValid()) {
                iter.remove();
            } else {
                if (!(e instanceof Slime)) {
                    continue;
                }
                if (this.getPhase() == Phase.TWO) {
                    if (this.random.nextInt(10) != 0) {
                        continue;
                    }
                    try {
                        de.inventivegames.particle.ParticleEffect.LARGE_SMOKE.sendToPlayers(Bukkit.getOnlinePlayers(), e.getLocation(), 1f, 1f, 1f, 0.01f, 5);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } else {
                    final Location loc = this.getEntity().getLocation();
                    final double distance = e.getLocation().distanceSquared(loc);
                    if (distance <= 45.0 && e.getTicksLived() >= 60) {
                        //final int heal = (int) Math.min(50.0, e.getHealth() * 2.0);
                        //this.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "+ " + heal + " HP (SOURCE: Minion)");
                        //this.onHeal(heal);
                        this.getEntity().getWorld().playSound(loc, Sound.SLIME_ATTACK, 15.0f, 0.8f);
                        if (this.random.nextInt(10) == 0) {
                            //ParticleEffect.sendToLocation(ParticleEffect.SLIME, e.getLocation(), 0.5f, 0.5f, 0.5f, 0.01f, 20);
                            try {
                                de.inventivegames.particle.ParticleEffect.SLIME.sendToPlayers(Bukkit.getOnlinePlayers(), e.getLocation(), 1f, 1f, 1f, 0.01f, 20);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        e.remove();
                        iter.remove();
                    } else {
                        if (distance > 300.0) {
                            e.teleport(BossUtils.getRandomLocationNearby(loc.clone(), 10, 5, -2));
                        }
                        final EntityInsentient insent = (EntityInsentient) ((CraftSlime) e).getHandle();
                        insent.getControllerLook().a(((CraftEntity) this.getEntity()).getHandle(), 30.0f, 30.0f);
                        insent.getControllerJump().a();
                        insent.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.5);
                    }
                }
            }
        }
    }

    private void updateSize() {
        final int maxSize = 15;
        final int currentSize = this.getSlime().getSize();
        final double health = this.getEntity().getHealth();
        this.health = health;
        final double percentageHealth = health / this.getEntity().getMaxHealth();
        final int size = (int) Math.max(5.0, Math.ceil(maxSize * percentageHealth));
        if (size != currentSize) {
            final Slime slime = this.getSlime();
            slime.setSize(size);
            slime.setMaxHealth((double) this.getMaxHealth());
            slime.setHealth(health);
            slime.getWorld().playSound(slime.getLocation(), Sound.SLIME_ATTACK, 15.0f, 0.7f);
        }
    }

    @Override
    public void onMinionUpdate(final Entity minion, final Event e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager().equals(minion) && event.getEntity() instanceof Player) {
                event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * 1.2);
            }
        } else if (e instanceof EntityTargetEvent || e instanceof EntityTargetLivingEntityEvent) {
            if (this.getPhase() == Phase.ONE) {
                final EntityTargetEvent event2 = (EntityTargetEvent) e;
                event2.setTarget((Entity) null);
            }
        } else if (e instanceof EntityDeathEvent) {
            try {
                de.inventivegames.particle.ParticleEffect.EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), minion.getLocation(), 1f, 1f, 1f, 0.01f, 20);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            minion.getWorld().playSound(minion.getLocation(), Sound.EXPLODE, 4.0f, 1.0f);
            for (final Entity ent : minion.getNearbyEntities(5.0, 5.0, 5.0)) {
                if (ent instanceof Player) {
                    final Player pl = (Player) ent;
                    BossUtils.dealPercentMaxHealthDamage(pl, 2, 2);
                }
            }
        } else if (e instanceof EntityDamageEvent) {
            final EntityDamageEvent event3 = (EntityDamageEvent) e;
            if (event3.getCause() == EntityDamageEvent.DamageCause.FIRE || event3.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event3.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event3.setCancelled(true);
            }
        }
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        final EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            event.setCancelled(true);
            if (this.getAntiFarm().checkStuck(this)) {
                this.getAntiFarm().unstickBoss();
            }
        }
        super.onDamageTaken(event);
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (!event.isCancelled()) {
            if (!BossUtils.canAffectPlayer(player, false)) {
                return;
            }
            if (this.getPhase() == Phase.TWO) {
                BossUtils.dealPercentMaxHealthDamage(player, 7.0, 7);
                player.setFireTicks(20);
            } else {
                BossUtils.dealPercentHealthDamage(player, 3.0, 2, 2);
            }
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final Entity damager) {
        super.onDamageTaken(event, damager);
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
        this.setPhase(newPhase);
        Slime normal = null;
        if (newPhase == Phase.ONE || newPhase == Phase.TWO) {
            this.location = this.getEntity().getLocation();
            final Slime currentSlime = this.getSlime();
            Boss.bosses.remove(currentSlime.getUniqueId());
            normal = (Slime) this.spawnBoss();
            normal.setSize(currentSlime.getSize());
            normal.setMaxHealth((double) this.getMaxHealth());
            normal.setHealth(currentSlime.getHealth());
            currentSlime.remove();
            String msg = ChatColor.GREEN + ChatColor.BOLD.toString() + this.getName() + " has entered Phase " + newPhase.getPhase();
            if (newPhase == Phase.TWO) {
                msg = "Behold the power of the Slime King! Explosive slimes, attack these cosmonaut scum!";
            } else {
                msg = "Come to me my children, heal me so we can crush these Cosmonauts!";
            }
            for (final Entity minion : (List<Entity>) Lists.newArrayList((Iterable) this.getMinions())) {
                if (newPhase == Phase.TWO && minion instanceof Slime) {
                    final MagmaCube cube = (MagmaCube) this.spawnMinion(EntityType.MAGMA_CUBE, minion.getLocation());
                    final Slime slime = (Slime) minion;
                    cube.setSize(slime.getSize());
                    cube.setMaxHealth(slime.getMaxHealth());
                    cube.setHealth(slime.getHealth());
                    minion.remove();
                    this.getMinions().remove(minion);
                } else {
                    if (newPhase != Phase.ONE || !(minion instanceof MagmaCube)) {
                        continue;
                    }
                    final Slime cube2 = (Slime) this.spawnMinion(EntityType.SLIME, minion.getLocation());
                    final MagmaCube slime2 = (MagmaCube) minion;
                    cube2.setSize(slime2.getSize());
                    cube2.setMaxHealth(slime2.getMaxHealth());
                    cube2.setHealth(slime2.getHealth());
                    this.applyNMS(cube2);
                    minion.remove();
                    this.getMinions().remove(minion);
                }
            }
            this.sendMessage(ChatColor.ITALIC + msg, Sound.ENDERDRAGON_GROWL, 1.4f);
        }
    }

    private void applyNMS(final Slime slime) {
        final EntitySlime ent = ((CraftSlime) slime).getHandle();
        final Object goalSelector = ReflectionUtils.getObject(EntityInsentient.class, ent, "goalSelector");
        final List<?> c = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, goalSelector, "c");
        c.clear();
        final List<?> b = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, goalSelector, "b");
        b.clear();
        final Object targetSelector = ReflectionUtils.getObject(EntityInsentient.class, ent, "targetSelector");
        final List<?> targC = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, targetSelector, "c");
        targC.clear();
        final List<?> targB = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, targetSelector, "b");
        targB.clear();
    }

    @Override
    public boolean checkStuck() {
        final Material type = this.getEntity().getLocation().getBlock().getType();
        if (type == Material.WATER || type == Material.STATIONARY_WATER || ((type == Material.STATIONARY_LAVA || type == Material.LAVA) && this.getPhase() == Phase.ONE)) {
            this.getAntiFarm().setStuckTicks(this.getAntiFarm().getStuckTicks() + 1);
            return this.getAntiFarm().isStuck();
        }
        return super.checkStuck();
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.WATER;
    }

    @Override
    public void cleanup() {
        this.removeMinions();
        this.modifier.replaceAll();
        this.modifier.removeAllBlocks(true);
        this.clearScoreboard();
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        super.onDeath(event);
        if (event != null) {
            this.sendMessage(ChatColor.ITALIC + "You... have defeated me.");
        }
    }

    private Slime getSlime() {
        return (Slime) this.getEntity();
    }

    @Override
    public void onMiscUpdate(final Event event) {
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("Are you challenging my rule?", false, 0), new BossMessage("You have slayed your last slime.", false, 0), new BossMessage("Ah, so you are [NAME], the infamous slime killer. Prepare to meet your doom!", false, -1), new BossMessage("You're making me angry die already!", true, 2)});
    }
}
