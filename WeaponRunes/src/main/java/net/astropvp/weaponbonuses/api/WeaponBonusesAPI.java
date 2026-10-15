package net.astropvp.weaponbonuses.api;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.astropvp.weaponbonuses.bonus.BonusType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class WeaponBonusesAPI {
    public static boolean hasWeaponBonuses(@Nullable ItemStack weapon) {
        return weapon != null && weapon.getType() != Material.AIR && (new NBTWrapper(weapon)).hasKey("weaponBonusData");
    }

    public static int getLevel(@Nullable ItemStack weapon) {
        return (new NBTWrapper(weapon)).getCompound("weaponBonusData").getInt("weaponBonusLevel");
    }

    public static Map<BonusType, Integer> getBonuses(ItemStack weapon) {
        Preconditions.checkNotNull(weapon, "Weapon can not be null");
        List<String> collect = (List)Arrays.stream((new NBTWrapper(weapon)).getCompound("weaponBonusData").getString("weaponBonuses").split("@")).collect(Collectors.toList());
        Map<BonusType, Integer> out = new HashMap();
        Iterator var3 = collect.iterator();

        while(var3.hasNext()) {
            String collected = (String)var3.next();
            out.put(BonusType.valueOf(collected), getLevel(weapon));
        }

        return out;
    }

    public static int getBonusLevel(ItemStack weapon, BonusType bonusType) {
        Integer result = (Integer)getBonuses(weapon).get(bonusType);
        return (Integer)Preconditions.checkNotNull(result, "Bonus " + bonusType + " not found on weapon!");
    }

    public static boolean hasBonus(ItemStack weapon, BonusType bonusType) {
        List<String> collect = (List)Arrays.stream((new NBTWrapper(weapon)).getCompound("weaponBonusData").getString("weaponBonuses").split("@")).collect(Collectors.toList());
        return collect.contains(bonusType.toString());
    }

    public static int getExperience(ItemStack weapon) {
        return (new NBTWrapper(weapon)).getCompound("weaponBonusData").getInt("weaponBonusExperience");
    }

    public static double getBonusValue(ItemStack weapon, BonusType bonusType) {
        return (Double)Preconditions.checkNotNull(bonusType.getLevelToValue().get(getBonusLevel(weapon, bonusType)), "Bonus " + bonusType + " not found on that weapon");
    }
}
