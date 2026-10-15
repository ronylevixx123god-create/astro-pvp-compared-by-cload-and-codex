/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.world.ChunkLoadEvent
 */
package com.compilex.CosmicDungeons.listeners;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.struct.DungeonDebuff;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class DungeonSanityListener
implements Listener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerTeleportIntoDungeon(PlayerTeleportEvent e) {
        if (!e.getFrom().getWorld().equals((Object)e.getTo().getWorld()) && e.getTo().getWorld().getName().startsWith("dungeon_")) {
            Player p = e.getPlayer();
            if (p.isOp() || p.hasPermission("essentials.tpo") || p.hasPermission("essentials.*")) {
                return;
            }
            if (e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName())) {
                e.setCancelled(true);
                return;
            }
            if (!e.getFrom().getBlock().hasMetadata("dungeonPortalBlock") && !e.getFrom().clone().add(0.0, 1.0, 0.0).getBlock().hasMetadata("dungeonPortalBlock")) {
                e.setCancelled(true);
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You can only teleport into dungeons through dungeon portals.");
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onChunkGenerateInDungeon(ChunkLoadEvent e) {
        if (e.getWorld().getName().startsWith("dungeon_") && e.isNewChunk()) {
            e.getChunk().unload(false, false);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            if (event.getItem() == null) {
                return;
            }
            if (event.getItem().getType() == Material.ENDER_PEARL) {
                if (DungeonsAPI.hasDungeonDebuff(player, DungeonDebuff.NO_ENDER_PEARLS)) {
                    event.setCancelled(true);
                    event.setUseItemInHand(Event.Result.DENY);
                    player.sendMessage(CC.translate((String)"&c&l(!) &cYou cannot use ender pearls in this heroic dungeon!"));
                }
            } else if (event.getItem().getType() == Material.POTION && DungeonsAPI.hasDungeonDebuff(player, DungeonDebuff.NO_POTIONS)) {
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
                player.sendMessage(CC.translate((String)"&c&l(!) &cYou cannot use potions in this heroic dungeon!"));
            }
        }
    }
}

