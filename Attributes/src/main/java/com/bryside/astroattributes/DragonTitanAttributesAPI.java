package com.bryside.astroattributes;

import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.items.HolyGrail;
import com.bryside.astroattributes.items.RuneExtractor;
import com.bryside.astroattributes.objects.Rune;
import com.bryside.astroattributes.objects.RuneCacher;
import com.bryside.astroattributes.utils.Utils;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DragonTitanAttributesAPI {
    public static ItemStack getRandomRune(int level, int success) {
        RuneType[] list = RuneType.values();
        return list[ThreadLocalRandom.current().nextInt(0, list.length)].getRune().getItemStack(level, success);
    }

    public static ItemStack getRune(RuneType type, int level, int success) {
        return type.getRune().getItemStack(level, success);
    }

    public static int getLevelOfRune(ItemStack item) {
        return RuneType.getLevelOfRune(item);
    }

    public static int getSuccessRateOfRune(ItemStack item) {
        return RuneType.getSuccessRateOfRune(item);
    }

    public static RuneType getRuneType(ItemStack item) {
        return RuneType.getType(item);
    }

    public static ItemStack extractRune(ItemStack item, int apply) {
        RuneCacher cacher = new RuneCacher(item);
        if (cacher.getRuneType() == null) {
            return null;
        } else {
            int level = cacher.getLevel();
            removeRune(item);
            return cacher.getRuneType().getRune().getItemStack(level, apply);
        }
    }

    public static boolean canExtractRune(ItemStack item) {
        RuneCacher cacher = new RuneCacher(item);
        return cacher.getRuneType() != null;
    }

    public static ItemStack removeRune(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore() && Utils.isWeapon(item)) {
            if (!canExtractRune(item)) {
                return item;
            } else {
                ItemMeta itemMeta = item.getItemMeta();
                List lore = itemMeta.getLore();

                for (int i = lore.size() - 1; i >= 0; --i) {
                    String l = (String) lore.get(i);
                    if (l.startsWith(CC.RedB + "TITAN ATTRIBUTE:")) {
                        lore.remove(i);
                    } else if (l.startsWith(CC.RedB + "TITAN XP:")) {
                        lore.remove(i);
                    }
                }

                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                return item;
            }
        } else {
            return item;
        }
    }

    public static void runeCorruptWeapon(ItemStack item) {
        if (item != null) {
            ItemMeta itemMeta = item.getItemMeta();
            List lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList();
            ((List) lore).add(CC.RedB + "** ATTRIBUTE CORRUPTED **");
            itemMeta.setLore((List) lore);
            item.setItemMeta(itemMeta);
        }
    }

    public static boolean isRuneCorrupted(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(CC.RedB + "** ATTRIBUTE CORRUPTED **");
    }

    public static void removeRuneCorrupt(ItemStack item) {
        if (isRuneCorrupted(item)) {
            ItemMeta itemMeta = item.getItemMeta();
            List lore = itemMeta.getLore();
            lore.remove(CC.RedB + "** ATTRIBUTE CORRUPTED **");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
        }
    }

    public static void addExperienceToRune(ItemStack item, int add) {
        RuneCacher cacher = new RuneCacher(item);
        RuneType type = cacher.getRuneType();
        if (type != null) {
            ItemMeta itemMeta = item.getItemMeta();
            List lore = itemMeta.getLore();
            int max = cacher.getRuneType().getRune().getMaxLevel();
            if (max > cacher.getLevel()) {
                boolean found = false;
                int increase = 0;

                for (int i = lore.size() - 1; i >= 0; --i) {
                    String l = (String) lore.get(i);
                    int experience;
                    if (!l.startsWith(CC.RedB + "TITAN XP:")) {
                        if (l.startsWith(CC.RedB + "TITAN ATTRIBUTE:") && !found) {
                            experience = cacher.getLevel() + increase;
                            experience = experience > max ? max : experience;
                            lore.set(i, CC.RedB + "TITAN ATTRIBUTE: " + CC.DarkGrayB + type.name() + " " + CC.RedB + "(" + CC.DarkGray + "Level " + experience + CC.RedB + ")");
                        }
                    } else {
                        experience = cacher.getExperience();
                        experience += add;
                        int level = cacher.getLevel();
                        Rune rune = type.getRune();
                        int expLevel = rune.getExpForLevel(level);
                        if (expLevel > experience) {
                            found = true;
                        } else {
                            while (expLevel <= experience && expLevel > 0 && level + increase < max) {
                                experience -= expLevel;
                                expLevel = rune.getExpForLevel(level + increase);
                                ++increase;
                            }

                            if (level + increase >= max) {
                                experience = 0;
                            }
                        }

                        String line = CC.RedB + "TITAN XP: " + CC.DarkGrayB + experience + " / " + CC.Red + type.getRune().getExpForLevel(level + increase);
                        lore.set(i, line);
                    }
                }

                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
            }
        }
    }

    public static ItemStack createRuneExtractor(int level) {
        return RuneExtractor.createNewRuneExtractor(level);
    }

    public static ItemStack createExperienceShard(int apply) {
        return RuneExtractor.createNewRuneExtractor(apply);
    }

    public static ItemStack createHolyGrail() {
        return HolyGrail.createNewHolyGrail();
    }
}
