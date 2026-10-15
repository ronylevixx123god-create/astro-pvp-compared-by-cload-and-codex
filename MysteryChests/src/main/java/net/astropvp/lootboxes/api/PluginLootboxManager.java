/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.astropvp.lootboxes.api;

import org.bukkit.plugin.java.JavaPlugin;

public interface PluginLootboxManager
        extends LootboxManager {
    JavaPlugin getManagingPlugin();

    Lootbox createManagedLootbox(String var1);

    boolean isHandleable();
}

