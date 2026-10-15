/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.MaskUtils
 *  org.arkhamnetwork.enchantments.Enchantments
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicmasks.listeners;

import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class TerminatorTeleblockListener
implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && MaskUtils.hasMaskEquipped((Player) event.getDamager(), "TERMINATOR") && Math.random() < 0.002) {
            event.getEntity().setMetadata("teleblock", new FixedMetadataValue(Enchantments.getInstance(), System.currentTimeMillis() + 6000L));
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && MaskType.hasEquipped((Player) event.getDamager(), "KILLER_CLOWN")) {
            event.setDamage(event.getDamage() * 0.9);
        }
    }
}

