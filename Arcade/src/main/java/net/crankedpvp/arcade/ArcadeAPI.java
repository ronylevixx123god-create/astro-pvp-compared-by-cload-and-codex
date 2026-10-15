package net.crankedpvp.arcade;

import com.earth2me.essentials.Essentials;
import net.crankedpvp.arcade.language.Language;
import net.crankedpvp.arcade.struct.ArcadeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArcadeAPI {
    public static String WORLD_NAME = "world_arcade";
    public static String SAFEZONE_REGION_NAME = "arcade_safezone";


    private static ArcadeManager arcadeManager = ArcadePlugin.getInstance().getArcadeManager();

    public static boolean canPvP(UUID uuid1, UUID uuid2) {
        return arcadeManager.inSameArcadeMatch(uuid1, uuid2);
    }

    public static boolean inArcadeWorld(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        return Bukkit.getPlayer(uuid).getWorld().getName().equals(WORLD_NAME);
    }

    public static boolean inArcadeMatch(UUID uuid) {
        return inArcadeWorld(uuid) && arcadeManager.inArcadeMatch(uuid);
    }



}
