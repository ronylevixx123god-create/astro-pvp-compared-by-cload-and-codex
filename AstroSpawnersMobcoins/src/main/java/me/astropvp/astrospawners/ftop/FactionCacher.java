package me.astropvp.astrospawners.ftop;

import com.google.common.collect.Maps;
import java.util.Map;

public class FactionCacher {
    private static Map<Integer, FactionData> factions = Maps.newHashMap();

    public static Map<Integer, FactionData> getData() {
        return factions;
    }

    public static void setFactionData(Map<Integer, FactionData> factions) {
        FactionCacher.factions = factions;
    }
}
