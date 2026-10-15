package com.astropvp.wildpvp.queue;

import com.astropvp.wildpvp.util.InvincibilityTimer;
import com.astropvp.wildpvp.util.KrizzUtil;
import com.astropvp.wildpvp.AstroWildPvP;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.astropvp.wildpvp.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class WildQueue {
    private Map<UUID, Location> previousLocationMap = Maps.newHashMap();
    private Location creationLocation;
    private InvincibilityTimer invincibilityTimer;
    private Player host;
    private Player opponent;
    private QueueState state;
    private long timeInitiated;
    private boolean hidden;
    private Set<UUID> denied;

    public void setPreviousLocationMap(Map<UUID, Location> previousLocationMap) {
        this.previousLocationMap = previousLocationMap;
    }

    public void setCreationLocation(Location creationLocation) {
        this.creationLocation = creationLocation;
    }

    public void setInvincibilityTimer(InvincibilityTimer invincibilityTimer) {
        this.invincibilityTimer = invincibilityTimer;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void setState(QueueState state) {
        this.state = state;
    }

    public void setTimeInitiated(long timeInitiated) {
        this.timeInitiated = timeInitiated;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setDenied(Set<UUID> denied) {
        this.denied = denied;
    }

    public Map<UUID, Location> getPreviousLocationMap() {
        return this.previousLocationMap;
    }

    public Location getCreationLocation() {
        return this.creationLocation;
    }

    public InvincibilityTimer getInvincibilityTimer() {
        return this.invincibilityTimer;
    }

    public Player getHost() {
        return this.host;
    }

    public Player getOpponent() {
        return this.opponent;
    }

    public QueueState getState() {
        return this.state;
    }

    public long getTimeInitiated() {
        return this.timeInitiated;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public Set<UUID> getDenied() {
        return this.denied;
    }

    public WildQueue(Player host) {
        this.host = host;
        this.creationLocation = host.getLocation();
        this.timeInitiated = System.currentTimeMillis();
        this.denied = Sets.newHashSet();
        this.state = QueueState.IDLE;
    }

    public ItemStack getQueueDisplayItem(Player viewer) {
        FPlayer fviewer = (FPlayer)FPlayers.getInstance().getByPlayer(viewer);
        FPlayer fhost = (FPlayer)FPlayers.getInstance().getByPlayer(this.host);
        ChatColor relationColor = fviewer.getColorTo(fhost);
        List<String> lore = Lists.newArrayList();
        lore.add("&7" + TimeUtil.getTime(this.getUptime()));
        lore.add("");
        lore.add("&a&lPlayer HP&a: &a&n" + (new Double(this.host.getHealth())).intValue());
        lore.add("");
        lore.add("&a&lFaction");
        lore.add(" &f" + fhost.getFaction().getTag(fviewer) + " &7(&a" + fhost.getFaction().getOnlinePlayers().size() + "&7 online)");
        lore.add("");
        lore.add("&a&lNearby");
        lore.add(" &f" + KrizzUtil.getNearbyPlayers(this.host, 32).size() + " Player(s)");
        lore.add("");
        lore.add("&7Left-Click to accept their request.");
        lore.add("&7Right-Click to view their inventory.");
        return KrizzUtil.createPlayerSkull(this.host.getName(), relationColor.toString() + ChatColor.BOLD + this.host.getName(), (List)lore);
    }

    public void addOpponentToQueue(Player opponent) {
        this.opponent = opponent;
        this.state = QueueState.INVINCIBILITY;
        this.invincibilityTimer = new InvincibilityTimer(this, 7);
        this.previousLocationMap.put(opponent.getUniqueId(), opponent.getLocation());
        opponent.teleport(this.creationLocation, TeleportCause.UNKNOWN);
        opponent.sendMessage("");
        KrizzUtil.sendMessage((LivingEntity)opponent, (String)("&a&l(!)&a You have joined &f&l" + this.host.getName() + "&a's Wild PvP Queue."));
        opponent.sendMessage("");
        this.host.sendMessage("");
        KrizzUtil.sendMessage((LivingEntity)this.host, (String)("&a&l(!) &f&l" + opponent.getName() + " &ahas joined &nYOUR&a Wild PvP Queue."));
        this.host.sendMessage("");
    }

    public void sendOpponentToPreviousLocation() {
        this.opponent.teleport((Location)this.previousLocationMap.get(this.opponent.getUniqueId()), TeleportCause.UNKNOWN);
        this.previousLocationMap.remove(this.opponent.getUniqueId());
    }

    public void removeOpponentAndRequeue() {
        KrizzUtil.sendMessage((LivingEntity)this.host, (String)("&c&l(!)&c&l " + this.opponent.getName() + "&c has left the &lWild PvP Queue&c."));
        KrizzUtil.sendMessage((LivingEntity)this.host, (String)"    &7&o(( You've been added back to the queue ))");
        this.opponent = null;
        this.state = QueueState.IDLE;
    }

    public boolean containsPlayer(Player player) {
        return this.host == player || this.opponent == player;
    }

    public List<Player> getBoth() {
        return Lists.newArrayList(new Player[]{this.host, this.opponent});
    }

    public void cancelQueue(Player whoCancelled, boolean teleportToSpawn) {
        this.removeInvincibility();
        this.getBoth().stream().filter((player) -> {
            return player != null;
        }).forEach((player) -> {
            KrizzUtil.sendMessage((LivingEntity)player, (String)("&c&l(!) &c" + whoCancelled.getName() + " has left the pvp queue."));
        });
        if (this.opponent != null) {
            this.sendOpponentToPreviousLocation();
            if (this.opponent == whoCancelled) {
                this.removeOpponentAndRequeue();
            }
        }

        if (this.host == whoCancelled) {
            if (teleportToSpawn) {
                this.host.teleport(Bukkit.getWorld("world").getSpawnLocation(), TeleportCause.UNKNOWN);
            }

            AstroWildPvP.getInstance().getQueueManager().removeExisitingQueue(this);
        }

    }

    public void removeInvincibility() {
        if (this.invincibilityTimer != null) {
            this.invincibilityTimer.remove();
            this.invincibilityTimer = null;
        }

    }

    public void hideChunkBorder() {
        KrizzUtil.getChunkBorderLocations(this.creationLocation, 3).forEach((loc) -> {
            loc.getBlock().getState().update();
        });
    }

    public void showChunkBorder() {
        KrizzUtil.getChunkBorderLocations(this.creationLocation, 3).forEach((loc) -> {
            this.host.sendBlockChange(loc, Material.STAINED_GLASS, (byte)14);
        });
    }

    public int getUptime() {
        return (int)(System.currentTimeMillis() - this.timeInitiated) / 1000;
    }

    public void endSuccesfully() {
        this.state = QueueState.END;
        AstroWildPvP.getInstance().getQueueManager().removeExisitingQueue(this);
    }
}
