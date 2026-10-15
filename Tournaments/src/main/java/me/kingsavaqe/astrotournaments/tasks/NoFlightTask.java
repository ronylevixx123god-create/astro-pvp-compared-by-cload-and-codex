package me.kingsavaqe.astrotournaments.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFlightTask extends BukkitRunnable {

    @Override
    public void run() {
        for(Player player : Bukkit.getWorld("tournament_world").getPlayers()) {
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
