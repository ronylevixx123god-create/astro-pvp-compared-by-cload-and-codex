package com.conaxgames.customskulls.ability;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.enums.AbilityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AbilityData {

    @Getter
    private static Map<String, AbilityData> abilityDataMap = new HashMap<>();

    private UUID owner;
    private String uuid;
    private Location location;
    private AbilityType abilityType;

    private Map<CrateType, Integer> crateKeys = new HashMap<>();
    private double xp = 0;
    private Entity blaze;
    private int level = 0;
    private long lastActivated = 0;

    public AbilityData(String uuid) {
        this.uuid = uuid;
        abilityDataMap.put(uuid, this);
    }

    public static AbilityData getByUUID(String uuid) {
        return abilityDataMap.get(uuid) == null ? new AbilityData(uuid) : abilityDataMap.get(uuid);
    }

    public static String getUUID(Location location) {
        return location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    public static String getUUID(int x, int y, int z) {
        return x + "_" + y + "_" + z;
    }


    public boolean isActive() {
        int time = 4500000;
        if (level == 1) {
            time = 5000000;
        } else if (level == 2) {
            time = 5850000;
        } else if (level == 3) {
            time = 7200000;
        }
        return System.currentTimeMillis() < lastActivated + time;
    }

    public void activate(Player player, String uuid) {
        CoreSkulls.instance.getMobGrinderManager().activate(player, uuid);
    }

    public void deActivate(String uuid) {
        CoreSkulls.instance.getMobGrinderManager().deActivate(uuid);
    }


}
