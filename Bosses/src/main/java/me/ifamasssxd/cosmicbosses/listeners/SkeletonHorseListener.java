package me.ifamasssxd.cosmicbosses.listeners;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.skeletonhorse.UndeadAssassin;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class SkeletonHorseListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerShoot(final EntityShootBowEvent e) {
        if (e.getEntity().hasMetadata("boss") && e.getEntity().hasMetadata("empower_arrow")) {
            final Boss boss = Boss.bosses.get(e.getEntity().getUniqueId());
            if (boss == null) {
                return;
            }
            if (boss instanceof UndeadAssassin) {
                final Projectile arrow = (Projectile) e.getProjectile();
                e.getEntity().removeMetadata("empower_arrow", (Plugin) CosmicBosses.getInstance());
                arrow.setVelocity(arrow.getVelocity().multiply(1.4));
                arrow.setFireTicks(100);
                arrow.setBounce(false);
                arrow.setMetadata("boss_empowered_arrow", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
                final Item tntItem = arrow.getLocation().getWorld().dropItem(arrow.getLocation(), new ItemStack(Material.TNT));
                tntItem.setVelocity(arrow.getVelocity());
                tntItem.setPickupDelay(Integer.MAX_VALUE);
                arrow.setPassenger((Entity) tntItem);
                boss.sendMessage(ChatColor.ITALIC + "Now.. " + ChatColor.RED + ChatColor.BOLD.toString() + "DIE!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProjectileLand(final ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("boss_empowered_arrow")) {
            if (e.getEntity().getPassenger() != null) {
                e.getEntity().getPassenger().remove();
            }
            e.getEntity().remove();
        }
    }
}
