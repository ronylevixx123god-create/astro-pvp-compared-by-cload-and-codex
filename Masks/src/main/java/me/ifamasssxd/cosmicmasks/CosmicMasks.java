/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmicmasks;

import java.io.File;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicmasks.commands.CommandMask;
import me.ifamasssxd.cosmicmasks.items.MultiMaskItem;
import me.ifamasssxd.cosmicmasks.listeners.CustomEnchantListener;
import me.ifamasssxd.cosmicmasks.listeners.InternalMaskListener;
import me.ifamasssxd.cosmicmasks.listeners.MaskEffectListener;
import me.ifamasssxd.cosmicmasks.listeners.MaskListener;
import me.ifamasssxd.cosmicmasks.listeners.TerminatorTeleblockListener;
import me.ifamasssxd.cosmicmasks.managers.MaskManager;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CosmicMasks
extends JavaPlugin {
    private MaskManager maskManager;
    private static CosmicMasks instance;
    private MultiMaskItem multiMaskItem;

    public MaskManager getMaskManager() {
        return this.maskManager;
    }



    public MultiMaskItem getMultiMaskItem() {
        return this.multiMaskItem;
    }

    public void onEnable() {
        instance = this;
        this.maskManager = new MaskManager();
        this.maskManager.onEnable(this);
        this.registerCommands();
        this.registerListeners();
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, () -> {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                MaskType.rescanMask(player, true);
            }
        }, 200L, 60L);
        this.multiMaskItem = new MultiMaskItem("multi_mask");
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem((CosmicItem)this.multiMaskItem);
    }

    public void onDisable() {
        this.maskManager.onDisable();
    }

    void registerCommands() {
        this.getCommand("mask").setExecutor((CommandExecutor)new CommandMask());
    }

    void registerListeners() {
        this.getServer().getPluginManager().registerEvents((Listener)new MaskListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InternalMaskListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new TerminatorTeleblockListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new CustomEnchantListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MaskEffectListener(), (Plugin)this);
    }


    public static CosmicMasks get() {
        return instance;
    }
}

