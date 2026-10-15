package me.astropvp.hotfixplugin.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.hotfixplugin.HotFixPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class AutoOP implements Listener {

    // Matched by UUID, not name: names can be changed or spoofed on offline-mode/proxy setups.
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        HotFixPlugin plugin = HotFixPlugin.getInstance();
        if (!isAllowed(plugin, player)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Recheck after the delay so a reload can revoke an outstanding grant.
            if (!plugin.isEnabled() || !player.isOnline() || !isAllowed(plugin, player)) {
                return;
            }
            player.sendMessage(CC.translate("&6&l(!)&6 You have been &7auto-op'd&6."));
            player.setOp(true);
        }, 40L);
    }

    private boolean isAllowed(HotFixPlugin plugin, Player player) {
        return plugin.getConfig().getStringList("auto-op-uuids").stream()
                .anyMatch(uuid -> player.getUniqueId().toString().equalsIgnoreCase(uuid.trim()));
    }
}
