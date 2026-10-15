package me.astropvp.hotfixplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFly extends BukkitRunnable {

    @Override
    public void run() {
        for(Player player : Bukkit.getWorld("event").getPlayers()) {
            if(player.isOp()) {
                continue;
            }
            if(player.isFlying()) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }

    }

}
