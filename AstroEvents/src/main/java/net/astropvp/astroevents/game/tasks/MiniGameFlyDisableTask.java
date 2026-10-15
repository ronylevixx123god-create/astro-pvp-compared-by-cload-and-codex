package net.astropvp.astroevents.game.tasks;

import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MiniGameFlyDisableTask extends BukkitRunnable {

        private final MiniGame<?> miniGame;

        public MiniGameFlyDisableTask(MiniGame<?> miniGame) {
                this.miniGame = miniGame;
        }

        @Override
        public void run() {
                if (this.miniGame.getMiniGameState() == MiniGameState.NOT_STARTED) {
                        this.cancel();
                        return;
                }

                this.miniGame.getParticipants().forEach((uuid, t) -> {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player.getAllowFlight() && !player.isOp()) {
                                player.setAllowFlight(false);
                        }
                        if (player.isFlying() && !player.isOp()) {
                                player.setFlying(false);
                        }
                });
        }
}
