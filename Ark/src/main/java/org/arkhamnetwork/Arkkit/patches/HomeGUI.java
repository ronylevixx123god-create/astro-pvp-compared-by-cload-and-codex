/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class HomeGUI
        extends ArkPlugin {
    private HomeGUIManager manager;

    public HomeGUI(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (EssentialsUtils.getEssentials() == null) {
            Bukkit.getLogger().severe("Disabling EssentialsHome patch due to no essentials.");
            this.kill();
            return;
        }
        this.manager = new HomeGUIManager();
        this.manager.loadHomes();
        this.registerListener(new HomeGUIListener(this.manager));
    }

    @Override
    public void disable() {
        this.manager.saveHomes();
        this.unregisterListeners();
    }

    public HomeGUIManager getManager() {
        return this.manager;
    }
}

