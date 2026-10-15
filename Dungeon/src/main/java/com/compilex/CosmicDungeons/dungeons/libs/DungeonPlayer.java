/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.libs.CooldownData;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.compilex.CosmicDungeons.utils.Logger;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DungeonPlayer {
    private UUID playerUUID;
    private String playerName;
    private Player bukkitPlayer;
    private CooldownData cooldowns;
    private YamlConfiguration playerConfig;
    private File playerConfigFile;
    private Map<DungeonType, Long> lastCompleted = new HashMap<DungeonType, Long>();
    private Dungeon currentDungeon;

    public CooldownData getCooldowns() {
        return this.cooldowns;
    }

    public Map<DungeonType, Long> getLastCompleted() {
        return this.lastCompleted;
    }

    public YamlConfiguration getPlayerConfig() {
        return this.playerConfig;
    }

    public File getPlayerConfigFile() {
        return this.playerConfigFile;
    }

    public Dungeon getCurrentDungeon() {
        return this.currentDungeon;
    }

    public DungeonPlayer(Player p) {
        this.bukkitPlayer = p;
        this.playerName = p.getName();
        this.playerUUID = p.getUniqueId();
        this.playerConfigFile = new File("plugins/CosmicDungeons/playerData/" + this.bukkitPlayer.getUniqueId().toString() + ".yml");
        if (!this.playerConfigFile.exists()) {
            try {
                this.playerConfigFile.createNewFile();
                Logger.debug("Created empty playerData file for " + this.playerName + " (" + this.playerUUID + ")");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.playerConfig = YamlConfiguration.loadConfiguration((File)this.playerConfigFile);
        this.cooldowns = new CooldownData(p, this.playerConfig);
        this.loadLastDungeonCompletions();
    }

    public void setBukkitPlayer(Player player) {
        this.cooldowns.setCooldownPlayer(player);
        this.bukkitPlayer = player;
    }

    public void save() {
        if (this.playerConfig != null) {
            try {
                this.playerConfig.save(this.playerConfigFile);
                Logger.debug("Saved playerData file of " + this.playerName + " (" + this.playerUUID + ")");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Logger.info("Failed to save " + this.playerName + "'s PlayerDungeonData to disc.");
        }
    }

    public void setCurrentDungeon(Dungeon dungeon) {
        this.currentDungeon = dungeon;
    }

    public boolean isOnline() {
        return this.bukkitPlayer != null && this.bukkitPlayer.isOnline();
    }

    public void leaveDungeon() {
        Location exit;
        if (this.currentDungeon == null) {
            return;
        }
        this.bukkitPlayer.removeMetadata("dungeonKeyUsed_" + this.currentDungeon.getDungeonType().getInternalName(), (Plugin)CosmicDungeonsPlugin.get());
        Location location = exit = this.currentDungeon.getPortalLocation() != null ? this.currentDungeon.getPortalLocation() : ((World)Bukkit.getWorlds().get(0)).getSpawnLocation();
        if (!this.bukkitPlayer.teleport(exit, PlayerTeleportEvent.TeleportCause.UNKNOWN)) {
            Bukkit.getLogger().info("[CosmicDungeons] Failed to execute .leaveDungeon() for player " + (Object)this.bukkitPlayer + " due to cancelled teleport event.");
        }
        this.currentDungeon = null;
    }

    public void leaveDungeon(boolean f) {
        if (this.currentDungeon == null) {
            return;
        }
        this.bukkitPlayer.removeMetadata("dungeonKeyUsed_" + this.currentDungeon.getDungeonType().getInternalName(), (Plugin)CosmicDungeonsPlugin.get());
        Location exit = this.currentDungeon.getPortalLocation() != null ? this.currentDungeon.getPortalLocation() : ((World)Bukkit.getWorlds().get(0)).getSpawnLocation();
        this.bukkitPlayer.setMetadata("bypassCombatTeleport", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)exit));
        if (!this.bukkitPlayer.teleport(exit, PlayerTeleportEvent.TeleportCause.UNKNOWN)) {
            Bukkit.getLogger().info("[CosmicDungeons] Failed to execute .leaveDungeon() for player " + (Object)this.bukkitPlayer + " due to cancelled teleport event.");
        }
        this.currentDungeon.getPlayers().remove((Object)this.bukkitPlayer);
        this.currentDungeon = null;
    }

    public void logDungeonAttempt(Dungeon d) {
        this.playerConfig.set("dungeons.stats." + d.getWorldName() + ".attempts", (Object)(this.playerConfig.contains("dungeons.stats." + d.getWorldName() + ".attempts") ? this.playerConfig.getInt("dungeons.stats." + d.getWorldName() + ".attempts") + 1 : 1));
    }

    public void logDungeonComplete(Dungeon d) {
        this.playerConfig.set("dungeons.stats." + d.getWorldName() + ".lastComplete", (Object)System.currentTimeMillis());
        this.lastCompleted.put(d.getDungeonType(), System.currentTimeMillis());
        this.playerConfig.set("dungeons.stats." + d.getWorldName() + ".wins", (Object)(this.playerConfig.contains("dungeons.stats." + d.getWorldName() + ".wins") ? this.playerConfig.getInt("dungeons.stats." + d.getWorldName() + ".wins") + 1 : 1));
    }

    private void loadLastDungeonCompletions() {
        if (this.playerConfig == null) {
            return;
        }
        for (DungeonType type : DungeonType.values()) {
            long lastCompleted = this.playerConfig.getLong("dungeons.stats." + type.getInternalName() + ".lastComplete");
            if (lastCompleted <= -1L) continue;
            this.lastCompleted.put(type, lastCompleted);
        }
    }

    public Player getBukkitPlayer() {
        if (this.bukkitPlayer != null && this.bukkitPlayer.isValid()) {
            return this.bukkitPlayer;
        }
        this.bukkitPlayer = Bukkit.getPlayer((UUID)this.playerUUID);
        return this.bukkitPlayer;
    }
}

