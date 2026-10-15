/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.compat.ParamBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ReflectionUtils
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  org.bukkit.Location
 */
package org.arkhamnetwork.Arkkit.utils;

import com.cosmicpvp.cosmicutils.compat.ParamBuilder;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import org.bukkit.Location;

public class FactionUtil {
    public static Relation getRelation(RelationParticipator part, RelationParticipator part2) {
        return (Relation) ReflectionUtils.callReflectionMethod(part, RelationParticipator.class, "getRelationTo", new ParamBuilder(RelationParticipator.class, part2));
    }

    public static boolean isNonPermFaction(Location destination) {
        Faction at = FactionUtils.getFactionAt(destination);
        return FactionUtil.isNonPermFaction(at);
    }

    public static boolean isNonPermFaction(Faction at) {
        return at != null && !((Boolean) FactionUtils.callFactionMethod(at, "isSafeZone", null)) && !((Boolean) FactionUtils.callFactionMethod(at, "isNone", null)) && (Boolean) FactionUtils.callFactionMethod(at, "isWarZone", null) == false;
    }

    public static String getTag(Faction faction) {
        return (String) FactionUtils.callFactionMethod(faction, "getTag", null);
    }
}

