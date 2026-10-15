/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.struct.newduel;

import lombok.Getter;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.struct.Arena;
import me.ifamasssxd.cosmicduel.struct.Kit;
import me.ifamasssxd.cosmicduel.struct.Settings;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import me.ifamasssxd.cosmicduel.struct.enums.*;
import me.ifamasssxd.cosmicduel.utils.JSONMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class NewDuel {
    public final int PVP_TIMER = 120;
    protected HashMap<UUID, StoredPlayer> storedPlayers = new HashMap();
    protected HashMap<UUID, Location> originalLocation = new HashMap();
    protected List<Player> players = new ArrayList<Player>();
    protected CopyOnWriteArrayList<Player> spectators = new CopyOnWriteArrayList();
    protected List<String> pendingInvites = new ArrayList<String>();
    protected List<Location> envoys = new ArrayList<Location>();
    protected Stakes stakes = new Stakes();
    protected Settings settings;
    protected Kit kit = Kit.BYOB;
    protected Arena arena;
    protected DuelState duelState = DuelState.IN_QUEUE;
    boolean isRanked = false;
    protected DuelCreateState createState = DuelCreateState.WAITING;
    protected boolean cancelled = false;
    protected boolean inQueueMenu = false;
    protected boolean globalQueued = false;
    protected NewStakeInventory stakeInventory;
    private HashMap<String, UUID> endingInventoryCache = new HashMap();
    protected int gameTimer = 0;
    protected int duelID;
    private int pvpTimer = 120;
    protected List<TeamData> teams;
    protected Map<UUID, Integer> hitsMap;
    protected Map<UUID, Integer> longestComboMap;
    protected Map<UUID, Integer> currentComboMap;
    protected Map<UUID, Double> damageMap;

    public DecimalFormat getDf() {
        return df;
    }

    private DecimalFormat df = new DecimalFormat("##.#");


    public Map<UUID, Double> getDamageMap() {
        return damageMap;
    }

    public Map<UUID, Integer> getLongestComboMap() {
        return longestComboMap;
    }

    public Map<UUID, Integer> getCurrentComboMap() {
        return currentComboMap;
    }

    public NewDuel(Player initiator, List<String> pendingInvites) {
        this.addPlayer(initiator, false);
        this.settings = new Settings(this);
        this.pendingInvites = pendingInvites;
        this.hitsMap = new HashMap<>();
        this.currentComboMap = new HashMap<>();
        this.longestComboMap = new HashMap<>();
        this.damageMap = new HashMap<>();
    }

    public NewDuel(Player initiator, List<String> pendingInvites, boolean isRanked) {
        this.addPlayer(initiator, false);
        this.settings = new Settings(this);
        this.pendingInvites = pendingInvites;
        this.hitsMap = new HashMap<>();
        this.currentComboMap = new HashMap<>();
        this.longestComboMap = new HashMap<>();
        this.isRanked = isRanked;
        this.damageMap = new HashMap<>();
    }

    public double getAverageDamage(UUID uuid) {
        return Double.parseDouble(df.format(damageMap.getOrDefault(uuid, 0.0)/hitsMap.getOrDefault(uuid, 1)));
    }

    public Map<UUID, Integer> getHitsMap() {
        return hitsMap;
    }

    public abstract String getName();

    public abstract DuelType getDuelType();

    public abstract int getPlayersToStart();

    public abstract void addPlayer(Player var1, boolean var2);

    public abstract void onDuelEnd(EndReason var1, Player var2);

    public abstract void prepareSpectator(Player var1);

    public abstract void removeSpectator(Player var1);

    public abstract void clearSpectators();

    public abstract void onStart();

    public abstract void start();

    public abstract void onEnd(boolean var1);

    public abstract void removePlayer(Player var1, Reason var2, boolean var3);

    public abstract void resetPlayerStats(Player var1);

    public abstract void returnItems(Player var1, boolean var2);

    public abstract void returnPlayerToStartLocation(Player var1);

    public abstract void resetPlayer(Player var1);

    public abstract List<Player> getPlayers();

    public abstract List<Player> getSpectators();

    public abstract boolean isCurrentlyInDuel();

    public abstract Player getOppositePlayer(Player var1);

    public abstract void applySettings(Player var1);

    public abstract void applyKit(Player var1);

    public abstract void awardStakes(Player var1, Player var2);

    public abstract void returnStakes();

    public abstract void giveWinnerPage(Player var1, Player var2);

    public abstract Inventory getDroppedItemsInventory(Player var1);

    public abstract void resetEnvoys();


    public abstract void onConfirmedQueue();

    public void sendMessage(String msg, boolean includeSpectators) {
        for (Player p : this.players) {
            p.sendMessage(msg);
        }
        if (includeSpectators) {
            for (Player p : this.spectators) {
                p.sendMessage(msg);
            }
        }
    }

    public void sendJsonMessage(JSONMessage msg, boolean includeSpectators) {
        for (Player p : this.players) {
            msg.sendToPlayer(p);
        }
        if (includeSpectators) {
            for (Player p : this.spectators) {
                msg.sendToPlayer(p);
            }
        }
    }

    public String getColoredMessage(String msg) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)msg);
    }

    public String getColoredMessage(String msg, Object ... args) {
        return String.format(this.getColoredMessage(msg), args);
    }

    public void forceTeleport(Player p, Location to) {
        p.setMetadata("ignore_combat_tag", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), (Object)""));
        p.teleport(to, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        p.removeMetadata("ignore_combat_tag", (Plugin)CosmicDuel.get());
    }

    public List<String> getPendingInvites() {
        return this.pendingInvites;
    }

    public List<Location> getEnvoys() {
        return this.envoys;
    }

    public Stakes getStakes() {
        return this.stakes;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Kit getKit() {
        return this.kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }


    public Arena getArena() {
        return this.arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public DuelState getDuelState() {
        return this.duelState;
    }

    public void setDuelState(DuelState duelState) {
        this.duelState = duelState;
    }

    public boolean isRanked() {
        return this.isRanked;
    }

    public void setRanked(boolean isRanked) {
        this.isRanked = isRanked;
    }

    public DuelCreateState getCreateState() {
        return this.createState;
    }

    public void setCreateState(DuelCreateState createState) {
        this.createState = createState;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void forceTeleportToSpectator(Player p, Location to) {
        Bukkit.getScheduler().runTaskLater((Plugin)CosmicDuel.get(), () -> {
            p.setMetadata("bypassCombatTeleport", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), to));
            p.teleport(to, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        },1L);
    }

    public boolean isInQueueMenu() {
        return this.inQueueMenu;
    }

    public boolean isGlobalQueued() {
        return this.globalQueued;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setInQueueMenu(boolean inQueueMenu) {
        this.inQueueMenu = inQueueMenu;
    }

    public void setGlobalQueued(boolean globalQueued) {
        this.globalQueued = globalQueued;
    }

    public NewStakeInventory getStakeInventory() {
        return this.stakeInventory;
    }

    public void setStakeInventory(NewStakeInventory stakeInventory) {
        this.stakeInventory = stakeInventory;
    }

    public HashMap<String, UUID> getEndingInventoryCache() {
        return this.endingInventoryCache;
    }

    public int getGameTimer() {
        return this.gameTimer;
    }

    public void setGameTimer(int gameTimer) {
        this.gameTimer = gameTimer;
    }

    public int getDuelID() {
        return this.duelID;
    }

    public int getPvpTimer() {
        return this.pvpTimer;
    }

    public void setPvpTimer(int pvpTimer) {
        this.pvpTimer = pvpTimer;
    }
}

