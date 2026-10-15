package org.arkhamnetwork.enchantments.enchantments.axes;

import org.bukkit.scheduler.*;
import org.bukkit.*;

final class BleedTask extends BukkitRunnable
{
    BleedEvent event;
    int x;

    public BleedTask(final BleedEvent e) {
        this.x = 0;
        this.event = e;
    }

    @Override
    public void run() {
        if (this.event.ticks <= this.x) {
            this.event.stop();
            return;
        }
        ++this.x;
        if (this.event.player != null && this.event.player.hasMetadata("ce_bleeding")) {
            this.event.player.damage(this.event.dmg);
            this.event.player.playSound(this.event.player.getLocation(), Sound.HURT_FLESH, 1.0f, 0.75f);
        }
    }
}
