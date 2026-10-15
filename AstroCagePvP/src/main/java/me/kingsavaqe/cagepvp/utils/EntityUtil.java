package me.kingsavaqe.cagepvp.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
    public static List<Player> getPlayersNearby(Location location, int radius) {
        List<Player> players = new ArrayList<>();
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, 255.0D, radius)) {
            if (!(entity instanceof Player))
                continue;
            players.add((Player)entity);
        }
        return players;
    }

    public static List<Player> getPlayersInChunk(Chunk chunk) {
        List<Player> players = new ArrayList<>();
        byte b;
        int i;
        Entity[] arrayOfEntity;
        for (i = (arrayOfEntity = chunk.getEntities()).length, b = 0; b < i; ) {
            Entity entity = arrayOfEntity[b];
            if (entity instanceof Player)
                players.add((Player)entity);
            b = (byte)(b + 1);
        }
        return players;
    }
}
