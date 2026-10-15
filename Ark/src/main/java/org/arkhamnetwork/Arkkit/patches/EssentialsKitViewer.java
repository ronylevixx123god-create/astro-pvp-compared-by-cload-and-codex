/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.Essentials;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EssentialsKitViewer
        extends ArkPlugin {
    public static LinkedHashMap<String, Material> kitData = new LinkedHashMap();
    public static String kit_inventory_name;
    private static Essentials ess = null;
    private final String configuration_path = "plugins/Arkkit/kitViewer.dat";

    public EssentialsKitViewer(Plugin p) {
        super(p);
    }

    public static Essentials getEssentials() {
        return ess;
    }

    @Override
    public void enable() {
        kit_inventory_name = Arkkit.get().getConfig().getString("language.kit_viewer.kit_inventory_name");
        ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        LinkedHashMap<String, String> config_data = null;
        try {
            this.getClass();
            config_data = ConfigUtils.getConfigurationMap(new File("plugins/Arkkit/kitViewer.dat"));
        } catch (IOException err) {
            Bukkit.getLogger().warning("Failed to load plugins/Arkkit/kitViewer.dat!");
            err.printStackTrace();
        }
        if (config_data != null) {
            Material m = null;
            for (Map.Entry<String, String> data : config_data.entrySet()) {
                block10:
                {
                    try {
                        if (ess.getKits().getKit(data.getKey()) == null) {
                            Bukkit.getLogger().warning("Invalid kit name specified, '" + data.getKey() + "', skipping.");
                        }
                        break block10;
                    } catch (Exception e) {
                        Bukkit.getLogger().warning("Invalid kit name specified, '" + data.getKey() + "', skipping.");
                        e.printStackTrace();
                    }
                    continue;
                }
                try {
                    m = Material.valueOf(data.getValue().toUpperCase());
                } catch (Exception err) {
                    Bukkit.getLogger().warning("Invalid material enum '" + data.getValue() + "' specified for " + data.getKey() + ", defaulting to stone.");
                    err.printStackTrace();
                    m = Material.STONE;
                }
                if (m == null) {
                    Bukkit.getLogger().warning("Invalid material enum '" + data.getValue() + "' specified for " + data.getKey() + ", defaulting to stone.");
                    m = Material.STONE;
                }
                kitData.put(data.getKey(), m);
                Bukkit.getLogger().info("Loaded kit " + data.getKey() + " with MATERIAL." + m.name());
            }
        }
        this.registerListener(new EssentialsKitViewerListener());
    }

    @Override
    public void disable() {
    }
}

