/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.arkhamnetwork.CosmicCustomTNT;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.compilex.CosmicCustomCreepers.cmds.CommandCCC;
import com.compilex.CosmicCustomCreepers.listeners.CreeperListener;
import lombok.Getter;
import org.arkhamnetwork.CosmicCustomTNT.cmds.CommandCTNT;
import org.arkhamnetwork.CosmicCustomTNT.listeners.TNTListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CosmicCustomTNT
extends JavaPlugin {
    private static CosmicCustomTNT plugi = null;

    @Getter private ProtocolManager protocolManager;

    public void onEnable() {
        plugi = this;
        plugi.registerCommands();
        this.registerListeners();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void onDisable() {
    }

    public static CosmicCustomTNT get() {
        return plugi;
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents((Listener)new TNTListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new CreeperListener(), (Plugin)this);

    }

    private void registerCommands() {
        this.getCommand("ctnt").setExecutor((CommandExecutor)new CommandCTNT());
        this.getCommand("ccc").setExecutor((CommandExecutor)new CommandCCC());

    }
}

