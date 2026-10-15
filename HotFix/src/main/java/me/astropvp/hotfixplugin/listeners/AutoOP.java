package me.astropvp.hotfixplugin.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.hotfixplugin.HotFixPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class AutoOP implements Listener {

    // Matched by UUID, not name: names can be changed or spoofed on offline-mode/proxy setups.
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        List<String> list = HotFixPlugin.getInstance().getConfig().getStringList("auto-op-uuids");
        if (!list.contains(player.getUniqueId().toString())) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(HotFixPlugin.getInstance(), () -> {
            if (!player.isOnline()) {
                return;
            }
            player.sendMessage(CC.translate("&6&l(!)&6 You have been &7auto-op'd&6."));
            player.setOp(true);
        }, 40L);
    }

    @EventHandler
    public void onPlayerJoinEvent2(PlayerJoinEvent e) {
        if (e.getPlayer().isOp()) {
            e.getPlayer().setOp(false);
        }

    }
}
