/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.intellectualcrafters.plot.api.PlotAPI
 *  com.intellectualcrafters.plot.object.Plot
 *  com.intellectualcrafters.plot.object.PlotPlayer
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;

final class ChatLimiterListener
        implements Listener {
    private final boolean plotSquaredEnabled = new File("plugins/PlotSquared.jar").exists() || new File("plugins/PlotSquared-3.2.23.jar").exists();

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player pl = e.getPlayer();
        if (pl.isOp()) {
            return;
        }
        boolean vip = pl.hasPermission("chatlimiter.vip") || pl.hasPermission("arkhamchat.vip");
        boolean plotChat = false;
        long chat_delay = vip ? ChatLimiter.vip_delay : ChatLimiter.default_delay;
        long last_chat_message = pl.hasMetadata("last_chat_message") ? pl.getMetadata("last_chat_message").get(0).asLong() : 0L;
        long dif = System.currentTimeMillis() - last_chat_message;
        if (dif < (plotChat ? 250L : chat_delay * 1000L)) {
            pl.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot send another message for " + (chat_delay * 1000L - dif) + "ms.");
            if (!vip) {
                pl.sendMessage(ChatColor.GRAY + "Purchase a rank at " + ChatColor.RED + ChatColor.UNDERLINE + ChatLimiter.store_url + ChatColor.GRAY + " to reduce the delay between chat messages!");
            }
            e.setCancelled(true);
            return;
        }
        pl.setMetadata("last_chat_message", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
    }
}

