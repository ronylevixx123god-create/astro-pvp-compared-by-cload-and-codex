package me.astropvp.astrospacegrinding.data;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import me.astropvp.astrospacegrinding.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class ForceFieldData {
    private long lastActivated;
    private UUID uuid;
    public static final Map<UUID, ForceFieldData> spaceData = new HashMap<>();


    public ForceFieldData(UUID uuid) {
        this.uuid = uuid;

    }

    public long getRemainingForceFieldTime() {
        return 1800000 - (System.currentTimeMillis()-lastActivated);
    }

    public void activateShield() {
        spaceData.put(uuid, this);
        this.setLastActivated(System.currentTimeMillis());

    }

    public boolean isActive() {
        return getRemainingForceFieldTime() > 0 && spaceData.containsKey(uuid);
    }

    public String getRemainingForceFieldTimeFormatted() {
        return TimeUtils.formatMs(getRemainingForceFieldTime());
    }

    public static ForceFieldData findPlayer(Player player) {
        if (spaceData.containsKey(player.getUniqueId())) {
            return spaceData.get(player.getUniqueId());
        }  else {
            return new ForceFieldData(player.getUniqueId());

        }
    }

    public static void remove(Player player) {
      spaceData.remove(player.getUniqueId());
    }

}
