package me.astropvp.hotfixplugin.anvil;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class AnvilSpamRunnable extends BukkitRunnable {

    public static Map<Player, Integer> playerIntegerMap = new HashMap<>();;

    public static void increment(Player player) {
        if(playerIntegerMap.containsKey(player)) {
            playerIntegerMap.put(player, playerIntegerMap.get(player) + 1);
            return;
        }
        playerIntegerMap.put(player, 1);
    }

    @Override
    public void run() {
        playerIntegerMap.clear();
    }

}
