/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.cosmicpvp.cosmicutils.inventory;

import org.bukkit.plugin.java.JavaPlugin;

public class SmartInvsPlugin
        extends JavaPlugin {
    private static SmartInvsPlugin instance;
    private static InventoryManager invManager;

    public void onEnable() {
        instance = this;
        invManager = new InventoryManager(this);
        invManager.init();
    }

    public static InventoryManager manager() {
        return invManager;
    }

    public static SmartInvsPlugin instance() {
        return instance;
    }
}

