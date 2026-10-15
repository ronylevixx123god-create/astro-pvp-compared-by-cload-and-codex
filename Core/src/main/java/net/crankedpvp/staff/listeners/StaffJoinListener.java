package net.crankedpvp.staff.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Collectors;

public class StaffJoinListener implements Listener {

    private final AstroCore staffPlugin;
    public StaffJoinListener(AstroCore plugin) {
        this.staffPlugin = plugin;
    }

    @EventHandler
    public void staffPlayerJoin(PlayerJoinEvent event) {
        if(event.getPlayer() == null) return;
        if(!event.getPlayer().hasPermission("astro.staff")) return;
        if(event.getPlayer().hasPermission("astro.admin")) return;
        this.staffPlugin.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("astro.staff")).collect(Collectors.toList()).forEach(player -> {
            player.sendMessage(CC.translate("&5[Staff] &a+" + event.getPlayer().getName()));
        });
    }

    @EventHandler
    public void staffLeaveJoin(PlayerQuitEvent event) {
        if(event.getPlayer() == null) return;
        if(!event.getPlayer().hasPermission("astro.staff")) return;
        if(event.getPlayer().hasPermission("astro.admin")) return;
        this.staffPlugin.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("astro.staff")).collect(Collectors.toList()).forEach(player -> {
            player.sendMessage(CC.translate("&5[Staff] &c-" + event.getPlayer().getName()));
        });
    }
}
