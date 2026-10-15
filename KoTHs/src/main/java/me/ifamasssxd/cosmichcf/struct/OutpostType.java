/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.material.MaterialData
 */
package me.ifamasssxd.cosmichcf.struct;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public enum OutpostType {
    COURTYARD(new MaterialData(Material.BLAZE_POWDER), ChatColor.RED, Lists.newArrayList("courtyard", "ct", "infernal", "Infernal"), CC.GreenB + "* " + CC.White + "Mob spawning in claims increase by " + CC.Green + "1.5x", CC.GreenB + "* " + CC.White + "Reduced claim purchase costs", CC.GreenB + "* " + CC.White + "Reduced Bard cooldowns by " + CC.Green + "1.5s", CC.GreenB + "* " + CC.White + "Reduced deathbans by " + CC.Green + "25%", CC.GreenB + "* " + CC.White + "Reduced saturation loss by " + CC.Green + "50%", CC.GreenB + "* " + CC.White + "Increased smelting by " + CC.Green + "100%"),
    THE_GREAT_HALL(new MaterialData(Material.INK_SACK, (byte) 15), ChatColor.AQUA, Lists.newArrayList("the_great_hall", "great_hall", "greathall", "frozen", "Frozen"), CC.AquaB + "* " + CC.White + "Increased EXP drops in claims", CC.AquaB + "* " + CC.White + "Reduced DTR freeze", CC.AquaB + "* " + CC.White + "Faster DTR Regen", CC.AquaB + "* " + CC.White + "Reduced Enderpearl cooldown by " + CC.Aqua + "1.5s", CC.AquaB + "* " + CC.White + "Passive Fire Resistance", CC.AquaB + "* " + CC.White + "Increased brewing speed by " + CC.Aqua + "25%"),
    SPACE(new MaterialData(Material.NETHER_STAR), ChatColor.LIGHT_PURPLE, Lists.newArrayList("space", "Space"), CC.AquaB + "* " + CC.White + "Increased EXP drops in claims", CC.AquaB + "* " + CC.White + "Reduced DTR freeze", CC.AquaB + "* " + CC.White + "Faster DTR Regen", CC.AquaB + "* " + CC.White + "Reduced Enderpearl cooldown by " + CC.Aqua + "1.5s", CC.AquaB + "* " + CC.White + "Passive Fire Resistance", CC.AquaB + "* " + CC.White + "Increased brewing speed by " + CC.Aqua + "25%");
    ;
    private MaterialData guiMaterial;
    private ChatColor chatColor;
    private List<String> internalNames;
    private List<String> rewardStrings;

    private OutpostType(MaterialData guiMaterial, ChatColor color, List<String> potentialNames, String ... rewards) {
        this.rewardStrings = Lists.newArrayList(rewards);
        this.internalNames = potentialNames;
        this.guiMaterial = guiMaterial;
        this.chatColor = color;
    }

    public void setRewardStrings(List<String> strings) {
        for (int i = 0; i < strings.size(); ++i) {
            String str = strings.get(i);
            if (str == null) continue;
            strings.set(i, ChatColor.translateAlternateColorCodes((char)'&', (String)str));
        }
        this.rewardStrings = strings;
    }

    public String getHoverFriendlyData(Outpost instance) {
        StringBuilder builder = new StringBuilder();
        builder.append(instance.getOutpostName()).append(" Bonus When Captured: ").append("\n\n");
        for (String line : this.rewardStrings) {
            builder.append(line).append("\n");
        }
        String ins = CosmicKOTHPlugin.get_().getBaseName();
        builder.append("\n").append(CC.Gray).append("Control the " + ins + " to gain access to these").append("\n");
        builder.append(CC.Gray).append(ins.toLowerCase() + " rewards for you and your faction!");
        String fine = builder.toString();
        if (fine.endsWith("\n")) {
            fine = fine.substring(0, fine.length() - 2);
        }
        return fine;
    }

    public static OutpostType getByInternalName(String name) {
        for (OutpostType rewards : OutpostType.values()) {
            if (!rewards.getInternalNames().contains(name)) continue;
            return rewards;
        }
        return null;
    }

    public MaterialData getGuiMaterial() {
        return this.guiMaterial;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public List<String> getInternalNames() {
        return this.internalNames;
    }

    public List<String> getRewardStrings() {
        return this.rewardStrings;
    }
}
