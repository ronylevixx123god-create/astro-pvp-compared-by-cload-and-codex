/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.metadata.MetadataValue
 */
package com.compilex.CosmicDungeons.listeners;

import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

public class DungeonChallengeChestListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        Set allowedLooters;
        Block block;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (block = event.getClickedBlock()) != null && block.getType() == Material.CHEST && block.hasMetadata("dungeonChallengeLootChest") && (allowedLooters = (Set)((MetadataValue)block.getMetadata("dungeonChallengeLootChest").get(0)).value()) != null && !allowedLooters.contains(event.getPlayer().getUniqueId()) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            event.getPlayer().sendMessage(ChatColor.RED.toString() + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You are not eligible to receive these dungeon challenge prizes!");
        }
    }
}

