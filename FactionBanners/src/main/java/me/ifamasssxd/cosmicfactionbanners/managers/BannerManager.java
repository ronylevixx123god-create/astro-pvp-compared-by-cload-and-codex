package me.ifamasssxd.cosmicfactionbanners.managers;

import me.ifamasssxd.cosmicfactionbanners.struct.*;
import java.lang.reflect.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.reflect.*;
import me.ifamasssxd.cosmicfactionbanners.*;
import org.bukkit.scheduler.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.io.*;
import com.massivecraft.factions.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class BannerManager
{
    private Map<String, FactionBanner> factionBannerMap;
    private Map<String, BannerDesign> bannerDesignMap;
    private Type token;
    public static Material bannerMaterial;
    private File bannerFile;
    
    public BannerManager() {
        this.factionBannerMap = new MapMaker().concurrencyLevel(32).makeMap();
        this.bannerDesignMap = new HashMap<String, BannerDesign>();
        this.token = new TypeToken<HashMap<String, BannerDesign>>() {}.getType();
    }
    
    public void onEnable(final CosmicFactionBanners instance) throws IOException {
        this.bannerFile = JSONUtils.getOrCreateFile(instance.getDataFolder(), "factionBanners.json");
        this.bannerDesignMap = (Map<String, BannerDesign>)JSONUtils.fromJson(this.bannerFile, this.token);
        if (this.bannerDesignMap == null) {
            this.bannerDesignMap = new HashMap<String, BannerDesign>();
        }
        new BukkitRunnable() {
            public void run() {
                BannerManager.this.factionBannerMap.forEach((fId, banner) -> {
                    if (banner.hasExpired()) {
                        Location loc = banner.getActiveLocation();
                        Bukkit.getLogger().info("[CosmicFactionBanners] Expiring faction banner for " + fId + " (" + banner.getWhoPlacedUsername() + ") at " + ((loc == null) ? "N/A" : LocationUtils.getStringFromLocation(loc)));
                        banner.removeBanner();
                        BannerManager.this.factionBannerMap.remove(fId);
                    }
                });
            }
        }.runTaskTimer((Plugin)instance, 20L, 20L);
    }
    
    public void onDisable(final CosmicFactionBanners instance) throws IOException {
        JSONUtils.saveJSONToFile(this.bannerFile, (Object)this.bannerDesignMap, (Object)this.token);
        this.factionBannerMap.values().forEach(FactionBanner::removeBanner);
    }
    
    public ItemStack createBanner(final Faction faction, final Player player, final boolean white) {
        return this.createBanner(faction, player, white, null);
    }
    
    public ItemStack createBanner(final Faction faction, final Player player, final boolean white, final String freeReason) {
        return CosmicFactionBanners.get().getBannerItem().createItem(player, faction, player, white, freeReason);
    }
    
    public Map<String, FactionBanner> getFactionBannerMap() {
        return this.factionBannerMap;
    }
    
    public Map<String, BannerDesign> getBannerDesignMap() {
        return this.bannerDesignMap;
    }
    
    static {
        BannerManager.bannerMaterial = Material.BANNER;
    }
}
