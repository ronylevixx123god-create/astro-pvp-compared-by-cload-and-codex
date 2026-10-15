package me.astropvp.astrospawners.struct;

import lombok.Getter;
import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GrindingManager {
    private Map<UUID, GrindingCache> grindingCacheMap;

    public GrindingManager() {
        this.grindingCacheMap = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AstroSpawners.getInstance(), ()-> {
            for (GrindingCache grindingCache : grindingCacheMap.values()) {
                grindingCache.claimGrindingRewards();
            }
        }, 40L, 15*20L);

    }

    public GrindingCache getGrindingCache(Player player) {
        UUID uuid = player.getUniqueId();
        if (!grindingCacheMap.containsKey(uuid)) {
            grindingCacheMap.put(uuid, new GrindingCache(uuid));
        }
        return grindingCacheMap.get(uuid);
    }


}
