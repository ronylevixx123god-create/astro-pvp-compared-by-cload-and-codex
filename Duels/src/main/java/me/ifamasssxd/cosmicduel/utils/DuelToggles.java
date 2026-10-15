/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmicduel.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelToggles
extends File {
    private static final long serialVersionUID = 1L;
    public static YamlConfiguration config;
    List<String> toggles = new ArrayList<String>();

    public DuelToggles() {
        super(CosmicDuel.get().getDataFolder(), "dueltoggles.yml");
        this.create();
        this.toggles = config.getStringList("Toggles");
    }

    public boolean hasDuelsToggled(Player p) {
        return this.toggles.contains(p.getUniqueId().toString());
    }

    public boolean togglePlayer(Player p) {
        if (this.toggles.contains(p.getUniqueId().toString())) {
            this.toggles.remove(p.getUniqueId().toString());
            return false;
        }
        this.toggles.add(p.getUniqueId().toString());
        return true;
    }

    public void create() {
        if (!this.exists()) {
            try {
                this.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration((File)this);
    }

    public void save(boolean async) {
        if (async) {
            new BukkitRunnable(){

                public void run() {
                    try {
                        config.set("Toggles", DuelToggles.this.toggles);
                        config.save((File)DuelToggles.this);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously((Plugin)CosmicDuel.get());
        } else {
            try {
                config.set("Toggles", this.toggles);
                config.save((File)this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

