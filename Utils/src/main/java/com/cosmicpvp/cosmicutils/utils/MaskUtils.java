/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicmasks.MaskType
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.entity.Player;

public class MaskUtils {
    public static boolean hasMaskEquipped(Player player, String maskName) {
        if (maskName == null) {
            return false;
        }
        MaskType type = MaskType.getFromName(maskName.toUpperCase());
        return type != null && type.hasEquipped(player);
    }
}

