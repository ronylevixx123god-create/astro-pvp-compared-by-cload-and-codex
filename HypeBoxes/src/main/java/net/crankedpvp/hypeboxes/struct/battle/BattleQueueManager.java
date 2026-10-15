package net.crankedpvp.hypeboxes.struct.battle;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BattleQueueManager {

    @Getter
    private static final Map<Player, Map<String, Integer>> battleQueue = new HashMap<>();

}
