package org.arkhamnetwork.enchantments.enchantments.axes;

import java.text.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.*;

final class HexListener
        implements Listener {
    private DecimalFormat df = new DecimalFormat("#.##");

    HexListener() {
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        LivingEntity damager;
        if (e.getDamager() instanceof LivingEntity && Hex.isHexed(damager = (LivingEntity)e.getDamager()) && e.getDamage() > 0.0) {
            double damage = Math.min(e.getDamage(), (double)(5 + Math.max(0, Hex.getHexLevel(damager) - 2)));
            damager.damage(damage);
            if (damager instanceof Player) {
                ((Player)damager).sendMessage(ChatColor.DARK_PURPLE + "* HEX [" + ChatColor.RED + this.df.format(damage) + ChatColor.DARK_PURPLE + " DMG] *");
            }
        }
    }
}
