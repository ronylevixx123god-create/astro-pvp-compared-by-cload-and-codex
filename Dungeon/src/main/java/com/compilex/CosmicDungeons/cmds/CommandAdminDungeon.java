/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.itembuilder.PhoenixFeatherBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.SpeedTrinketBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.YijkiStaffBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons.cmds;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.DungeonsRewardsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.*;
import com.compilex.CosmicDungeons.items.DungeonKeyItem;
import com.compilex.CosmicDungeons.struct.PortalLog;
import com.compilex.CosmicDungeons.tasks.HolidayPortalTask;
import com.compilex.CosmicDungeons.utils.ItemUtils;
import com.compilex.CosmicDungeons.utils.LocationUtils;
import com.compilex.CosmicDungeons.utils.Logger;
import com.compilex.CosmicDungeons.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.itembuilder.PhoenixFeatherBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.SpeedTrinketBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.YijkiStaffBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class CommandAdminDungeon
implements CommandExecutor {
    /*
     * WARNING - void declaration
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        DungeonType dungeonType2;
        DungeonPlayer dungeonPlayer;
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("/adungeon <debug/reloadconfig/setcooldown/givekey/saveplayers/givecoords/givemap/findmap/setspawn/materials/soulpearl/soulanvil/reset/lootbag/setholidayportal/togglekey/destroyedoutpostcomplete>");
            sender.sendMessage("/adungeon giveportalkey <player> <dungeonname: ABANDONED_SHIP, other>");
            return true;
        }
        if (args.length == 1 && args[0].equals("reloadconfig")) {
            CosmicDungeonsPlugin.get_().reloadConfig();
            sender.sendMessage("reloaded config");
            return true;
        }
        if (args[0].equalsIgnoreCase("destroyedoutpostcomplete")) {
            boolean newValue;
            DestroyedOutpostDungeon destroyedOutpostDungeon = (DestroyedOutpostDungeon)DungeonType.DESTROYED_OUTPOST.getDungeonInstance();
            destroyedOutpostDungeon.hasBeenDefeated = newValue = !destroyedOutpostDungeon.hasBeenDefeated;
            CosmicDungeonsPlugin.get().getConfig().set("destroyedOutpostDefeated", newValue);
            CosmicDungeonsPlugin.get().saveConfig();
            sender.sendMessage(CC.Red + "Destroyed Outpost been defeated set to: " + newValue);
            return true;
        }
        if (args[0].equalsIgnoreCase("giveclock")) {
            ((Player)sender).getInventory().addItem(((DestroyedOutpostDungeon)DungeonType.DESTROYED_OUTPOST.getDungeonInstance()).createReturningClock());
            sender.sendMessage(CC.Red + "clock received!");
            return true;
        }
        if (args[0].equalsIgnoreCase("givekey")) {
            if (args.length < 2) {
                sender.sendMessage(CC.Red + "Please provide a dungeon type: " + Arrays.toString(DungeonType.values()));
                return true;
            }
            try {
                DungeonType dungeonType3 = DungeonType.valueOf(args[1]);
                if (args.length == 3) {
                    Bukkit.getPlayer(args[2]).getInventory().addItem(dungeonType3.getDungeonInstance().getDungeonPortal());
                } else {
                    ((Player)sender).getInventory().addItem(dungeonType3.getDungeonInstance().getDungeonPortal());
                }
                sender.sendMessage(CC.Red + " Portal Added!");
                return true;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                sender.sendMessage(CC.Red + "Unable to find dungeon of that type: " + Arrays.toString(DungeonType.values()));
                sender.sendMessage(CC.RedB + "/adungeon givekey <dungeonType>");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("giveportalkey")) {
            String string = args[1];
            String dungeon = args[2];
            Player p = Bukkit.getPlayer(string);
            if (p != null) {
                Long val;
                DungeonType type3 = DungeonType.getFromName(dungeon);
                if (type3 == null) {
                    sender.sendMessage(CC.Red + "Dungeon Type invalid! Valid: " + Arrays.toString(DungeonType.values()));
                    return true;
                }
                ItemStack key = args.length == 4 && (val = NumberUtils.parseLongOrNull(args[3])) != null ? new DungeonKeyItem(type3, val).build() : new DungeonKeyItem(type3, -1L).build();
                if (key != null) {
                    PlayerUtil.giveOrDropProtectedItem(p, key, -1);
                    p.sendMessage(CC.YellowB + "(!) " + CC.Yellow + type3.getDungeonName() + " Dungeon Portal Key" + CC.Yellow + " added to your inventory!");
                    sender.sendMessage(CC.Red + "Key added!");
                }
                return true;
            }
            sender.sendMessage(CC.Red + "Player not online!");
            return true;
        }
        if (args[0].equals("debug")) {
            Logger.debug = !Logger.debug;
            sender.sendMessage("CosmicDungeons debug => " + Logger.debug);
            return true;
        }
        if (args[0].equals("getchestlocs") && (dungeonPlayer = CosmicDungeonsPlugin.get_().getApi().getPlayerDungeonData((Player)sender)) != null && dungeonPlayer.getCurrentDungeon() != null && dungeonPlayer.getCurrentDungeon().getDungeonType() == DungeonType.PLANET_NUL) {
            ((PlanetNulDungeon)dungeonPlayer.getCurrentDungeon()).getDungeonEvents().chestLocations.forEach(l -> sender.sendMessage(CC.translate("&c&lLocation: " + l.getX() + ", " + l.getY() + ", " + l.getZ())));
        }
        if (args[0].equals("disable")) {
            CosmicDungeonsPlugin.get_().setDisableDungeons(true);
            sender.sendMessage("Dungeons disabled!");
            return true;
        }
        if (args[0].equals("enable")) {
            CosmicDungeonsPlugin.get_().setDisableDungeons(false);
            sender.sendMessage("Dungeons enabled!");
            return true;
        }
        if (args[0].equalsIgnoreCase("givemap") || args[0].equalsIgnoreCase("findmap") || args[0].equalsIgnoreCase("map")) {
            Player player2 = sender instanceof Player ? (Player)sender : null;
            if (args.length >= 2) {
                Player player3 = Bukkit.getPlayer(args[1]);
            }
            if (player2 == null) {
                sender.sendMessage(CC.Red + "Invalid player given!");
                return true;
            }
            DungeonPlayer dp2 = CosmicDungeonsPlugin.get_().getApi().getPlayerDungeonData(player2);
            if (dp2 == null || dp2.getCurrentDungeon() == null || dp2.getCurrentDungeon().getDungeonType() != DungeonType.DESTROYED_OUTPOST) {
                sender.sendMessage(CC.Red + "Player '" + player2.getName() + "' needs to be inside an active Destroyed Outpost dungeon to have the proper Dungeon data.");
                return true;
            }
            DestroyedOutpostDungeon dungeon = (DestroyedOutpostDungeon)dp2.getCurrentDungeon();
            if (args[0].equalsIgnoreCase("findmap")) {
                Location mapChest = dungeon.getDungeonEvents().getDimensionalMapChest();
                if (mapChest == null) {
                    sender.sendMessage(CC.Red + "Unable to find a chest that stored the map, didnt spawn?");
                    return true;
                }
                sender.sendMessage(CC.Red + "Chest containing map: " + LocationUtils.convertLocationToString(mapChest));
                sender.sendMessage(CC.Red + " hasBeenOpened: " + dungeon.getDungeonEvents().getLootedChestBlocks().contains(mapChest));
                player2.teleport(mapChest, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                return true;
            }
            if (args[0].equalsIgnoreCase("map")) {
                sender.sendMessage(CC.Red + "Active Directions:");
                dungeon.getDungeonEvents().getActivePortalDestinations().forEach((value, direction) -> sender.sendMessage(CC.Red + "#" + (value + 1) + " - " + direction));
                return true;
            }
            ItemStack item = dungeon.getDungeonEvents().createDimensionalMap();
            player2.getInventory().addItem(item);
            sender.sendMessage(CC.Red + "Dimensional Map added to " + player2.getName() + "'s inventory!");
            return true;
        }
        if (args[0].equalsIgnoreCase("setfastestdungeonrun") && args.length == 3) {
            DungeonType dungeonType4 = DungeonType.getFromName(args[1]);
            if (dungeonType4 == null) {
                sender.sendMessage(CC.Red + "Invalid dungeon type: " + Arrays.toString(DungeonType.values()));
                return true;
            }
            Integer completed = NumberUtils.parseIntOrNull(args[2]);
            if (completed == null) {
                sender.sendMessage(CC.RedB + " (!) " + CC.Red + "Invalid time given!");
                return true;
            }
            dungeonType4.getDungeonInstance().getTopDungeonTime().setSecondsCompletedIn(completed);
            dungeonType4.getDungeonInstance().getTopDungeonTime().saveData();
            sender.sendMessage(CC.Red + "Dungeon time set to: " + completed);
            return true;
        }
        if (args[0].equalsIgnoreCase("givecoordinates") || args[0].equalsIgnoreCase("givecoords")) {
            if (args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);
                DungeonPlayer pl = CosmicDungeonsPlugin.get().getApi().getPlayerDungeonData(player);
                DungeonType type4 = DungeonType.getFromName(args[2]);
                if (type4 == null) {
                    sender.sendMessage(CC.Red + "Invalid dungeon type: " + Arrays.toString(DungeonType.values()));
                    return true;
                }
                if (pl != null) {
                    pl.getLastCompleted().put(type4, System.currentTimeMillis());
                    pl.getPlayerConfig().set("dungeons.stats." + type4.getInternalName() + ".lastComplete", System.currentTimeMillis());
                    sender.sendMessage(CC.Red + "Last Complete time set to current time!");
                    return true;
                }
            }
            sender.sendMessage(CC.Red + "/adungeon givecoords <player> <dungeon>");
            return true;
        }
        if (args[0].equals("setcooldown")) {
            if (args.length == 4) {
                String string = args[1];
                String dungeon = args[2];
                long duration = Long.parseLong(args[3]);
                Player p = Bukkit.getPlayer(string);
                if (p != null) {
                    DungeonPlayer dp3 = CosmicDungeonsPlugin.get_().getApi().getPlayerDungeonData(p);
                    Dungeon d = CosmicDungeonsPlugin.get_().getApi().getDungeonByWorldName(dungeon);
                    dp3.getCooldowns().setDungeonCooldown(d, duration);
                    sender.sendMessage("set cooldown of " + d.toString() + " for " + dp3.getBukkitPlayer().getName() + " to +" + duration);
                    return true;
                }
            } else {
                sender.sendMessage("/adungeo setcooldown <player> <dungeon> <duration>");
            }
            return true;
        }
        if (args[0].equals("resetcooldowns")) {
            if (args.length == 2) {
                String string = args[1];
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p == null) continue;
                    DungeonPlayer dp4 = CosmicDungeonsPlugin.get_().getApi().getPlayerDungeonData(p);
                    Dungeon d = CosmicDungeonsPlugin.get_().getApi().getDungeonByWorldName(string);
                    dp4.getCooldowns().removeDungeonCooldown(d);
                    sender.sendMessage("set cooldown of " + d.toString() + " for " + dp4.getBukkitPlayer().getName() + " to +" + '\u0000');
                }
                return true;
            }
            sender.sendMessage("/adungeon resetcooldowns <dungeon>");
            return true;
        }
        if (args[0].equals("setspawn") && sender instanceof Player) {
            Player player = (Player)sender;
            Dungeon dungeon = DungeonsAPI.get().getDungeonByWorldName(player.getWorld().getName());
            if (dungeon != null) {
                dungeon.setSpawnLocation(player.getLocation());
                dungeon.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                CosmicDungeonsPlugin.get_().getConfig().set("dungeons." + dungeon.getWorldName() + ".spawnLocation", LocationUtils.convertLocationToString(player.getLocation()));
                CosmicDungeonsPlugin.get_().saveConfig();
                player.sendMessage("Spawn for " + dungeon.getDungeonName() + "(id: " + dungeon.getDungeonID() + ") has been set to " + player.getLocation());
                return true;
            }
            player.sendMessage("ERROR: dungeon = null");
            return true;
        }
        if (args[0].equals("materials") && sender instanceof Player) {
            Player player = (Player)sender;
            player.getInventory().addItem(ItemUtils.setAmount(DungeonsAPI.get().getEnchantedObsidian().clone(), 10));
            player.getInventory().addItem(DungeonsAPI.get().getDimensionWeb());
            player.getInventory().addItem(DungeonsAPI.get().getCosmonautFuel());
            player.getInventory().addItem(DungeonsAPI.get().getTeleportationCrystal());
            player.getInventory().addItem(DungeonsAPI.get().getInterdimensionalCore());
            return true;
        }
        if (args[0].equals("soulanvil") && sender instanceof Player) {
            Player player = (Player)sender;
            player.getInventory().addItem(DungeonsRewardsAPI.getSoulAnvil());
            return true;
        }
        if (args[0].equals("speedtrinket") && sender instanceof Player) {
            Player player = (Player)sender;
            player.getInventory().addItem(new SpeedTrinketBuilder().build());
            return true;
        }
        if (args[0].equals("yijkistaff") && sender instanceof Player) {
            Player player = (Player)sender;
            player.getInventory().addItem(new YijkiStaffBuilder().build());
            return true;
        }
        if (args[0].equals("phoenixfeather") && sender instanceof Player) {
            Player player = args.length > 1 ? Bukkit.getPlayer(args[1]) : (Player)sender;
            player.getInventory().addItem(new PhoenixFeatherBuilder().build());
            return true;
        }
        if (args[0].equals("reset")) {
            int n = Integer.parseInt(args[1]);
            Dungeon d = DungeonsAPI.get().getDungeon(n);
            d.reset();
            sender.sendMessage("Executed reset() for: " + d);
            return true;
        }
        if (args[0].equals("lootbag") && sender instanceof Player) {
            Player player = (Player)sender;
            player.getInventory().addItem(DungeonsRewardsAPI.getDungeonLootBag(player, DungeonsAPI.get().getDungeon(0)));
            player.getInventory().addItem(DungeonsRewardsAPI.getDungeonLootBag(player, DungeonsAPI.get().getDungeon(1)));
            player.getInventory().addItem(DungeonsRewardsAPI.getDungeonLootBag(player, DungeonsAPI.get().getDungeon(2)));
            return true;
        }
        if (args[0].equals("saveplayers")) {
            for (DungeonPlayer player3 : CosmicDungeonsPlugin.get_().getApi().getPlayers().values()) {
                player3.save();
            }
            sender.sendMessage("saved " + CosmicDungeonsPlugin.get_().getApi().getPlayers().size() + " DungeonPlayers");
            return true;
        }
        if (args[0].equals("setholidayportal") && sender instanceof Player) {
            Player player = (Player)sender;
            Location l1 = player.getLocation();
            Location l2 = player.getLocation().add(0.0, 1.0, 0.0);
            CosmicDungeonsPlugin.get_().getConfig().set("holiday.portalBlockA", LocationUtils.convertLocationToString(l1));
            CosmicDungeonsPlugin.get_().getConfig().set("holiday.portalBlockB", LocationUtils.convertLocationToString(l2));
            CosmicDungeonsPlugin.get_().saveConfig();
            HolidayPortalTask.setPortalBlockA(l1.getBlock());
            HolidayPortalTask.setPortalBlockB(l2.getBlock());
            player.sendMessage("Set locations of Holiday Portal to...");
            player.sendMessage("l1: " + l1.getBlock().getLocation());
            player.sendMessage("l2: " + l2.getBlock().getLocation());
            return true;
        }
        if (args[0].equals("spawnholidayportal")) {
            HolidayPortalTask.setForceSpawn(true);
            sender.sendMessage("Holiday portal will spawn in next task tick (<60 seconds)");
            return true;
        }
        if (args[0].equals("toggleholidayportals")) {
            CosmicDungeonsPlugin.get_().getConfig().set("holiday.dailyPortals", !CosmicDungeonsPlugin.get_().getConfig().getBoolean("holiday.dailyPortals") ? 1 : 0);
            CosmicDungeonsPlugin.get_().saveConfig();
            sender.sendMessage("dailyPortals => " + CosmicDungeonsPlugin.get_().getConfig().getBoolean("holiday.dailyPortals"));
            return true;
        }
        if (args[0].equalsIgnoreCase("togglecheapkeys")) {
            boolean bl = !CosmicDungeonsPlugin.get().isLowCostKeys();
            CosmicDungeonsPlugin.get().setLowCostKeys(bl);
            CosmicDungeonsPlugin.get().getConfig().set("lowCostKeys", bl);
            CosmicDungeonsPlugin.get().saveConfig();
            sender.sendMessage(CC.Red + "Low Cost Keys: " + bl);
            return true;
        }
        if (args[0].equals("togglekey")) {
            String dungeonName = args[1];
            Dungeon d = DungeonsAPI.get().getDungeonByWorldName(dungeonName);
            d.setKeyRequired(!(d.isKeyRequired()));
            sender.sendMessage("key requirement for next " + d + " dungeon portal: " + d.isKeyRequired());
            return true;
        }
        if (args[0].equals("togglekeybuy") || args[0].equalsIgnoreCase("togglebuy")) {
            DungeonType dungeonType5 = DungeonType.getFromName(args[1]);
            if (dungeonType5 == null) {
                sender.sendMessage(CC.Red + "Invalid dungeon type, valid: " + Arrays.toString(DungeonType.values()));
                return true;
            }
            if (CosmicDungeonsPlugin.get().getDisabledDungeons().contains(dungeonType5)) {
                CosmicDungeonsPlugin.get().getDisabledDungeons().remove(dungeonType5);
                CosmicDungeonsPlugin.get().getConfig().set("dungeons.disabled." + dungeonType5.name(), false);
            } else {
                CosmicDungeonsPlugin.get().getDisabledDungeons().add(dungeonType5);
                CosmicDungeonsPlugin.get().getConfig().set("dungeons.disabled." + dungeonType5.name(), true);
            }
            CosmicDungeonsPlugin.get().saveConfig();
            sender.sendMessage(CC.Red + "Keys toggled from Dungeon Master, disabled: " + CosmicDungeonsPlugin.get().getDisabledDungeons().contains(dungeonType5));
            return true;
        }
        if (args[0].equalsIgnoreCase("cleartoptimes")) {
            for (DungeonType type5 : DungeonType.values()) {
                type5.getDungeonInstance().getTopDungeonTime().clear();
                type5.getDungeonInstance().getTopDungeonTime().save(CosmicDungeonsPlugin.get().getConfig());
                type5.getDungeonInstance().getTopHeroicDungeonTime().clear();
                type5.getDungeonInstance().getTopHeroicDungeonTime().save(CosmicDungeonsPlugin.get().getConfig());
            }
            CosmicDungeonsPlugin.get().saveConfig();
            sender.sendMessage(CC.Red + "Top times cleared!");
            return true;
        }
        if (args[0].equalsIgnoreCase("toggletelecoords")) {
            DungeonType dungeonType6 = DungeonType.DESTROYED_OUTPOST;
            if (dungeonType6.getDungeonInstance() != null) {
                dungeonType6.getDungeonInstance().setGlobalPortal(!dungeonType6.getDungeonInstance().isGlobalPortal());
                sender.sendMessage(CC.RedB + "Set Destroyed Outpost Portal to Global: " + dungeonType6.getDungeonInstance().isGlobalPortal());
                sender.sendMessage(CC.Gray + "This will be reset the next time the dungeon is reset!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("toggleplanetnulglobal") && (dungeonType2 = DungeonType.PLANET_NUL).getDungeonInstance() != null) {
            dungeonType2.getDungeonInstance().setGlobalPortal(!dungeonType2.getDungeonInstance().isGlobalPortal());
            sender.sendMessage(CC.RedB + "Set Planet Nul Portal to Global: " + dungeonType2.getDungeonInstance().isGlobalPortal());
            sender.sendMessage(CC.Gray + "This will be reset the next time the dungeon is reset!");
        }
        if (args[0].equalsIgnoreCase("portalhistory")) {
            sender.sendMessage(CC.Red + "Viewing all logs...");
            CosmicDungeonsPlugin.get().getPortalLogs().forEach((dungeonType, logs) -> {
                LinkedList<PortalLog> logList = Lists.newLinkedList((Iterable)logs);
                logList.sort(Comparator.comparingLong(PortalLog::getPlacetime));
                sender.sendMessage(CC.Red + " " + dungeonType.getDungeonName() + " Portal Log:");
                for (PortalLog log : logList) {
                    sender.sendMessage(" " + log.getPlacer() + " placed at " + log.getX() + "x, " + log.getY() + "y, " + log.getZ() + "z " + TimeUtils.formatDifference(System.currentTimeMillis() - log.getPlacetime()) + " ago");
                }
            });
            return true;
        }
        return true;
    }
}

