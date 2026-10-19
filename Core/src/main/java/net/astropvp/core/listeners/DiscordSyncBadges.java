package net.astropvp.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Chat badges provided by AstroDiscordSync (Nitro booster, faction leader).
 * If AstroDiscordSync is missing or disabled the badges are skipped, so chat keeps working;
 * AstroDiscordSync's classes are only loaded while it is enabled.
 */
final class DiscordSyncBadges {
    static final String PLUGIN_NAME = "AstroDiscordSync";

    private DiscordSyncBadges() {
    }

    static String getNitroLeader(Player player) {
        if (!Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            return "";
        }
        return Lookup.getNitroLeader(player);
    }

    // Unchanged badge logic from ChatListener.getNitroLeader (added 2022-05-28, 38522d0ad).
    private static final class Lookup {
        static String getNitroLeader(Player player) {
            String prefix = "";
            if (me.astropvp.discordsync.listeners.ChatListener.isNitroBooster(player)) {
                prefix += me.astropvp.discordsync.listeners.ChatListener.BOOSTER_BADGE;
            }
            if (me.astropvp.discordsync.listeners.ChatListener.isFactionLeader(player)) {
                prefix += me.astropvp.discordsync.listeners.ChatListener.LEADER_BADGE;
            }
            if (!prefix.equals("")) {
                prefix = "&8<" + prefix + "&8> ";
            }
            return prefix;
        }
    }
}
