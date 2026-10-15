/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package org.arkhamnetwork.Arkkit;

import org.bukkit.Material;

public class CustomPV {
    String customName;
    Material icon = Material.EMERALD;

    CustomPV() {
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Material getIcon() {
        return this.icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}

