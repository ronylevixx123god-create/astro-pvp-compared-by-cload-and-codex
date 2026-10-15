package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetWeapon;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DragonArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&e&lDecapitated Dragon Skull")).addLoreLines(CC.translate("&eThe mythical helmet of dragons.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&e&lFirey Chestplate of Dragons")).addLoreLines(CC.translate("&eThe mythical chestplate of dragons.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&e&lScorched Leggings of Dragons")).addLoreLines(CC.translate("&eThe mythical leggings of dragons.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&e&lDragon Slayer Battle Boots")).addLoreLines(CC.translate("&eThe mythical boots of dragons.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 0;
    }


    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 5;
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount * 0.01)));
    }


    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 20;
        event.setDamage(event.getDamage() * (1 - (reductionPercent * 0.01)));
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 15;
        event.setDamage(event.getDamage() * (1 + (increasePercent * 0.01)));
    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 3;
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&e&lDRAGON SET BONUS"));
        lore.add(CC.translate("&8● &eEnemies deal 20% less damage to you."));
        lore.add(CC.translate("&8● &eDeal +15% damage to all enemies."));
        lore.add(CC.translate("&8● &eImmune to Freezes."));
        lore.add(CC.translate("&8● &eEnchant Reflect X."));
        lore.add(CC.translate("&8● &eNegate 75% of enemy Silence Enchant."));
        lore.add(CC.translate("&7(&oRequires all 4 dragon items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.Yellow + "+3% Outgoing Damage");
        message.add(CC.Yellow + "-5% Incoming Damage");
        message.add(CC.Yellow + "10% Silence Enchant Immunity");
        message.add(CC.Yellow + "10% Freeze Immunity");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&e&lDRAGON SET BONUS"));
        message.add(CC.translate("&8● &eEnemies deal 20% less damage to you."));
        message.add(CC.translate("&8● &eDeal +15% damage to all enemies."));
        message.add(CC.translate("&8● &eImmune to Freezes."));
        message.add(CC.translate("&8● &eEnchant Reflect X."));
        message.add(CC.translate("&8● &eNegate 75% of enemy Silence Enchant."));
        message.add("");
        return message;
    }


}
