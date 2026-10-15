/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Material;

import java.util.concurrent.ConcurrentHashMap;

class CustomHomes
        extends ConcurrentHashMap<String, Material> {
    CustomHomes() {
    }

    public boolean isCustomHome(String homeName) {
        return this.containsKey(homeName);
    }
}

