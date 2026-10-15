package net.astropvp.loottable;


import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import net.astropvp.loottable.data.LootTableData;
import net.astropvp.loottable.data.LootTableManager;
import net.astropvp.loottable.struct.LootTable;
import net.astropvp.loottable.utils.RandomUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LootTableAPI {
    private static LootTableManager lootTableManager = LootTablePlugin.getInstance().getLootTableManager();

    public static ItemStack getRandomLootItem(LootTable lootTableType) {
        LootTableData lootTable = lootTableManager.getLootTable(lootTableType);
        return RandomUtils.getRandomItem(lootTable.getLootFromEcoUpdate(lootTable.getCurrentEcoUpdateLoot()));
    }

    public static List<ItemStack> getAllLootFromLootTable(LootTable type) {
        LootTableData lootTable = lootTableManager.getLootTable(type);
        Set<ItemStack> loot  = lootTable.getLootFromEcoUpdate(lootTable.getCurrentEcoUpdateLoot()).keySet();
        return loot.stream().sorted(Comparator.comparing(ItemStack::getType)).collect(Collectors.toList());
    }

    public static void fixLoot(LootTable type) {
        LootTableData lootTable = lootTableManager.getLootTable(type);
        Set<ItemStack> loot  = lootTable.getLootFromEcoUpdate(lootTable.getCurrentEcoUpdateLoot()).keySet();
        for (ItemStack itemStack  : loot) {
            if (ItemUtils.isAirOrNull(itemStack)) {
                lootTable.removeItem(lootTable.getCurrentEcoUpdateLoot(), itemStack);
            }
        }
    }
}