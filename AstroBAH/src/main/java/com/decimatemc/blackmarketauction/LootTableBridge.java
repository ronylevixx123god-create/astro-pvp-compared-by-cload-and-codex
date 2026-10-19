package com.decimatemc.blackmarketauction;

import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.LootTablePlugin;
import net.astropvp.loottable.data.LootTableData;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Uses the existing BAH table and its selected economy stage, not a second weight format. */
public final class LootTableBridge {
    private final BlackMarketAuction plugin;
    private final String mode;

    public LootTableBridge(BlackMarketAuction plugin) {
        this.plugin = plugin;
        mode = plugin.getConfig().getString("loot-source", "auto").toLowerCase(java.util.Locale.ROOT);
        if (!mode.equals("auto") && !mode.equals("legacy") && !mode.equals("loottable")) {
            throw new IllegalStateException("loot-source must be auto, legacy, or loottable");
        }
        if (mode.equals("loottable") && !loaded()) {
            throw new IllegalStateException("loot-source=loottable requires enabled AstroLoottables");
        }
    }

    private boolean loaded() {
        PluginManager manager = Bukkit.getPluginManager();
        return manager != null && manager.isPluginEnabled("AstroLoottables");
    }

    private LootTableData data() {
        return LootTablePlugin.getInstance().getLootTableManager().getLootTable(LootTable.BAH);
    }

    public boolean isSelected() {
        return mode.equals("loottable") || (mode.equals("auto") && hasRewards());
    }

    public boolean hasRewards() {
        if (mode.equals("legacy") || !loaded()) return false;
        Map<ItemStack, Double> rewards = data().getLootFromEcoUpdate(data().getCurrentEcoUpdateLoot());
        if (rewards.isEmpty()) return false;
        double sum = 0;
        for (Map.Entry<ItemStack, Double> entry : rewards.entrySet()) {
            if (entry.getKey() == null || entry.getKey().getType() == Material.AIR
                    || entry.getValue() == null || !Double.isFinite(entry.getValue()) || entry.getValue() < 0) {
                return false;
            }
            sum += entry.getValue();
        }
        return Double.isFinite(sum) && sum > 0;
    }

    public ItemStack randomReward() {
        if (!hasRewards()) throw new IllegalStateException("BAH LootTable has no valid weighted rewards");
        return LootTableAPI.getRandomLootItem(LootTable.BAH).clone();
    }

    public List<ItemStack> rewards() {
        if (!hasRewards()) return new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        LootTableAPI.getAllLootFromLootTable(LootTable.BAH).forEach(item -> items.add(item.clone()));
        return items;
    }

    public String description() {
        return "LootTable BAH / " + data().getCurrentEcoUpdateLoot().name();
    }

    public boolean add(ItemStack item) {
        if (!loaded()) return false;
        data().addItem(data().getCurrentEcoUpdateLoot(), 1.0, item);
        LootTablePlugin.getInstance().getLootTableManager().saveAll();
        return true;
    }

    public boolean remove(ItemStack item) {
        if (!loaded()) return false;
        Map<ItemStack, Double> rewards = data().getLootFromEcoUpdate(data().getCurrentEcoUpdateLoot());
        if (!rewards.containsKey(item)) return false;
        data().removeItem(data().getCurrentEcoUpdateLoot(), item);
        LootTablePlugin.getInstance().getLootTableManager().saveAll();
        return true;
    }
}
