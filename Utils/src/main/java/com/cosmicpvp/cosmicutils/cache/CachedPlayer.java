package com.cosmicpvp.cosmicutils.cache;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CachedPlayer {

    private final Map<String, Cache> caches;
    private final Player player;
    private final CacheManager manager;

    public CachedPlayer(Player player, CacheManager manager) {
        this.player = player;
        this.manager = manager;
        this.caches = new HashMap<>();
    }

    public boolean hasCache(String name) {
        return this.caches.containsKey(name);
    }

    public Cache getCache(String name) {
        return this.caches.get(name);
    }

    public Cache registerCache(Cache cache) {
        this.caches.put(cache.getName(), cache);
        return cache;
    }

    public void removeCache(String cacheName) {
        this.caches.remove(cacheName);
    }

}
