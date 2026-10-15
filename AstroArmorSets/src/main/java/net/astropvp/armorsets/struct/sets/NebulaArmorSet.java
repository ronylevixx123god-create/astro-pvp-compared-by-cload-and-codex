package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetWeapon;
import net.astropvp.armorsets.utils.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NebulaArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&5&lNebula Dome")).
                addLoreLines(CC.translate("&5Astronomical Nebula Dome.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&5&lNebula Suit")).
                addLoreLines(CC.translate("&5Astronomical Nebula Suit.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&5&lNebula Shorts")).
                addLoreLines(CC.translate("&5Astronomical Nebula Shorts")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&5&lNebula Space Boots")).addLoreLines(
                        CC.translate("&5Astronomical Nebula Space Boots."))
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();

    }

    @Override
    public int getLadderTier() {
        return 0;
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 45*  (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("nebulaSetDamageIncrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent  * 0.01)));    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 8* (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("nebulaSetDamageIncrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));    }

    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 6 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("nebulaSetDamageDecrease")  *  0.01));
        event.setDamage(event.getDamage() * (1-(reductionPercent*amount*0.01)));    }

    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 20 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("nebulaSetDamageDecrease")  *  0.01));
        event.setDamage(event.getDamage() * (1-(reductionPercent*0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&5&lNEBULA SET BONUS"));
        lore.add(CC.translate("&8● &5Enemies deal 20% less damage to you."));
        lore.add(CC.translate("&8● &5Deal +45% damage to all enemies."));
        lore.add(CC.translate("&8● &5Instant Health Pots heal for more HP"));
        lore.add(CC.translate("&8● &530% Chance to bypass Heroic Armor"));
        lore.add(CC.translate("&8● &515% Nebula Ability chance"));
        lore.add(CC.translate("&7(&oRequires all 4 nebula items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.DarkPurple + "+8% Outgoing Damage");
        message.add(CC.DarkPurple + "-6% Incoming Damage");
        message.add(CC.DarkPurple + "Instant Health Pots heal for more HP");
        message.add(CC.DarkPurple + "15% Chance to bypass Heroic Armor");
        message.add(CC.DarkPurple + "10% Nebula Ability chance");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&5&lNEBULA SET BONUS"));
        message.add(CC.translate("&8● &5Enemies deal 20% less damage to you."));
        message.add(CC.translate("&8● &5Deal +45% damage to all enemies."));
        message.add(CC.translate("&8● &5Instant Health Pots heal for more HP"));
        message.add(CC.translate("&8● &530% Chance to bypass Heroic Armor"));
        message.add(CC.translate("&8● &515% Nebula Ability chance"));
        message.add("");
        return message;
    }



}
