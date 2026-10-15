package net.astropvp.astroevents.game.tasks;

import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.games.ParkourMiniGame;
import net.astropvp.astroevents.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

public class ParkourTimeTask extends BukkitRunnable {

    private final ParkourMiniGame miniGame;

    public ParkourTimeTask(ParkourMiniGame miniGame) {
        this.miniGame = miniGame;
    }

    @Override
    public void run() {

        if (miniGame.getMiniGameState() != MiniGameState.IN_PROGRESS || miniGame.getParticipants().isEmpty()) {
            this.cancel();
            return;
        }

        String time = Utils.getTimeFromSecsToMins((System.currentTimeMillis() - miniGame.getStartTime()) / 1000);

        miniGame.getParticipants().forEach((player, parkourPlayer) -> {
            Utils.sendActionBarMessage(parkourPlayer.getPlayer(), Utils.color(miniGame.getParkourTimeMessage().replace("{time}", time)));
        });
    }
}