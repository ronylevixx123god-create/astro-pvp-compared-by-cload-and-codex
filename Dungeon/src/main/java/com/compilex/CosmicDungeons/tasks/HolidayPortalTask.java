
package com.compilex.CosmicDungeons.tasks;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonState;
import com.compilex.CosmicDungeons.utils.LocationUtils;
import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.TimeZone;

import de.inventivegames.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class HolidayPortalTask
implements Runnable {
    long lastPortalSpawnedTick = 0L;
    public static int spawnHour9AM = 9;
    public static int spawnHour7PM = 19;
    public static LinkedList<Integer> spawnHours = Lists.newLinkedList((Iterable)Lists.newArrayList((Object[])new Integer[]{spawnHour9AM, spawnHour7PM}));
    private static boolean forceSpawn = false;
    private static Block portalBlockA = null;
    private static Block portalBlockB = null;

    public static void setForceSpawn(boolean forceSpawn) {
        HolidayPortalTask.forceSpawn = forceSpawn;
    }

    public static void setPortalBlockA(Block portalBlockA) {
        HolidayPortalTask.portalBlockA = portalBlockA;
    }

    public static void setPortalBlockB(Block portalBlockB) {
        HolidayPortalTask.portalBlockB = portalBlockB;
    }

    public HolidayPortalTask() {
        Location lA = LocationUtils.convertStringToLocation(CosmicDungeonsPlugin.get_().getConfig().getString("holiday.portalBlockA"));
        Location lB = LocationUtils.convertStringToLocation(CosmicDungeonsPlugin.get_().getConfig().getString("holiday.portalBlockB"));
        if (lA != null && lB != null) {
            portalBlockA = lA.getBlock();
            portalBlockB = lB.getBlock();
        }
    }

    @Override
    public void run() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("CST"));
        if (spawnHours.contains(c.get(11)) || forceSpawn) {
            if ((long)MinecraftServerUtils.getCurrentServerTick() - this.lastPortalSpawnedTick < 144000L) {
                return;
            }
            int id = Math.random() <= 0.5 ? 1 : 0;
            final Dungeon dungeon = DungeonsAPI.get().getDungeon(id);
            if (dungeon.getDungeonStatus() == DungeonState.IN_PROGRESS) {
                if (dungeon.getPlayers().size() == 0) {
                    dungeon.reset();
                    return;
                }
                return;
            }
            if (dungeon.getDungeonStatus() == DungeonState.LOADING) {
                return;
            }
            if (portalBlockA == null || portalBlockB == null) {
                return;
            }
            if (portalBlockA.getType() != Material.AIR || portalBlockB.getType() != Material.AIR) {
                return;
            }
            this.lastPortalSpawnedTick = MinecraftServerUtils.getCurrentServerTick();
            Bukkit.broadcastMessage((String)"");
            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON PORTAL ***"));
            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "A dungeon portal to... " + dungeon.getDungeonName()));
            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "will spawn in " + (Object)ChatColor.YELLOW + "5 minutes" + (Object)ChatColor.GOLD + " at: " + (Object)ChatColor.YELLOW + portalBlockA.getX() + "x, " + portalBlockA.getY() + "y, " + portalBlockA.getZ() + "z"));
            Bukkit.broadcastMessage((String)"");
            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                @Override
                public void run() {
                    Bukkit.broadcastMessage((String)"");
                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON PORTAL ***"));
                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "A dungeon portal to... " + dungeon.getDungeonName()));
                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "will spawn in " + (Object)ChatColor.YELLOW + "1 minute" + (Object)ChatColor.GOLD + " at: " + (Object)ChatColor.YELLOW + portalBlockA.getX() + "x, " + portalBlockA.getY() + "y, " + portalBlockA.getZ() + "z"));
                    Bukkit.broadcastMessage((String)"");
                    Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                        @Override
                        public void run() {
                            if (dungeon.getDungeonStatus() == DungeonState.IN_PROGRESS) {
                                if (dungeon.getPlayers().size() == 0) {
                                    dungeon.reset();
                                    return;
                                }
                                return;
                            }
                            if (dungeon.getDungeonStatus() == DungeonState.LOADING) {
                                return;
                            }
                            dungeon.reset();
                            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                                pl.removeMetadata("lastDungeonDeath", (Plugin)CosmicDungeonsPlugin.get_());
                            }
                            portalBlockA.setMetadata("dungeonPortalBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)dungeon.getDungeonID()));
                            portalBlockB.setMetadata("dungeonPortalBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)dungeon.getDungeonID()));
                            portalBlockA.setTypeIdAndData(90, (byte)0, false);
                            portalBlockB.setTypeIdAndData(90, (byte)0, false);
                            CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.add(portalBlockA.getLocation());
                            CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.add(portalBlockB.getLocation());
                            dungeon.setGlobalPortal(true);
                            final Block dungeon_portalBlockA = dungeon.getSpawnLocation().getBlock();
                            final Block dungeon_portalBlockB = dungeon.getSpawnLocation().clone().add(0.0, 1.0, 0.0).getBlock();
                            dungeon_portalBlockA.setMetadata("dungeonPortalBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)dungeon.getDungeonID()));
                            dungeon_portalBlockB.setMetadata("dungeonPortalBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)dungeon.getDungeonID()));
                            dungeon_portalBlockA.setTypeIdAndData(90, (byte)0, false);
                            dungeon_portalBlockB.setTypeIdAndData(90, (byte)0, false);
                            CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.add(dungeon_portalBlockA.getLocation());
                            CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.add(dungeon_portalBlockB.getLocation());
                            try {
                                de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockA.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }

                            try {
                                ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockA.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }

                            try {
                                ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockB.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            portalBlockA.getWorld().playSound(portalBlockA.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 0.35f);
                            dungeon.setDungeonStatus(DungeonState.LOADING);
                            dungeon.setPortalLocation(portalBlockA.getLocation());
                            final Hologram holo_a = HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)portalBlockB.getLocation().add(0.5, 1.25, 0.5), (String[])new String[]{dungeon.getDungeonName()});
                            final Hologram holo_b = HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)dungeon_portalBlockB.getLocation().add(0.5, 1.25, 0.5), (String[])new String[]{(Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "Exit Portal"});
                            if (dungeon.isAbandondedSpaceship()) {
                                dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)new Location(dungeon.getWorld(), -2.0, 82.0, 90.0), (String[])new String[]{(Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "LOCKED", (Object)ChatColor.GRAY + "The airlock will automatically", (Object)ChatColor.GRAY + "unlock once the dungeon portal closes."}));
                            }
                            Bukkit.broadcastMessage((String)"");
                            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON PORTAL ***"));
                            Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "???" + (Object)ChatColor.GOLD + " has spawned a dungeon portal to "));
                            Bukkit.broadcastMessage((String)(dungeon.getDungeonName() + (Object)ChatColor.GOLD + " at: " + (Object)ChatColor.YELLOW + portalBlockA.getX() + "x, " + portalBlockA.getY() + "y, " + portalBlockA.getZ() + "z"));
                            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "The portal will close in " + (Object)ChatColor.UNDERLINE + "100 seconds" + (Object)ChatColor.GOLD + ", queue up fast!"));
                            Bukkit.broadcastMessage((String)"");
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    dungeon.broadcastMessage("The dungeon portal will close in 75s...");
                                }
                            }, 300L);
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    dungeon.broadcastMessage("The dungeon portal will close in 60s...");
                                    dungeon.broadcastTitle("Dungeon Portal", "The dungeon portal will close in 60s...", ChatColor.GOLD, ChatColor.YELLOW);
                                    Bukkit.broadcastMessage((String)"");
                                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON PORTAL ***"));
                                    Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "The dungeon portal to "));
                                    Bukkit.broadcastMessage((String)(dungeon.getDungeonName() + (Object)ChatColor.GOLD + " at: " + (Object)ChatColor.YELLOW + portalBlockA.getX() + "x, " + portalBlockA.getY() + "y, " + portalBlockA.getZ() + "z will close in " + (Object)ChatColor.UNDERLINE + "60 seconds" + (Object)ChatColor.GOLD + ", hurry up!"));
                                    Bukkit.broadcastMessage((String)"");
                                }
                            }, 600L);
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    dungeon.broadcastMessage("The dungeon portal will close in 30s...");
                                    dungeon.broadcastTitle("Dungeon Portal", "The dungeon portal will close in 30s...", ChatColor.GOLD, ChatColor.YELLOW);
                                }
                            }, 1200L);
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    dungeon.broadcastMessage("The dungeon portal will close in 15s...");
                                }
                            }, 1500L);
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    dungeon.broadcastMessage("The dungeon portal will close in 10s...");
                                }
                            }, 1800L);
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    dungeon.broadcastMessage("The dungeon portal will close in 5s...");
                                    dungeon.broadcastTitle("Dungeon Portal", "The dungeon portal will close in 5s...", ChatColor.GOLD, ChatColor.YELLOW);
                                }
                            }, 1900L);
                            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get_(), new Runnable(){

                                @Override
                                public void run() {
                                    portalBlockA.removeMetadata("dungeonPortalBlock", (Plugin)CosmicDungeonsPlugin.get_());
                                    portalBlockB.removeMetadata("dungeonPortalBlock", (Plugin)CosmicDungeonsPlugin.get_());
                                    dungeon_portalBlockA.removeMetadata("dungeonPortalBlock", (Plugin)CosmicDungeonsPlugin.get_());
                                    dungeon_portalBlockB.removeMetadata("dungeonPortalBlock", (Plugin)CosmicDungeonsPlugin.get_());
                                    portalBlockA.getLocation().getChunk().load();
                                    portalBlockA.setType(Material.AIR);
                                    portalBlockB.setType(Material.AIR);
                                    dungeon_portalBlockA.setType(Material.AIR);
                                    dungeon_portalBlockB.setType(Material.AIR);
                                    CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.remove((Object)portalBlockA.getLocation());
                                    CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.remove((Object)portalBlockB.getLocation());
                                    CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.remove((Object)dungeon_portalBlockA.getLocation());
                                    CosmicDungeonsPlugin.get_().getDungeonPortalListener().portalBlocks.remove((Object)dungeon_portalBlockB.getLocation());
                                    holo_a.delete();
                                    holo_b.delete();
                                    portalBlockA.getWorld().playSound(portalBlockA.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 0.35f);
                                    try {
                                        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockA.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }

                                    try {
                                        ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockA.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }

                                    try {
                                        ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockB.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                    dungeon_portalBlockA.getWorld().playSound(portalBlockA.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 0.35f);
                                    try {
                                        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockA.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }

                                    try {
                                        ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockA.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }

                                    try {
                                        ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), portalBlockB.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                    Bukkit.broadcastMessage((String)"");
                                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON PORTAL ***"));
                                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "The portal has closed, " + (Object)ChatColor.UNDERLINE + dungeon.getPlayers().size() + (Object)ChatColor.GOLD + " player(s) made it into the"));
                                    Bukkit.broadcastMessage((String)(dungeon.getDungeonName() + (Object)ChatColor.GOLD + " /dungeon!"));
                                    Bukkit.broadcastMessage((String)"");
                                    if (dungeon.getPlayers().size() > 0) {
                                        dungeon.setDungeonStatus(DungeonState.IN_PROGRESS);
                                    } else {
                                        dungeon.setDungeonStatus(DungeonState.EMPTY);
                                    }
                                    dungeon.getPlayers().stream().filter(pl -> !pl.hasMetadata("NPC")).forEach(pl -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("contests add " + pl.getName() + " dungeonsEntered 1")));
                                }
                            }, 2000L);
                        }
                    }, 1200L);
                }
            }, 4800L);
        }
    }
}

