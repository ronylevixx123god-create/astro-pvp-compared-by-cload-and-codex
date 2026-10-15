package me.ifamasssxd.cosmicbosses.listeners;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MinionListener implements Listener {
    @EventHandler
    public void onEntityTarget(final EntityTargetEvent event) {
        if (event.getEntity().hasMetadata("minion")) {
            final Boss boss = BossUtils.getBossFromMinion(event.getEntity());
            if (boss == null) {
                return;
            }
            boss.onMinionUpdate(event.getEntity(), (Event) event);
        }
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetLivingEntityEvent event) {
        if (event.getEntity().hasMetadata("minion")) {
            final Boss boss = BossUtils.getBossFromMinion(event.getEntity());
            if (boss == null) {
                return;
            }
            boss.onMinionUpdate(event.getEntity(), (Event) event);
            if (event.isCancelled()) {
                event.setTarget((Entity) null);
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlockEvent(final EntityChangeBlockEvent event) {
        if (event.getEntity().hasMetadata("minion")) {
            final Boss boss = BossUtils.getBossFromMinion(event.getEntity());
            if (boss != null) {
                boss.onMinionUpdate(event.getEntity(), (Event) event);
            }
        }
    }


    @EventHandler
    public void onSlimeSplit(final SlimeSplitEvent e) {
        if (e.getEntity().hasMetadata("no_slime_split")) {
            e.setCancelled(true);
            e.setCount(0);
        }
    }

    @EventHandler
    public void onEntityCombust(final EntityCombustEvent event) {
        if (event.getEntity().hasMetadata("minion")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMinionDeath(final EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("minion") || e.getEntity().hasMetadata("no_drops")) {
            e.getDrops().clear();
            e.setDroppedExp(0);
            final Boss boss = BossUtils.getBossFromMinion((Entity) e.getEntity());
            if (boss == null) {
                return;
            }
            boss.onMinionUpdate((Entity) e.getEntity(), (Event) e);
            boss.removeMinion((Entity) e.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMinionDeathMonitor(final EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("minion") || e.getEntity().hasMetadata("no_drops")) {
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }
}
