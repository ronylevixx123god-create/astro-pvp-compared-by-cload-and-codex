/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Scoreboard
 */
package net.astropvp.core.cosmictoggles.manager;

import com.google.common.collect.Lists;
import net.astropvp.core.AstroCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TogglesManager {
    private File toggleFile;
    private FileConfiguration config;
    private final List<UUID> scoreboardToggles = new ArrayList<UUID>();

    public void loadToggles() {
        try {
            this.toggleFile = new File(AstroCore.getInstance().getDataFolder(), "toggles.yml");
            if (!this.toggleFile.exists()) {
                this.toggleFile.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.config = YamlConfiguration.loadConfiguration(this.toggleFile);
        this.scoreboardToggles.addAll(this.config.getStringList("scoreboard-toggles").stream().map(UUID::fromString).collect(Collectors.toList()));
    }

    public void clearScoreboard(Player player) {
        player.setMetadata("toggle_scoreboard", new FixedMetadataValue(AstroCore.getInstance(), ""));
    }

    public void saveToggles() {
        if (!this.toggleFile.exists()) {
            try {
                this.toggleFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList list = Lists.newArrayList();
        list.addAll(this.scoreboardToggles.stream().map(UUID::toString).collect(Collectors.toList()));
        this.config.set("scoreboard-toggles", list);
        try {
            this.config.save(this.toggleFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public List<UUID> getScoreboardToggles() {
        return this.scoreboardToggles;
    }
}

