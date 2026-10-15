package net.astropvp.astroevents.game.tasks;

import lombok.AllArgsConstructor;
import net.astropvp.astroevents.game.MiniGameManager;
import net.astropvp.astroevents.game.MiniGameRegister;
import net.astropvp.astroevents.game.MiniGameType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class AutoMiniGameTask extends BukkitRunnable {

    private final MiniGameManager miniGameManager;
    private final MiniGameRegister miniGameRegister;

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Bukkit.getConsoleSender().sendMessage("&6[AstroEvents] Not enough online players to start the event");
            this.miniGameManager.setLastMiniGameTime(System.currentTimeMillis());
            return;
        }

        MiniGameType nextMiniGame = this.miniGameManager.getNextMiniGame();

        this.miniGameRegister.getMiniGame(nextMiniGame).startMiniGameCountDown();
        this.miniGameManager.setLastMiniGameTime(System.currentTimeMillis());
        this.miniGameManager.setPreviousMiniGame(nextMiniGame);
    }
}