/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.Tuple;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


final class AntiLootStealListener implements Listener {

    private HashMap<Item, Tuple<UUID, Long>> itemsMap;

    public AntiLootStealListener() {
        itemsMap = new HashMap<>();
    }

    private int pickupDelayInMiliseconds = AntiLootSteal.itemPickUpDelayInSeconds * 1000;

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (AntiLootSteal.disabledWorlds.contains(event.getPlayer().getWorld().getName()))
            return;
//        if (event.getItem().hasMetadata("antiLoot_uuid")) {
        if (itemsMap.containsKey(event.getItem())) {
        Player player = event.getPlayer();
            if (!player.hasPermission("anti.loot.bypass")) {
//                UUID killerUUID = UUID.fromString(((MetadataValue)event.getItem().getMetadata("antiLoot_uuid").get(0)).asString());
                Tuple<UUID, Long> tuple = itemsMap.get(event.getItem());
                UUID killerUUID = tuple.getLeft();
                long timeDifference = System.currentTimeMillis() - tuple.getRight();
                if (player.getUniqueId().equals(killerUUID))
                    return;
                if (timeDifference >= this.pickupDelayInMiliseconds)
                    return;
                event.setCancelled(true);
                if (!player.hasMetadata("antiLoot_nextWarning") || ((MetadataValue)player.getMetadata("antiLoot_nextWarning").get(0)).asLong() < System.currentTimeMillis()) {
                    player.setMetadata("antiLoot_nextWarning", (MetadataValue)new FixedMetadataValue((Plugin)Arkkit.get(), Long.valueOf(System.currentTimeMillis() + 10000L)));
                    player.sendMessage(CC.RedB + "(LOOT) " + CC.Red + "You cannot loot this item for " + ((this.pickupDelayInMiliseconds - timeDifference) / 1000L) + "s!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            if (AntiLootSteal.disabledWorlds.contains(event.getEntity().getWorld().getName()))
                return;
            Player killer = event.getEntity().getPlayer().getKiller();
            Player player = event.getEntity().getPlayer();
            for (ItemStack stack : event.getDrops()) {
                Item item = player.getWorld().dropItemNaturally(player.getLocation(), stack);
                long time = System.currentTimeMillis();
                UUID uuid = killer.getUniqueId();
                itemsMap.put(item, new Tuple<>(uuid, time));
/*                item.setMetadata("antiLoot_uuid", (MetadataValue)new FixedMetadataValue((Plugin)Arkkit.get(), killer.getUniqueId()));
                item.setMetadata("antiLoot_time", (MetadataValue)new FixedMetadataValue((Plugin)Arkkit.get(), Long.valueOf(time)));*/
            }
            event.getDrops().clear();
        }
    }
}
