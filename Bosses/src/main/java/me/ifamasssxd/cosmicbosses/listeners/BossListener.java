package me.ifamasssxd.cosmicbosses.listeners;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer.Enforcer;
import me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer.Marksman;
import me.ifamasssxd.cosmicbosses.bosses.plaguebloater.PlagueBloater;
import me.ifamasssxd.cosmicbosses.bosses.plaguebloater.PlagueBomb;
import me.ifamasssxd.cosmicbosses.bosses.yijki.Yijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.blockmanager.BlockChangeManager;
import me.ifamasssxd.cosmicbosses.struct.blockmanager.StoredBlock;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BossListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawnReinforce(final CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("bossTeleport")) {
            if (player.getMetadata("bossTeleport").get(0).value().equals(event.getTo())) {
                event.setCancelled(false);
                player.removeMetadata("bossTeleport", CosmicBosses.getInstance());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("boss")) {
            Entity damager;
            Boss boss = Boss.bosses.get(e.getEntity().getUniqueId());
            if (boss == null)
                return;
            if (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) {
                damager = (Entity)((Projectile)e.getDamager()).getShooter();
                if (boss instanceof me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer.Marksman || boss instanceof me.ifamasssxd.cosmicbosses.bosses.intergalacticenforcer.Enforcer) {
                    e.setCancelled(true);
                    e.getDamager().remove();
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ZOMBIE_INFECT, 1.5F, 2.0F);
                    return;
                }
            } else {
                damager = e.getDamager();
            }
            boss.onDamageTaken(e, damager);
        } else if ((e.getDamager().hasMetadata("boss") || isBossProjectile(e.getDamager())) && e.getEntity() instanceof Player) {
            Entity ent = (e.getDamager() instanceof Projectile) ? (Entity)((Projectile)e.getDamager()).getShooter() : e.getDamager();
            Boss boss2 = (Boss)Boss.bosses.get(ent.getUniqueId());
            if (boss2 == null)
                return;
            try {
                boss2.onAttackPlayer(e, (Player)e.getEntity());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isBossProjectile(final Entity ent) {
        return ent instanceof Projectile && ((Projectile) ent).getShooter() instanceof Entity && ((Entity) ((Projectile) ent).getShooter()).hasMetadata("boss");
    }

    @EventHandler
    public void onEntityRegain(final EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            final Boss boss = Boss.bosses.get(event.getEntity().getUniqueId());
            if (boss != null && !(boss instanceof Yijki)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHighest(EntityDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("boss")) {
            Boss boss = (Boss)Boss.bosses.get(e.getEntity().getUniqueId());
            if (boss == null)
                return;
            if (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player)) {
                Player player;
                if (e.getDamager() instanceof Projectile) {
                    player = (Player)((Projectile)e.getDamager()).getShooter();
                } else {
                    player = (Player)e.getDamager();
                }
                boss.getDamageTracker().trackDamage((Entity)player, e);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowestEntityDAmage(EntityDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("boss") && CosmicBosses.isMcMMOEnabled() && (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player))) {
            Player player;
            if (e.getDamager() instanceof Projectile) {
                player = (Player)((Projectile)e.getDamager()).getShooter();
            } else {
                player = (Player)e.getDamager();
            }
            player.setMetadata("no_exp_gain", (MetadataValue)new FixedMetadataValue((Plugin)CosmicBosses.getInstance(), ""));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkUnload(ChunkUnloadEvent unloadEvent) {
        for (Iterator<Boss> iterator = Boss.bosses.values().iterator(); iterator.hasNext(); ) {
            Boss boss = iterator.next();
            if (boss != null && !boss.getEntity().isDead() && boss.getEntity().getLocation().getChunk().getX() == unloadEvent.getChunk().getX() && boss.getEntity().getLocation().getChunk().getZ() == unloadEvent.getChunk().getZ() && boss.getEntity().getLocation().getChunk().getWorld().getName().equals(unloadEvent.getChunk().getWorld().getName()) && !unloadEvent.isCancelled()) {
                unloadEvent.setCancelled(true);
                Bukkit.getLogger().info("Cancelling chunk unload for boss at " + unloadEvent.getChunk().getX() + "X, " + unloadEvent.getChunk().getZ() + "Z, world: " + unloadEvent.getChunk().getWorld().getName() + "!");
                return;
            }
            if (boss == null || (boss.getBossType() != BossType.HOLIDAY_SPIRIT && boss.getBossType() != BossType.CHRISTMAS_SPIRIT))
                continue;
            BlockChangeManager change = CosmicBosses.getInstance().getBlockChangeManager();
            List<Location> toRemove = Lists.newArrayList();
            change.getStoredBlocks().entrySet().stream().filter(entry -> ((Location)entry.getKey()).getChunk().equals(unloadEvent.getChunk())).forEach(entry -> {
                change.restoreBlock((Location)entry.getKey(), (StoredBlock)entry.getValue());
                toRemove.add(entry.getKey());
            });
            toRemove.forEach(loc -> change.getStoredBlocks().remove(loc));
            Bukkit.getLogger().info("Replacing " + toRemove.size() + " blocks in unloading chunk..");
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("boss")) {
            final Boss boss = Boss.bosses.get(e.getEntity().getUniqueId());
            if (boss == null) {
                return;
            }
            boss.onDamageTaken(e);
        }
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent event) {
        if (event.getEntity().hasMetadata("boss")) {
            final Boss boss = Boss.bosses.get(event.getEntity().getUniqueId());
            if (boss == null) {
                return;
            }
            boss.onMiscUpdate((Event) event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(final ChunkLoadEvent event) {
        final String chunk = event.getChunk().getX() + ":" + event.getChunk().getZ();
        if (CosmicBosses.getInstance().getPendingBosses().containsKey(chunk)) {
            final List<String[]> bosses = CosmicBosses.getInstance().getPendingBosses().remove(chunk);
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> {
                Iterator<String[]> iterator = bosses.iterator();
                while (iterator.hasNext()) {
                    String[] boss = iterator.next();
                    BossUtils.spawnBoss(boss);
                }
            }, 20L);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity().hasMetadata("boss")) {
            final Boss boss = Boss.bosses.get(event.getEntity().getUniqueId());
            if (boss == null) {
                return;
            }
            boss.onEntityChangeBlock(event, event.getEntity());
        } else if (event.getEntity().hasMetadata("chainReactionBlock")) {
            UUID id = (UUID) event.getEntity().getMetadata("chainReactionBlock").get(0).value();
            Boss boss2 = Boss.bosses.get(id);
            if (boss2 == null) {
                return;
            }
            boss2.onEntityChangeBlock(event, event.getEntity());
        }
    }

    @EventHandler
    public void onEntityTeleport(final EntityTeleportEvent event) {
        if (event.getEntity().hasMetadata("boss")) {
            final Boss boss = Boss.bosses.get(event.getEntity().getUniqueId());
            if (boss == null) {
                return;
            }
            boss.onEntityTeleport(event, event.getEntity());
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("boss")) {
            e.getDrops().clear();
            e.setDroppedExp(0);
            final Boss boss = Boss.bosses.get(e.getEntity().getUniqueId());
            boss.onDeath(e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTNTExplode(final EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            final boolean deathPlague = PlagueBomb.tntList.contains(e.getEntity().getUniqueId());
            if (deathPlague || e.getEntity().hasMetadata("dragon_bomb")) {
                for (Object b : Lists.newArrayList((Iterable) e.blockList())) {
                    Block block = (Block) b;
                    e.blockList().remove(block);
                }
                if (deathPlague) {
                    e.setCancelled(true);
                    PlagueBomb.tntList.remove(e.getEntity().getUniqueId());
                    for (final Boss boss : BossUtils.getAllBossesFromType(PlagueBloater.class)) {
                        final PlagueBloater bloater = (PlagueBloater) boss;
                        if (bloater.getPlagueBomb().getPrimedTNT().contains(e.getEntity())) {
                            bloater.onTNTExplode(e.getEntity());
                            break;
                        }
                    }
                }
            }
        }
    }
}
