package org.arkhamnetwork.enchantments.enchantments.armor;

import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnderWalkerListener implements Listener {
    public static List<UUID> enderWalkerPlayers = new ArrayList<>();
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPotionAdd(PotionEffectAddEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!enderWalkerPlayers.contains(event.getEntity().getUniqueId())) return;
        if (event.getEffect().getType().getName().equals(PotionEffectType.POISON.getName()) || event.getEffect().getType().getName().equals(PotionEffectType.WITHER.getName())) {
            PlayerPotionEffects.untrackPotionEffect((Player) event.getEntity(), event.getEffect().getType());
            event.setCancelled(true);
            if (Math.random() < 0.1) {
                GeneralUtil.healPlayer(event.getEntity(), 1);
            }
        }
    }
}
