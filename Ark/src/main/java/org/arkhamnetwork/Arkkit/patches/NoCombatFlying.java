/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class NoCombatFlying
        extends ArkPlugin {
    public NoCombatFlying(Plugin p) {
        super(p);
    }

    protected static boolean inCombat(String p_name) {
        if (Bukkit.getPlayer(p_name) != null) {
            Player pl = Bukkit.getPlayer(p_name);
            try {
                if (pl.hasMetadata("ncf_combat") && pl.getMetadata("ncf_combat").get(0) != null && System.currentTimeMillis() - pl.getMetadata("ncf_combat").get(0).asLong() <= 10000L) {
                    return true;
                }
            } catch (Exception err) {
                err.printStackTrace();
                pl.setMetadata("ncf_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
            }
        }
        return false;
    }

    @Override
    public void enable() {
        this.registerListener(new NoCombatFlyingListener());
    }

    @Override
    public void disable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.removeMetadata("nfc_combat", Arkkit.get());
        }
    }
}

