package com.astropvp.cc.player;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CosmicPlayers {
    private static CosmicPlayers i = new CosmicPlayers();
    public static CosmicPlayers get(){return i;}

    private CosmicPlayers(){
        cosmicPlayerMap = new ConcurrentHashMap<>();
    }

    public Collection<CosmicPlayer> getAll(){return cosmicPlayerMap.values();}

    public CosmicPlayer getCosmicPlayer(Player player){
        return getCosmicPlayer(player.getUniqueId());
    }

    public CosmicPlayer getCosmicPlayer(UUID uuid){
        return cosmicPlayerMap.get(uuid);
    }

    public CosmicPlayer wrapPlayer(Player player){
        CosmicPlayer cosmicPlayer = new CosmicPlayer(player);

        cosmicPlayerMap.put(player.getUniqueId(), cosmicPlayer);

        return cosmicPlayer;
    }

    public void unwrapPlayer(Player player){
        unwrapPlayer(player.getUniqueId());
    }

    public void unwrapPlayer(UUID uuid){
        cosmicPlayerMap.remove(uuid);
    }

    private Map<UUID, CosmicPlayer> cosmicPlayerMap;
}
