package org.arkhamnetwork.enchantments.tasks;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerPotionEffectPersistTask implements Runnable {
    boolean statusPlusEnabled;

    public PlayerPotionEffectPersistTask() {
        this.statusPlusEnabled = false;
    }

    @Override
    public void run() {
        for (final Map.Entry<UUID, PlayerPotionEffects> trackedEffects : PlayerPotionEffectListener.getPlayerPotionEffectTracker().entrySet()) {
            final Player p = trackedEffects.getValue().getBukkitPlayer();
            if (p != null && !p.isDead() && p.isValid()) {

                if (p.getLocation().getWorld().getName().equals("events_world")) {
                    continue;
                }
                if (p.getLocation().getWorld().getName().equals("world_outpost_pluto")) {
                    continue;
                }
                if (p.hasMetadata("armorRescanRunning")) {
                    continue;
                }
                if (p.hasMetadata("lastDuelEnd") && MinecraftServerUtils.getCurrentServerTick() < p.getMetadata("lastDuelEnd").get(0).asLong() + 80L) {
                    continue;
                }
                final boolean worldChange = p.hasMetadata("lastWorldChange") && System.currentTimeMillis() - p.getMetadata("lastWorldChange").get(0).asLong() < 2500L;
                final boolean recentDeath = p.hasMetadata("lastPlayerDeath") && System.currentTimeMillis() - p.getMetadata("lastPlayerDeath").get(0).asLong() < 1000L;
                if (worldChange || recentDeath) {
                    continue;
                }
                trackedEffects.getValue().applyPotionEffects(true);
            }
        }
    }
}
