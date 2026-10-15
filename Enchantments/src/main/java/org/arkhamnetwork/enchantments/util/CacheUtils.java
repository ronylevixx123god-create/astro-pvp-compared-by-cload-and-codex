package org.arkhamnetwork.enchantments.util;

import com.cosmicpvp.cosmicutils.cache.CacheManager;
import org.bukkit.entity.Player;

public class CacheUtils {

    public static RageStacksCache getRageStackCache(Player player) {
        RageStacksCache rageStacksCache = (RageStacksCache) CacheManager.getInstance().getCachedPlayer(player).getCache("rage_stacks");

        if (rageStacksCache == null) CacheManager.getInstance().getCachedPlayer(player).registerCache(new RageStacksCache());

        return (RageStacksCache) CacheManager.getInstance().getCachedPlayer(player).getCache("rage_stacks");
    }
}
