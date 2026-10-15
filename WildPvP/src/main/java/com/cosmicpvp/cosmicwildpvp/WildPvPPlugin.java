package com.cosmicpvp.cosmicwildpvp;

import com.cosmicpvp.cosmicwildpvp.commands.WildPvPCommand;
import com.cosmicpvp.cosmicwildpvp.listeners.MenuListener;
import com.cosmicpvp.cosmicwildpvp.listeners.QueueListener;
import com.cosmicpvp.cosmicwildpvp.queue.QueueManager;
import com.cosmicpvp.cosmicwildpvp.settings.CagesSetting;
import com.cosmicpvp.cosmicwildpvp.settings.MatchSetting;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;

import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import com.google.common.collect.Lists;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class WildPvPPlugin extends JavaPlugin
{
    private static WildPvPPlugin instance;
    private QueueManager queueManager;
    private List<MatchSetting> settingList;
    private Cages cages;

    public void onEnable() {
        WildPvPPlugin.instance = this;
        this.queueManager = new QueueManager(this);
        this.cages = new Cages(this);
        (this.settingList = Lists.newArrayList()).add(new CagesSetting());
        Bukkit.getPluginManager().registerEvents((Listener)new QueueListener(this), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new MenuListener(), (Plugin)this);
        this.getCommand("wildpvp").setExecutor((CommandExecutor)new WildPvPCommand(this));
        System.out.println("[AstroWildPvP] Loaded...");
    }

    public void onDisable() {
    }

    public Location getSpawnLocation() {
        if (Bukkit.getWorld("world") != null) {
            return Bukkit.getWorld("world").getSpawnLocation();
        }
        return null;
    }

    public static void tpNow(final Player player, final Location loc) {
        player.setMetadata("queuedTeleportPass", (MetadataValue)new FixedMetadataValue((Plugin)Arkkit.get(), (Object)true));
        player.teleport(loc, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    public List<MatchSetting> getSettingList() {
        return this.settingList;
    }

    public Cages getCages() {
        return this.cages;
    }

    public static WildPvPPlugin getInstance() {
        return WildPvPPlugin.instance;
    }
}
