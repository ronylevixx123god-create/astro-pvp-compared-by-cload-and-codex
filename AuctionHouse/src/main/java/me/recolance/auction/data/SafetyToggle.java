/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package me.recolance.auction.data;

import me.recolance.auction.Auction;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SafetyToggle {
    private List<String> toggles = new ArrayList<String>();
    private List<String> notifytoggles = new ArrayList<String>();
    private File file = new File(Auction.getPlugin().getDataFolder(), "toggles.yml");
    private YamlConfiguration config;

    public SafetyToggle() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration((File) this.file);
        this.toggles = this.config.getStringList("toggles");
        this.notifytoggles = this.config.getStringList("notify-toggles");
    }

    public boolean isToggled(Player player) {
        return this.toggles.contains(player.getUniqueId().toString());
    }

    public boolean isBidMessageToggled(Player player) {
        return this.notifytoggles.contains(player.getUniqueId().toString());
    }

    public void saveFile() {
        try {
            this.config.set("toggles", this.toggles);
            this.config.set("notify-toggles", this.notifytoggles);
            this.config.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getToggles() {
        return this.toggles;
    }

    public List<String> getNotifytoggles() {
        return this.notifytoggles;
    }
}

