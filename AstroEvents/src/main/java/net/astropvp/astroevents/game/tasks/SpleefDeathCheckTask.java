package net.astropvp.astroevents.game.tasks;

import lombok.AllArgsConstructor;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.games.SpleefMiniGame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class SpleefDeathCheckTask extends BukkitRunnable {

    private final SpleefMiniGame miniGame;


    @Override
    public void run() {
        if (this.miniGame.getMiniGameState() != MiniGameState.IN_PROGRESS) {
            this.cancel();
            return;
        }

        this.miniGame.getParticipants().forEach((uuid, spleefPlayer) -> {
            if (isPlayerInWater(spleefPlayer.getPlayer())) {
                this.miniGame.leaveMiniGame(spleefPlayer.getPlayer());
            }
        });
    }

    private boolean isPlayerInWater(Player spleefPlayer) {
        Material material = spleefPlayer.getLocation().getBlock().getType();
        return material == Material.WATER || material == Material.STATIONARY_WATER;
    }
}
