package me.astropvp.astrospacegrinding.tasks;

import de.inventivegames.particle.ParticleEffect;
import lombok.SneakyThrows;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingAPI;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingPlugin;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.astrospacegrinding.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnMobsRepeatingTask extends BukkitRunnable {

	private int ticks;
	public SpawnMobsRepeatingTask() {
		this.ticks = 0;
	}
	
	@SneakyThrows
	@Override
	public void run() {
		ticks++;
		int delay = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spawnDelayInSeconds");
		if(ticks % delay == 0) {
			for(String value : AstroSpaceGrindingAPI.getBossLocations()) {
				Object[] values = AstroSpaceGrindingAPI.getValues(value);
				MonsterType type = (MonsterType) values[0];
				Location location = (Location) values[1];

				try {
					ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), location, 2f, 2f, 2f, 0.05f, 100);
				} catch (Exception e) {
					e.printStackTrace();
				}

				int playersInChunk = 0;
				int mobsAmount = 0;
				for (Entity entity : location.getWorld().getNearbyEntities(location, 16, 16, 16)) {
					if (entity.getType() == EntityType.PLAYER) {
						playersInChunk++;
					} else if (entity instanceof LivingEntity) {
						mobsAmount++;
					}
				}
				if (playersInChunk == 0) {
					continue;
				}
				if (mobsAmount >= 8) {
					continue;
				}

				type.getInstance().spawn(location);
			}
		}
		
	}

}
