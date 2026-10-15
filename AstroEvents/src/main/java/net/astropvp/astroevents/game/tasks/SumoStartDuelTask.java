package net.astropvp.astroevents.game.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.games.SumoMiniGame;
import net.astropvp.astroevents.game.players.SumoPlayer;
import net.astropvp.astroevents.utils.Utils;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SumoStartDuelTask extends BukkitRunnable {

    private final SumoMiniGame miniGame;
    private int countdown;
    private final SumoPlayer left;
    private final SumoPlayer right;

    public SumoStartDuelTask(SumoMiniGame miniGame) {
        this.miniGame = miniGame;
        this.countdown = 5;
        this.left = this.miniGame.getDuelPlayers().getLeft();
        this.right = this.miniGame.getDuelPlayers().getRight();
    }

    @Override
    public void run() {
        if (this.miniGame.getMiniGameState() != MiniGameState.IN_PROGRESS) {
            this.cancel();
            return;
        }

        if (this.miniGame.getDuelPlayers().getLeft() == null || miniGame.getDuelPlayers().getRight() == null) {
            this.cancel();
            return;
        }

        if (this.countdown <= 0) {
            Utils.sendTitle(left.getPlayer(), CC.YellowB + "GO!", "", 10, 40, 10);
            Utils.sendTitle(right.getPlayer(), CC.YellowB + "GO!", "", 10, 40, 10);
            this.cancel();
            return;
        }

        left.getPlayer().teleport(this.miniGame.getFightLocationOne(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        right.getPlayer().teleport(this.miniGame.getFightLocationTwo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);

        String color = countdown >= 4 ? CC.GreenB : CC.RedB;

        Utils.sendTitle(left.getPlayer(), color + countdown, "", 10, 40, 10);
        Utils.sendTitle(right.getPlayer(), color + countdown, "", 10, 40, 10);

        this.countdown--;
    }


}
