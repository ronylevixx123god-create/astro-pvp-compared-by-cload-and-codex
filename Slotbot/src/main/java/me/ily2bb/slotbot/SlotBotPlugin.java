package me.ily2bb.slotbot;

import me.ily2bb.slotbot.commands.CommandSkinCommand;
import me.ily2bb.slotbot.commands.CommandSlotBot;
import me.ily2bb.slotbot.listener.SlotBotInventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SlotBotPlugin extends JavaPlugin {
    public static SlotBotPlugin me;
    FileConfiguration config;

    public SlotBotPlugin() {
        this.config = this.getConfig();
    }

    public static SlotBotPlugin getInstance() {
        return SlotBotPlugin.me;
    }

    public void onEnable() {
        final File file = new File(this.getDataFolder() + File.separator + "config.yml");
        if (file.exists()) {
            this.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully loaded Slot Bots..."));
        } else {
            this.getConfig().addDefault("slot-bot-enabled", true);
            this.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully created config for Slot Bots..."));
        }
        this.loadConfig();
        this.config = this.getConfig();
        this.registerListeners();
        this.registerCommands();

        SlotBotPlugin.me = this;


    }

    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public void onDisable() {
        SlotBotPlugin.me = null;
    }

    public void registerListeners() {
        this.registerListener(new SlotBotInventoryListener());
    }


    public void registerCommands() {
        this.getCommand("slot").setExecutor(new CommandSlotBot());
        this.getCommand("addskin").setExecutor(new CommandSkinCommand());
    }

    public void registerListener(final Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }


}
