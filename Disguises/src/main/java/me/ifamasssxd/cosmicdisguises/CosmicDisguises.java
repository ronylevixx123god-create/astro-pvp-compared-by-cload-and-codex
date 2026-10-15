/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  lilypad.client.connect.api.Connect
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmicdisguises;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicdisguises.commands.CommandDisguises;
import me.ifamasssxd.cosmicdisguises.listeners.DisguiseItemListener;
import me.ifamasssxd.cosmicdisguises.listeners.PlayerListener;
import me.ifamasssxd.cosmicdisguises.managers.DisguiseManager;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Set;

public class CosmicDisguises
extends JavaPlugin {
    private static CosmicDisguises instance;
    private DisguiseManager disguiseManager;
    private final boolean overwriteAliases = true;
    private boolean devplanet = false;

    public void onEnable() {
        instance = this;
        this.disguiseManager = new DisguiseManager();
        this.disguiseManager.onEnable(this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new DisguiseItemListener(), this);

        this.getCommand("disguises").setExecutor(new CommandDisguises());

        ArrayList players = Lists.newArrayList();
        players.addAll(Bukkit.getOnlinePlayers());
        players.forEach(pl -> {
            Set<DisguiseType> set = this.disguiseManager.getAvailableDisguises((Player)pl, true);
        });
    }

    public static CosmicDisguises get() {
        return instance;
    }

    public DisguiseManager getDisguiseManager() {
        return this.disguiseManager;
    }

    public boolean isOverwriteAliases() {
        return this.overwriteAliases;
    }

    public boolean isDevplanet() {
        return this.devplanet;
    }
}

