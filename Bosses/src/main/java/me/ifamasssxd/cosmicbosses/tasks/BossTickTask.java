package me.ifamasssxd.cosmicbosses.tasks;

import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.scheduler.BukkitRunnable;

public class BossTickTask extends BukkitRunnable {
    public void run() {
        for (Boss boss : Boss.bosses.values()) {
            if (boss.isBossSpawned())
                boss.onTick();
        }
    }
}
