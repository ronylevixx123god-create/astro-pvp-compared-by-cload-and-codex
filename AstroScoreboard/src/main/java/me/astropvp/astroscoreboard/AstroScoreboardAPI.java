package me.astropvp.astroscoreboard;

import com.compilex.CompilexPermissions.PermissionsAPI;
import com.cosmicpvp.cosmicutils.struct.factions.CosmicFactionHandler;
import com.cosmicpvp.cosmicutils.struct.factions.FactionHandler;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.*;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class AstroScoreboardAPI {

    static Map<String, String> rankOrder = new HashMap<>();


    static {
        rankOrder.put("owner", "1");
        rankOrder.put("manager", "2");
        rankOrder.put("developer", "3");
        rankOrder.put("admin", "4");
        rankOrder.put("banteam", "5");
        rankOrder.put("srmod", "6");
        rankOrder.put("mod", "7");
        rankOrder.put("helper", "8");
        rankOrder.put("media", "9");
        rankOrder.put("interstellar", "a");
        rankOrder.put("apollo", "b");
        rankOrder.put("astro+", "c");
        rankOrder.put("astro", "d");

    }

    public static String getTeamName(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        String rank = PermissionsAPI.getPermissionUser(player.getUniqueId()).getGroup();
        String faction;
        Faction factionName = FPlayers.getInstance().getByPlayer(player).getFaction();
        if (!(FactionUtils.isWilderness(factionName))) {
            faction = CC.strip(fPlayer.getFaction().getTag());
        } else {
            faction = "zzzzz";
        }
        //return faction;
        return rankOrder.get(rank) + faction;
    }


    public static String getDefaultSuffix(Player player, Player other) {
        FPlayer fp = FactionUtils.getFPlayer(player);
        String faction;
        FPlayer fother = FactionUtils.getFPlayer(other);
        Faction factionName = FPlayers.getInstance().getByPlayer(player).getFaction();
        if (!(FactionUtils.isWilderness(factionName))) {
            faction = CC.strip(fp.getFaction().getTag());
        } else {
            faction = "";
        }

        String color = FactionUtils.isWilderness(fp.getFaction()) ? CC.White : fp.getRelationTo(fother).getColor().toString();
        ChatColor rankColor;
        //String rank = PermissionsAPI.getPermissionUser(player.getUniqueId()).getGroup();


        return color + faction;
    }

    public static String getDefaultPrfix(Player player) {
        String prefix;
        String rank = PermissionsAPI.getPermissionUser(player.getUniqueId()).getGroup();
        switch (Objects.requireNonNull(rank)) {
            case "apollo":
                prefix = "&bApollo";
                break;
            case "interstellar":
                prefix = "&5Nebula";
                break;
            case "owner":
                prefix = "&4Owner";
                break;
            case "manager":
                prefix = "&cManager";
                break;
            case "developer":
                prefix = "&cDev";
                break;
            case "media":
                prefix = "&dMedia";
                break;
            case "admin":
                prefix = "&cAdmin";
                break;
            case "mod":
                prefix = "&3Mod";
                break;
            case "srmod":
                prefix = "&3SrMod";
                break;
            case "helper":
                prefix = "&aHelper";
                break;
            default:
                prefix = "&eAstro";
                break;
        }


        return CC.translate(prefix + " &f");

    }


}
