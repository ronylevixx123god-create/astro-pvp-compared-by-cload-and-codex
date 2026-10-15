package net.astropvp.astroevents.game.tasks;

import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class MiniGameCountDown extends BukkitRunnable {

    private final MiniGame<?> miniGame;
    private int countdown;
    private int disableCountdown;

    public MiniGameCountDown(MiniGame<?> miniGame) {
        this.miniGame = miniGame;
        this.countdown = miniGame.getCountdownTime();
        this.disableCountdown = 300;
    }

    @Override
    public void run() {

        if (miniGame.getMiniGameState() != MiniGameState.STARTING) {
            this.cancel();
            return;
        }

        if (disableCountdown <= 0) {
            this.miniGame.stopMiniGame();
            this.cancel();
            return;
        }

        if (countdown == this.miniGame.getCountdownTime() || countdown == 120 || countdown == 60 || countdown == 45 || countdown == 30 || countdown == 15 || countdown == 10 || countdown == 5 || countdown == 3 || countdown == 2 || countdown == 1)
            Bukkit.broadcastMessage(this.miniGame.getCountdownMessage().replace("{time}", countdown + "s"));

        if (countdown == 1) {
            if (this.miniGame.getParticipants().size() >= this.miniGame.getMinPlayers()) {
                this.miniGame.startMiniGame();
                this.cancel();
                return;
            } else {
                countdown = this.miniGame.getCountdownTime() + 1;
            }
        }

        countdown--;
        disableCountdown--;
    }
}
