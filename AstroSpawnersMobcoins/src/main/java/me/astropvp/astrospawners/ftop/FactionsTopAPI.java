package me.astropvp.astrospawners.ftop;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Faction;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.utils.SortUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class FactionsTopAPI {
    public static void recalculateFactionsTop() {
        FactionCacher.getData().clear();
        Bukkit.getScheduler().runTaskAsynchronously(AstroSpawners.getInstance(), (Runnable)new BukkitRunnable() {
            public void run() {
                Map<FactionData, Long> factions = Maps.newHashMap();
                for (Faction faction : FactionUtils.getAllFactions()) {
                    if (faction.isSafeZone() || faction.isWarZone() || faction.isWilderness())
                        continue;
                    FactionData factionData = new FactionData(faction);
                    factions.put(factionData, Long.valueOf(factionData.getSpawnerValue()));
                }
                factions = SortUtils.sort(factions);
                Map<Integer, FactionData> data = Maps.newLinkedHashMap();
                int pos = 0;
                for (Map.Entry<FactionData, Long> entry : factions.entrySet()) {
                    data.put(Integer.valueOf(pos), entry.getKey());
                    pos++;
                }
                FactionCacher.setFactionData(data);
            }
        });
    }

    public static Map<Integer, FactionData> getTopFactions(int from, int to) {
        Map<Integer, FactionData> data = Maps.newHashMap();
        for (Map.Entry<Integer, FactionData> entry : FactionCacher.getData().entrySet()) {
            int value = entry.getKey().intValue();
            if (from <= value && to >= value)
                data.put(Integer.valueOf(value), entry.getValue());
        }
        return data;
    }

    public static int getFactionTop(Faction faction) {
        int place=0;
        for (Map.Entry<Integer, FactionData> entry : getTopFactions(0, 9999).entrySet()) {
            if (entry.getValue().getFaction().getId().equals(faction.getId())) {
                place = entry.getKey();
                return place +1;
            }
        }
        return place;
    }

    public static long getValueOfSpawner(EntityType type) {

        switch (type) {
            case BLAZE:
                return 0L;
            case MUSHROOM_COW:
                return 10000000L;
            case GHAST:
                return 20000000L;
            case SNOWMAN:
                return 40000000L;
            case IRON_GOLEM:
                return 5000000L;
            case VILLAGER:
                return 15000000L;
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case CAVE_SPIDER:
                return 85000L;
            case CREEPER:
                return 390000L;
            case WITCH:
                return 10000000L;
            case PIG_ZOMBIE:
            case SLIME:
                return 450000L;
            default:
                return 50000L;

        }
    }
}
