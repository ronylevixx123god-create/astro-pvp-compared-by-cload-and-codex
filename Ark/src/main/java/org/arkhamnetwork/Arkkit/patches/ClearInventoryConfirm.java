/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONUtils
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.JSONUtils;
import com.earth2me.essentials.Essentials;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClearInventoryConfirm
        extends ArkPlugin {
    public static final String clearInventoryPending = "compilex_clearinventory_pending";
    public static final String clearInventoryConfirmed = "compilex_clearinventory_confirmed";
    private final Set<UUID> clearInventoryToggled = new HashSet<UUID>();
    Essentials ess;
    private File clearInventoryToggles;
    private FileConfiguration config;

    public ClearInventoryConfirm(Plugin p) {
        super(p);
    }

    @Override
    public void enable() throws IOException {
        this.ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        this.registerListener(new ClearInventoryListener(this));
        this.registerCommand(new CommandToggleClearInventoryConfirm(this));
        this.clearInventoryToggles = JSONUtils.getOrCreateFile(this.getBukkitPlugin().getDataFolder(), "ci_toggles.yml");
        this.config = YamlConfiguration.loadConfiguration(this.clearInventoryToggles);
        List<String> toggled = this.config.getStringList("ci_toggled");
        if (toggled != null) {
            for (String toggle : toggled) {
                this.clearInventoryToggled.add(UUID.fromString(toggle));
            }
        }
    }

    @Override
    public void disable() {
        List toggled = this.clearInventoryToggled.stream().map(UUID::toString).collect(Collectors.toList());
        this.config.set("ci_toggled", toggled);
        try {
            this.config.save(this.clearInventoryToggles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<UUID> getClearInventoryToggled() {
        return this.clearInventoryToggled;
    }
}

