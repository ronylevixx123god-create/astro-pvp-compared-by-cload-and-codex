/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

public class PotionFix
        extends ArkPlugin {
    protected static boolean[] EnabledFixes = new boolean[3];
    protected static int[] Amounts = new int[2];

    public PotionFix(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        PotionFix.EnabledFixes[0] = true;
        PotionFix.EnabledFixes[1] = true;
        PotionFix.EnabledFixes[2] = true;
        PotionFix.Amounts[0] = 6;
        PotionFix.Amounts[1] = 6;
        this.registerListener(new PotionFixListener());
    }

    @Override
    public void disable() {
    }
}

