package net.astropvp.core.utils;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.core.objects.EnchantmentCrystalType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCrystalUtils {
    public static boolean isEnchantmentCrystal(ItemStack item) {
        if(item == null || item.getType() == Material.AIR)
            return false;
        NBTWrapper wrapper = new NBTWrapper(item);
        if(!wrapper.hasKey("cosmicType"))
            return false;
        return wrapper.getString("cosmicType").equals("enchantmentCrystal");
    }

    public static boolean hasEnchantment(ItemStack item, Enchantment enchantment) {
        return item.getEnchantments().containsKey(enchantment);
    }

    public static Enchantment getEnchantmentTypeFromCrystal(ItemStack item) {
        if(item == null || item.getType() == Material.AIR || !item.hasItemMeta())
            return null;
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("conaxData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData")) : new NBTTagCompoundBuilder();
        EnchantmentCrystalType type = EnchantmentCrystalType.valueOf(cosmicData.getString("enchantmentCrystalType"));
        return type.getEnchantment();
    }
}
