package org.arkhamnetwork.enchantments.enchantments.armor;

import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import com.cosmicpvp.cosmicutils.utils.*;

final class RegenerationTask extends BukkitRunnable
{
    Player p;
    long durationTicks;
    long delayTicks;
    double healValue;

    RegenerationTask(final Player p, final long durationTicks, final long delayTicks, final double healAmount) {
        this.p = p;
        this.durationTicks = durationTicks;
        this.delayTicks = delayTicks;
        this.healValue = healAmount;
    }

    @Override
    public void run() {
        this.healPlayer(this.p, this.healValue);
    }

    private void healPlayer(final Player p, final double amount) {
        if (p.getHealth() == p.getMaxHealth() || p.isDead()) {
            return;
        }
        GeneralUtil.healPlayer(p, amount);    }
}
