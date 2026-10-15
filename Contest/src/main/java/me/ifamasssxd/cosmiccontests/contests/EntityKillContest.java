/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.entity.EntityDeathEvent
 */
package me.ifamasssxd.cosmiccontests.contests;

import me.ifamasssxd.cosmiccontests.struct.contests.Contest;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillContest
extends Contest {
    private EntityType entityType;
    private String name;

    public EntityKillContest(String name, EntityType et, Material display) {
        super(display);
        this.name = name;
        this.entityType = et;
    }

    public void onEntityKill(ContestUser user, EntityDeathEvent event) {
        user.incrementValue(Contests.MOBS_KILLED, "mobsKilled", 1.0);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%sMobs Killed\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY}) + " " + this.getName();
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.MOBS_KILLED, "mobsKilled");
    }

    public EntityType getEntityType() {
        return this.entityType;
    }
}

