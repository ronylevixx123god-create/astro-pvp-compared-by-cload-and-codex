package org.arkhamnetwork.enchantments.listeners;

import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class VanillaDamageListener implements Listener {
    public static double getLastRawDamage(final Entity e) {
        return e.hasMetadata("lastDamageEventRawDamage") ? e.getMetadata("lastDamageEventRawDamage").get(0).asDouble() : 0.0;
    }

    public static double getLastFinalDamage(final Entity e) {
        return e.hasMetadata("lastDamageEventFinalDamage") ? e.getMetadata("lastDamageEventFinalDamage").get(0).asDouble() : 0.0;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(final EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            e.getEntity().setMetadata("lastDamageEventRawDamage", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) e.getDamage()));
            e.getEntity().setMetadata("lastDamageEventFinalDamage", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) e.getFinalDamage()));
        }
    }



}
