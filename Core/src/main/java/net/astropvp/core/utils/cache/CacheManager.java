package net.astropvp.core.utils.cache;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Getter
public class CacheManager {

    @Getter
    private static CacheManager instance;
    private final HashMap<Player, CachedPlayer> playerCaches;

    public CacheManager() {
        instance = this;
        this.playerCaches = new HashMap<>();
    }

    public CachedPlayer getCachedPlayer(Player player) {
        if (!this.playerCaches.containsKey(player))
            this.playerCaches.put(player, new CachedPlayer(player, this));
        return this.playerCaches.get(player);
    }
}