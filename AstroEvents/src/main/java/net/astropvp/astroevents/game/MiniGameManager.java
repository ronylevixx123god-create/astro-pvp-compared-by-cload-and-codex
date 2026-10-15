package net.astropvp.astroevents.game;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.tasks.AutoMiniGameTask;
import net.astropvp.astroevents.utils.Utils;


@Getter
@Setter
public class MiniGameManager {

    private static final long TIME_BETWEEN_EVENTS_IN_SECS = 7200;

    private long lastMiniGameTime;
    private final MiniGameRegister miniGameRegister;
    private final AstroEvents plugin;
    private AutoMiniGameTask autoMiniGameTask;
    private MiniGameType previousMiniGame;

    public MiniGameManager(AstroEvents plugin) {
        this.miniGameRegister = plugin.getMiniGameRegister();
        this.previousMiniGame = MiniGameType.SUMO;
        this.plugin = plugin;
    }

    public void startAutoMiniGameTask() {
        this.autoMiniGameTask = new AutoMiniGameTask(this, this.miniGameRegister);
        this.autoMiniGameTask.runTaskTimer(plugin, TIME_BETWEEN_EVENTS_IN_SECS * 20, TIME_BETWEEN_EVENTS_IN_SECS * 20);
        this.lastMiniGameTime = System.currentTimeMillis();
    }

    public MiniGameType getNextMiniGame() {
        return previousMiniGame == MiniGameType.PARKOUR ? MiniGameType.SUMO : MiniGameType.PARKOUR;
    }

    public long getTimeInMsUntilNextMiniGame() {
        return (lastMiniGameTime + TIME_BETWEEN_EVENTS_IN_SECS * 1000) - System.currentTimeMillis();
    }

    public String getTimeUntilNextMiniGame() {
        return Utils.getSecondsAsString(this.getTimeInMsUntilNextMiniGame() / 1000);
    }
}
