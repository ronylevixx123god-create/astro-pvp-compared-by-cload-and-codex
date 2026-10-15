package me.astropvp.astrospawners.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerDataManager {
    private final AstroSpawners instance;

    private Map<UUID, PlayerData> playerDataStorage;

    public PlayerDataManager(AstroSpawners instance) {
        this.playerDataStorage = new HashMap<>();
        this.instance = instance;
        for (Player p : Bukkit.getOnlinePlayers()) {
            createPlayerData(p);
        }
    }


    public void createPlayerData(Player player) {
        File configFile = new File(this.instance.getDataFolder().toString() + "/player-data", player.getUniqueId().toString() + ".yml");
        int coins = 0;
        if (configFile.exists())
            coins = YamlConfiguration.loadConfiguration(configFile).getInt("Coins");
        this.playerDataStorage.put(player.getUniqueId(), new PlayerData(player.getUniqueId(), coins));
    }

    public void createPlayerDataAsync(final Player player) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)this.instance, new Runnable() {
            public void run() {
                PlayerDataManager.this.createPlayerData(player);
            }
        });
    }

    public void savePlayerData(PlayerData playerData) {
        File configFile = new File(String.valueOf(String.valueOf(this.instance.getDataFolder().toString())) + "/player-data", playerData.getUUID().toString() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
        yamlConfiguration.set("Coins", Integer.valueOf(playerData.getCoins()));
        try {
            yamlConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerDataAsync(final PlayerData playerData) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)this.instance, new Runnable() {
            public void run() {
                PlayerDataManager.this.savePlayerData(playerData);
            }
        });
    }

    public PlayerData getPlayerData(Player player) {
        return this.playerDataStorage.get(player.getUniqueId());
    }

    public void removePlayerData(Player player) {
        this.playerDataStorage.remove(player.getUniqueId());
    }

    public boolean hasPlayerData(Player player) {
        return this.playerDataStorage.containsKey(player.getUniqueId());
    }
}
