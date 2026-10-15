package com.bryside.astroattributes;

import com.bryside.astroattributes.commands.CommandRune;
import com.bryside.astroattributes.listeners.*;
import com.bryside.astroattributes.runes.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DragonRunesPlugin extends JavaPlugin {
    public static DragonRunesPlugin DragonRunesPlugin;

    public static DragonRunesPlugin getInstance() {

        return DragonRunesPlugin;
    }

    public void onEnable() {
        this.registerListeners();
        this.registerCommands();

        DragonRunesPlugin = this;
    }

    public void onDisable() {

        DragonRunesPlugin = null;
    }

    public void registerListeners() {
        this.registerListener(new RuneGUIListener());
        this.registerListener(new RuneApplyListener());
        this.registerListener(new RuneExtractorListener());
        this.registerListener(new RuneDamagerListener());
        this.registerListener(new RuneExperienceShardListener());
        this.registerListener(new HolyGrailListener());
        this.registerListener(new AtlasRune());
        this.registerListener(new KronosRune());
        this.registerListener(new OuranosRune());
        this.registerListener(new EosRune());
        this.registerListener(new PrometheusRune());
    }


    public void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerCommands() {
        this.getCommand("titan").setExecutor(new CommandRune());
    }
}
