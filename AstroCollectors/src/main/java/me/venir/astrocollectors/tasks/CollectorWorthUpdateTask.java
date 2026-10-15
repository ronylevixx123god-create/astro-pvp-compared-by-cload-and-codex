package me.venir.astrocollectors.tasks;

import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import org.bukkit.scheduler.BukkitRunnable;

public class CollectorWorthUpdateTask extends BukkitRunnable {

    private final CollectorManager collectorManager;

    public CollectorWorthUpdateTask() {
        this.collectorManager = CollectorManager.DELEGATE;
    }

    @Override
    public void run() {
        this.collectorManager.forEach(Collector::update);
    }
}
