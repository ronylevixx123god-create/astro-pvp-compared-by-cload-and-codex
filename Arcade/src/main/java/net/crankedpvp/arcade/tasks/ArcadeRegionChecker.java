package net.crankedpvp.arcade.tasks;

import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import net.crankedpvp.arcade.ArcadeAPI;
import net.crankedpvp.arcade.ArcadePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArcadeRegionChecker extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getWorld(ArcadeAPI.WORLD_NAME).getPlayers()) {

            if (WorldGuardUtils.getRegionName(player.getLocation()).equals(ArcadeAPI.SAFEZONE_REGION_NAME) && ArcadeAPI.inArcadeMatch(player.getUniqueId())) {
                ArcadePlugin.getInstance().getArcadeManager().endMatch(player.getUniqueId());
                if (CooldownUtils.isCooldownActive(player, "arcadeGracePeriod")) {
                    CooldownUtils.removeCooldown(player, "arcadeGracePeriod");
                }
            }
        }
    }
}
