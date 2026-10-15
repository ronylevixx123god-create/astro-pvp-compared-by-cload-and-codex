package com.cosmicpvp.cosmicutils.utils;


import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.cache.CachedPlayer;
import com.cosmicpvp.cosmicutils.cache.cooldown.CurrentCooldown;
import org.bukkit.entity.Player;

public class CooldownUtils {

    public static void registerCooldown(Player player, CurrentCooldown cache) {
        CachedPlayer cachedPlayer = CosmicUtils.get().getCacheManager().getCachedPlayer(player);
        cachedPlayer.registerCache(cache);
    }

    public static String getFormattedCooldown(Player player, String cache) {
        CachedPlayer cachedPlayer = CosmicUtils.get().getCacheManager().getCachedPlayer(player);
        return ((CurrentCooldown)cachedPlayer.getCache(cache)).getFormattedTime();
    }

    public static boolean isCooldownActive(Player player, String cache) {
        CachedPlayer cachedPlayer = CosmicUtils.get().getCacheManager().getCachedPlayer(player);
        return cachedPlayer.hasCache(cache) && ((CurrentCooldown)cachedPlayer.getCache(cache)).isActive();
    }

    public static void removeCooldown(Player player, String cacheName) {
        CachedPlayer cachedPlayer = CosmicUtils.get().getCacheManager().getCachedPlayer(player);
        cachedPlayer.removeCache(cacheName);
    }
}
