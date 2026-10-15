package net.astropvp.weaponbonuses.bonus;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.astropvp.weaponbonuses.util.Constants;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BonusWeapon {
    private final ItemStack itemStack;
    private final Map<Integer, Integer> levelToExperience = new HashMap<Integer, Integer>() {
        {
            this.put(1, 1000);
            this.put(2, 2000);
            this.put(3, 0);
        }
    };

    public BonusWeapon(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "ItemStack must not be null");
        this.itemStack = itemStack;
    }

    public NBTTagCompound getDataCompound() {
        return (new NBTWrapper(this.itemStack)).getCompoundOrCreate("weaponBonusData").get();
    }

    public int getLevel() {
        return this.getDataCompound().getInt("weaponBonusLevel");
    }

    public void setLevel(int level) {
        this.getDataCompound().setInt("weaponBonusLevel", level);
    }

    public List<String> getBonuses() {
        return Arrays.stream(this.getDataCompound().getString("weaponBonuses").split("@")).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setBonuses(List<String> bonuses) {
        this.getDataCompound().setString("weaponBonuses", String.join("@", bonuses));
    }

    public int getExperience() {
        return this.getDataCompound().getInt("weaponBonusExperience");
    }

    public void setExperience(int experience) {
        this.getDataCompound().setInt("weaponBonusExperience", experience);
    }

    public void addExperience(int experience) {
        if (this.getLevel() == 3) {
            this.setExperience(0);
        } else {
            Integer toNext = this.levelToExperience.get(this.getLevel());
            if (toNext != null) {
                this.setExperience(this.getExperience() + experience);
                if (this.getExperience() >= toNext) {
                    this.setExperience(this.getExperience() - toNext);
                    this.setLevel(this.getLevel() + 1);
                    this.addExperience(0);
                }
            }

        }
    }

    public void levelUp() {
        if (this.getLevel() != 3) {
            int newLevel = this.getLevel() + 1;
            this.setLevel(newLevel);
            this.setExperience(0);
        }

    }

    public void applyVisualChanges() {
        this.applyVisualChanges("6");
    }

    public void applyVisualChanges(String inColor) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            ItemMeta newMeta = itemMeta.clone();
            List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : Lists.newArrayList();
            boolean hasWeaponBonus = false;

            for (Object o : lore) {
                String l = (String) o;
                if (l.contains("Weapon Bonuses:")) {
                    hasWeaponBonus = true;
                    break;
                }
            }

            if (!hasWeaponBonus) {
                lore.add("");
                lore.add("§" + inColor + "§lWeapon Bonuses:");
                this.getBonuses().stream().filter((strx) -> {
                    return !strx.isEmpty();
                }).map((raw) -> {
                    raw = raw.replaceAll(" ", "_").toUpperCase();

                    try {
                        BonusType result = BonusType.valueOf(raw);
                        return result;
                    } catch (Exception var3) {
                        throw new IllegalStateException("Could not get BonusType for '" + raw + "'");
                    }
                }).forEach((type) -> {
                    lore.add("  §" + inColor + "§l* §7" + type.getFormat().replace("%value%", type.getLevelToValue().get(this.getLevel()).toString()));
                });
                lore.add("");
            }

            int levelIndex = -1;
            Iterator var11 = ((List)lore).iterator();

            while(var11.hasNext()) {
                String str = (String)var11.next();
                if (str.matches("§.§lWEAPON TIER: §a.+")) {
                    levelIndex = lore.indexOf(str);
                }
            }

            if (levelIndex == -1) {
                lore.add("§4Level placeholder");
                levelIndex = lore.indexOf("§4Level placeholder");
            }

            int expIndex = -1;
            Iterator var13 = ((List)lore).iterator();

            while(var13.hasNext()) {
                String str = (String)var13.next();
                if (str.matches("§.§lWEAPON XP: §7\\(§a\\d+ §7/ \\d+ XP\\)")) {
                    expIndex = lore.indexOf(str);
                }
            }

            if (expIndex == -1) {
                lore.add("EXP-placeholder");
                expIndex = lore.size() - 1;
            }

            ((List)lore).set(levelIndex, Constants.Lore.WEAPON_LEVEL.replace("%current%", this.getLevel() + "").replace("%max%", "3").replace("%color%", inColor));
            ((List)lore).set(expIndex, Constants.Lore.WEAPON_EXPERIENCE.replace("%current%", this.getExperience() + "").replace("%max%", this.levelToExperience.getOrDefault(this.getLevel(), 0) + "").replace("%color%", inColor));
            newMeta.setLore(lore);
            this.itemStack.setItemMeta(newMeta);
        }
    }

    private <T> T orDefault(@Nullable T value, T defaultValue) {
        Preconditions.checkNotNull(defaultValue, "Default value must not be null");
        return value == null ? defaultValue : value;
    }
}
