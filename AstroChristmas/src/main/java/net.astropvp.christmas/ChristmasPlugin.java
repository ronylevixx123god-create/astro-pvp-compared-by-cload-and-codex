package net.astropvp.christmas;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.christmas.commands.ChristmasCommand;
import net.astropvp.christmas.managers.ChristmasManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ChristmasPlugin extends JavaPlugin {
    private static ChristmasPlugin instance;
    private ChristmasManager christmasManager;

    public void onEnable() {
        instance = this;
        File file = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            this.saveDefaultConfig();
        }
        this.christmasManager = new ChristmasManager();
        this.christmasManager.loadAll();
        this.getCommand("christmas").setExecutor((CommandExecutor)new ChristmasCommand());
    }

    public void onDisable() {
        this.getChristmasManager().saveShop();
        this.christmasManager.saveAll();
        instance = null;
    }


    public static ChristmasPlugin getInstance() {
        return instance;
    }

    public ChristmasManager getChristmasManager() {
        return this.christmasManager;
    }
}
