package me.ifamasssxd.cosmicfactionbanners;

import org.bukkit.plugin.java.*;
import me.ifamasssxd.cosmicfactionbanners.managers.*;
import java.io.*;
import me.ifamasssxd.cosmicfactionbanners.struct.*;
import java.util.concurrent.*;
import me.ifamasssxd.cosmicskycustoms.*;
import me.ifamasssxd.cosmicskycustoms.struct.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import me.ifamasssxd.cosmicfactionbanners.listeners.*;
import org.bukkit.command.*;
import me.ifamasssxd.cosmicfactionbanners.commands.*;

public final class CosmicFactionBanners extends JavaPlugin
{
    private static CosmicFactionBanners instance;
    private BannerManager bannerManager;
    private BannerItem bannerItem;
    private BannerListener bannerListener;
    
    public void onEnable() {
        CosmicFactionBanners.instance = this;
        this.bannerManager = new BannerManager();
        this.registerCommands();
        this.registerListeners();
        try {
            this.bannerManager.onEnable(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        FactionBanner.secondCooldown = this.getConfig().getInt("bannerCooldown");
        FactionBanner.expired = TimeUnit.SECONDS.toMillis(FactionBanner.secondCooldown);
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem((CosmicItem)(this.bannerItem = new BannerItem()));
    }
    
    private void registerListeners() {
        this.bannerListener = new BannerListener();
        this.getServer().getPluginManager().registerEvents((Listener)this.bannerListener, (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new CommandListener(), (Plugin)this);
    }
    
    private void registerCommands() {
        this.getCommand("fsetbanner").setExecutor((CommandExecutor)new CommandSetBanner());
        this.getCommand("fbanner").setExecutor((CommandExecutor)new CommandBanner());
        this.getCommand("frecall").setExecutor((CommandExecutor)new CommandRecall());
    }
    
    public void onDisable() {
        try {
            this.bannerManager.onDisable(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static CosmicFactionBanners get() {
        return CosmicFactionBanners.instance;
    }
    
    public BannerManager getBannerManager() {
        return this.bannerManager;
    }
    
    public BannerItem getBannerItem() {
        return this.bannerItem;
    }
    
    public BannerListener getBannerListener() {
        return this.bannerListener;
    }
}
