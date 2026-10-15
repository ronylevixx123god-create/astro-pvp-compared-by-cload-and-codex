package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.LandClaimEvent;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import java.util.Map;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarzoneClaimListener implements Listener {
    private int maxClaims;

    private Map<String, String> regions;

    private String warzoneRegionName;

    private String claimLimiterRegionName;

    public void setWarzoneRegionName(String warzoneRegionName) {
        this.warzoneRegionName = warzoneRegionName;
    }

    public String getWarzoneRegionName() {
        return this.warzoneRegionName;
    }

    public void setClaimLimiterRegionName(String claimLimiterRegionName) {
        this.claimLimiterRegionName = claimLimiterRegionName;
    }

    public String getClaimLimiterRegionName() {
        return this.claimLimiterRegionName;
    }

    public WarzoneClaimListener() {
        this.maxClaims = 2;
        this.regions = new HashMap<>();
        this.regions.put("spawnwarzone", "spawnwarzoneclaimlimiter");
        this.regions.put("desertwarzone", "desertwarzoneclaimlimiter");
        this.regions.put("infernalwarzone", "infernalstrongholdclaimlimiter");
        this.regions.put("frozenwarzone", "frozenstrongholdclaimlimiter");
        this.regions.put("flatwarzone", "flatwarzoneclaimlimiter");

    }

    @EventHandler
    public void onLandClaim(LandClaimEvent event) {
        World w = event.getLocation().getWorld();
        FLocation fl = event.getLocation();
        if (isCloseToWarzone(w.getChunkAt((int)fl.getX(), (int)fl.getZ()))) {
            String rn = getFixedRegionName();
            if (!canClaim(event.getFaction(), getClaimLimiterRegionName(), Bukkit.getWorld(w.getName()))) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!) &cYou can only claim maximum of &n" + this.maxClaims + "&c claims touching the " + rn + " WarZone!"));
                event.setCancelled(true);
                return;
            }
            event.getPlayer().sendMessage(CC.translate("&a&l(!) &aYou have obtained a &f/f claim&a touching the &f" + rn + " WarZone&a."));
            event.getFaction().getOnlinePlayers().forEach(p -> {
                if (p != event.getPlayer())
                    p.sendMessage(CC.translate("&a&l/F CLAIM: &f" + event.getPlayer().getName() + " &ahas obtained your faction a /f claim touching the &f" + rn + " WarZone&a."));
            });
        }
    }

    private boolean canClaim(Faction faction, String region, World world) {
        if (faction.isWarZone()) return true;
        if (getClaimsInRadius(faction, region, world) >= this.maxClaims)
            return false;
        return true;
    }

    private int getClaimsInRadius(Faction faction, String regionName, World world) {
        WorldGuardPlugin worldGaurd = WorldGuardPlugin.inst();
        RegionManager manager = worldGaurd.getRegionManager(world);
        ProtectedRegion region = manager.getRegion(regionName);
        BlockVector topLeft = region.getMaximumPoint();
        BlockVector bottom = region.getMinimumPoint();
        int maxChunkX = topLeft.getBlockX() >> 4;
        int maxChunkZ = topLeft.getBlockZ() >> 4;
        int minChunkX = bottom.getBlockX() >> 4;
        int minChunkZ = bottom.getBlockZ() >> 4;
        int claims = 0;
        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                FLocation flocation = new FLocation(world.getName(), x, z);
                Faction at = FactionUtils.getFactionAt(flocation);
                if (at == faction)
                    claims++;
            }
        }
        return claims;
    }

    private boolean isCloseToWarzone(Chunk c) {
        int x;
        if (checkChunk(c)) return true;
        return checkChunk(c);
    }

    private boolean checkChunk(Chunk c) {
        int x;
        for (x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    Chunk ch = c.getWorld().getChunkAt(c.getX() + x, c.getZ() + z);
                    Location l = ch.getBlock(8, 8, 8).getLocation();
                    FLocation fl = new FLocation(l);
                    if (FactionUtils.getFactionAt(fl).isWarZone() && !WorldGuardUtils.isPvPDisabled(l)) {
                        String regionName = WorldGuardUtils.getRegionName(l);
                        if (regionName.contains("warzone")) {
                            setWarzoneRegionName(regionName);
                            setClaimLimiterRegionName(this.regions.get(regionName));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String getFixedRegionName() {
        String regionName;
        if (getWarzoneRegionName() != null) {
            int endoff = getWarzoneRegionName().indexOf("w");
            regionName = getWarzoneRegionName().substring(0, endoff);
            regionName = regionName.replace(regionName.substring(0, 1), regionName.substring(0, 1).toUpperCase());
        } else {
            regionName = "UNKNOWN";
        }
        if (regionName.equalsIgnoreCase("Spa"))
            regionName = "Spawn";
        return regionName;
    }
}
