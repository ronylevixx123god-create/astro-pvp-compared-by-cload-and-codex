/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

final class AntiWitherSpawnerListener
        implements Listener {
    AntiWitherSpawnerListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.SKELETON && e.getEntity().getWorld().getEnvironment() == World.Environment.NETHER) {
            ItemStack witherSkull = null;
            for (ItemStack i : e.getDrops()) {
                if (i.getType() != Material.SKULL_ITEM) continue;
                witherSkull = i;
                break;
            }
            if (witherSkull != null) {
                e.getDrops().remove(witherSkull);
            }
        }
    }
}

