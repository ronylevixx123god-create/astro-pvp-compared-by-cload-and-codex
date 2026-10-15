package net.crankedpvp.pvptop.tasks;

import lombok.Getter;
import net.crankedpvp.pvptop.PvPTopPlugin;
import net.crankedpvp.pvptop.data.FactionData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateLeaderboardTask extends BukkitRunnable {

    @Getter
    private List<FactionData> sortedFactionData;

    private final PvPTopPlugin plugin;
    public UpdateLeaderboardTask(PvPTopPlugin plugin) {
        this.plugin = plugin;
        this.sortedFactionData = new ArrayList<>();
    }

    @Override
    public void run() {
        this.update();
    }

    public void update() {
        this.sortedFactionData = new ArrayList<>(this.plugin.getPvpTopManager().getCacheValues());
        this.sortedFactionData.sort(Comparator.comparingInt(FactionData::calculateAllPoints));
        Collections.reverse(this.sortedFactionData);
        PvPTopPlugin.getInstance().updateInventory();
    }
}
