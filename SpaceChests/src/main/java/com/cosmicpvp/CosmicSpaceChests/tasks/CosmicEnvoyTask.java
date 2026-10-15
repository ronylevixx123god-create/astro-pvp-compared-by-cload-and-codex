package com.cosmicpvp.CosmicSpaceChests.tasks;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.utils.LocationUtils;
import com.cosmicpvp.CosmicSpaceChests.utils.MiscUtil;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CosmicEnvoyTask
implements Runnable {
    private static CosmicSpaceChestsPlugin plugin;
    private static long lastEnvoyEventTime;
    private static long currentEnvoyDelay;
    private boolean firstRun = true;
    Random r;

    public CosmicEnvoyTask(CosmicSpaceChestsPlugin fdp) {
        plugin = fdp;
        this.r = new Random();
        currentEnvoyDelay = 99999999;
    }

    @Override
    public void run() {
        if (Bukkit.getServer().getOnlinePlayers().size() == 0) {
            return;
        }
        if (!this.firstRun) {
            currentEnvoyDelay = (long)new Random().nextInt(plugin.getConfig().getInt("cosmic_envoy.max_delay") - plugin.getConfig().getInt("cosmic_envoy.min_delay")) * 1200L;
        }
        this.firstRun = false;
        Bukkit.getScheduler().runTaskLater((Plugin)CosmicSpaceChestsPlugin.plugin, new Runnable(){

            @Override
            public void run() {
                lastEnvoyEventTime = System.currentTimeMillis();
                CosmicEnvoyTask.spawnCosmicEnvoy(null);
            }
        }, currentEnvoyDelay);
    }

    public static long getNextEnvoySpawn() {
        return lastEnvoyEventTime + currentEnvoyDelay * 50L + (long)(plugin.getConfig().getInt("cosmic_envoy.min_delay") * 60 * 20 * 50);
    }

    public static long getPreviousEnvoySpawn() {
        return lastEnvoyEventTime;
    }

    public static String getPreviousEnvoySpawnString() {
        return CosmicEnvoyTask.getTimeString(System.currentTimeMillis() - lastEnvoyEventTime);
    }

    public static String getPreviousEnvoyCmdSpawnString() {
        return CosmicEnvoyTask.getCmdTimeString(System.currentTimeMillis() - lastEnvoyEventTime);
    }

    public static String getNextEnvoyTimeString() {
        return CosmicEnvoyTask.getTimeString(CosmicEnvoyTask.getNextEnvoySpawn() - System.currentTimeMillis());
    }

    private static String getTimeString(long millis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private static String getCmdTimeString(long millis) {
        return String.format("%02dm %02ds", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static void spawnCosmicEnvoy(String summoner) {
        ArrayList<Player> warzonePlayers = new ArrayList<Player>();
        boolean factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
        ArrayList<Player> availableRankedPlayers = new ArrayList<Player>();
        ArrayList<Player> availableTraineePlayers = new ArrayList<Player>();
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            String region;
            FLocation ploc;
            if (factions && !Board.getInstance().getFactionAt((FLocation)(ploc = new FLocation(pl.getLocation()))).isWarZone() || !pl.getWorld().equals(Bukkit.getWorlds().get(0)) || (region = MiscUtil.getRegionName(pl.getLocation())).startsWith("spawn_") || region.contains("frozen_") || region.contains("infernal_")) continue;
            boolean warpRegion = region.contains("warp");
            if (pl.hasPermission("cosmicpvp.vip")) {
                availableRankedPlayers.add(pl);
                warzonePlayers.add(pl);
                continue;
            }
            availableTraineePlayers.add(pl);
            if (warpRegion) continue;
            warzonePlayers.add(pl);
        }
        if (warzonePlayers.size() <= 0) {
            Bukkit.getLogger().info("[CosmicDropPackages] Failed to execute spawnCosmicEnvoy -- no warzonePlayers entries!");
            Bukkit.broadcastMessage((String)"");
            Bukkit.broadcastMessage((String)(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "*** " + ChatColor.RED.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "COSMIC ALERT" + ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + " *** "));
            Bukkit.broadcastMessage((String)((Object)ChatColor.DARK_RED + "No " + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Cosmic Envoy" + (Object)ChatColor.DARK_RED + " could occur because " + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "no Cosmonauts were in the Warzone!"));
            Bukkit.broadcastMessage((String)"");
            return;
        }
        double chestPerRanked = Math.max(1L, Math.round(90.0 / (double)warzonePlayers.size()));
        double chestPerTrainee = Math.max(1L, Math.round(40.0 / (double)warzonePlayers.size()));
        block1: for (Player p : warzonePlayers) {
            int i = 0;
            while (true) {
                double d = i;
                double d2 = availableRankedPlayers.contains((Object)p) ? chestPerRanked : chestPerTrainee;
                if (!(d < d2)) continue block1;
                Location l = LocationUtils.getNearbyLocation(p.getLocation(), 75, 12, 75);
                if (l != null && !MiscUtil.getRegionName(l).startsWith("spawn_")) {
                    CosmicSpaceChestsAPI.dropChest(l);
                }
                ++i;
            }
        }
        CosmicSpaceChestsPlugin.log.debug("Executed spawnCosmicEnvoy() with warzonePlayers = " + warzonePlayers.size() + ", chestPerRanked = " + chestPerRanked + ", chestPerTrainee = " + chestPerTrainee + ", availableTrainees = " + availableTraineePlayers.size() + ", availableRankedPlayers = " + availableRankedPlayers.size(), CosmicEnvoyTask.class);
        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "*** " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "COSMIC ENVOY" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + " *** ");
        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage((Object)ChatColor.WHITE + "A " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Cosmic Envoy" + (Object)ChatColor.WHITE + " has appeared underneath the main " + (Object)ChatColor.UNDERLINE + "/spawn" + (Object)ChatColor.WHITE + " and all PvP " + (Object)ChatColor.UNDERLINE + "/warps" + (Object)ChatColor.WHITE + ", supply crates can be seen falling all over the " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Warzone!");
        Bukkit.getServer().broadcastMessage("");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasMetadata("envoyTip")) continue;
            p.setMetadata("envoyTip", (MetadataValue)new FixedMetadataValue((Plugin)Arkkit.get(), (Object)true));
            p.sendMessage((Object)ChatColor.WHITE + "(( " + (Object)ChatColor.GRAY + "Chests filled with random tier loot have spawned at 50-100 random locations throughout the planet's warzone and warps, go get some loot! Remember, more chests spawn wherever there are more players." + (Object)ChatColor.WHITE + " ))");
            p.sendMessage("");
        }
        if (summoner != null) {
            Bukkit.getServer().broadcastMessage(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Cosmic Envoy Summoner: " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + summoner + "!");
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "*** " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "COSMIC ENVOY" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + " *** ");
        }
        Bukkit.getServer().broadcastMessage("");
    }

    static {
        lastEnvoyEventTime = System.currentTimeMillis();
        currentEnvoyDelay = 0L;
    }
}

