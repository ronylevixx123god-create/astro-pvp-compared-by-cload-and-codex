package me.ifamasssxd.cosmicfactionbanners.listeners;

import org.bukkit.event.player.*;
import org.bukkit.event.*;

public class CommandListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreProcesS(final PlayerCommandPreprocessEvent event) {
        final String lower = event.getMessage().toLowerCase();
        if (lower.startsWith("/f banner")) {
            event.setMessage(lower.replace("/f banner", "/fbanner"));
        }
        else if (lower.startsWith("/f recall") || lower.startsWith("/f assist")) {
            event.setMessage(lower.replace("/f recall", "/frecall").replace("/f assist", "/frecall"));
        }
        else if (lower.startsWith("/f setbanner")) {
            event.setMessage(lower.replace("/f setbanner", "/fsetbanner"));
        }
    }
}
