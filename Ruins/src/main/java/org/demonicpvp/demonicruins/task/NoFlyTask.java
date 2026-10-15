package org.demonicpvp.demonicruins.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFlyTask extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : Bukkit.getWorld("world_ruins").getPlayers()) {
            if(!player.isOp() && player.isFlying()) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }
    }
}
