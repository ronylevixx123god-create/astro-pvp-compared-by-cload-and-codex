/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.compilex.CosmicDungeons.utils;

import org.bukkit.Bukkit;

public class Logger {
    public static boolean debug = false;

    public static void debug(String s) {
        if (debug) {
            Bukkit.getLogger().info("[CosmicDungeons (DEBUG)] " + s);
        }
    }

    public static void info(String s) {
        Bukkit.getLogger().info("[CosmicDungeons] " + s);
    }
}

