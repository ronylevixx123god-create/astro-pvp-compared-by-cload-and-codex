package net.crankedpvp.hypeboxes.utils;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static ItemStack getRandomItem(Map<ItemStack, Double> itemsMap) {

        double maxValue = 0;
        for(Double value : itemsMap.values()) {
            maxValue = maxValue+value;
        }

        double currentValue = 0;
        double randomDouble = ThreadLocalRandom.current().nextDouble(maxValue);

        for(Map.Entry<ItemStack, Double> entry : SortUtils.sort(itemsMap).entrySet()) {
            if(randomDouble > currentValue && randomDouble < currentValue + entry.getValue()) {
                return entry.getKey();
            }
            currentValue = currentValue + entry.getValue();
        }
        return null;
    }

    public static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }

    public static <E> E getRandomCollectionElement(Collection<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }
}
