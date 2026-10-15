package com.compilex.CosmicKOTH.customfiles;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomConfig {

    private static File file;
    private static FileConfiguration customFile;

    //Finds or generates the custom config file
    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("CosmicKOTH").getDataFolder(), "kothWins.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static Map<String, Integer> load() {
        Map<String, Integer> kothWins = new HashMap<>();
        try {
            for (String key : customFile.getConfigurationSection("kothWins.").getKeys(false)) {
                if (key == null) continue;
                int wins = customFile.getInt("kothWins." + Integer.parseInt(key));
                kothWins.put(key, wins);
            }
            return kothWins;

        } catch (Exception e) {
            e.printStackTrace();
            return kothWins;
        }
    }

}
