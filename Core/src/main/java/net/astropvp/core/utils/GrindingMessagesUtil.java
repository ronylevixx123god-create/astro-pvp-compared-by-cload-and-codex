package net.astropvp.core.utils;

import com.google.common.collect.Lists;
import net.astropvp.core.AstroCore;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GrindingMessagesUtil {



    public static boolean isPlayerIndicating(Player player) {
        return AstroCore.grindingMessages.containsKey(player.getUniqueId());
    }

    public static void addPlayer(Player player) {
        List<String> players = Lists.newArrayList();
        if (AstroCore.getInstance().getConfig().contains("players-indicating"))
            players = AstroCore.getInstance().getConfig().getStringList("players-indicating");
        players.add(player.getUniqueId().toString());
        AstroCore.getInstance().getConfig().set("players-indicating", players);
        AstroCore.getInstance().saveConfig();
        AstroCore.getInstance().reloadConfig();
        AstroCore.grindingMessages.put(player.getUniqueId(), player);
    }

    public static void removePlayer(Player player) {
        List<String> players = Lists.newArrayList();
        if (AstroCore.getInstance().getConfig().contains("players-indicating"))
            players = AstroCore.getInstance().getConfig().getStringList("players-indicating");
        players.remove(player.getUniqueId().toString());
        AstroCore.getInstance().getConfig().set("players-indicating", players);
        AstroCore.getInstance().saveConfig();
        AstroCore.getInstance().reloadConfig();
        AstroCore.grindingMessages.remove(player.getUniqueId());
    }

    public static boolean isPlayerContained(Player player) {
        List<String> players = Lists.newArrayList();
        if (AstroCore.getInstance().getConfig().contains("players-indicating"))
            players = AstroCore.getInstance().getConfig().getStringList("players-indicating");
        return players.contains(player.getUniqueId().toString());
    }

}
