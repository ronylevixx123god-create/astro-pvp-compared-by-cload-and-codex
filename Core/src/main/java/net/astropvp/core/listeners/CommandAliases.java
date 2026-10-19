package net.astropvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class CommandAliases implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        String message = e.getMessage();
        String cmd = message.toLowerCase(Locale.ROOT);
        int argumentStart = message.indexOf(' ');
        String arguments = argumentStart < 0 ? "" : message.substring(argumentStart);
        if (cmd.equalsIgnoreCase("/f upgrades")) {
            e.setMessage("/f upgrade");
            return;
        }


        if (cmd.contains(" ")) {
            cmd = cmd.split(" ")[0];
        }

        if (cmd.equalsIgnoreCase("/f top")) {
            e.setMessage("/f topp");
            return;
        }
        if (cmd.equals("/mask")) {
            e.setMessage("/cmask" + arguments);
            return;
        }
        if (cmd.equals("/tpyes")) {
            e.setMessage("/tpaccept" + arguments);
            return;
        }
        if (cmd.equals("/tpno")) {
            e.setMessage("/tpdeny" + arguments);
            return;
        }

        if (cmd.equals("/f unclaim all")) {
            e.setMessage("/f unclaimall");
            return;
        }

        if (cmd.equals("/customblocks") || cmd.equals("/customblock")) {
            e.setMessage("/cblocks view");
            return;
        }
        if (cmd.equals("/craft")) {
            e.setMessage("/workbench" + arguments);
            return;
        }
        if (cmd.equals("/elightning") || cmd.equals("/smite") || cmd.equals("/esmite") || cmd.equals("/strike") || cmd.equals("/estrike") || cmd.equals("/shock") || cmd.equals("/eshock") || cmd.equals("/thor") || cmd.equals("/ethor")) {
            e.setMessage("/lightning" + arguments);
            return;
        }
        if (cmd.equals("/eat")) {
            e.setMessage("/feed" + arguments);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPreProcessCommand(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        String message = e.getMessage();
        if (message == null || message.isEmpty()) return;
        int argumentStart = message.indexOf(' ');
        String command = (argumentStart < 0 ? message : message.substring(0, argumentStart)).toLowerCase(Locale.ROOT);
        if (command.equals("/lootbox")) {
            e.setMessage("/mc" + (argumentStart < 0 ? "" : message.substring(argumentStart)));
        }
    }
}
