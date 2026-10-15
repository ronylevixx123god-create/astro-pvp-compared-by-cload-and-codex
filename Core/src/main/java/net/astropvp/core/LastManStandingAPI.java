package net.astropvp.core;

import net.astropvp.core.cachers.LastManStandingCacher;
import net.astropvp.core.enums.CapType;
import org.bukkit.entity.Player;

public class LastManStandingAPI {
    public static boolean isPlayerCapping(Player player) {
        return (LastManStandingCacher.getPlayer(player) != null);
    }

    public static boolean isPlayerLMS(Player player) {
        LastManStandingCacher top = LastManStandingCacher.getLongestPlayerAlive(CapType.DESERT);
        if (top != null && !top.getPlayerName().contentEquals(player.getName()))
            top = LastManStandingCacher.getLongestPlayerAlive(CapType.SPAWN);
        return (top != null && top.getPlayerName().contentEquals(player.getName()));
    }
}

