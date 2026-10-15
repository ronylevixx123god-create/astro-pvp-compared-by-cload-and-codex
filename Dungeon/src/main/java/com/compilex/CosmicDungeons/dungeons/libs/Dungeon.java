/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TitleManager
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TitleManager$TitleColor
 *  com.gmail.filoghost.holograms.api.Hologram
 *  com.massivecraft.factions.Faction
 *  me.ifamasssxd.cosmictoggles.CosmicToggles
 *  me.ifamasssxd.cosmictoggles.toggles.RegisteredToggle
 *  org.apache.commons.lang.Validate
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.events.AbandonedShipEvents;
import com.compilex.CosmicDungeons.dungeons.events.DestroyedOutpostEvents;
import com.compilex.CosmicDungeons.dungeons.events.DungeonEvents;
import com.compilex.CosmicDungeons.dungeons.events.PlanetNulEvents;
import com.compilex.CosmicDungeons.events.DungeonStateChangeEvent;
import com.compilex.CosmicDungeons.items.DungeonKeyItem;
import com.compilex.CosmicDungeons.items.KeyPair;
import com.compilex.CosmicDungeons.struct.DungeonChallenge;
import com.compilex.CosmicDungeons.struct.DungeonDebuff;
import com.compilex.CosmicDungeons.utils.EssentialsUtils;
import com.compilex.CosmicDungeons.utils.LocationUtils;
import com.compilex.CosmicDungeons.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.gmail.filoghost.holograms.api.Hologram;
import com.massivecraft.factions.Faction;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.github.paperspigot.Title;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Dungeon {
    public World world;
    private final String dungeonName;
    private final String worldName;
    private final LinkedList<String> dungeonDescription;
    private final int dungeonID;
    private DungeonState dungeonStatus;
    private Location spawnLocation;
    private Location portalLocation;
    private long portalPlaceCooldown;
    private DungeonEvents dungeonEvents;
    private TimeRestrictionTask timeTask;
    private long dungeonStartTime;
    private boolean bossSlayerTrinket;
    private Hologram objectiveHologram;
    private String protectedFor;
    private boolean heroic;
    private boolean keyRequired = true;
    private boolean globalPortal = false;
    private final Set<DungeonChallenge> dungeonChallengesCompleted = new HashSet<DungeonChallenge>();
    private final Set<DungeonDebuff> dungeonDebuffs = new HashSet<DungeonDebuff>();
    private boolean playersDied = false;
    private boolean bossSpawned = false;
    private boolean onARoll = true;
    private long bossSpawnTime;
    public DungeonType dungeonType;
    protected Random random = new Random();
    private final TopDungeonTime topDungeonTime;
    private final TopHeroicDungeonTime topHeroicDungeonTime;
    private ItemStack key;
    public boolean resetting = false;
    private ItemStack dungeonPortal;

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public boolean isHeroic() {
        return this.heroic;
    }

    public void setHeroic(boolean heroic) {
        this.heroic = heroic;
    }

    public String getDungeonName() {
        if (this.isHeroic()) {
            return CC.translate("&b&k!!!&f&l&nHeroic&5&k!!!&r ") + this.dungeonName;
        }
        return this.dungeonName;
    }

    public String getRealDungeonName() {
        return this.dungeonName;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public LinkedList<String> getDungeonDescription() {
        return this.dungeonDescription;
    }

    public int getDungeonID() {
        return this.dungeonID;
    }

    public DungeonState getDungeonStatus() {
        return this.dungeonStatus;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public void setPortalLocation(Location portalLocation) {
        this.portalLocation = portalLocation;
    }

    public Location getPortalLocation() {
        return this.portalLocation;
    }

    public long getPortalPlaceCooldown() {
        return this.portalPlaceCooldown;
    }

    public void setPortalPlaceCooldown(long portalPlaceCooldown) {
        this.portalPlaceCooldown = portalPlaceCooldown;
    }

    public DungeonEvents getDungeonEvents() {
        return this.dungeonEvents;
    }

    public TimeRestrictionTask getTimeTask() {
        return this.timeTask;
    }

    public long getDungeonStartTime() {
        return this.dungeonStartTime;
    }

    public Hologram getObjectiveHologram() {
        return this.objectiveHologram;
    }

    public void setKeyRequired(boolean keyRequired) {
        this.keyRequired = keyRequired;
    }

    public void setGlobalPortal(boolean globalPortal) {
        this.globalPortal = globalPortal;
    }

    public boolean isKeyRequired() {
        return this.keyRequired;
    }

    public boolean isGlobalPortal() {
        return this.globalPortal;
    }

    public boolean isHalfTime() {
        return this.getDungeonDebuffs().contains(DungeonDebuff.HALF_OBJECTIVE_TIME);
    }

    public Set<DungeonChallenge> getDungeonChallengesCompleted() {
        return this.dungeonChallengesCompleted;
    }

    public Set<DungeonDebuff> getDungeonDebuffs() {
        return this.dungeonDebuffs;
    }

    public Set<DungeonDebuff> getRandomDungeonDebuffs() {
        HashSet<DungeonDebuff> copy = new HashSet<DungeonDebuff>(this.dungeonDebuffs);
        copy.remove(DungeonDebuff.BOSSES_DEAL_MORE_DAMAGE);
        copy.remove(DungeonDebuff.BOSSES_TAKE_LESS_DAMAGE);
        copy.remove(DungeonDebuff.HALF_OBJECTIVE_TIME);
        return copy;
    }

    public void setPlayersDied(boolean playersDied) {
        this.playersDied = playersDied;
    }

    public void setBossSpawned(boolean bossSpawned) {
        this.bossSpawned = bossSpawned;
    }

    public void setOnARoll(boolean onARoll) {
        this.onARoll = onARoll;
    }

    public boolean isPlayersDied() {
        return this.playersDied;
    }

    public boolean isBossSpawned() {
        return this.bossSpawned;
    }

    public boolean isOnARoll() {
        return this.onARoll;
    }

    public void setBossSpawnTime(long bossSpawnTime) {
        this.bossSpawnTime = bossSpawnTime;
    }

    public long getBossSpawnTime() {
        return this.bossSpawnTime;
    }

    public DungeonType getDungeonType() {
        return this.dungeonType;
    }

    public String getProtectedFor() {
        return this.protectedFor;
    }

    public void setProtectedFor(String facId) {
        this.protectedFor = facId;
    }

    public void setBossSlayerTrinket(boolean f) {
        this.bossSlayerTrinket = f;
    }

    public boolean getBossSlayerTrinket() {
        return this.bossSlayerTrinket;
    }

    public Random getRandom() {
        return this.random;
    }

    public TopHeroicDungeonTime getTopHeroicDungeonTime() {
        return this.topHeroicDungeonTime;
    }

    public TopDungeonTime getTopDungeonTime() {
        return this.topDungeonTime;
    }

    public void addPlayer(Player player) {
    }

    public String toString() {
        return ChatColor.stripColor(this.dungeonName) + " (id:" + this.dungeonID + ") (world:" + this.world.getName() + ")";
    }

    public int getDungeonMenuSlot() {
        return this.dungeonID + 18 + 1 + 1;
    }

    public int getDungeonKeyMenuSlot() {
        return this.dungeonID + 27 + 1 + 1;
    }

    public int getHeroicDungeonKeyMenuSlot() {
        return this.dungeonID + 27 + 1 + 1 + 9;
    }

    public String getPermissionNode() {
        return "cosmic.dungeons." + this.worldName;
    }

    public boolean playerHasKey(Player player) {
        for (ItemStack item : player.getInventory()) {
            KeyPair keyData;
            if (item == null || item.getType() == Material.AIR || (keyData = DungeonKeyItem.getDataFromKey(new NBTWrapper(item))) == null) continue;
            Long expiration = keyData.getValue();
            if (expiration > 0L && expiration < System.currentTimeMillis()) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "Removing expired dungeon key " + ItemUtils.getDisplayName(item) + CC.Red + "!");
                player.getInventory().removeItem(item);
                continue;
            }
            if (keyData.getKey() != this.dungeonType) continue;
            return true;
        }
        return false;
    }

    public boolean removePlayerKey(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            KeyPair keyData;
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR || (keyData = DungeonKeyItem.getDataFromKey(new NBTWrapper(item))) == null) continue;
            Long expiration = keyData.getValue();
            if (expiration > 0L && expiration <= System.currentTimeMillis()) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "Removing expired dungeon key " + ItemUtils.getDisplayName(item) + CC.Red + "!");
                player.getInventory().setItem(i, null);
                continue;
            }
            if (keyData.getKey() != this.dungeonType) continue;
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItem(i, null);
            }
            return true;
        }
        return false;
    }

    public ItemStack getKey() {
        if (this.key == null) {
            this.key = new DungeonKeyItem(this.dungeonType).build();
        }
        return this.key;
    }

    public boolean playerHasPermission(Player p) {
        if (p.hasPermission(this.getPermissionNode())) {
            return true;
        }
        Faction faction = FactionUtils.getFaction(p);
        if (faction == null || !faction.isNormal()) {
            return false;
        }
        String idIn = faction.getId();
        String foundOnStart = CosmicDungeonsPlugin.get().getFactionsInsideOnEnable().get(p.getUniqueId());
        return foundOnStart != null && foundOnStart.equals(idIn);
    }

    public boolean playerHasCooldown(DungeonPlayer player) {
        if (player.getBukkitPlayer().isOp()) return false;
        return !player.getCooldowns().canPlayerDoDungeon(this);
    }

    public boolean hasCompletedRecently(DungeonPlayer playerData, long hoursToCheck) {
        Long lastCompleted = playerData.getLastCompleted().get(this.getDungeonType());
        if (playerData.getBukkitPlayer().isOp()) return true;
        return lastCompleted != null && System.currentTimeMillis() - lastCompleted <= TimeUnit.HOURS.toMillis(hoursToCheck);
    }

    public void setSpawnLocation(Location l) {
        this.spawnLocation = l;
        CosmicDungeonsPlugin.get_().getConfig().set("dungeons." + this.getWorldName() + ".spawnLocation", LocationUtils.convertLocationToString(l));
    }

    public List<Player> getPlayers() {
        return this.getWorld().getPlayers();
    }

    public Set<Player> getValidPlayers() {
        HashSet<Player> retr = new HashSet<Player>();
        this.getPlayers().forEach(pl -> {
            if (pl.isOp() && EssentialsUtils.isVanished(pl)) {
                return;
            }
            retr.add(pl);
        });
        return retr;
    }

    public void teleportPlayers(Location toTeleportTo, Collection<Player> players) {
        Location finalToTeleportTo = toTeleportTo.clone();
        players.forEach(p -> this.teleportPlayer(finalToTeleportTo, p, true));
    }

    public void teleportPlayer(Location location, Player p, boolean forceLoad) {
        p.setMetadata("bypassCombatTeleport", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), location));
        p.setMetadata("queuedTeleportPass", new FixedMetadataValue(Arkkit.get(), location));
        Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicDungeonsPlugin.get(), () -> p.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN), 1L);
    }

    public Set<UUID> getValidPlayerUUIDs() {
        HashSet<UUID> retr = new HashSet<UUID>();
        this.getPlayers().forEach(pl -> {
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(pl);
            if (dp == null || !this.equals(dp.getCurrentDungeon())) {
                return;
            }
            retr.add(pl.getUniqueId());
        });
        return retr;
    }

    public void broadcastMessage(String message) {
        for (Player p : this.getPlayers()) {
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "(DUNGEON) " + ChatColor.RESET + message);
        }
    }

    public void broadcastTitle(String title, String subTitle, ChatColor titleColor, ChatColor subColor) {
//        RegisteredToggle toggle = CosmicToggles.get().getTogglesManager().getRegisteredToggle("dungeonobjective");
        for (Player p : this.getPlayers()) {
//            if (!CosmicToggles.get().getTogglesManager().isToggleEnabled(p, toggle)) continue;
            if (title != null && subTitle == null) {
                p.sendTitle(new Title(titleColor + title, "", 10, 40, 10));
            }
            if (title != null && subTitle != null) continue;
            p.sendTitle(new Title(titleColor + title, subColor + subTitle, 10, 40, 10));
        }
    }

    public Dungeon(World dungeonWorld, DungeonType type, String dungeonName, LinkedList<String> dungeonDescription) {
        this.world = dungeonWorld;
        this.worldName = this.world.getName();
        this.dungeonName = ChatColor.translateAlternateColorCodes('&', dungeonName);
        this.dungeonID = CosmicDungeonsPlugin.get_().getApi().nextDungeonID++;
        this.dungeonStatus = DungeonState.EMPTY;
        this.spawnLocation = CosmicDungeonsPlugin.get_().getConfig().contains("dungeons." + this.worldName + ".spawnLocation") ? LocationUtils.convertStringToLocation(CosmicDungeonsPlugin.get_().getConfig().getString("dungeons." + this.worldName + ".spawnLocation")) : this.world.getSpawnLocation();
        this.dungeonDescription = new LinkedList();
        for (String s : dungeonDescription) {
            this.dungeonDescription.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        this.dungeonType = type;
        this.dungeonEvents = this.dungeonType.createNewListenerInstance(this);
        Bukkit.getPluginManager().registerEvents(this.dungeonEvents, CosmicDungeonsPlugin.get_());
        this.topDungeonTime = new TopDungeonTime(type, CosmicDungeonsPlugin.get_().getConfig().getConfigurationSection("top-times"));
        this.topHeroicDungeonTime = new TopHeroicDungeonTime(type, CosmicDungeonsPlugin.get_().getConfig().getConfigurationSection("top-heroic-times"));
    }

    public boolean isResetting() {
        return this.resetting;
    }

    public void reset() {
        if (this.resetting) {
            return;
        }
        this.resetting = true;
        this.globalPortal = false;
        this.heroic = false;
        try {
            if (this.isDestroyedOutpost() || this.isPlanetNul()) {
                HandlerList.unregisterAll(this.dungeonEvents);
            }
            DungeonsAPI.get().resetDungeonWorldIO(this);
            this.setPortalLocation(null);
            this.setObjectiveHologram(null);
            this.world = Bukkit.getWorld(this.worldName);
            this.setProtectedFor("-1");
            this.setBossSlayerTrinket(false);
            this.spawnLocation = CosmicDungeonsPlugin.get_().getConfig().contains("dungeons." + this.worldName + ".spawnLocation") ? LocationUtils.convertStringToLocation(CosmicDungeonsPlugin.get_().getConfig().getString("dungeons." + this.worldName + ".spawnLocation")) : this.world.getSpawnLocation();
            if (!CosmicDungeonsPlugin.get_().isDisableCalled()) {
                HandlerList.unregisterAll(this.dungeonEvents);
                if (this.getDungeonType().equals(DungeonType.ABANDONED_SHIP)) {
                    ((AbandonedShipEvents)this.dungeonEvents).unregisterTasks();
                    this.dungeonEvents = new AbandonedShipEvents(this);
                } else if (this.getDungeonType().equals(DungeonType.DESTROYED_OUTPOST)) {
                    ((DestroyedOutpostEvents)this.dungeonEvents).unregisterTasks();
                    this.dungeonEvents = new DestroyedOutpostEvents(this);
                } else if (this.getDungeonType().equals(DungeonType.PLANET_NUL)) {
                    ((PlanetNulEvents)this.dungeonEvents).unregisterTasks();
                }
                this.playersDied = false;
                this.bossSpawned = false;
                this.onARoll = true;
                this.bossSpawnTime = 0L;
                this.dungeonChallengesCompleted.clear();
                this.dungeonDebuffs.clear();
                if (this.dungeonEvents instanceof AbandonedShipEvents) {
                    ((AbandonedShipEvents)this.dungeonEvents).reset();
                } else if (this.dungeonEvents instanceof DestroyedOutpostEvents) {
                    ((DestroyedOutpostEvents)this.dungeonEvents).reset();
                } else if (this.dungeonEvents instanceof PlanetNulEvents) {
                    ((PlanetNulEvents)this.dungeonEvents).reset();
                }
                Bukkit.getPluginManager().registerEvents(this.dungeonEvents, Arkkit.get());
                this.setDungeonStatus(DungeonState.EMPTY);
                this.endTimeRestrictionTask();
            }
        }
        catch (IOException e) {
            Bukkit.getLogger().info("Unable to unload world: " + this.getWorld() + " Retrying?");
            e.printStackTrace();
            this.resetting = false;
            this.reset();
        }
        this.resetting = false;
    }

    public void setDungeonStatus(DungeonState newState) {
        DungeonStateChangeEvent event = new DungeonStateChangeEvent(this, newState);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (this.dungeonStatus != DungeonState.LOADING && newState == DungeonState.LOADING && this.dungeonType.equals(DungeonType.DESTROYED_OUTPOST)) {
                ((DestroyedOutpostEvents)this.dungeonEvents).restart();
            }
            if (this.dungeonStatus != DungeonState.LOADING && newState == DungeonState.LOADING && this.dungeonType.equals(DungeonType.PLANET_NUL)) {
                ((PlanetNulEvents)this.dungeonEvents).restart();
            }
            this.dungeonStatus = newState;
            if (this.dungeonStatus == DungeonState.IN_PROGRESS) {
                this.dungeonStartTime = System.currentTimeMillis();
            }
            if (this.dungeonStatus == DungeonState.COMPLETE) {
                this.setKeyRequired(true);
            }
        }
    }

    public void endDungeon(DungeonEndReason reason) {
        for (Player p : this.getPlayers()) {
            p.sendMessage("");
            p.sendMessage(reason.getDetailColor() + "" + ChatColor.BOLD + "Dungeon End");
            p.sendMessage(reason.getDetailText());
            p.sendMessage("");
            p.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
        this.setKeyRequired(true);
        this.reset();
    }

    public void endTimeRestrictionTask() {
        if (this.timeTask != null) {
            Bukkit.getScheduler().cancelTask(this.timeTask.getTaskId());
            this.timeTask = null;
        }
    }

    public long getTimeSinceDungeonStart() {
        return System.currentTimeMillis() - this.dungeonStartTime;
    }

    public void updateTimeRestrictionTask(long deadline, String objective) {
        if (this.timeTask == null) {
            this.timeTask = new TimeRestrictionTask(this, deadline, objective);
            this.timeTask.runTaskTimer(CosmicDungeonsPlugin.get_(), 20L, 20L);
        } else {
            double timeThreshholdRequired;
            long timeLeft = this.timeTask.getExpireTime() - System.currentTimeMillis();
            if ((double)timeLeft < (timeThreshholdRequired = (double)this.timeTask.getInitialDifference() * 0.25) && timeThreshholdRequired > 0.0) {
                this.onARoll = false;
                Bukkit.getLogger().info("Dungeon: " + this.getDungeonID() + " (" + this.dungeonType.name() + ") is no longer eligible for 'On a Roll' due to completing in " + timeLeft + " time, < " + timeThreshholdRequired);
            } else {
                Bukkit.getLogger().info("Still on a roll by completing " + this.timeTask.getObjectiveName() + " with " + timeLeft + " left, > " + timeThreshholdRequired + " required");
            }
            this.timeTask.setExpireTime(deadline);
            this.timeTask.setObjectiveName(objective);
        }
        this.broadcastMessage(ChatColor.GOLD + "NEW OBJECTIVE: " + CC.YellowU + objective);
        this.broadcastMessage(ChatColor.GOLD + "Time Limit: " + ChatColor.GREEN + TimeUtils.formatDifference(deadline + 1000L - System.currentTimeMillis()));
    }

    public void setObjectiveHologram(Hologram h) {
        if (this.objectiveHologram != null) {
            this.objectiveHologram.delete();
        }
        if (h != null) {
            this.objectiveHologram = h;
        }
    }

    public boolean isAbandondedSpaceship() {
        return this.dungeonType == DungeonType.ABANDONED_SHIP;
    }

    public boolean isDestroyedOutpost() {
        return this.dungeonType == DungeonType.DESTROYED_OUTPOST;
    }

    public boolean isPlanetNul() {
        return this.dungeonType == DungeonType.PLANET_NUL;
    }

    public void setDungeonPortal(ItemStack dungeonPortal) {
        this.dungeonPortal = dungeonPortal;
    }

    public ItemStack getDungeonPortal() {
        return this.dungeonPortal.clone();
    }
}

