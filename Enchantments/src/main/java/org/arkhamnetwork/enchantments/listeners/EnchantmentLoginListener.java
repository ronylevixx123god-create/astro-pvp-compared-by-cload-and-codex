package org.arkhamnetwork.enchantments.listeners;

import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.enchanting.EListener;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.Map;

public class EnchantmentLoginListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    return;
                }
                for (final PotionEffect e : p.getActivePotionEffects()) {
                    p.removePotionEffect(e.getType());
                }
                for (final Map.Entry<CustomEnchantment, Integer> data : EListener.getValidEnchantments((LivingEntity) p, Lists.newArrayList(p.getInventory().getArmorContents())).entries()) {
                    data.getKey().applyUnequipEffect(p, (int) data.getValue());
                }
                Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
                    @Override
                    public void run() {
                        for (final Map.Entry<CustomEnchantment, Integer> data : EListener.getValidEnchantments((LivingEntity) p, Lists.newArrayList(p.getInventory().getArmorContents())).entries()) {
                            data.getKey().applyEquipEffect(p, (int) data.getValue());
                        }
                    }
                }, 8L);
            }
        }, 2L);
    }
}
