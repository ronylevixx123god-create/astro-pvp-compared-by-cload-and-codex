package me.ifamasssxd.cosmicbosses.utils;

import java.util.*;

public class MapUtils {
    public static LinkedHashMap<UUID, Integer> sortByComparator(final HashMap<UUID, Integer> unsortMap, final boolean order, final int amount) {
        if (unsortMap == null) {
            return null;
        }
        final List<Map.Entry<UUID, Integer>> list = new LinkedList<Map.Entry<UUID, Integer>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<UUID, Integer>>() {
            @Override
            public int compare(final Map.Entry<UUID, Integer> o1, final Map.Entry<UUID, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        final LinkedHashMap<UUID, Integer> sortedMap = new LinkedHashMap<UUID, Integer>();
        for (final Map.Entry<UUID, Integer> entry : list) {
            if (sortedMap.size() >= amount) {
                break;
            }
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
