/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.MinecraftServer
 *  net.minecraft.util.io.netty.util.ResourceLeakDetector
 *  net.minecraft.util.io.netty.util.ResourceLeakDetector$Level
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import io.netty.util.ResourceLeakDetector;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.LinkedList;

public class AntiLag
        extends ArkPlugin {
    LinkedList<Double> previousTPS;
    private long startTime;
    private boolean rebooting = false;

    public AntiLag(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.startTime = System.currentTimeMillis();
        this.previousTPS = new LinkedList();
        try {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskTimer(Arkkit.get(), new Runnable() {

            @Override
            public void run() {
                if (AntiLag.this.rebooting) {
                    return;
                }
                AntiLag.this.previousTPS.add(MinecraftServer.getServer().recentTps[0]);
                if (AntiLag.this.previousTPS.size() > 6) {
                    AntiLag.this.previousTPS.removeFirst();
                    if (System.currentTimeMillis() - AntiLag.this.startTime >= 21600000L) {
                        Bukkit.getLogger().info("[Arkkit (AntiLag)] averageTPS: " + AntiLag.this.getAverageTPS());
                        if (AntiLag.this.getAverageTPS() < 18.0) {
                            AntiLag.this.rebooting = true;
                            //AntiCrash.restartServer(60);
                        }
                    }
                }
            }
        }, 200L, 200L);
        this.registerListener(new AntiLagListener());
    }

    @Override
    public void disable() {
        this.unregisterListeners();
    }

    private double getAverageTPS() {
        double d = this.previousTPS.size();
        double t = 0.0;
        Iterator iterator = this.previousTPS.iterator();
        while (iterator.hasNext()) {
            double x = (Double) iterator.next();
            t += x;
        }
        return t / d;
    }
}

