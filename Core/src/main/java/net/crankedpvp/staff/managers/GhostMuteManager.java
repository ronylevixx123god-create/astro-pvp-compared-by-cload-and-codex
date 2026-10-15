package net.crankedpvp.staff.managers;

import net.astropvp.core.AstroCore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GhostMuteManager {

    private final List<Player> ghostMuted;

    private final AstroCore plugin;
    public GhostMuteManager(AstroCore plugin) {
        this.plugin = plugin;
        this.ghostMuted = new ArrayList<>();
    }

    public void addPlayerToGhostMute(Player player) {
        if(ghostMuted.contains(player)) return;
        ghostMuted.add(player);
    }

    public void addPlayerToGhostMuteFor(Player player, double ms) {
        if(ghostMuted.contains(player)) return;
        ghostMuted.add(player);
        removeAfterTime(player, ms);
    }

    public void addPlayerToGhostMuteFor(Player player, String time) {
        if(ghostMuted.contains(player)) return;
        ghostMuted.add(player);
        removeAfterTime(player, getTimeInMs(time));
    }

    private void removeAfterTime(Player player, double ms) {
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.removePlayerFromGhostMute(player), (long)ms / 50);
    }

    public void removePlayerFromGhostMute(Player player) {
        if(!ghostMuted.contains(player)) return;
        ghostMuted.remove(player);
    }

    public boolean isPlayerInGhostMute(Player player) {
        return ghostMuted.contains(player);
    }

    public long getTimeInMs(String timeString) {
        long multiplier;
        long milliseconds;

        char unit = timeString.charAt(timeString.length() - 1);
        String numberString = timeString.substring(0, timeString.length() - 1);

        long number = Long.parseLong(numberString);

        switch (unit) {
            case 's':
                multiplier = 1000L; // 1 second = 1000 milliseconds
                break;
            case 'm':
                multiplier = 60000L; // 1 minute = 60000 milliseconds
                break;
            case 'h':
                multiplier = 3600000L; // 1 hour = 3600000 milliseconds
                break;
            case 'd':
                multiplier = 86400000L; // 1 day = 86400000 milliseconds
                break;
            default:
                throw new IllegalArgumentException("Invalid time unit: " + unit);
        }

        milliseconds = number * multiplier;
        return milliseconds;
    }



}
