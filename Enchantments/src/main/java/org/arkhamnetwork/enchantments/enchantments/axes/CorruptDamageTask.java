package org.arkhamnetwork.enchantments.enchantments.axes;

import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.cosmicpvp.cosmicutils.utils.*;

public class CorruptDamageTask extends BukkitRunnable
{
    Player p;
    long durationTicks;
    double dmgValue;

    public CorruptDamageTask(final Player p, final long durationTicks, final double dmgAmount) {
        this.p = p;
        this.durationTicks = durationTicks;
        this.dmgValue = dmgAmount;
    }

    @Override
    public void run() {
        this.dmgPlayer(this.p, this.dmgValue);
    }

    private void dmgPlayer(final Player p, final double amount) {
        if (p.getHealth() <= 0.0 || p.isDead()) {
            return;
        }
        p.damage(amount);
        WorldUtil.sendWorldEffect(p.getLocation().add(0.0, 1.0, 0.0).getBlock(), Material.REDSTONE_WIRE.getId(), 2001);
    }
}
