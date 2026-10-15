package org.arkhamnetwork.enchantments.mastery;

import com.google.common.cache.CacheBuilder;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class MasteryDemonicGateway extends MasteryCustomEnchantment {
    private ConcurrentMap<Object, Object> demonicGateway;
    private ConcurrentMap<Object, Object> crystalShootTicks;

    public MasteryDemonicGateway() {
        super("Demonic Gateway", GeneralUtil.armor, 11);
        this.demonicGateway = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build().asMap();
        this.crystalShootTicks = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.SECONDS).build().asMap();
        this.max = 6;
        this.armorSet = "NECROMANCER";
    }

    public void applyDefenseEffect(final LivingEntity user, final LivingEntity target, final int enchantLevel, final EntityDamageEvent event) {
        if (!(target instanceof Player)) {
            return;
        }
        /*final Player damaged = (Player) user;
        final double chance = 0.015 + 0.005 * enchantLevel;
        if (Math.random() <= chance) {
            if (this.demonicGateway.containsKey(damaged.getUniqueId())) {
                return;
            }
            if (!new CustomEnchantProcOnEntityEvent((LivingEntity) damaged, target, (Event) event, this, enchantLevel).callEvent()) {
                return;
            }
            final int crystalsToSpawn = Math.min(2 + enchantLevel / 2, 5);
            final double spawnDuration = Math.min(enchantLevel * 2.5, 20.0);
            final double degreesInBetween = 360.0 / crystalsToSpawn;
            final double distance = 3 + this.max / 2 + crystalsToSpawn / 2;
            final Location start = damaged.getLocation();
            final Set<EnderCrystal> spawnedCrystals = new HashSet<EnderCrystal>();
            for (int i = 0; i < crystalsToSpawn; ++i) {
                final double radians = Math.toRadians(i * degreesInBetween);
                final double newX = distance * Math.cos(radians);
                final double newZ = distance * Math.sin(radians);
                final Location location = start.clone().add(newX, 0.0, newZ);
                final Location toSpawn = this.getOpenBlock(location, 4);
                if (toSpawn == null || !WorldGuardUtils.isPvPEnabled(toSpawn)) {
                    Bukkit.getLogger().info("[Demonic Gateway] Skipping spawn at " + location + " for ender crystal..");
                } else {
                    final EnderCrystal crystal = (EnderCrystal) toSpawn.getWorld().spawnEntity(toSpawn.clone().add(0.0, 1.0, 0.0), EntityType.ENDER_CRYSTAL);
                    crystal.getWorld().playSound(crystal.getLocation(), Sound.GHAST_FIREBALL, 3.0f, 0.9f);
                    crystal.getWorld().playEffect(crystal.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                    crystal.setMetadata("demonicGateway", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) damaged.getName()));
                    crystal.getWorld().strikeLightningEffect(crystal.getLocation());
                    spawnedCrystals.add(crystal);
                    final Entity entity = (Entity) ((CraftEntity) crystal).getHandle();
                    try {
                        ReflectionUtils.setObject((Object) entity, ReflectionUtils.getField((Class) Entity.class, "invulnerable"), (Object) true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Bukkit.getLogger().info("[Demonic Gateway] " + crystalsToSpawn + " EnderCrystal spawning near " + damaged.getLocation().toString() + " for " + damaged.getName() + "!");
            if (spawnedCrystals.size() > 0) {
                this.demonicGateway.put(damaged.getUniqueId(), System.currentTimeMillis());
                for (final org.bukkit.entity.Entity nearbyEntity : damaged.getNearbyEntities(24.0, 24.0, 24.0)) {
                    if (nearbyEntity instanceof Player) {
                        final Player pNear = (Player) nearbyEntity;
                        if (GeneralUtil.isAtleastTruce(pNear, damaged)) {
                            continue;
                        }
                        pNear.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "** DEMONIC GATEWAY " + this.getRomanNumeral(enchantLevel) + " (" + ChatColor.GREEN + damaged.getName() + ChatColor.DARK_GREEN + ChatColor.BOLD + ") **");
                    }
                }
                final double crystalScanRadius = 5 + enchantLevel;
                new BukkitRunnable() {
                    public void run() {
                        if (spawnedCrystals.isEmpty() || !damaged.isValid()) {
                            this.cancel();
                            return;
                        }
                        final Faction damagedFaction = FactionUtils.getFaction(damaged, true);
                        boolean allDead = true;
                        for (final EnderCrystal crystal : spawnedCrystals) {
                            if (crystal.isValid()) {
                                if (crystal.isDead()) {
                                    continue;
                                }
                                allDead = false;
                                Integer cooldownTicks = (Integer) MasteryDemonicGateway.this.crystalShootTicks.get(crystal);
                                if (cooldownTicks != null && cooldownTicks > 0) {
                                    MasteryDemonicGateway.this.crystalShootTicks.put(crystal, --cooldownTicks);
                                } else {
                                    final EntityEnderCrystal ender = (EntityEnderCrystal) ((CraftEntity) crystal).getHandle();
                                    final List<EntityHuman> nearbyPlayers = ender.world.a(EntityHuman.class, ender.getBoundingBox().grow(crystalScanRadius, crystalScanRadius, crystalScanRadius), (Predicate<? super EntityHuman>) ent -> ent != null && damaged.canSee((Player) ent.getBukkitEntity()) && ((Player) ent.getBukkitEntity()).getGameMode() == GameMode.SURVIVAL && WorldGuardUtils.isPvPEnabled(new Location(damaged.getWorld(), ent.locX, ent.locY, ent.locZ)));
                                    final Location crystalLocation = crystal.getLocation();
                                    for (final Object obj : nearbyPlayers) {
                                        if (obj instanceof EntityHuman) {
                                            MasteryDemonicGateway.this.crystalShootTicks.put(crystal, 8 + ThreadLocalRandom.current().nextInt(6));
                                            final EntityHuman toShoot = (EntityHuman) obj;
                                            final Player player = (Player) toShoot.getBukkitEntity();
                                            if (player.equals(damaged)) {
                                                continue;
                                            }
                                            if (GeneralUtil.isAtleastTruce(player, damaged)) {
                                                continue;
                                            }
                                            final Location shoot = toShoot.getBukkitEntity().getLocation();
                                            final Vector newDirection = new Vector(shoot.getX() - crystalLocation.getX(), shoot.getY() + toShoot.getHeadHeight() - 1.100000023841858 - crystalLocation.getY(), shoot.getZ() - crystalLocation.getZ());
                                            if (newDirection.length() != 0.0) {
                                                newDirection.multiply(0.05 + enchantLevel * 0.0025);
                                            }
                                            Bukkit.getLogger().info("Shooting skull: " + newDirection + " Damaged: " + damaged.getName() + "!");
                                            MasteryDemonicGateway.this.createCrystalSkullProjectile(damagedFaction, damaged, crystal, crystal.getLocation().add(0.0, 1.0, 0.0), enchantLevel, newDirection);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (allDead) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin) Enchantments.getInstance(), 30L, 3L);
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Enchantments.getInstance(), () -> spawnedCrystals.forEach(crystalEntity -> {
                    try {
                        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), crystalEntity.getLocation(), 0f,0.5f,0f,0.7f,16);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    crystalEntity.remove();
                }), (long) (spawnDuration * 20.0));
            }
        }
    }

    public EntityWitherSkull createCrystalSkullProjectile(final Faction faction, final Player shooterPlayer, final EnderCrystal crystal, final Location location, final int level, final Vector direction) {
        final WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        final EntityWitherSkull skull = new EntityWitherSkull(world, ((CraftPlayer) shooterPlayer).getHandle(), direction.getX(), direction.getY(), direction.getZ()) {
            public void setDirection(final double d0, final double d1, final double d2) {
                final double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
                this.dirX = d0 / d3 * 0.1;
                this.dirY = d1 / d3 * 0.1;
                this.dirZ = d2 / d3 * 0.1;
            }

            protected void a(final MovingObjectPosition movingobjectposition) {
                if (movingobjectposition.entity instanceof EntityHuman) {
                    final Player pl = (Player) movingobjectposition.entity.getBukkitEntity();
                    if (pl == null || pl.hasMetadata("NPC") || pl.hasMetadata("spectator") || pl.getGameMode() != GameMode.SURVIVAL) {
                        return;
                    }
                    final FPlayer other = FPlayers.getInstance().getByPlayer(pl);
                    if (other != null && faction != null && other.hasFaction() && other.getFaction().getRelationTo(faction).isAtLeast(Relation.TRUCE)) {
                        return;
                    }
                    if (!new CustomEnchantProcOnEntityEvent(shooterPlayer, pl, null, MasteryDemonicGateway.this, level).callEvent()) {
                        return;
                    }
                } else if (movingobjectposition.entity != null) {
                    return;
                }
                if (!this.world.isClientSide) {
                    if (movingobjectposition.entity != null) {
                        if (this.shooter != null) {
                            movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shooter), 8.0f);
                        } else {
                            movingobjectposition.entity.damageEntity(DamageSource.MAGIC, 5.0f);
                        }
                    }
                    this.die();
                }
            }
        };
        skull.fromMobSpawner = true;
        final EntityWitherSkull entityWitherSkull = skull;
        final EntityWitherSkull entityWitherSkull2 = skull;
        final EntityWitherSkull entityWitherSkull3 = skull;
        final double motX = 0.0;
        entityWitherSkull3.motZ = motX;
        entityWitherSkull2.motY = motX;
        entityWitherSkull.motX = motX;
        skull.locX = location.getX();
        skull.locY = location.getY();
        skull.locZ = location.getZ();
        skull.setDirection(direction.getX(), direction.getY(), direction.getZ());
        skull.getBukkitEntity().setVelocity(direction);
        skull.getBukkitEntity().setMetadata("skullLevel", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) level));
        skull.getBukkitEntity().setMetadata("crystalSkull", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) shooterPlayer.getName()));
        skull.getBukkitEntity().setMetadata("crystalShooter", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) crystal));
        world.addEntity(skull, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return skull;
    }

    public Location getOpenBlock(final Location start, final int yToCheck) {
        for (int y = -yToCheck; y < yToCheck; ++y) {
            final Location newLoc = start.clone().add(0.0, (double) y, 0.0);
            if (newLoc.getY() > 0.0) {
                final Block block = newLoc.getBlock();
                final Material type = block.getType();
                if (type.isSolid() && this.isSpawnable(block.getRelative(BlockFace.UP).getType()) && this.isSpawnable(block.getRelative(BlockFace.UP, 2).getType())) {
                    return newLoc;
                }
            }
        }
        return null;
    }

    private boolean isSpawnable(final Material type) {
        return type == Material.AIR || type == Material.WATER || type == Material.STATIONARY_WATER || type == Material.LAVA || type == Material.STATIONARY_LAVA;
    }

    public void trapTarget(final Player player, final String damagerName, final int level) {
        if (player.getWalkSpeed() < 0.2f) {
            return;
        }
        player.setWalkSpeed(0.0f);
        final int duration = (int) Math.min(5.0, 1.0 + level * 0.125);
        Bukkit.getLogger().info("Trapping " + player.getName() + " by " + duration + " seconds!");
        player.sendMessage(CC.RedB + "* DEMONIC GATEWAY TRAP [by: " + CC.Gray + damagerName + " (" + NumberUtils.formatSeconds((double) duration) + "s)" + CC.RedB + "] *");
        Enchantments.getInstance().getServer().getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> player.setWalkSpeed(0.2f), (long) (duration * 20));
    }

    public String getRomanNumeral(final int i) {
        switch (i) {
            case 1: {
                return "I";
            }
            case 2: {
                return "II";
            }
            case 3: {
                return "III";
            }
            case 4: {
                return "IV";
            }
            case 5: {
                return "V";
            }
            case 6: {
                return "VI";
            }
            default: {
                return String.valueOf(i);
            }
        }
    }

    class MasteryDemonicGatewayListener extends CustomEnchantListener<MasteryDemonicGateway> {
        public MasteryDemonicGatewayListener(final MasteryDemonicGateway enchant) {
            super(enchant);
        }

        @EventHandler
        public void onEntityExplodePrime(final ExplosionPrimeEvent event) throws Exception {
            if (event.getEntity().hasMetadata("crystalSkull") || event.getEntity().hasMetadata("demonicGateway")) {
                event.setCancelled(true);
                de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), event.getEntity().getLocation(), 0.5f, 0.5f, 0.5f, 0.1f, 10);

            }
        }

        @EventHandler
        public void onEntityExplode(final EntityExplodeEvent event) throws Exception {
            if (event.getEntity().hasMetadata("crystalSkull") || event.getEntity().hasMetadata("demonicGateway")) {
                event.setCancelled(true);
                de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), event.getEntity().getLocation(), 0.5f, 0.5f, 0.5f, 0.1f, 10);
            }
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onEntityDamage(final EntityDamageEvent event) {
            if (event.getEntity() instanceof EnderCrystal && event.getEntity().hasMetadata("demonicGateway")) {
                event.setCancelled(true);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onSkullExplodes(final EntityDamageByEntityEvent event) {
            if (event.getEntity().hasMetadata("demonicGateway")) {
                event.setCancelled(true);
                return;
            }
            if (!(event.getEntity() instanceof Player)) {
                return;
            }
            if (event.getDamager() instanceof WitherSkull && event.getDamager().hasMetadata("crystalSkull") && WorldGuardUtils.isPvPEnabled(event.getEntity().getLocation()) && event.getDamager().isValid()) {
                final org.bukkit.entity.Entity witherSkull = event.getDamager();
                final Player damaged = (Player) event.getEntity();
                final double toDeal = damaged.getMaxHealth() * 0.05;
                event.setCancelled(true);
                witherSkull.remove();
                if (witherSkull.hasMetadata("skullLevel") && !damaged.isDead()) {
                    final String shooterName = witherSkull.getMetadata("crystalSkull").get(0).asString();
                    final int level = witherSkull.getMetadata("skullLevel").get(0).asInt();
                    if (!new CustomEnchantProcOnEntityEvent((LivingEntity) ((WitherSkull) witherSkull).getShooter(), (LivingEntity) damaged, null, this.customEnchant, level).callEvent()) {
                        return;
                    }
                    if (Math.random() <= 0.06 + level * 0.03) {
                        MasteryDemonicGateway.this.trapTarget(damaged, shooterName, level);
                    }
                }
                damaged.playEffect(EntityEffect.HURT);
                GeneralUtil.reducePlayerHealth(damaged, toDeal);
                Bukkit.getLogger().info("[DemonicGateway] Damaging " + damaged.getName() + " by " + toDeal + " HP!");
                if (witherSkull.hasMetadata("crystalShooter") && !damaged.isDead() && damaged.getWalkSpeed() > 0.0f) {
                    final EnderCrystal crystal = (EnderCrystal) witherSkull.getMetadata("crystalShooter").get(0).value();
                    if (crystal != null && !crystal.isDead() && crystal.getWorld().getName().equals(event.getEntity().getWorld().getName())) {
                        final double distance = crystal.getLocation().distanceSquared(event.getEntity().getLocation());
                        if (distance < 16.0) {
                            GeneralUtil.pushAwayEntity(crystal.getLocation(), event.getEntity(), 1.100000023841858);
                            damaged.playSound(damaged.getLocation(), Sound.WITHER_SHOOT, 1.0f, 1.1f);
                            Bukkit.getLogger().info("[Demonic gate] Pushing away " + damaged.getName() + " from Crystal: " + NumberUtils.formatSeconds(distance));
                        }
                    }
                    witherSkull.removeMetadata("crystalShooter", (Plugin) Enchantments.getInstance());
                }
            }
        }
    }*/
    }
}