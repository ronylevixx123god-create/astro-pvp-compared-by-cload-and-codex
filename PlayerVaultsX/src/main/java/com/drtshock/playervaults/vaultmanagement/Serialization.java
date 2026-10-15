package com.drtshock.playervaults.vaultmanagement;

import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.json.simple.*;
import org.bukkit.configuration.serialization.*;

public class Serialization
{
    @Deprecated
    public static Map<String, Object> toMap(final JSONObject jsonObject) {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (jsonObject == null) {
            return hashMap;
        }
        for (final Object next : jsonObject.keySet()) {
            hashMap.put(next.toString(), fromJson(jsonObject.get(next)));
        }
        return hashMap;
    }
    
    @Deprecated
    private static Object fromJson(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JSONObject) {
            return toMap((JSONObject)o);
        }
        if (o instanceof JSONArray) {
            return toList((JSONArray)o);
        }
        return o;
    }
    
    @Deprecated
    public static List<Object> toList(final JSONArray jsonArray) {
        final ArrayList<Object> list = new ArrayList<Object>();
        final Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            list.add(fromJson(iterator.next()));
        }
        return list;
    }

    @Deprecated
    public static List<String> toString(Inventory paramInventory) {
        ArrayList<String> arrayList = new ArrayList();
        ArrayList<ItemStack> arrayList1 = new ArrayList();
        Collections.addAll(arrayList1, paramInventory.getContents());
        for (ConfigurationSerializable configurationSerializable : arrayList1) {
            if (configurationSerializable == null) {
                arrayList.add("null");
                continue;
            }
            arrayList.add((new JSONObject(serialize(configurationSerializable))).toString());
        }
        return arrayList;
    }
    @Deprecated
    public static Inventory toInventory(final List<String> list, final int n, final int n2, final String s) {
        final VaultHolder vaultHolder = new VaultHolder(n);
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)vaultHolder, n2, s);
        vaultHolder.setInventory(inventory);
        final ArrayList<ItemStack> list2 = new ArrayList<ItemStack>();
        for (final String s2 : list) {
            if (s2.equalsIgnoreCase("null")) {
                list2.add(null);
            }
            else {
                list2.add((ItemStack)deserialize(toMap((JSONObject)JSONValue.parse(s2))));
            }
        }
        final ItemStack[] contents = new ItemStack[list2.size()];
        for (int i = 0; i < list2.size(); ++i) {
            contents[i] = (ItemStack)list2.get(i);
        }
        inventory.setContents(contents);
        return inventory;
    }
    
    @Deprecated
    public static Map<String, Object> serialize(final ConfigurationSerializable configurationSerializable) {
        final Map<String, Object> handleSerialization = handleSerialization(configurationSerializable.serialize());
        handleSerialization.put("==", ConfigurationSerialization.getAlias((Class)configurationSerializable.getClass()));
        return handleSerialization;
    }

    @Deprecated
    private static Map<String, Object> handleSerialization(Map<String, Object> paramMap) {
        Map<String, Object> map = recreateMap(paramMap);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof ConfigurationSerializable) {
                entry.setValue(serialize((ConfigurationSerializable)entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof Iterable) {
                ArrayList<Map<String, Object>> arrayList = new ArrayList();
                for (Map<String, Object> map1 : (Iterable<Map<String, Object>>)entry.getValue()) {
                    if (map1 instanceof ConfigurationSerializable)
                        map1 = serialize((ConfigurationSerializable)map1);
                    arrayList.add(map1);
                }
                entry.setValue(arrayList);
                continue;
            }
            if (entry.getValue() instanceof Map)
                entry.setValue(handleSerialization((Map<String, Object>)entry.getValue()));
        }
        return map;
    }
    
    @Deprecated
    public static Map<String, Object> recreateMap(final Map<String, Object> m) {
        return new HashMap<String, Object>(m);
    }

    public static Object deserialize(Map<String, Object> paramMap) {
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            if (entry.getValue() instanceof Map) {
                entry.setValue(deserialize((Map<String, Object>)entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof Iterable) {
                entry.setValue(convertIterable((Iterable)entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof Number)
                entry.setValue(convertNumber((Number)entry.getValue()));
        }
        return paramMap.containsKey("==") ? ConfigurationSerialization.deserializeObject(paramMap) : paramMap;
    }
    
    private static List<?> convertIterable(final Iterable<?> iterable) {
        final ArrayList<Number> list = new ArrayList<Number>();
        for (Object o : iterable) {
            if (o instanceof Map) {
                o = deserialize((Map<String, Object>)o);
            }
            else if (o instanceof List) {
                o = convertIterable((Iterable<?>)o);
            }
            else if (o instanceof Number) {
                o = convertNumber((Number)o);
            }
            list.add((Number)o);
        }
        return list;
    }
    
    private static Number convertNumber(final Number n) {
        if (n instanceof Long) {
            final Long n2 = (Long)n;
            if (n2 == n2.intValue()) {
                return n2.intValue();
            }
        }
        return n;
    }
}
