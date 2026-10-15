package org.arkhamnetwork.enchantments.listeners;

import lombok.Getter;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class AutoBlessListener implements Listener {

    public static List<UUID> blessedPlayers = new ArrayList<>();

    @EventHandler
    public void onPotionEffectInKothSet(PotionEffectAddEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = ((Player) event.getEntity());
        if (blessedPlayers.contains(player.getUniqueId())) {
            if (getDebuffsList().contains(event.getEffect().getType())) {
                PlayerPotionEffects.untrackPotionEffect(player, event.getEffect().getType());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void clarityEnchant(PotionEffectAddEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = ((Player) event.getEntity());
        if (player.hasMetadata("clarityEnchant")) {
            if (event.getEffect().getType() == PotionEffectType.BLINDNESS) {
                event.setCancelled(true);
            }
        }

    }

    @Getter
    private static final Set<PotionEffectType> debuffsList = new HashSet<>(Arrays.asList(
            PotionEffectType.BLINDNESS,
            PotionEffectType.CONFUSION,
            PotionEffectType.HARM,
            PotionEffectType.SLOW,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.WEAKNESS,
            PotionEffectType.WITHER,
            PotionEffectType.POISON
    ));
}
