package me.astropvp.outpost.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFlyTask extends BukkitRunnable {

    @Override
    public void run() {
        for(Player player : Bukkit.getWorld("world_outpost_pluto").getPlayers()) {
            if(player.isOp()) {
                continue;
            }
            if(player.isFlying()) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }
        for(Player player : Bukkit.getWorld("world_outpost_mars").getPlayers()) {
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
