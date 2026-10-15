/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CompilexPermissions.event.PermissionChangeEvent
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package net.astropvp.core.titles.listeners;

import net.astropvp.core.titles.sql.TitleDatabaseManager;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PermissionListener
implements Listener {
    private TitleDatabaseManager dbM;

    public PermissionListener(TitleDatabaseManager dm) {
        this.dbM = dm;
    }

    @EventHandler
    public void onPermissionChange(NodeMutateEvent event) {
        Set<String> titles = event.getTarget().getCachedData().getPermissionData().getPermissionMap().keySet();
        boolean title_perms_changed = false;
        ArrayList<String> newtitles = this.dbM.getUnlockedTitles().computeIfAbsent(((User)event.getTarget()).getUniqueId(), e -> Lists.newArrayList());
        for (String s : titles) {
            if (!s.startsWith("titles.title.") || newtitles.contains(s.replace("titles.title.", ""))) continue;
            newtitles.add(s.replace("titles.title.", ""));
            title_perms_changed = true;
        }
        if (Bukkit.getPlayer(((User)event.getTarget()).getUniqueId()) != null && title_perms_changed) {
            this.dbM.getUnlockedTitles().put(((User)event.getTarget()).getUniqueId(), newtitles);
            Bukkit.getPlayer(((User)event.getTarget()).getUniqueId()).sendMessage((Object)ChatColor.GREEN + "" + (Object)ChatColor.BOLD + ">> " + (Object)ChatColor.GRAY + "Your available titles have updated. Use " + (Object)ChatColor.YELLOW + (Object)ChatColor.BOLD + "/title" + (Object)ChatColor.GRAY + " to view them.");
        }
    }
}

