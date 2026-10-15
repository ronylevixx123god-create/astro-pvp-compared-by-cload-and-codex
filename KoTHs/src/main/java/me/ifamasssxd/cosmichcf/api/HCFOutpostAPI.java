/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.massivecraft.factions.Faction
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmichcf.api;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Faction;
import me.ifamasssxd.cosmichcf.managers.OutpostManager;
import me.ifamasssxd.cosmichcf.struct.Outpost;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.entity.Player;

public class HCFOutpostAPI {
    public static boolean isControllingOutpost(Faction faction, OutpostType outpost) {
        return HCFOutpostAPI.getOutpostIfControlling(faction, outpost) != null;
    }

    public static Outpost getOutpostIfControlling(Faction faction, OutpostType outpost) {
        if (faction == null) {
            return null;
        }
        OutpostManager manager = CosmicKOTHPlugin.get_().getOutpostManager();
        String factionId = FactionUtils.getFactionId((Faction)faction);
        for (Outpost out : manager.getOutposts()) {
            if (out.getOutpostType() == null || !out.getOutpostType().equals((Object)outpost) || out.getLastFactionToCap() == null || !out.getLastFactionToCap().equals(factionId)) continue;
            return out;
        }
        return null;
    }

    public static void clear(Player player) {
        for (Outpost outpost : CosmicKOTHPlugin.get_().getOutpostManager().getOutposts()) {
            outpost.onPlayerLeave(player);
        }
    }

    public static boolean isControllingOutpost(Player player, OutpostType outpost) {
        Faction faction = FactionUtils.getFaction((Player)player);
        Outpost controlling = HCFOutpostAPI.getOutpostIfControlling(faction, outpost);
        return controlling != null;
    }
}

