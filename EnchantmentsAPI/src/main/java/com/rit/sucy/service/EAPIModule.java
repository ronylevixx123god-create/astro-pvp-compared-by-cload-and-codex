/*
 * Decompiled with CFR 0.150.
 */
package com.rit.sucy.service;

import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.service.IModule;

public abstract class EAPIModule
implements IModule {
    protected final EnchantmentAPI plugin;

    protected EAPIModule(EnchantmentAPI plugin) {
        this.plugin = plugin;
    }
}

