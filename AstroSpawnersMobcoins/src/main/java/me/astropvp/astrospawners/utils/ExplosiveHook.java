package me.astropvp.astrospawners.utils;

import com.compilex.CosmicCustomCreepers.enums.CreeperTypes;
import com.compilex.CosmicCustomCreepers.listeners.CreeperListener;
import org.arkhamnetwork.CosmicCustomTNT.enums.TNTTypes;
import org.arkhamnetwork.CosmicCustomTNT.listeners.TNTListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

/**
 * Spawner drop bonuses for the custom creepers and TNT from the Explosive plugin
 * (which registers itself as "CosmicCustomTNT" and contains both).
 *
 * Explosive's classes are only touched while that plugin is enabled, so explosions keep
 * working with the normal drop chance if it is missing or disabled.
 */
public final class ExplosiveHook {
    public static final String PLUGIN_NAME = "CosmicCustomTNT";

    private ExplosiveHook() {
    }

    public static double adjustChance(Entity entity, double chance) {
        if (!Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            return chance;
        }
        return Lookup.adjustChance(entity, chance);
    }

    // Separate class so Explosive's classes are only loaded once the plugin is known to be enabled.
    private static final class Lookup {
        static double adjustChance(Entity entity, double chance) {
            if (entity instanceof Creeper) {
                CreeperTypes eggType = CreeperListener.creepers.get(entity.getUniqueId());
                if (eggType == CreeperTypes.Raid) {
                    return 1;
                }
                if (eggType == CreeperTypes.Lucky) {
                    return chance + 0.3;
                }
            } else if (entity instanceof TNTPrimed) {
                TNTTypes tntType = TNTListener.customTnt.get(entity.getUniqueId());
                if (tntType == TNTTypes.Lucky) {
                    return chance + 0.35;
                }
            }
            return chance;
        }
    }
}
