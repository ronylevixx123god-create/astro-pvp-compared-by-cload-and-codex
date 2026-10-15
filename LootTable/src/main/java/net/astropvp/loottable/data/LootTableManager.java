package net.astropvp.loottable.data;


import net.astropvp.loottable.LootTablePlugin;
import net.astropvp.loottable.struct.LootTable;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;

public class LootTableManager extends SaveLoadManager<LootTableData> {
    public LootTableManager(LootTablePlugin plugin) {
        super(plugin, LootTablePlugin.getInstance().getGson(), "/data", LootTableData.class);
        for (LootTable type : LootTable.values()) {
            addToCache(new LootTableData(type));
        }
    }

    public LootTableData getLootTable(LootTable lootTableType) {
        if (!contains(lootTableType.name())) {
            LootTableData lootTable = new LootTableData(lootTableType);
            addToCache(lootTable);
        }
        return getFromCache(lootTableType.name());
    }
}