package me.ifamasssxd.cosmicmysterychest.data;

import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;

import java.util.*;

public class LootboxFilterManager extends SaveLoadManager<LootboxFilterData> {
    public LootboxFilterManager() {
        super(CosmicMysteryChest.getInstance(), StoragePlugin.getInstance().getGson(), "data/", LootboxFilterData.class);
    }

    public LootboxFilterData getLootFilterData(UUID uuid) {
        if (getFromCache(uuid.toString()) == null) {
            addToCache(new LootboxFilterData(uuid, new HashMap<>()));
        }
        return getFromCache(uuid.toString());
    }
}
