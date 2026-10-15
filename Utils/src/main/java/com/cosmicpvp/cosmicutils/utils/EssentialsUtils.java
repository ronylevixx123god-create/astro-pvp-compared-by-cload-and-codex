/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.earth2me.essentials.User
 */
package com.cosmicpvp.cosmicutils.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class EssentialsUtils {
    private static Essentials ess;

    static {
        if (ess == null) {
            ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }
    }

    public static boolean isVanished(Player player) {
        return EssentialsUtils.isVanished(ess.getUser(player));
    }

    public static boolean setVanished(Player player, boolean toggle) {
        return EssentialsUtils.setVanished(ess.getUser(player), toggle);
    }

    public static boolean isVanished(User user) {
        return user.isVanished();
    }

    public static boolean setVanished(User user, boolean vanished) {
        user.setVanished(vanished);
        return user.isVanished();
    }

    public static boolean isInGodMode(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return true;
        }
        if (ess != null) {
            User user = ess.getUser(player);
            return EssentialsUtils.isVanished(user) || user.isGodModeEnabled();
        }
        return false;
    }
}

