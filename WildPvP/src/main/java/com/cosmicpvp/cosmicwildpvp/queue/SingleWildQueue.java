package com.cosmicpvp.cosmicwildpvp.queue;

import com.cosmicpvp.cosmicwildpvp.Cages;
import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import com.cosmicpvp.cosmicwildpvp.util.BryUtil;
import com.cosmicpvp.cosmicwildpvp.util.InvincibilityTimer;

import org.bukkit.command.CommandSender;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.iface.RelationParticipator;
import org.bukkit.OfflinePlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FPlayer;
import org.bukkit.inventory.ItemStack;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.UUID;
import java.util.Map;

public class SingleWildQueue
{
    protected Map<UUID, Location> previousLocationMap;
    protected Location creationLocation;
    protected InvincibilityTimer invincibilityTimer;
    protected Player host;
    protected Player opponent;
    protected QueueState state;
    protected long timeInitiated;
    protected boolean hidden;
    protected boolean cageMatch;
    protected Map<String, Boolean> settingKeyMap;

    public SingleWildQueue(final Player host) {
        this.previousLocationMap = Maps.newHashMap();
        this.host = host;
        this.creationLocation = host.getLocation();
        this.timeInitiated = System.currentTimeMillis();
        this.settingKeyMap = Maps.newHashMap();
        this.state = QueueState.IDLE;
    }

    public ItemStack getQueueDisplayItem(final Player viewer) {
        final FPlayer fviewer = (FPlayer)FPlayers.getInstance().getByPlayer(viewer);
        final FPlayer fhost = (FPlayer)FPlayers.getInstance().getByPlayer(this.host);
        final ChatColor relationColor = fviewer.getColorTo((RelationParticipator)fhost);
        final List<String> lore = Lists.newArrayList();
        lore.add("&7" + TimeUtils.formatSeconds((long)this.getUptime()));
        lore.add("");
        lore.add("&a&lPlayer HP&a: &a&n" + new Double(this.host.getHealth()).intValue());
        lore.add("");
        lore.add("&a&lFaction");
        lore.add(" &f" + fhost.getFaction().getTag(fviewer) + " &7(&a" + fhost.getFaction().getOnlinePlayers().size() + "&7 online)");
        lore.add("");
        lore.add("&a&lNearby");
        lore.add(" &f" + BryUtil.getNearbyPlayers(this.host, 64).size() + " Player(s)");
        lore.add("");
        lore.add("&7Left-Click to accept their request.");
        lore.add("&7Right-Click to view their inventory.");
        if (!this.cageMatch) {
            return BryUtil.createPlayerSkull(this.host.getName(), relationColor.toString() + ChatColor.BOLD + this.host.getName(), lore);
        }
        lore.add(1, "&e&lCAGE MATCH ENABLED");
        return BryUtil.createItem(Material.IRON_FENCE, relationColor.toString() + ChatColor.BOLD + this.host.getName(), lore);
    }

    public void addOpponentToQueue(final Player opponent) {
        this.opponent = opponent;
        this.state = QueueState.INVINCIBILITY;
        this.invincibilityTimer = new InvincibilityTimer(this, 7);
        this.previousLocationMap.put(opponent.getUniqueId(), opponent.getLocation());
        WildPvPPlugin.tpNow(opponent, this.creationLocation);
        opponent.sendMessage("");
        BryUtil.sendMessage((CommandSender)opponent, "&a&l(!)&a You have joined " + this.host.getName() + "'s Wild PvP Queue.");
        opponent.sendMessage("");
        this.host.sendMessage("");
        BryUtil.sendMessage((CommandSender)this.host, "&a&l(!) &f&l" + opponent.getName() + " &ahas joined &nYOUR&a Wild PvP Queue.\n");
        this.host.sendMessage("");
    }

    public void sendOpponentToPreviousLocation() {
        WildPvPPlugin.tpNow(this.opponent, this.previousLocationMap.get(this.opponent.getUniqueId()));
        this.previousLocationMap.remove(this.opponent.getUniqueId());
    }

