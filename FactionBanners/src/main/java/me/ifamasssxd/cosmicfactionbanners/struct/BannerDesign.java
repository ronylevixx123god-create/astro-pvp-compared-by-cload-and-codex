package me.ifamasssxd.cosmicfactionbanners.struct;

import org.bukkit.block.banner.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import com.massivecraft.factions.*;
import me.ifamasssxd.cosmicfactionbanners.*;
import org.bukkit.entity.*;
import com.google.common.collect.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

public class BannerDesign
{
    private DyeColor baseColor;
    private LinkedList<Pattern> bannerPatterns;
    private transient ItemStack cachedItem;
    
    public BannerDesign() {
        this.bannerPatterns = new LinkedList<Pattern>();
    }
    
    public void setBanner(final ItemStack newBanner) {
        if (newBanner == null || newBanner.getType() != Material.BANNER) {
            this.cachedItem = null;
            this.bannerPatterns.clear();
            return;
        }
        this.cachedItem = null;
        final BannerMeta meta = (BannerMeta)newBanner.getItemMeta();
        this.baseColor = DyeColor.getByDyeData(newBanner.getData().getData());
        this.bannerPatterns = new LinkedList<Pattern>(meta.getPatterns());
    }
    
    public boolean hasBanner() {
        return this.bannerPatterns != null;
    }
    
    public ItemStack getBannerItem(final Faction faction) {
        if (this.cachedItem != null) {
            return this.cachedItem;
        }
        this.cachedItem = CosmicFactionBanners.get().getBannerManager().createBanner(faction, null, false);
        final BannerMeta meta = (BannerMeta)this.cachedItem.getItemMeta();
        final List<Pattern> newLists = Lists.newArrayList();
        newLists.addAll(this.bannerPatterns);
        meta.setPatterns((List)newLists);
        meta.setBaseColor((this.baseColor == null) ? DyeColor.WHITE : this.baseColor);
        this.cachedItem.setItemMeta((ItemMeta)meta);
        this.cachedItem.setDurability((short)meta.getBaseColor().getDyeData());
        return this.cachedItem;
    }
}
