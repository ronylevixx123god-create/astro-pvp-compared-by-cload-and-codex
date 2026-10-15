package net.astropvp.itemfilter;

import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import net.astropvp.itemfilter.command.Commandfilter;
import net.astropvp.itemfilter.data.FilterManager;
import net.astropvp.itemfilter.listeners.CommandListener;
import net.astropvp.itemfilter.listeners.FilterListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemFilterPlugin extends JavaPlugin {

    @Getter
    private static ItemFilterPlugin instance;
    private FilterManager filterManager;
    private CommandManager commandManager;


    @Override
    public void onEnable() {
        reloadConfig();
        instance = this;
        this.commandManager = new CommandManager(this, true);
        commandManager.register(new Commandfilter());
        filterManager = new FilterManager(this);
        filterManager.loadAll();

        Bukkit.getPluginManager().registerEvents(new FilterListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);

    }

    @Override
    public void onDisable() {
        filterManager.saveAll();
        instance = null;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> colorList(List<String> inputList) {
        List<String> output = new ArrayList<>();
        for(String input : inputList)
        output.add(ChatColor.translateAlternateColorCodes('&', input));

        return output;
    }

}
