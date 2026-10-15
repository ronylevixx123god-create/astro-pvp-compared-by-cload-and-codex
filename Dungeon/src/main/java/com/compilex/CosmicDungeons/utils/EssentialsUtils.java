/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.earth2me.essentials.User
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicDungeons.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EssentialsUtils {
    private static Essentials essentials;

    public static boolean isVanished(Player player) {
        User user;
        if (essentials == null) {
            essentials = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        }
        return (user = essentials.getUser(player)) != null && user.isVanished();
    }
}

