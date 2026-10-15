package net.astropvp.loottable.utils;

import com.google.common.util.concurrent.AtomicDouble;
import net.astropvp.loottable.LootTableAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomUtils {
    public static ItemStack getRandomItem(Map<ItemStack, Double> itemsMap) {
        double maxValue = itemsMap.values().stream().mapToDouble(Double::doubleValue).sum();
        List<Map.Entry<ItemStack, Double>> sortedEntries = itemsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
        while (true) {
            double randomDouble = ThreadLocalRandom.current().nextDouble(maxValue);
            for (Map.Entry<ItemStack, Double> entry : sortedEntries) {
                if (randomDouble < entry.getValue()) {
                    return entry.getKey();
                }
                randomDouble -= entry.getValue();
            }
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sort(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map.Entry<K, V> entry = list.get(i);
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
