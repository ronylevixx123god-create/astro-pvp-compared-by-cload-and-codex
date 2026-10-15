package org.demonicpvp.demonictrials.trial;

import org.bukkit.ChatColor;
import org.demonicpvp.demonictrials.util.Utils;

public enum TrialTier {
    RECRUIT("Recruit", ChatColor.WHITE.toString(), 10, 15, "trials_decision_box_white", Utils.rgbaToInt(255, 255, 255, 255)),
    APPRENTICE("Apprentice", ChatColor.GREEN.toString(), 8, 30, "trials_decision_box_green", Utils.rgbaToInt(0, 255, 21, 255)),
    MASTER("Master", ChatColor.AQUA.toString(), 6, 30, "trials_decision_box_blue", Utils.rgbaToInt(0, 255, 237, 255)),
    HARDCORE("Hardcore", ChatColor.YELLOW.toString(), 5, 30, "trials_decision_box_yellow", Utils.rgbaToInt(255, 255, 0, 255)),
    DEMONIC("Demonic", ChatColor.GOLD.toString(), 4, 30, "trials_decision_box_orange", Utils.rgbaToInt(255, 138, 0, 255));

    private final String name;
    private final String color;
    private final int setMinutes;
    private final int extraSeconds;
    private final String schemName;
    private final int itemColor;

    TrialTier(String name, String color, int setMinutes, int extraSeconds, String schemName, int itemColor) {
        this.name = name;
        this.color = color;
        this.setMinutes = setMinutes;
        this.extraSeconds = extraSeconds;
        this.schemName = schemName;
        this.itemColor = itemColor;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public int getSetMinutes() {
        return this.setMinutes;
    }

    public int getExtraSeconds() {
        return this.extraSeconds;
    }

    public String getSchemName() {
        return this.schemName;
    }

    public int getItemColor() {
        return this.itemColor;
    }
}
