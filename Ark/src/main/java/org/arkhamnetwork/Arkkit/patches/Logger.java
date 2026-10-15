/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Bukkit;

final class Logger {
    Logger() {
    }

    public void notice(String s, Class<?> c) {
        Bukkit.getLogger().info("[AntiCrash (" + c.getSimpleName() + ")] " + s);
    }

    public void debug(String s, Class<?> c) {
        Bukkit.getLogger().info("(DE) [AntiCrash (" + c.getSimpleName() + ")] " + s);
    }

    public void log(String s, Class<?> c) {
        Bukkit.getLogger().info("[AntiCrash (" + c.getSimpleName() + ")] " + s);
    }

    public void warning(String s, Class<?> c) {
        Bukkit.getLogger().info("[AntiCrash (" + c.getSimpleName() + ")] " + s);
    }

    public void error(String s, Class<?> c) {
        Bukkit.getLogger().info("[AntiCrash (" + c.getSimpleName() + ")] " + s);
    }
}

