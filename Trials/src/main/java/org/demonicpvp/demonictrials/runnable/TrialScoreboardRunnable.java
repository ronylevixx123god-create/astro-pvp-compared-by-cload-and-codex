package org.demonicpvp.demonictrials.runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonictrials.manager.TrialManager;

public class TrialScoreboardRunnable extends BukkitRunnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            TrialManager.getTrial(p).ifPresent(trial -> trial.updateScoreboard(p));
        });
    }
}
