/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.betterpay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.astropvp.core.AstroCore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PayToggle {
    private File file;
    private YamlConfiguration config;
    private List<String> toggles = new ArrayList<String>();

    public PayToggle(String fileName) {
        if (!AstroCore.getInstance().getDataFolder().exists()) {
            AstroCore.getInstance().getDataFolder().mkdir();
        }
        this.file = new File(AstroCore.getInstance().getDataFolder(), fileName);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration((File)this.file);
        this.toggles = this.config.getStringList("toggles");
    }

    public void saveToggles() {
        this.config.set("toggles", this.toggles);
        try {
            this.config.save(this.file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasToggle(Player p) {
        return this.toggles.contains(p.getUniqueId().toString());
    }

    public boolean togglePay(Player p) {
        String uuid = p.getUniqueId().toString();
        if (this.toggles.contains(uuid)) {
            this.toggles.remove(uuid);
        } else {
            this.toggles.add(uuid);
        }
        return this.toggles.contains(uuid);
    }
}

