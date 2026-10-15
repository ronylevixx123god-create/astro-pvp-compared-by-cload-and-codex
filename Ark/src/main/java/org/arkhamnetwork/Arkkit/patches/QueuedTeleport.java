/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

final class QueuedTeleport {
    public Player player;
    public Location targetLoc;
    public double teleportWarmup;
    public Location castLocation;
    public int taskID;
    public PlayerTeleportEvent.TeleportCause teleportCause;

    QueuedTeleport(Player p, Location targetLoc, double warmup, PlayerTeleportEvent.TeleportCause cause) {
        int taskID;
        this.player = p;
        this.targetLoc = targetLoc;
        this.teleportWarmup = warmup;
        this.castLocation = this.player.getLocation();
        this.teleportCause = cause;
        final Location l = this.targetLoc;
        this.taskID = taskID = Bukkit.getScheduler().runTaskLater(Arkkit.get(), new Runnable() {

            @Override
            public void run() {
                QueuedTeleport.this.player.removeMetadata("queuedTeleport", Arkkit.get());
                QueuedTeleport.this.player.removeMetadata("queuedTeleportTaskID", Arkkit.get());
                if (QueuedTeleport.this.player.getLocation().getWorld().equals(QueuedTeleport.this.castLocation.getWorld()) && QueuedTeleport.this.player.getLocation().distanceSquared(QueuedTeleport.this.castLocation) <= 4.0) {
                    QueuedTeleport.this.player.setMetadata("queuedTeleportPass", new FixedMetadataValue(Arkkit.get(), l));
                    QueuedTeleport.this.player.playSound(QueuedTeleport.this.player.getLocation(), Sound.BAT_TAKEOFF, 0.8f, 0.65f);
                    QueuedTeleport.this.player.teleport(l, QueuedTeleport.this.teleportCause);
                    QueuedTeleport.this.player.removeMetadata("queuedTeleportPass", Arkkit.get());
                } else {
                    QueuedTeleport.this.player.sendMessage(ChatColor.DARK_RED + "Pending teleport request cancelled due to movement.");
                }
            }
        }, Math.round(this.teleportWarmup * 20.0)).getTaskId();
        this.player.setMetadata("queuedTeleportTaskID", new FixedMetadataValue(Arkkit.get(), taskID));
    }
}

