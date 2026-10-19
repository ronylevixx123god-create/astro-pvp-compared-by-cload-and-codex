package me.astropvp.outpost.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFlyTask extends BukkitRunnable {

    @Override
    public void run() {
        enforceNoFlight(Bukkit.getWorld("world_outpost_pluto"));
        enforceNoFlight(Bukkit.getWorld("world_outpost_mars"));
    }

    private void enforceNoFlight(World world) {
        if (world == null) return;
        for (Player player : world.getPlayers()) {
            if (player.isOp()) continue;
            if (player.isFlying()) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }
    }
}
