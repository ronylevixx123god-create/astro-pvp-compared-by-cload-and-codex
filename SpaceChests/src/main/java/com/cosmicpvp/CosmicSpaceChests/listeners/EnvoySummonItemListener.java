/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.listeners;

import com.cosmicpvp.CosmicSpaceChests.tasks.CosmicEnvoyTask;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EnvoySummonItemListener
implements Listener {
    public static ItemStack buildEnvoySpawnItem() {
        return ItemUtils.buildItem(Material.REDSTONE_TORCH_ON, (short)0, (Object)ChatColor.GOLD + "Cosmic Envoy Summon " + (Object)ChatColor.GRAY + "(Right Click)", (Object)ChatColor.GRAY + "Spawns a Cosmic Envoy at the warzone!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (!this.isEnvoySpawnItem(e.getItem())) {
                return;
            }
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
            if (e.getItem().getAmount() == 1) {
                e.getPlayer().setItemInHand(null);
            } else {
                ItemStack is = e.getItem().clone();
                is.setAmount(is.getAmount() - 1);
                e.getPlayer().setItemInHand(is);
            }
            e.getPlayer().updateInventory();
            CosmicEnvoyTask.spawnCosmicEnvoy(e.getPlayer().getName());
            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(e.getPlayer(), "ENVOY_SUMMON", new Object[]{e.getItem()}));
        }
    }

    private boolean isEnvoySpawnItem(ItemStack is) {
        return is.getType() == Material.REDSTONE_TORCH_ON && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith((Object)ChatColor.GOLD + "Cosmic Envoy Summon") && ((String)is.getItemMeta().getLore().get(0)).equals((Object)ChatColor.GRAY + "Spawns a Cosmic Envoy at the warzone!");
    }
}