    public void removeOpponentAndRequeue() {
        BryUtil.sendMessage((CommandSender)this.host, "&c&l(!)&c&l " + this.opponent.getName() + "&c has left the &lWild PvP Queue&c.");
        BryUtil.sendMessage((CommandSender)this.host, "    &7&o(( You've been added back to the queue ))");
        this.opponent = null;
        this.state = QueueState.IDLE;
    }

    public boolean containsPlayer(final Player player) {
        return this.host == player || this.opponent == player;
    }

    public List<Player> getBoth() {
        return Lists.newArrayList(this.host, this.opponent);
    }

    public void cancelQueue(final Player whoCancelled) {
        this.removeInvincibility();
        this.getBoth().stream().filter(player -> player != null).forEach(player -> BryUtil.sendMessage(player, "&c&l(!) &c" + whoCancelled.getName() + " has left the pvp queue."));
        if (this.opponent != null) {
            this.sendOpponentToPreviousLocation();
            if (this.opponent == whoCancelled) {
                this.removeOpponentAndRequeue();
            }
        }
        if (this.host == whoCancelled) {
            this.state = QueueState.CANCELLED;
            WildPvPPlugin.getInstance().getQueueManager().removeExisitingQueue(this);
        }
    }

    public void removeInvincibility() {
        if (this.invincibilityTimer != null) {
            this.invincibilityTimer.remove();
            this.invincibilityTimer = null;
        }
    }

    public void hideChunkBorder() {
        BryUtil.getChunkBorderLocations(this.creationLocation, 3).forEach(loc -> loc.getBlock().getState().update());
    }

    public void showChunkBorder() {
        if (this.cageMatch) {
            WildPvPPlugin.getInstance().getCages().spawnCage(this.host.getLocation().getChunk(), this.host.getLocation().getBlockY());
        }
        else {
            BryUtil.getChunkBorderLocations(this.creationLocation, 3).forEach(loc -> this.host.sendBlockChange(loc, Material.STAINED_GLASS, (byte)14));
        }
    }

    public int getUptime() {
        return (int)(System.currentTimeMillis() - this.timeInitiated) / 1000;
    }

    public void endSuccesfully() {
        this.state = QueueState.END;
        Cages.noClaimUntil.put(this.creationLocation.getChunk(), System.currentTimeMillis() + 90000L);
        WildPvPPlugin.getInstance().getQueueManager().removeExisitingQueue(this);
    }

    protected static int getSecondsLEftSinceTabbed(final Player pl) {
        if (pl.hasMetadata("cl_combat") && pl.getMetadata("cl_combat").get(0) != null) {
            return (int)(15000L - System.currentTimeMillis() - pl.getMetadata("cl_combat").get(0).asLong()) / 1000;
        }
        return 0;
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

    public boolean isCageMatch() {
        return this.cageMatch;
    }

    public Map<String, Boolean> getSettingKeyMap() {
        return this.settingKeyMap;
    }

    public void setPreviousLocationMap(final Map<UUID, Location> previousLocationMap) {
        this.previousLocationMap = previousLocationMap;
    }

    public void setCreationLocation(final Location creationLocation) {
        this.creationLocation = creationLocation;
    }

    public void setInvincibilityTimer(final InvincibilityTimer invincibilityTimer) {
        this.invincibilityTimer = invincibilityTimer;
    }

    public void setHost(final Player host) {
        this.host = host;
    }

    public void setOpponent(final Player opponent) {
        this.opponent = opponent;
    }

    public void setState(final QueueState state) {
        this.state = state;
    }

    public void setTimeInitiated(final long timeInitiated) {
        this.timeInitiated = timeInitiated;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public void setCageMatch(final boolean cageMatch) {
        this.cageMatch = cageMatch;
    }

    public void setSettingKeyMap(final Map<String, Boolean> settingKeyMap) {
        this.settingKeyMap = settingKeyMap;
    }
}
