package org.arkhamnetwork.enchantments.enchantments.axes;

import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;

final class BleedEvent
{
    protected Player player;
    protected double dmg;
    protected int ticks;
    protected BleedTask bleedTask;

    BleedEvent(final Player p, final double damage_per_interval, final int intervals) {
        this.player = p;
        this.dmg = damage_per_interval;
        this.ticks = intervals;
    }

    public void start() {
        (this.bleedTask = new BleedTask(this)).runTaskTimer((Plugin)Enchantments.getInstance(), 20L, 35L);
        this.player.setMetadata("ce_bleeding", new FixedMetadataValue((Plugin)Enchantments.getInstance(), this.bleedTask.getTaskId()));
    }

    public void stop() {
        this.player.removeMetadata("ce_bleeding", (Plugin)Enchantments.getInstance());
        this.bleedTask.cancel();
    }
}
