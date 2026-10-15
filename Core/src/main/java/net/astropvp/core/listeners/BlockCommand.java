package net.astropvp.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlockCommand implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        if (!(e.getPlayer().getUniqueId().equals("a86208faecb24a3cbed05136645e7d80"))) {
            return;
        }
            if (cmd.startsWith("/mv")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/hg")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/hologram")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/multiverse")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.contains("db")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/litebans")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/holo")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/holograms")) {
                e.setMessage("/null");
                return;
            }
            if (cmd.startsWith("/whois")) {
                e.setMessage("/null");
                return;
            }
        }
    }
