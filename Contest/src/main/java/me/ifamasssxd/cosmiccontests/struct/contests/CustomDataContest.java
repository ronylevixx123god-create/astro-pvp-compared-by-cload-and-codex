/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.material.MaterialData
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import java.text.DecimalFormat;
import me.ifamasssxd.cosmiccontests.struct.contests.Contest;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class CustomDataContest
extends Contest {
    private static DecimalFormat format = new DecimalFormat("#,###.##");
    private String key;
    private String name;

    public CustomDataContest(String name, String key, Material display) {
        super(display);
        this.key = key;
        this.name = name;
    }

    public CustomDataContest(String name, String key, MaterialData display) {
        super(display);
        this.key = key;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%s" + this.getName() + "\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public String getFormattedSuffix(ContestUser user) {
        return String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY}) + " " + this.getName();
    }

    @Override
    public String getFormattedData(ContestUser user) {
        Contests outer = Contests.getContestFromInnerKey(this.getKey());
        if (outer != null) {
            return "%s" + format.format(user.getDouble(outer, this.getKey()));
        }
        return "%sN/A";
    }

    public String getKey() {
        return this.key;
    }
}

