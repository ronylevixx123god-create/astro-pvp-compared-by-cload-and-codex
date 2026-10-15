package me.astropvp.utils;

import me.astropvp.objects.DeathInventory;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public class SortMapByValue {
    public static boolean ASC = true;
    public static boolean DESC = false;

    public static Map<DeathInventory, Long> sortByComparator(Map<DeathInventory, Long> unsortMap, final boolean order) {
        LinkedList<Map.Entry<DeathInventory, Long>> list = new LinkedList<Map.Entry<DeathInventory, Long>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<DeathInventory, Long>>(){

            @Override
            public int compare(Map.Entry<DeathInventory, Long> o1, Map.Entry<DeathInventory, Long> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        LinkedHashMap<DeathInventory, Long> sortedMap = new LinkedHashMap<DeathInventory, Long>();
        for (Map.Entry entry : list) {
            sortedMap.put((DeathInventory)entry.getKey(), (Long)entry.getValue());
        }
        return sortedMap;
    }

    public static void printMap(Map<DeathInventory, Long> map) {
        for (Map.Entry<DeathInventory, Long> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
    }
}
