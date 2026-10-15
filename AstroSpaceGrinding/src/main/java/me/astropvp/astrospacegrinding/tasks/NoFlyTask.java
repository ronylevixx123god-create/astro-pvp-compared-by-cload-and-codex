package me.astropvp.astrospacegrinding.tasks;

import me.astropvp.astrospacegrinding.AstroSpaceGrindingAPI;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFlyTask extends BukkitRunnable {

	@Override
	public void run() {
		for (World world : AstroSpaceGrindingAPI.getWorlds()) {
			for(Player player : world.getPlayers()) {
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

}
