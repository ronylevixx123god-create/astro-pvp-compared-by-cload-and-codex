/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 */
package net.astropvp.core.autobroadcast.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import net.astropvp.core.AstroCore;
import net.astropvp.core.autobroadcast.struct.BroadcastMessages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoBroadcastTask
        extends BukkitRunnable {

    private int currentMessage = 0;
    private List<List<String>> messages = new ArrayList<>(new ArrayList<>());

    private final AstroCore plugin;
    public AutoBroadcastTask(AstroCore plugin) {
        this.plugin = plugin;

    }




    private List<String> getNextMessage() {
        currentMessage++;
        if(currentMessage >= messages.size()) currentMessage = 0;
        return this.messages.get(currentMessage);
    }

    public void setupMessages() {

        File file = new File(this.plugin.getDataFolder(), "auto-messages.yml");

        file = new File(plugin.getDataFolder(), "auto-messages.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("auto-messages.yml", false);
        }

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(file);
        } catch (IOException |org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for(String test : yamlConfiguration.getConfigurationSection("Messages").getKeys(false)) {
            messages.add(yamlConfiguration.getStringList("Messages." + test));
        }
    }

    public void reload() {
        messages.clear();
        File file = new File(this.plugin.getDataFolder(), "auto-messages.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(file);
        } catch (IOException |org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for(String test : yamlConfiguration.getConfigurationSection("Messages").getKeys(false)) {
            messages.add(yamlConfiguration.getStringList("Messages." + test));
        }
    }

    @Override
    public void run() {
        List<String> message = getNextMessage();
        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            message.forEach(line -> player.sendMessage(CC.translate(line)));
        });
    }
}
