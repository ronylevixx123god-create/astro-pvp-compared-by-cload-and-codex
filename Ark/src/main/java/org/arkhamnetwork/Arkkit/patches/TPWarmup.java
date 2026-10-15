/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.factionupgrades.FactionUpgradeAPI
 *  me.ifamasssxd.factionupgrades.net.astropvp.armorsets.struct.FactionUpgrade
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World$Environment
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;

public class TPWarmup
        extends ArkPlugin {
    public TPWarmup(Plugin p) {
        super(p);
    }

    protected static double getTeleportWarmupDelay(Player p) {
        double xp = p.getTotalExperience();
        double teleportWarmup = 7.0;

        if (p.hasPermission("astrocore.galaxy")) return 0;
        if (p.getWorld().getEnvironment() == World.Environment.THE_END) {
            return 1.0;
        }
        if (xp <= 50000.0) {
            return teleportWarmup - xp / 25000.0;
        }
        if (xp <= 100000.0) {
            return teleportWarmup - Math.max(2.0, xp / 25000.0);
        }
        if (xp <= 500000.0) {
            return teleportWarmup - Math.max(4.0, xp / 83000.0 * 0.5);
        }
        if (xp > 500000.0) {
            return Math.max(0.5, teleportWarmup - Math.max(6.0, xp / 75000.0 * 0.5));
        }
        return p.getWorld().getEnvironment() == World.Environment.THE_END ? 1.0 : teleportWarmup;
    }

    public static void cancelQueuedTeleport(Player p) {
        if (p.hasMetadata("queuedTeleportTaskID")) {
            Bukkit.getScheduler().cancelTask(p.getMetadata("queuedTeleportTaskID").get(0).asInt());
            p.removeMetadata("queuedTeleport", Arkkit.get());
            p.removeMetadata("queuedTeleportTaskID", Arkkit.get());
            p.removePotionEffect(PotionEffectType.CONFUSION);
        }
    }

    public static void queuedTeleport(Player p, Location l, PlayerTeleportEvent.TeleportCause tc) {
        p.setMetadata("queuedTeleport", new FixedMetadataValue(Arkkit.get(), new QueuedTeleport(p, l, TPWarmup.getTeleportWarmupDelay(p), tc)));
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) (20.0 * TPWarmup.getTeleportWarmupDelay(p) + 40.0), 0));
    }

    @Override
    public void enable() {
        YamlConfiguration essentialsConfig = YamlConfiguration.loadConfiguration(new File("plugins/Essentials/config.yml"));
        if (essentialsConfig.getInt("teleport-delay") > 0) {
            Bukkit.getLogger().info("[Arkkit (TPWarmup)] Setting Essential's teleport-delay => 0");
            essentialsConfig.set("teleport-delay", 0);
            try {
                essentialsConfig.save(new File("plugins/Essentials/config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ess r");
        }
        this.registerListener(new TPWarmupListener());
        this.registerTask(new TPWarmupMovementMonitorTask().runTaskTimerAsynchronously(Arkkit.get(), 20L, 20L), TPWarmupMovementMonitorTask.class);
    }

    @Override
    public void disable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasMetadata("queuedTeleportTaskID")) continue;
            Bukkit.getScheduler().cancelTask(p.getMetadata("queuedTeleportTaskID").get(0).asInt());
            p.removeMetadata("queuedTeleport", Arkkit.get());
            p.removeMetadata("queuedTeleportTaskID", Arkkit.get());
            p.removeMetadata("queuedTeleportPass", Arkkit.get());
        }
    }
}

