/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package org.arkhamnetwork.Arkkit.patches.chat_filter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

final class ChatFilterListener
        implements Listener {
    ChatFilterListener() {
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        String lower;
        Player player = event.getPlayer();
        if (player.hasPermission("arkkit.chat_filter.admin") || player.hasPermission("simplechat.admin")) {
            return;
        }
        if (ChatFilter.isChatHalted()) {
            player.sendMessage(ChatColor.RED + "Chat has been temporary disabled by an administrator.");
            player.sendMessage(ChatColor.GRAY + "Please be patient, chat will be re-enabled promptly.");
            event.setCancelled(true);
            return;
        }
        /*if (ChatFilter.block_chat_until_move && ChatFilter.hasntMoved.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " chat until your character has moved.");
            event.setCancelled(true);
            return;
        }*/
        if (ChatFilter.block_duplicate_messages) {
            if (ChatFilter.lastMessage.containsKey(player.getUniqueId())) {
                long last_send;
                String oldmsg = ChatFilter.lastMessage.get(player.getUniqueId());
                long l = last_send = ChatFilter.lastMessage_time.containsKey(player.getUniqueId()) ? ChatFilter.lastMessage_time.get(player.getUniqueId()) : 0L;
                if (System.currentTimeMillis() <= last_send + 5000L && event.getMessage().equalsIgnoreCase(oldmsg)) {
                    player.sendMessage(ChatColor.RED + "Please do not send duplicate messages so quickly!");
                    ChatFilter.lastMessage_time.put(player.getUniqueId(), System.currentTimeMillis());
                    event.setCancelled(true);
                    return;
                }
            }
            ChatFilter.lastMessage_time.put(player.getUniqueId(), System.currentTimeMillis());
            ChatFilter.lastMessage.put(player.getUniqueId(), event.getMessage());
        }
        if (!player.hasPermission("cosmicpvp.vip") && ChatUtils.checkMessageForAdvertising(event.getMessage().toLowerCase())) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "URL or advertisement link was found in your message, and therefore blocked.");
            Bukkit.getLogger().info("[Arkkit (ChatFilter)] Filtered message: \"" + event.getMessage() + "\" from " + player.getName());
            event.setCancelled(true);
            return;
        }
        if (!player.hasPermission("cosmicpvp.advertising") && ChatUtils.checkMessageForIP(event.getMessage())) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Advertisement link was found in your message, and therefore blocked.");
            Bukkit.getLogger().info("[Arkkit (ChatFilter)] Filtered advertisment / ip message: \"" + event.getMessage() + "\" from " + player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChatFilter.remove_chat_colors) {
            event.setMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getMessage())));
        }
        for (Object c : event.getMessage().toCharArray()) {
            if (Character.UnicodeBlock.of((char) c) == Character.UnicodeBlock.BASIC_LATIN) continue;
            player.sendMessage(ChatColor.RED + "Invalid character '" + c + "' in message.");
            event.setCancelled(true);
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            event.setMessage(ChatUtils.getFilteredSwearMessage(event.getMessage(), true));
            event.setMessage(ChatUtils.getFilteredUppercaseMessage(event.getMessage()));
        }
        if ((lower = event.getMessage().toLowerCase()).contains("play") && lower.contains("enderman") && lower.contains("us")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("factionpvp") && lower.contains("us")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("ogcosmicpvp") && lower.contains("us")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("play") && lower.contains("spawner") && lower.contains("us")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("play") && lower.contains("creeper") && lower.contains("us")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("aged") && lower.contains("cf")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("freeranks") && lower.contains("trade")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("exoticraids") && lower.contains("pw")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("play") && lower.contains("mcraids") && lower.contains("us")) {
            event.setCancelled(true);
            return;
        }
        if (lower.contains("cosmicpvptest")) {
            event.setCancelled(true);
            return;
        }
        event.setMessage(this.capitalizeFirstLetter(event.getMessage()));
    }

    public String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onKick(PlayerKickEvent event) {
        ChatFilter.lastMessage.remove(event.getPlayer().getUniqueId());
        if (ChatFilter.block_chat_until_move) {
            ChatFilter.hasntMoved.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        ChatFilter.lastMessage.remove(event.getPlayer().getUniqueId());
        if (ChatFilter.block_chat_until_move) {
            ChatFilter.hasntMoved.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (ChatFilter.block_chat_until_move) {
            ChatFilter.hasntMoved.add(event.getPlayer().getUniqueId());
        }
    }

    /*@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (!ChatFilter.block_chat_until_move) {
            return;
        }
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        ChatFilter.hasntMoved.remove(event.getPlayer().getUniqueId());
    }*/

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (!player.getName().equals("Vaquxine") && (player.hasPermission("arkkit.chat_filter.admin") || player.hasPermission("simplechat.admin") || player.hasPermission("arkhamnetwork.joker_perks"))) {
            return;
        }
        String m = e.getMessage().toLowerCase();
        if (m.startsWith("/bounty ") && ChatFilter.isChatHalted()) {
            player.sendMessage(ChatColor.RED + "Chat has been temporary disabled by an administrator.");
            player.sendMessage(ChatColor.GRAY + "Please be patient, /bounty will be re-enabled promptly.");
            e.setCancelled(true);
            return;
        }
        if (m.startsWith("/msg ") || m.startsWith("/message ") || m.startsWith("/tell ") || m.startsWith("/whisper ") || m.startsWith("/w ") || m.startsWith("/emsg ") || m.startsWith("/m ") || m.startsWith("/t ") || m.startsWith("/etell ") || m.startsWith("/ewhisper ") || m.startsWith("/r ") || m.startsWith("/repl ") || m.startsWith("/reply ") || m.startsWith("/f desc ")) {
            if (m.startsWith("/r ") || m.startsWith("/repl ") || m.startsWith("/reply ") || m.startsWith("/f desc ")) {
                e.setMessage(ChatUtils.getFilteredSwearMessage(e.getMessage(), true));
                e.setMessage(ChatUtils.getFilteredUppercaseMessage(e.getMessage()));
            } else {
                String cmdString = e.getMessage().split(" ")[0] + " " + e.getMessage().split(" ")[1];
                e.setMessage(cmdString + " " + ChatUtils.getFilteredUppercaseMessage(ChatUtils.getFilteredSwearMessage(e.getMessage().replace(cmdString, ""), true)));
            }
        }
    }
}

