package net.astropvp.astroevents.game.tasks;

import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.games.SumoMiniGame;
import net.astropvp.astroevents.game.players.SumoPlayer;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class SumoDeathCheckTask extends BukkitRunnable {

    private final SumoMiniGame miniGame;

    public SumoDeathCheckTask(SumoMiniGame miniGame) {
        this.miniGame = miniGame;
    }

    @Override
    public void run() {
        if (this.miniGame.getMiniGameState() != MiniGameState.IN_PROGRESS) {
            this.cancel();
            return;
        }

        if (this.miniGame.getDuelPlayers().getLeft() == null || miniGame.getDuelPlayers().getRight() == null) return;
        if (this.miniGame.getDuelPlayers().getLeft().getPlayer() == null || miniGame.getDuelPlayers().getRight().getPlayer() == null) return;
        if (!this.miniGame.getDuelPlayers().getLeft().getPlayer().isOnline() || !this.miniGame.getDuelPlayers().getLeft().getPlayer().isOnline()) return;

        if (this.isPlayerInWater(this.miniGame.getDuelPlayers().getLeft())) {
            this.miniGame.choseWinner(this.miniGame.getDuelPlayers().getRight().getPlayer(), this.miniGame.getDuelPlayers().getLeft().getPlayer());
        }

        if (this.isPlayerInWater(this.miniGame.getDuelPlayers().getRight())) {
            this.miniGame.choseWinner(this.miniGame.getDuelPlayers().getLeft().getPlayer(), this.miniGame.getDuelPlayers().getRight().getPlayer());
        }
    }

    private boolean isPlayerInWater(SumoPlayer sumoPlayer) {
        Material material = sumoPlayer.getPlayer().getLocation().getBlock().getType();
        return material == Material.WATER || material == Material.STATIONARY_WATER;
    }
}