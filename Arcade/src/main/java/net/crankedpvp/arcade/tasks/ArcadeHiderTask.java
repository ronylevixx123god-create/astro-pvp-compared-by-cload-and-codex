package net.crankedpvp.arcade.tasks;

import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import net.crankedpvp.arcade.ArcadeAPI;
import net.crankedpvp.arcade.ArcadePlugin;
import net.crankedpvp.arcade.struct.ArcadeMatch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArcadeHiderTask extends BukkitRunnable {
    @Override
    public void run() {
        for (ArcadeMatch arcadeMatch : ArcadePlugin.getInstance().getArcadeManager().getMatches()) {
            Player player1 = Bukkit.getPlayer(arcadeMatch.getPlayers().getLeft());
            Player player2 = Bukkit.getPlayer(arcadeMatch.getPlayers().getRight());
            for (Player player : Bukkit.getWorld(ArcadeAPI.WORLD_NAME).getPlayers()) {
                if (player.getUniqueId() == player1.getUniqueId() || player.getUniqueId() == player2.getUniqueId())
                    continue;
                if(player.hasPermission("arcade.see")) continue;
                if(EssentialsUtils.isVanished(player)) continue;
                if (player.canSee(player1)) player.hidePlayer(player1);
                if (player.canSee(player2)) player.hidePlayer(player2);
                if (player1.canSee(player)) player1.hidePlayer(player);
                if (player2.canSee(player)) player2.hidePlayer(player);
            }
        }
    }
}
