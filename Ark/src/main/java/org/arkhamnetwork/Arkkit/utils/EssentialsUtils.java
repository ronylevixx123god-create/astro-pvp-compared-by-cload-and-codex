/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 */
package org.arkhamnetwork.Arkkit.utils;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;

public class EssentialsUtils {
    private static Essentials essentials;

    public static Essentials getEssentials() {
        if (essentials == null) {
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }
        return essentials;
    }
}

