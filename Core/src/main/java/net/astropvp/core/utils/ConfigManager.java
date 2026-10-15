package net.astropvp.core.utils;

import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private AstroCore plugin;


    private String name;
    private File path;
    private String folder;
    private File file;
    private YamlConfiguration config;
    private ConsoleCommandSender console;

    public ConfigManager(String name, File path, String folder, AstroCore plugin) {
        this.name = name;
        this.path = path;
        this.folder = folder;
        this.plugin = plugin;
        this.console = Bukkit.getServer().getConsoleSender();
    }

    public ConfigManager() {
        this.name = "";
        this.path = null;
        this.folder = "";
        this.plugin = plugin;
        this.console = Bukkit.getServer().getConsoleSender();
    }

    public static ConfigManager get() {
        return new ConfigManager();
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPath(String str) {
        this.path = new File(str);
    }

    public void setFolder(String str) {
        this.folder = str;
    }

    public void setup() {

        if (this.folder == null) {
            this.file = new File(this.path, this.name);
        } else {
            this.file = new File(this.path + File.separator + folder, this.name);
        }

        if (!this.file.exists()) {

            if (folder != null) {
                plugin.saveResource(folder + File.separator + name, false);
            } else {
                plugin.saveResource(name, false);
            }


            this.config = YamlConfiguration.loadConfiguration(this.file);
            console.sendMessage(AstroCore.getPlugin(AstroCore.class).getConsolePrefix() + " " + this.name + " has been created!");

        } else {
            this.loadConfig();
            console.sendMessage(AstroCore.getPlugin(AstroCore.class).getConsolePrefix() + " " + this.name + " already exists. Loaded saved " + this.name);
        }
    }

    public void loadConfig() {
        if (this.file.exists()) {
            if (this.folder == null) {
                this.file = new File(this.path, this.name);
            } else {
                this.file = new File(this.path + File.separator + folder, this.name);
            }
            this.config = YamlConfiguration.loadConfiguration(this.file);
            console.sendMessage(ChatColor.GREEN + this.name + " has been loaded!");
        }
    }

    public void saveConfig() {
        try {
            getConfig().save(getFile());
            console.sendMessage(this.name + ChatColor.GREEN + " has been saved!");
        } catch (IOException e) {
            e.printStackTrace();
            console.sendMessage(this.name + ChatColor.RED + " could not be saved!");
        }
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void setConfig(YamlConfiguration config) {
        this.config = config;
    }

}
