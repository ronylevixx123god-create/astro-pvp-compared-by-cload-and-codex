package com.cosmicpvp.cosmicutils.serializer.uuid;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UUIDListSerializer {


    public static String serialize(List<UUID> l) {
        StringBuilder b = new StringBuilder();
        if(l == null) return "";
        for(UUID u : l)
            b.append(u.toString()).append(",");
        return b.toString();
    }

    public static List<UUID> deserialize(String s) {
        if(s==null || s.isEmpty()) return null;
        String[] array = s.split(",");
        List<UUID> uuids = new ArrayList<>();
        for(String str : array) {
            str = str.replace(",", "");
            if(str.isEmpty()) continue;
            uuids.add(UUID.fromString(str));
        }
        return uuids;
    }

}
