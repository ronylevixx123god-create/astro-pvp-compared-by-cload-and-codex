/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  org.arkhamnetwork.enchantments.util.GeneralUtil
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.generators;

import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemBuilder;
import com.cosmicpvp.CosmicSpaceChests.utils.MiscUtil;
import java.util.List;
import java.util.Random;

import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ArmorGenerator {
    static Random r = new Random();


    public static ItemStack generate(ItemRarity ir) {

        ArmorType type = ArmorType.DIAMOND;
        ArmorPiece piece = ArmorGenerator.getPiece();
        int percent = 0;
        int enchantsAmount = new Random().nextInt(3) + 1;
        Enchantment highest = null;
        if (ir == ItemRarity.SIMPLE) {
            percent = 50;
        }
        if (ir == ItemRarity.UNIQUE) {
            percent = 75;
        }
        if (ir == ItemRarity.ELITE) {
            percent = 90;
        }
        if (ir == ItemRarity.ULTIMATE) {
            percent = 100;
        }
        if (ir == ItemRarity.ELITE && new Random().nextBoolean()) {
            type = ArmorType.DIAMOND;
        }
        if (ir == ItemRarity.ULTIMATE || ir == ItemRarity.LEGENDARY) {
            type = ArmorType.DIAMOND;
        }
        if (ir == ItemRarity.ELITE || ir == ItemRarity.ULTIMATE) {
            ++enchantsAmount;
        }
        ItemBuilder builder = new ItemBuilder(type.getMaterial(piece));
        for (int i = 0; i < enchantsAmount; ++i) {
            Enchantment enchant;
            int highestLevel = 0;
            if (r.nextInt(100) > percent || (enchant = ArmorGenerator.getEnchantment()) == null) continue;
            int level = MiscUtil.getHardNextInt(ir == ItemRarity.ELITE || ir == ItemRarity.ULTIMATE ? 4 : 3) + 1;
            if (ir == ItemRarity.ULTIMATE || ir == ItemRarity.LEGENDARY) {
                level = enchant.getMaxLevel();
            }
            builder.addEnchantment(enchant, level);
            if (level <= highestLevel) continue;
            highest = enchant;
            highestLevel = level;
        }
        if (ir == ItemRarity.ULTIMATE || ir == ItemRarity.LEGENDARY) {
            builder.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        }
        StringBuilder name = new StringBuilder();
        if (highest != null) {
            name.append(ArmorGenerator.getArmorLore(highest));
        }
        String armorName = type.getMaterial(piece).name();
        String NarmorName = "";
        String[] arrstring = armorName.split("_");
        int n = arrstring.length;
        for (int i = 0; i < n; ++i) {
            String s;
            String o_s = s = arrstring[i];
            s = s.substring(0, 1).toUpperCase() + s.substring(1, s.length()).toLowerCase();
            NarmorName = NarmorName + s + (!armorName.endsWith(o_s) ? " " : "");
        }
        name.append(NarmorName);
        name.append(" of ");
        name.append(ArmorGenerator.getRandomSuffix());
        builder.setTitle(name.toString(), ir);
        return builder.build();
    }

    private static ArmorType getType() {
        return ArmorType.values()[r.nextInt(ArmorType.values().length)];
    }

    private static ArmorPiece getPiece() {
        return ArmorPiece.values()[r.nextInt(ArmorPiece.values().length)];
    }

    private static String getRandomSuffix() {
        String suffix = Suffixes.values()[r.nextInt(Suffixes.values().length)].name();
        return suffix.replace("_", " ");
    }

    public static String getArmorLore(Enchantment enchantment) {
        for (ArmorLores lore : ArmorLores.values()) {
            if (lore.enchant != enchantment) continue;
            return lore.name().replace('_', ' ') + " ";
        }
        return "";
    }

    private static Enchantment getEnchantment() {
        int random = r.nextInt(6);
        switch (random) {
            case 1: {
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            }
            case 2: {
                return Enchantment.PROTECTION_EXPLOSIONS;
            }
            case 3: {
                return Enchantment.PROTECTION_FALL;
            }
            case 4: {
                return Enchantment.PROTECTION_FIRE;
            }
            case 5: {
                return Enchantment.THORNS;
            }
        }
        return null;
    }

    private static Material getMaterial(ArmorPiece piece, ArmorType type) {
        return Material.getMaterial((String)(type.name() + '_' + piece.name()));
    }

    public static enum ArmorType {
        IRON,
        DIAMOND;


        public Material getMaterial(ArmorPiece piece) {
            return ArmorGenerator.getMaterial(piece, this);
        }
    }

    public static enum ArmorPiece {
        HELMET(3),
        CHESTPLATE(2),
        LEGGINGS(1),
        BOOTS(0);

        private final int id;

        private ArmorPiece(int id) {
            this.id = id;
        }

        public Material getMaterial(ArmorType type) {
            return ArmorGenerator.getMaterial(this, type);
        }

        public int getId() {
            return this.id;
        }
    }

    public static enum ArmorLores {
        Armored(Enchantment.PROTECTION_ENVIRONMENTAL),
        Enchanted(Enchantment.PROTECTION_FIRE),
        Reinforced(Enchantment.PROTECTION_FALL),
        Heavy(Enchantment.PROTECTION_PROJECTILE),
        Cursed(Enchantment.THORNS);

        Enchantment enchant;

        private ArmorLores(Enchantment enchantment) {
            this.enchant = enchantment;
        }
    }

    public static enum Suffixes {
        Ancients,
        Might,
        Strength,
        Mojang,
        Swag,
        Ownage,
        Epicness,
        Victory,
        Lore,
        Partying,
        War,
        Battle,
        Bloodshed,
        Anarchy,
        Destruction,
        Obstruction,
        Vitality,
        Mystery,
        Obsidian,
        Evil,
        Goodness,
        Darkness,
        Thanksgiving,
        Hatred,
        Love,
        Insanity,
        Kingship,
        History,
        Legend,
        Myth,
        Obliteration,
        Restoration,
        Notch,
        Holidays;

    }
}

