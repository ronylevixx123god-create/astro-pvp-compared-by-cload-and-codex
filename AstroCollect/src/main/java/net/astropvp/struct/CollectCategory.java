package net.astropvp.struct;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CollectCategory implements Serializable {
    private CollectType type;

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public ItemStack[] getItems() {
        return items;
    }

    private ItemStack[] items;

    private String name;

    private final long timeCollected;



    public CollectCategory(CollectType type, ItemStack[] items, String name, long timeCollected) {
        this.type = type;
        this.items = stackItems(items);
        this.name = name;
        this.timeCollected = timeCollected;
    }

    private ItemStack[] stackItems(ItemStack[] items) {
        Map<ItemStack, Integer> itemMap = new HashMap<>();

        for (ItemStack item : items) {
            boolean found = false;
            for (ItemStack key : itemMap.keySet()) {
                if (item.isSimilar(key)) {
                    itemMap.put(key, itemMap.get(key) + item.getAmount());
                    found = true;
                    break;
                }
            }

            if (!found) {
                itemMap.put(item.clone(), item.getAmount());
            }
        }

        List<ItemStack> stackedItems = new ArrayList<>();

        for (Map.Entry<ItemStack, Integer> entry : itemMap.entrySet()) {
            ItemStack baseItem = entry.getKey();
            int totalAmount = entry.getValue();
            int maxStackSize = baseItem.getMaxStackSize();

            while (totalAmount > 0) {
                ItemStack stackedItem = baseItem.clone();
                if (totalAmount > maxStackSize) {
                    stackedItem.setAmount(maxStackSize);
                    totalAmount -= maxStackSize;
                } else {
                    stackedItem.setAmount(totalAmount);
                    totalAmount = 0;
                }
                stackedItems.add(stackedItem);
            }
        }

        return stackedItems.toArray(new ItemStack[0]);
    }


    public CollectType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public long getTimeCollected() {
        return timeCollected;
    }


    public boolean isExpired() {
        return System.currentTimeMillis() > timeCollected + 86400000;
    }

    public ItemStack[] removeItem(ItemStack item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                items[i]=new ItemStack(Material.AIR);
                return items;
            }
        }
        return items;
    }

    public void sort() {
        Arrays.sort(items, Comparator.comparing(a -> a.hasItemMeta() ? a.getItemMeta().getDisplayName(): a.getType().name()));
        List<ItemStack> stackedItems =  Arrays.stream(items).collect(Collectors.toList());
        stackedItems.removeIf(item -> item == null || item.getType() == Material.AIR);
        items = stackedItems.toArray(new ItemStack[0]);
    }


    public boolean onlyContainsAir() {
        for (ItemStack item : items) {
            if (item.getType() != Material.AIR) {
                return false;

            }
        }
        return true;
    }

}
