package me.astropvp.astrospawners.ftop;

import org.bukkit.scheduler.BukkitRunnable;

public class RecalculationTask extends BukkitRunnable {
    public void run() {
        FactionsTopAPI.recalculateFactionsTop();
    }
}
