package me.astropvp.astropvpskins.utils;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ItemSkinUtils {
    public static boolean canPvP(Player attacker, Player target, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return false;
        if (target.getLocation().getWorld().getName().equals("tournament_world")) return true;
        if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(target.getLocation())) return false;
        if (attacker.hasMetadata("frozenSkrrt")) return false;
        if (target.hasMetadata("frozenSkrrt")) return false;
        if (FactionUtils.inSameFaction(attacker, target)) return false;
        Faction factionAtacker = FactionUtils.getFaction(attacker);
        Faction factionTarget = FactionUtils.getFaction(target);

        if (factionAtacker.getRelationTo(factionTarget) == Relation.ALLY) return false;
        if (attacker.getWorld().getName().equals("world_outpost_pluto")) {
            return false;
        }
        if (factionAtacker.getRelationTo(factionTarget) == Relation.TRUCE) return false;

        if (attacker.getLocation().getWorld().getName().equals("nebula")) return false;
        if (attacker.getLocation().getWorld().getName().equals("world_duels")) return false;


        return true;
    }
}
