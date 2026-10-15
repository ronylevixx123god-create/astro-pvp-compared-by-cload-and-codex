/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicmysterychest;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShardAnimations {
    private File file;
    private YamlConfiguration config;
    private List<String> toggles = new ArrayList<String>();

    public ShardAnimations(String fileName) {
        if (!CosmicMysteryChest.get().getDataFolder().exists()) {
            CosmicMysteryChest.get().getDataFolder().mkdir();
        }
        this.file = new File(CosmicMysteryChest.get().getDataFolder(), fileName);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration((File) this.file);
        this.toggles = this.config.getStringList("toggles");
    }

    public void saveToggles() {
        this.config.set("toggles", this.toggles);
        try {
            this.config.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasToggle(Player p) {
        return this.toggles.contains(p.getUniqueId().toString());
    }

    public boolean anotoggle(Player p) {
        String uuid = p.getUniqueId().toString();
        if (this.toggles.contains(uuid)) {
            this.toggles.remove(uuid);
            this.toggles.add(uuid);
        } else {
            this.toggles.add(uuid);
        }
        return true;
    }
}

