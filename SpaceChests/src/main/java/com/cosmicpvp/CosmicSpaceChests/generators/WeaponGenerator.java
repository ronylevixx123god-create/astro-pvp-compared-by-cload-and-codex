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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class WeaponGenerator {
    static Random r = new Random();



    public static ItemStack generate(ItemRarity ir) {

        int percent = 0;
        int enchantsAmount = new Random().nextInt(3) + 1;
        HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
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
        if (ir == ItemRarity.ELITE || ir == ItemRarity.ULTIMATE) {
            ++enchantsAmount;
        }
        if (ir == ItemRarity.LEGENDARY) {
            ++enchantsAmount;
        }
        WeaponTypes type = WeaponGenerator.getRandomWeapon();
        Material mat = type.getRandomType();
        if (ir == ItemRarity.LEGENDARY || ir == ItemRarity.ULTIMATE || ir == ItemRarity.ELITE && new Random().nextBoolean()) {
            if (mat == Material.IRON_SWORD || mat == Material.IRON_HOE) {
                mat = Material.DIAMOND_SWORD;
                type = WeaponTypes.Sword;
            } else if (mat == Material.IRON_AXE || mat == Material.IRON_SPADE) {
                mat = Material.DIAMOND_AXE;
                type = WeaponTypes.Axe;
            } else if (mat == Material.BOW) {
                mat = Material.BOW;
                type = WeaponTypes.Bow;
            }
        }
        ItemBuilder builder = new ItemBuilder(mat);
        Enchantment highest = null;
        for (int i = 0; i < enchantsAmount; ++i) {
            if (r.nextInt(100) > percent) continue;
            int highestLevel = 0;
            Enchantment enchant = WeaponGenerator.getEnchantment(type);
            if (enchants.containsKey((Object)enchant)) continue;
            int level = MiscUtil.getHardNextInt(ir == ItemRarity.ELITE || ir == ItemRarity.ULTIMATE ? 4 : 3) + 1;
            if (ir == ItemRarity.ULTIMATE) {
                level = enchant.getMaxLevel();
            }
            enchants.put(enchant, level);
            if (level <= highestLevel) continue;
            highest = enchant;
            highestLevel = level;
        }
        if (ir == ItemRarity.ULTIMATE) {
            if (mat == Material.BOW) {
                enchants.put(Enchantment.ARROW_DAMAGE, 5);
                enchants.put(Enchantment.ARROW_FIRE, 2);
            } else {
                enchants.put(Enchantment.DAMAGE_ALL, 5);
                enchants.put(Enchantment.FIRE_ASPECT, 2);
            }
        }
        for (Map.Entry entry : enchants.entrySet()) {
            builder.addEnchantment((Enchantment)entry.getKey(), (Integer)entry.getValue());
        }
        StringBuilder name = new StringBuilder();
        if (highest != null) {
            if (type == WeaponTypes.Sword) {
                name.append(WeaponGenerator.getSwordLore(highest));
            } else if (type == WeaponTypes.Axe) {
                name.append(WeaponGenerator.getAxeLore(highest));
            } else if (type == WeaponTypes.Bow) {
                name.append(WeaponGenerator.getBowLore(highest));
            } else if (type == WeaponTypes.Hoe) {
                name.append(WeaponGenerator.getHoeLore(highest));
            } else if (type == WeaponTypes.Spade) {
                name.append(WeaponGenerator.getHoeLore(highest));
            }
        }
        String weaponName = type.name();
        name.append(weaponName);
        name.append(" of ");
        name.append(WeaponGenerator.getRandomSuffix());
        builder.setTitle(name.toString(), ir);
        return builder.build();
    }

    private static String getRandomSuffix() {
        String suffix = Suffixes.values()[r.nextInt(Suffixes.values().length)].name();
        return suffix.replace("_", " ");
    }

    private static WeaponTypes getRandomWeapon() {
        return WeaponTypes.values()[r.nextInt(WeaponTypes.values().length)];
    }

    private static Enchantment getEnchantment(WeaponTypes wt) {
        if (wt == WeaponTypes.Sword) {
            return SwordLores.values()[new Random().nextInt((int)SwordLores.values().length)].enchant;
        }
        if (wt == WeaponTypes.Axe) {
            return AxeLores.values()[new Random().nextInt((int)AxeLores.values().length)].enchant;
        }
        if (wt == WeaponTypes.Bow) {
            return BowLores.values()[new Random().nextInt((int)BowLores.values().length)].enchant;
        }
        if (wt == WeaponTypes.Hoe) {
            return HoeLores.values()[new Random().nextInt((int)HoeLores.values().length)].enchant;
        }
        if (wt == WeaponTypes.Spade) {
            return SpadeLores.values()[new Random().nextInt((int)SpadeLores.values().length)].enchant;
        }
        return null;
    }

    public static String getSwordLore(Enchantment enchantment) {
        for (SwordLores lore : SwordLores.values()) {
            if (lore.enchant != enchantment) continue;
            return lore.name().replace('_', ' ') + " ";
        }
        return "";
    }

    public static String getAxeLore(Enchantment enchantment) {
        for (AxeLores lore : AxeLores.values()) {
            if (lore.enchant != enchantment) continue;
            return lore.name().replace('_', ' ') + " ";
        }
        return "";
    }

    public static String getBowLore(Enchantment enchantment) {
        for (BowLores lore : BowLores.values()) {
            if (lore.enchant != enchantment) continue;
            return lore.name().replace('_', ' ') + " ";
        }
        return "";
    }

    public static String getHoeLore(Enchantment enchantment) {
        for (HoeLores lore : HoeLores.values()) {
            if (lore.enchant != enchantment) continue;
            return lore.name().replace('_', ' ') + " ";
        }
        return "";
    }

    public static String getSpadeLore(Enchantment enchantment) {
        for (SpadeLores lore : SpadeLores.values()) {
            if (lore.enchant != enchantment) continue;
            return lore.name().replace('_', ' ') + " ";
        }
        return "";
    }

    public static enum WeaponTypes {
        Sword(Material.DIAMOND_SWORD),
        Axe(Material.DIAMOND_AXE),
        Bow(Material.BOW),
        Hoe(Material.DIAMOND_HOE),
        Spade(Material.DIAMOND_SPADE);

        private final List<Material> types;

        private WeaponTypes(Material ... mat) {
            this.types = Arrays.asList(mat);
        }

        public Material getRandomType() {
            if (this.types.size() <= 1) {
                return this.types.get(0);
            }
            return this.types.get(r.nextInt(this.types.size()));
        }
    }

    public static enum SpadeLores {
        Imbued(Enchantment.FIRE_ASPECT),
        Oversized(Enchantment.KNOCKBACK),
        Lucky(Enchantment.LOOT_BONUS_BLOCKS);

        Enchantment enchant;

        private SpadeLores(Enchantment enchantment) {
            this.enchant = enchantment;
        }
    }

    public static enum HoeLores {
        Imbued(Enchantment.FIRE_ASPECT),
        Oversized(Enchantment.KNOCKBACK),
        Lucky(Enchantment.LOOT_BONUS_BLOCKS);

        Enchantment enchant;

        private HoeLores(Enchantment enchantment) {
            this.enchant = enchantment;
        }
    }

    public static enum BowLores {
        Powerful(Enchantment.ARROW_DAMAGE),
        Imbued(Enchantment.ARROW_FIRE),
        Oversized(Enchantment.ARROW_KNOCKBACK),
        Unbreaking(Enchantment.DURABILITY),
        Infinite(Enchantment.ARROW_INFINITE);

        Enchantment enchant;

        private BowLores(Enchantment enchantment) {
            this.enchant = enchantment;
        }
    }

    public static enum AxeLores {
        Powerful(Enchantment.DAMAGE_ALL),
        Imbued(Enchantment.FIRE_ASPECT),
        Oversized(Enchantment.KNOCKBACK),
        Unbreaking(Enchantment.DURABILITY),
        Lucky(Enchantment.LOOT_BONUS_MOBS),
        Holy(Enchantment.DAMAGE_UNDEAD);

        Enchantment enchant;

        private AxeLores(Enchantment enchantment) {
            this.enchant = enchantment;
        }
    }

    public static enum SwordLores {
        Powerful(Enchantment.DAMAGE_ALL),
        Imbued(Enchantment.FIRE_ASPECT),
        Oversized(Enchantment.KNOCKBACK),
        Unbreaking(Enchantment.DURABILITY),
        Lucky(Enchantment.LOOT_BONUS_MOBS),
        Holy(Enchantment.DAMAGE_UNDEAD);

        Enchantment enchant;

        private SwordLores(Enchantment enchantment) {
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

