package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;

public class Deathbringer extends CustomEnchantment {
    public Deathbringer() {
        super("Deathbringer", GeneralUtil.armor, 9);
        this.max = 3;
        this.base = 25.0;
        this.interval = 7.0;
        Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
            @Override
            public void run() {
                Deathbringer.this.heroicVersion = EnchantmentAPI.getEnchantment((String)"Planetary Deathbringer");
            }
        }, 2L);
        Bukkit.getPluginManager().registerEvents((Listener) new DeathbringEvents(), (Plugin) Enchantments.getInstance());
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        DeathbringEvents.doubleDamage.add(player);
        DeathbringEvents.playerLevels.put(player, enchantLevel);
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        DeathbringEvents.doubleDamage.remove(player);
        DeathbringEvents.playerLevels.remove(player);
    }

    private static class DeathbringEvents implements Listener {
        private static HashSet<Player> doubleDamage;
        private static HashMap<Player, Integer> playerLevels;

        static {
            DeathbringEvents.doubleDamage = new HashSet<Player>();
            DeathbringEvents.playerLevels = new HashMap<Player, Integer>();
        }

        @EventHandler
        public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
            double chance;
            double random;
            Entity damage = e.getDamager();
            if ((damage instanceof Projectile && doubleDamage.contains(((Projectile)damage).getShooter()) || doubleDamage.contains(damage)) && (random = Math.random()) < (chance = 0.1 * (double)(playerLevels.containsKey(damage instanceof Projectile ? ((Projectile)damage).getShooter() : damage) ? playerLevels.get(damage instanceof Projectile ? ((Projectile)damage).getShooter() : damage) : 1))) {
                e.setDamage(e.getDamage() * 1.35);
            }
        }

        @EventHandler
        public void onPlayerQuit(final PlayerQuitEvent e) {
            DeathbringEvents.doubleDamage.remove(e.getPlayer());
            DeathbringEvents.playerLevels.remove(e.getPlayer());
        }
    }
}
