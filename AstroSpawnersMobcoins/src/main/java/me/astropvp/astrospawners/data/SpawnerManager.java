package me.astropvp.astrospawners.data;

import me.astropvp.astrospawners.AstroSpawners;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class SpawnerManager extends SaveLoadManager<SpawnerData> {


    public SpawnerManager(AstroSpawners plugin) {
        super(plugin, StoragePlugin.getInstance().getGson(), "/data", SpawnerData.class);

    }

    public SpawnerData getSpawnerData(Location location, EntityType type, int amount) {
        if (!contains(SpawnerData.locationToString(location))) {
            SpawnerData spawnerData = new SpawnerData(type, amount, location);
            addToCache(spawnerData);
        }
        return getFromCache(SpawnerData.locationToString(location));

    }

    public SpawnerData getSpawnerData(Location location) {
        return getFromCache(SpawnerData.locationToString(location));

    }


    public static boolean canPlaceStackedSpawnerInThisChunk(Chunk chunk, EntityType spawnertype) {
        for (BlockState blockState : chunk.getTileEntities()) {
            if (blockState instanceof CreatureSpawner) {
                EntityType type = ((CreatureSpawner) blockState).getSpawnedType();
                if (type == spawnertype) {
                    return false;
                }
            }
        }
        return true;
    }

}

