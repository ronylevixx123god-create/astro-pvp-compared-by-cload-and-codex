package net.astropvp.core.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class RealtimeUUIDCooldown {
    private final Map<String, Long> cooldownMap;

    private final String cooldownId;

    public RealtimeUUIDCooldown(String cooldownId) {
        this.cooldownMap = new HashMap<>();
        this.cooldownId = cooldownId;
    }

    public void add(Player player, Long time) {
        add(player, this.cooldownId, time);
    }

    public void add(UUID uuid, Long time) {
        add(uuid, this.cooldownId, time);
    }

    public void add(Player player, String cooldownId, Long time) {
        add(player.getUniqueId(), cooldownId, time);
    }

    public void add(UUID uuid, String cooldownId, Long time) {
        this.cooldownMap.put(String.valueOf(uuid) + "?=" + cooldownId, time);
    }

    public boolean isOnCooldown(Player player, String cooldownId) {
        return (getRemainingMillis(player, cooldownId) > 1L);
    }

    public long getRemainingMillis(Player player, String cooldownId) {
        if (!this.cooldownMap.containsKey(player.getUniqueId() + "?=" + cooldownId))
            return 0L;
        return ((Long)this.cooldownMap.get(player.getUniqueId() + "?=" + cooldownId)).longValue() - System.currentTimeMillis();
    }

    public int getRemainingSeconds(Player player, String cooldownId) {
        return (int)(getRemainingMillis(player, cooldownId) / 1000L);
    }

    public String getRemainingTimeFormatted(Player player, String cooldownId) {
        return getTime(getRemainingSeconds(player, cooldownId));
    }

    public static String getTime(int seconds) {
        if (seconds < 60)
            return seconds + "s";
        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;
        if (minutes < 60)
            return (secondsLeft > 0) ? String.valueOf(minutes + "m " + secondsLeft + "s") : String.valueOf(minutes + "m");
        if (minutes < 1440) {
            int i = minutes / 60;
            String str = i + "h";
            int j = 60 * i;
            int k = minutes - j;
            if (k >= 1)
                str = str + " " + k + "m";
            if (secondsLeft > 0)
                str = str + " " + secondsLeft + "s";
            return str;
        }
        int days = minutes / 1440;
        String time = days + "d";
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;
        if (leftOver >= 1)
            if (leftOver < 60) {
                time = time + " " + leftOver + "m";
            } else {
                int hours = leftOver / 60;
                time = time + " " + hours + "h";
                int hoursInMins = 60 * hours;
                int minsLeft = leftOver - hoursInMins;
                time = time + " " + minsLeft + "m";
            }
        if (secondsLeft > 0)
            time = time + " " + secondsLeft + "s";
        return time;
    }
}
