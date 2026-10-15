/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.Essentials;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SmarterEssentials
        extends ArkPlugin {
    protected static Essentials ess3;
    protected static boolean factions;

    static {
        factions = false;
    }

    public SmarterEssentials(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            ess3 = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }
        factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
        if (ess3 != null) {
            if (ess3.getSettings().getTpaAcceptCancellation() != 30L) {
                Bukkit.getLogger().info("[Arkkit (SmarterEssentials)] Setting tpa-accept-cancellation to 30s, reloading Essentials config...");
                Path path = Paths.get("plugins/Essentials/config.yml");
                Charset charset = StandardCharsets.UTF_8;
                try {
                    String content = new String(Files.readAllBytes(path), charset);
                    content = content.replaceAll("tpa-accept-cancellation: 120", "tpa-accept-cancellation: 30");
                    Files.write(path, content.getBytes(charset));
                    ess3.reload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.registerListener(new SmarterEssentialsListener());
            if (factions) {
                this.registerListener(new SellSanity());
                this.registerListener(new HomeSanityListener());
                //this.registerTask(new NoEvilPotionEffects().runTaskTimer(Arkkit.get(), 15L, 15L), SmarterEssentials.class);
            }
        }
    }

    @Override
    public void disable() {
    }
}

