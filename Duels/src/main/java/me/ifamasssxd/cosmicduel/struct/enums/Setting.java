/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.struct.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Setting {
    GOLDEN_APPLES("Golden Apples", "%s Golden Apples during the duel.", Material.GOLDEN_APPLE, 0, true, true),
    //MCMMO("MCMMO", "%s MCMMO skills during the duel.", Material.DIAMOND_AXE, 0, true, false),
    POTIONS("Potions", "%s all potions during the duel.", Material.BREWING_STAND_ITEM, 0, true, true),
    BOWS("Bows", "%s Bows during the duel.", Material.BOW, 0, true, true),
    HEALING("Healing", "%s Vanilla regen during the duel.", Material.GHAST_TEAR, 0, true, true),
    FOOD_LOSS("Food Loss", "%s hunger loss during the duel.", Material.COOKED_BEEF, 0, true, false),
    ENDER_PEARLS("Ender Pearls", "%s Ender Pearl throws during the duel.", Material.ENDER_PEARL, 0, true, true),
    DROPITEMS("Risk Inventory", "%s Drops during a BYOG duel.", Material.BONE, 0, false, false),
    BOUNTIES("Bounty", "Play for the loser's bounty.", Material.GOLD_INGOT, 0, false, false),
    ARMOR("Armor", "%s the use of armor.", Material.DIAMOND_CHESTPLATE, 0, true, true),
    WEAPONS("Weapons", "%s the use of weapons.", Material.DIAMOND_SWORD, 0, true, true),
    FIX("/fix", "%s the use of /fix", Material.ANVIL, 0, true, false),
    FLY("/fly", "%s the use of /fly", Material.FEATHER, 0, false, false),
    PET("Pets", "Allows pets in the duel", Material.SKULL_ITEM, 0, false, false),
    SOUL_GEM("Soul Gems", "Allows soul gems in the duel.", Material.EMERALD, 0, true, true),
    DEATH_CERT("Death Certificates", "Toggle death certificate drops.", Material.PAPER, 0, true, false);

    String name;
    String description;
    Material icon;
    short icondata;
    boolean defaultSetting;
    boolean rankedDefault;

    Setting(String name, String descr, Material mat, int data, boolean defaultSetting, boolean rankedDefault) {
        this.name = name;
        this.rankedDefault = rankedDefault;
        this.description = descr;
        this.icon = mat;
        this.icondata = (short) data;
        this.defaultSetting = defaultSetting;
    }

    public static Setting getSetting(ItemStack is) {
        for (Setting setting : Setting.values()) {
            if (!setting.getIcon().equals((Object)is.getType()) || !is.getItemMeta().hasDisplayName() || !is.getItemMeta().getDisplayName().contains(setting.getName())) continue;
            return setting;
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Material getIcon() {
        return this.icon;
    }

    public short getIcondata() {
        return this.icondata;
    }

    public boolean isDefaultSetting() {
        return this.defaultSetting;
    }

    public boolean isRankedDefault() {
        return this.rankedDefault;
    }
}

