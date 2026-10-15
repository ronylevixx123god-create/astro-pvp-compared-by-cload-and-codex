/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.collect.Lists
 *  com.massivecraft.factions.Faction
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsRewardsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TopDungeonTime {
    private long timeCompleted;
    private int secondsCompletedIn;
    private List<String> factionsWhoCompleted = Lists.newArrayList();
    private DungeonType dungeonType;
    private int completeCount;

    public void setFactionsWhoCompleted(List<String> factionsWhoCompleted) {
        this.factionsWhoCompleted = factionsWhoCompleted;
    }

    public void setTimeCompleted(long timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public void setSecondsCompletedIn(int secondsCompletedIn) {
        this.secondsCompletedIn = secondsCompletedIn;
    }

    public void setDungeonType(DungeonType dungeonType) {
        this.dungeonType = dungeonType;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public List<String> getFactionsWhoCompleted() {
        return this.factionsWhoCompleted;
    }

    public long getTimeCompleted() {
        return this.timeCompleted;
    }

    public int getSecondsCompletedIn() {
        return this.secondsCompletedIn;
    }

    public DungeonType getDungeonType() {
        return this.dungeonType;
    }

    public int getCompleteCount() {
        return this.completeCount;
    }

    public TopDungeonTime(DungeonType type, ConfigurationSection section) {
        if (section == null) {
            return;
        }
        this.dungeonType = type;
        this.timeCompleted = section.getLong(type.name() + ".timestamp");
        this.secondsCompletedIn = section.getInt(type.name() + ".fastest-time");
        this.factionsWhoCompleted = section.getStringList(type.name() + ".factions");
        this.completeCount = section.getInt(type.name() + ".times-completed", 0);
    }

    public void clear() {
        this.timeCompleted = 0L;
        this.secondsCompletedIn = 0;
        this.completeCount = 0;
        this.factionsWhoCompleted = Lists.newArrayList();
    }

    public void checkLatestTime(int seconds, Dungeon dungeon, List<Player> players, Set<UUID> whoCouldReceiveLoot) {
        int requiredToBeat = (int)Math.floor((double)this.secondsCompletedIn * 0.98);
        if (this.secondsCompletedIn == 0 || seconds <= requiredToBeat) {
            CosmicDungeonsPlugin.get().getLogger().info("Required to beat: " + requiredToBeat + " previouslyCompleted: " + this.secondsCompletedIn + " new: " + seconds);
            if (this.secondsCompletedIn == 0) {
                Bukkit.broadcastMessage((String)(CC.GoldB + "(!) " + CC.Gold + "Their team has set the time for fastest dungeon completion: " + CC.GoldU + TimeUtils.formatSeconds((long)seconds)));
            } else {
                Bukkit.broadcastMessage((String)(CC.GoldB + "(!) " + CC.Gold + "They have beat the previous fastest completion time of: " + CC.GoldU + TimeUtils.formatSeconds((long)this.secondsCompletedIn) + CC.Gold + "!"));
            }
            if (this.completeCount >= 10 || dungeon.getDungeonType() == DungeonType.PLANET_NUL) {
                Bukkit.broadcastMessage((String)(CC.Gold + " They received a bonus Dungeon Lootbag!"));
                players.forEach(pl -> {
                    if (!whoCouldReceiveLoot.contains(pl.getUniqueId())) {
                        pl.sendMessage(CC.RedB + "(!) " + CC.Red + "You qualified for a bonus Dungeon Lootbag because of setting a new runtime record, but you already received this dungeon loot recently!");
                    } else {
                        PlayerUtil.giveOrDropProtectedItem((Player)pl, (ItemStack)DungeonsRewardsAPI.getDungeonLootBag(pl, dungeon), (int)-1, null, (String)"Your inventory was full, dropped on the ground!");
                    }
                });
            } else {
                players.forEach(pl -> pl.sendMessage(CC.GoldB + "(!)" + CC.Gold + " Your group defeated the current fastest completion record for " + this.dungeonType.getDungeonName() + CC.Gold + ". However, because the dungeon has not yet been cleared 10 times this map, you will not receive the record time bonus."));
            }
            this.secondsCompletedIn = seconds;
            this.timeCompleted = System.currentTimeMillis();
            this.factionsWhoCompleted = Lists.newArrayList();
            players.forEach(pl -> {
                Faction fac = FactionUtils.getFaction((Player)pl, (boolean)false);
                if (fac == null || !fac.isNormal() || this.factionsWhoCompleted.contains(fac.getTag())) {
                    return;
                }
                this.factionsWhoCompleted.add(fac.getTag());
            });
            ++this.completeCount;
            this.saveData();
            return;
        }
        ++this.completeCount;
        this.saveData();
    }

    public void saveData() {
        this.save(CosmicDungeonsPlugin.get().getConfig());
        CosmicDungeonsPlugin.get().saveConfig();
    }

    public void save(FileConfiguration config) {
        config.set("top-times." + this.dungeonType.name() + ".timestamp", (Object)this.timeCompleted);
        config.set("top-times." + this.dungeonType.name() + ".fastest-time", (Object)this.secondsCompletedIn);
        config.set("top-times." + this.dungeonType.name() + ".factions", this.factionsWhoCompleted);
        config.set("top-times." + this.dungeonType.name() + ".times-completed", (Object)this.completeCount);
    }
}

