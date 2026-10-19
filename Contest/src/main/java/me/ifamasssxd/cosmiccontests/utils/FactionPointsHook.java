package me.ifamasssxd.cosmiccontests.utils;

import com.cosmicpvp.factionpoints.FactionsPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * Faction point prizes from the CosmicFactionPoints plugin.
 *
 * Contest used Utils' FactionPointsUtils for this, which Astro removed from Utils in Jan 2021
 * ("Removed factionsPoints"). Same behavior: a point item only when CosmicFactionPoints is
 * running, otherwise null (no point prize). Its classes are only touched while it is enabled.
 */
public final class FactionPointsHook {
    public static final String PLUGIN_NAME = "CosmicFactionPoints";

    private FactionPointsHook() {
    }

    public static ItemStack getFactionPointsItem(String eventName, Object data) {
        if (!Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            return null;
        }
        return Lookup.getFactionPointsItem(eventName, data);
    }

    // Separate class so CosmicFactionPoints' classes are only loaded once the plugin is known to be enabled.
    private static final class Lookup {
        static ItemStack getFactionPointsItem(String eventName, Object data) {
            return FactionsPointsAPI.getFactionPointItem(null, null, eventName, data);
        }
    }
}
