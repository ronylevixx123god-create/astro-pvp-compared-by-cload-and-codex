package me.kingsavaqe.cagepvp.worldguard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;

public class WorldGuardManager {
    private WorldGuardPlugin worldGuard;

    public WorldGuardManager() {
        registerWorldGuard();
    }

    public void registerWorldGuard() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (pluginManager.getPlugin("WorldGuard") != null) {
            System.out.println("Hooked into Worldguard!");
            this.worldGuard = (WorldGuardPlugin) pluginManager.getPlugin("WorldGuard");
        } else {
            System.out.println("Could not find worldguard :(");
        }
    }

    public boolean isRegionAtLocation(Location location) {
        RegionManager regionManager = this.worldGuard.getRegionManager(location.getWorld());
        ApplicableRegionSet regionsAtLocation = regionManager.getApplicableRegions(location);
        return regionsAtLocation.size() != 0;
    }

    public WorldGuardPlugin getWorldGuard() {
        if (this.worldGuard == null)
            registerWorldGuard();
        return this.worldGuard;
    }

    public boolean hasWorldGuard() {
        return (getWorldGuard() != null);
    }
}
