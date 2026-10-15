package net.astropvp.astroevents.game.tasks;

import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameState;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpectatorTeleportBackTask  extends BukkitRunnable {

    private final MiniGame<?> game;

    public SpectatorTeleportBackTask(MiniGame<?> game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (this.game.getMiniGameState() == MiniGameState.NOT_STARTED) {
            this.cancel();
            return;
        }

        this.game.getSpectators().forEach(spectator -> {
            if (spectator.getPlayer() == null) {
                return;
            }
            if (!spectator.getPlayer().getWorld().getName().equals(this.game.getGameWorld().getName())) return;
            if (this.game.getSpawnLocation().distance(spectator.getPlayer().getLocation()) > 200)
                    spectator.getPlayer().teleport(this.game.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        });
    }
}