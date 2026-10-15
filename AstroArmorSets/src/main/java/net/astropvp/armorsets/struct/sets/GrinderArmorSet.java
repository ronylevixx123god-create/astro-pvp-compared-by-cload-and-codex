package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetWeapon;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GrinderArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&8&lGrinder Helmet")).addLoreLines(CC.translate("&8Grinding blazes all day long....")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&8&lGrinder Torso")).addLoreLines(CC.translate("&8Grinding blazes all day long....")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&8&lGrinder Pants")).addLoreLines(CC.translate("&8Grinding blazes all day long....")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&8&lGrinder Kicks")).addLoreLines(CC.translate("&8Grinding blazes all day long....")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 0;
    }


    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&8&lGRINDER SET BONUS"));
        lore.add(CC.translate("&8● &81.5x Increased EXP Gain."));
        lore.add(CC.translate("&8● &8Increased chance for gaining keys."));
        lore.add(CC.translate("&7(&oRequires all 4 grinder items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        return new ArrayList<>();
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&8&lGRINDER SET BONUS"));
        message.add(CC.translate("&8● &81.5x Increased EXP Gain."));
        message.add(CC.translate("&8● &8Increased chance for gaining keys."));
        message.add("");
        return message;
    }
}
