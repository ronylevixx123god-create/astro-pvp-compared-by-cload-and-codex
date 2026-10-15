/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.struct.log;

import org.bukkit.Bukkit;

public class CosmicLog {
    public static boolean debugEnabled = false;
    public static boolean debugUUID = false;

    public static void logDebug(String header, String debug) {
        if (!debugEnabled) {
            return;
        }
        Bukkit.getLogger().info((header != null ? header : "") + " " + debug);
    }

    public static void logDebug(String debug) {
        CosmicLog.logDebug(null, debug);
    }
}

