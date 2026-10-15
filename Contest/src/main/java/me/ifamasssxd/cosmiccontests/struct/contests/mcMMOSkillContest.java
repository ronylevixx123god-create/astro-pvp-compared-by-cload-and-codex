/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.gmail.nossr50.datatypes.skills.SkillType
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.google.common.collect.Lists;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.contests.Rerollable;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.contests.CustomDataContest;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class mcMMOSkillContest
extends CustomDataContest
implements Rerollable {
    private String skillName = "";
    private static DecimalFormat format = new DecimalFormat("#,###.##");

    public mcMMOSkillContest(Material material) {
        super("mcMMO XP Gained In ", "xpGained", material);
        this.reroll();
    }

    @Override
    public String getName() {
        return "mcMMO XP Gain in " + this.getFormattedSkillName();
    }

    public String getSkillName() {
        return this.skillName;
    }

    public String getFormattedSkillName() {
        return StringUtils.capitaliseAllWords((String)this.getSkillName().replace("_", " ").toLowerCase());
    }

    public void onPlayerGainXP(ContestUser user, String skillName, float amount) {
        user.incrementValue(Contests.MCMMO_SKILL, amount);
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + format.format(user.getDouble(Contests.MCMMO_SKILL, this.getKey()));
    }

    @Override
    public String getFormattedLine(ContestUser user) {
        return "%sXP Gained in " + this.getFormattedSkillName() + "\\ " + String.format(this.getFormattedData(user), new Object[]{ChatColor.GRAY});
    }

    @Override
    public void reroll() {
        if (CosmicContests.get().isMcMMOEnabled()) {
            ArrayList possibleSkills = Lists.newArrayList(SkillType.values());
            Collections.shuffle(possibleSkills);
            possibleSkills.remove((Object)SkillType.SALVAGE);
            possibleSkills.remove((Object)SkillType.SMELTING);
            possibleSkills.remove((Object)SkillType.HERBALISM);
            this.skillName = ((SkillType)possibleSkills.get(ThreadLocalRandom.current().nextInt(possibleSkills.size()))).name();
            Bukkit.getLogger().info("Loaded " + this.skillName + " as mcmmo skill.");
        }
    }

    @Override
    public void loadData(String data) {
        this.setSkillName(data);
    }

    @Override
    public String getExtraData() {
        return "&" + this.skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}

