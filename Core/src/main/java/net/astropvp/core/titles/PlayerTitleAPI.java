/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CompilexPermissions.PermissionsAPI
 *  com.compilex.CompilexPermissions.structure.PermissionUser
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.titles;


import java.util.ArrayList;
import java.util.UUID;

import com.cosmicpvp.cosmicutils.utils.LuckPermUtils;
import net.astropvp.core.AstroCore;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerTitleAPI {
    public static String getEquippedTitle(String playerName) {
        String equippedtitle = AstroCore.getInstance().getDataManager().getEquippedTitle(Bukkit.getOfflinePlayer((String)playerName).getUniqueId());
        if (equippedtitle.equalsIgnoreCase("none")) {
            equippedtitle = null;
        }
        if (equippedtitle != null && equippedtitle.endsWith("L") && equippedtitle.contains("\u00b0^\u00b0")) {
            equippedtitle = "(/\u00b0^\u00b0)/L";
        }
        return equippedtitle;
    }

    public static String getEquippedTitle(UUID uuid) {
        String equippedtitle = AstroCore.getInstance().getDataManager().getEquippedTitle(uuid);
        if (equippedtitle.equalsIgnoreCase("none")) {
            equippedtitle = null;
        }
        if (equippedtitle != null && equippedtitle.contains("\u00b0^\u00b0")) {
            equippedtitle = "(/\u00b0^\u00b0)/L";
        }
        if (equippedtitle != null && equippedtitle.contains("??\u00af?")) {
            equippedtitle = "(\u3064\u2580\u00af\u2580)\u3064";
        }
        return equippedtitle;
    }

    public static boolean hasSomeTitleEquipped(String playerName) {
        String equippedtitle = AstroCore.getInstance().getDataManager().getEquippedTitle(Bukkit.getOfflinePlayer((String)playerName).getUniqueId());
        return !equippedtitle.equalsIgnoreCase("none");
    }

    public static boolean hasSomeTitleEquipped(UUID uuid) {
        String equippedtitle = AstroCore.getInstance().getDataManager().getEquippedTitle(uuid);
        return !equippedtitle.equalsIgnoreCase("none");
    }

    public static void setEquippedTitle(UUID uuid, String newTitle) {
        AstroCore.getInstance().getDataManager().setEquippedTitle(uuid, newTitle);
    }

    public static ArrayList<String> getUnlockedTitles(UUID uuid) {
        if (AstroCore.getInstance().getDataManager().getUnlockedTitles().containsKey(uuid)) {
            return AstroCore.getInstance().getDataManager().getUnlockedTitles().get(uuid);
        }
        Player online = Bukkit.getPlayer((UUID)uuid);
        if (online != null) {
            ArrayList<String> at = new ArrayList<String>();
            //PermissionUser user = PermissionsAPI.getPermissionUser((UUID)uuid);
            User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
            for (String perm : user.getCachedData().getPermissionData().getPermissionMap().keySet()) {
                if (!perm.toLowerCase().startsWith("titles.title.")) continue;
                at.add(perm.replace("titles.title.", ""));
            }
            AstroCore.getInstance().getDataManager().getUnlockedTitles().put(uuid, at);
            return at;
        }
        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        ArrayList<String> at = new ArrayList<String>();
        for (String perm : user.getCachedData().getPermissionData().getPermissionMap().keySet()) {
            if (!perm.toLowerCase().startsWith("titles.title.")) continue;
            at.add(perm.replace("titles.title.", ""));
        }
        return at;
    }
}

