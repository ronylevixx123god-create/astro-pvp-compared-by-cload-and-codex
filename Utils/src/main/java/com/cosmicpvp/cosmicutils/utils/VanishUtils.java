/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.kitteh.vanish.VanishPlugin
 */
package com.cosmicpvp.cosmicutils.utils;

import org.bukkit.entity.Player;

public class VanishUtils {

    public static boolean toggleEssentialsVanished(Player player, boolean vanished) {
        return EssentialsUtils.setVanished(player, vanished);
    }

    public static boolean isEssentialsVanished(Player player) {
        return EssentialsUtils.isVanished(player);
    }

    public static boolean isVanished(Player player) {
        return VanishUtils.isEssentialsVanished(player);
    }
}

