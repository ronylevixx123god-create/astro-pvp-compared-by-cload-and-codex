/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.Faction
 *  me.ifamasssxd.cosmichcf.api.HCFOutpostAPI
 *  me.ifamasssxd.cosmichcf.net.astropvp.armorsets.struct.OutpostType
 */
package com.cosmicpvp.cosmicutils.utils;

import com.massivecraft.factions.Faction;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.entity.Player;

import java.io.File;

public class HCFOutpostUtils {
    private static boolean hcfOutpostsEnabled = false;



    public static boolean doesControlOutpost(Player player, String outpostName) {
        if (!hcfOutpostsEnabled) {
            return false;
        }
        try {
            OutpostType type = outpostName.equalsIgnoreCase("infernal") ? OutpostType.COURTYARD : (outpostName.equalsIgnoreCase("frozen") ? OutpostType.THE_GREAT_HALL : OutpostType.valueOf((String) outpostName));
            return HCFOutpostAPI.isControllingOutpost((Player) player, (OutpostType) type);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean doesControlOutpost(Faction faction, String outpostName) {
        if (!hcfOutpostsEnabled) {
            return false;
        }
        try {
            OutpostType type = outpostName.equalsIgnoreCase("infernal") ? OutpostType.COURTYARD : (outpostName.equalsIgnoreCase("frozen") ? OutpostType.THE_GREAT_HALL : OutpostType.valueOf((String) outpostName));
            return HCFOutpostAPI.isControllingOutpost((Faction) faction, (OutpostType) type);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isHCFOutpostsEnabled() {
        return hcfOutpostsEnabled;
    }
}

