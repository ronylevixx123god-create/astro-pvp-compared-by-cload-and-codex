package com.cosmicpvp.cosmicutils.cache;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class CacheManager {

    @Getter
    private static CacheManager instance;
    private final HashMap<UUID, CachedPlayer> playerCaches;

    public CacheManager() {
        instance = this;
        this.playerCaches = new HashMap<>();
    }

    public CachedPlayer getCachedPlayer(Player player) {
        if(!this.playerCaches.containsKey(player.getUniqueId()))
            this.playerCaches.put(player.getUniqueId(), new CachedPlayer(player, this));
        return this.playerCaches.get(player.getUniqueId());
    }

}
