/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChatLimiter
        extends ArkPlugin {
    protected static String store_url = "";
    protected static int default_delay = 3;
    protected static int vip_delay = 1;

    public ChatLimiter(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        store_url = Arkkit.get().getConfig().contains("language.spigot_branding.store_url") ? Arkkit.get().getConfig().getString("language.spigot_branding.store_url") : "our shop";
        default_delay = Arkkit.get().getConfig().getInt("settings.chat_limiter.default_delay");
        vip_delay = Arkkit.get().getConfig().getInt("settings.chat_limiter.vip_delay");
        this.registerListener(new ChatLimiterListener());
    }

    @Override
    public void disable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.removeMetadata("last_chat_message", this.getBukkitPlugin());
        }
    }
}

