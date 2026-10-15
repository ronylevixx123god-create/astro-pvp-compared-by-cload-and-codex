package net.astropvp.core.utils;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CooldownUtils {
    private final HashMap<String, Long> playerMap = Maps.newHashMap();

    private final String defaultCooldownId;

    public CooldownUtils(String defaultCooldownId) {
        this.defaultCooldownId = defaultCooldownId;
    }

    public void add(Player player, String cooldownId, long duration) {
        this.playerMap.put(player.getName() + cooldownId, Long.valueOf(System.currentTimeMillis() + duration));
    }

    public void add(Player player, long duration) {
        add(player, this.defaultCooldownId, duration);
    }

    public boolean isOnCooldown(Player player, String cooldownId) {
        return (getRemainingMillis(player, cooldownId) > 1L);
    }

    public long getRemainingMillis(Player player, String cooldownId) {
        return !this.playerMap.containsKey(player.getName() + cooldownId) ? 0L : (((Long)this.playerMap.get(player.getName() + cooldownId)).longValue() - System.currentTimeMillis());
    }

    public int getRemainingSeconds(Player player, String cooldownId) {
        return (int)(getRemainingMillis(player, cooldownId) / 1000L);
    }

    public String getRemainingTimeFormatted(Player player, String cooldownId) {
        return TimeUtils.getTime(getRemainingSeconds(player, cooldownId));
    }

    public HashMap<String, Long> getPlayerMap() {
        return this.playerMap;
    }

    public String getDefaultCooldownId() {
        return this.defaultCooldownId;
    }
}

