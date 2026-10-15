package net.astropvp.armorsets.struct.heroicArmor;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import de.tr7zw.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.astropvp.armorsets.utils.Pair;
import net.astropvp.armorsets.utils.modifiers.AttributeModifier;
import net.astropvp.armorsets.utils.modifiers.ModifierType;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum HeroicArmor {
    HELMET(3, 1, Material.LEATHER_HELMET, CC.DarkRedB + "Heroic Helmet", false),
    CHESTPLATE(8, 1, Material.LEATHER_CHESTPLATE, CC.DarkRedB + "Heroic Chestplate", false),
    LEGGINGS(6, 1, Material.LEATHER_LEGGINGS, CC.DarkRedB + "Heroic Leggings", false),
    BOOTS(3, 1, Material.LEATHER_BOOTS, CC.DarkRedB + "Heroic Boots", false),
    SWORD(1, 4, Material.GOLD_SWORD, CC.DarkRedB + "Heroic Sword", true),
    AXE(1, 4, Material.GOLD_AXE, CC.DarkRedB + "Heroic Axe", true);

    public int reduction;
    public double increase;
    public Material type;
    public String display;
    public boolean isWeapon;



    public static List<ItemStack> getHeroicItems() {
        List<ItemStack> items = new ArrayList<>();
        for (HeroicArmor armor : HeroicArmor.values()) {
            ItemStack item;
            if (armor.isWeapon) {
                item = new ItemBuilder(armor.type).setColor(Color.RED)
                        .setName(armor.display)
                        .addLoreLine("")
                        .addLoreLine(CC.Gray + "+" + (int) armor.increase + " Bonus Attack Damage")
                        .addLoreLine(CC.DarkRed + "This weapon is stronger than diamond.")
                        .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                        .addEnchantment(Enchantment.DURABILITY, 3)
                        .build();
                ItemMeta meta = item.getItemMeta();
                meta.spigot().setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                Pair<ModifierType, Double> modifierTypePair = new Pair<>(ModifierType.ATTACK_DAMAGE, 7D);
                item= AttributeModifier.applyModifications(item, Collections.singletonList(modifierTypePair));
            } else {
                item = new ItemBuilder(armor.type).setColor(Color.RED)
                        .setName(armor.display)
                        .addLoreLine("")
                        .addLoreLine(CC.Gray + "+" + (int) armor.reduction + " Armor Value")
                        .addLoreLine(CC.DarkRed + "This armor is stronger than diamond.")
                        .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                        .addEnchantment(Enchantment.DURABILITY, 3)
                        .build();
                ItemMeta meta = item.getItemMeta();
                meta.spigot().setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setInteger("wine-armor-override", armor.reduction);
                item = nbtItem.getItem();
            }

            NBTWrapper wrapper = new NBTWrapper(item);


            NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("cosmicData");
            builder.setBoolean("heroicArmor", true);
            wrapper.setNBTBase("cosmicData", (NBTBase) builder.get());
            items.add(wrapper.build());


        }
        return items;
    }

    public static HeroicArmor getFromMaterial(Material material) {
        String materialName = material.name();
        for (HeroicArmor type : HeroicArmor.values()) {
            if (!materialName.endsWith(type.name())) continue;
            return type;
        }
        return null;
    }
}
