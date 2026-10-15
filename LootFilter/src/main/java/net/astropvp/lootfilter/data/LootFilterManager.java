package net.astropvp.lootfilter.data;

import net.astropvp.lootfilter.AstroLootFilterPlugin;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;

import java.util.HashMap;
import java.util.UUID;

public class LootFilterManager extends SaveLoadManager<LootFilterData> {
    public LootFilterManager() {
        super(AstroLootFilterPlugin.getInstance(), StoragePlugin.getInstance().getGson(), "data/", LootFilterData.class);
    }

    public LootFilterData getLootFilterData(UUID uuid) {
        if (getFromCache(uuid.toString()) == null) {
            addToCache(new LootFilterData(uuid, new HashMap<>()));
        }
        return getFromCache(uuid.toString());
    }
}
