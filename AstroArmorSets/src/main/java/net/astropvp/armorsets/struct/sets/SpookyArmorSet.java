package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.managers.HeadlessAbilityManager;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.utils.config.ConfigManager;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpookyArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&6&lS&8&lp&6&lo&8&lo&6&lk&8&ly &6&lP&8&lu&6&lm&8&lp&6&lk&8&li&6&ln")).addLoreLines(CC.translate("&7Scary helmet for halloween.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&6&lS&8&lp&6&lo&8&lo&6&lk&8&ly &6&lC&8&lh&6&le&8&ls&6&lt&8&lp&6&ll&8&la&6&lt&8&le")).addLoreLines(CC.translate("&7Scary chestplate for halloween.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&6&lS&8&lp&6&lo&8&lo&6&lk&8&ly &6&lL&8&le&6&lg&8&lg&6&li&8&ln&6&lg&8&ls")).addLoreLines(CC.translate("&7Scary leggings for halloween.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&6&lS&8&lp&6&lo&8&lo&6&lk&8&ly &6&lB&8&lo&6&lo&8&lt&6&ls")).addLoreLines(CC.translate("&7Scary boots for halloween."))
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 4;
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 35 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spookySetDamageIncrease") * 0.01));

        event.setDamage(event.getDamage() * (1 + (increasePercent * 0.01)));

        if (Math.random() < 0.04 && attacked instanceof Player) {
            AstroArmorSets.getInstance().getHeadlessAbilityManager().activateAbility(attacker, (Player) attacked);
        }
    }


    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 10 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spookySetDamageIncrease") * 0.01));

        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));

        if (Math.random() < 0.01 && attacked instanceof Player) {
            AstroArmorSets.getInstance().getHeadlessAbilityManager().activateAbility(attacker, (Player) attacked);
        }
    }

    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 5 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spookySetDamageDecrease") * 0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount * 0.01)));
    }

    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 20 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spookySetDamageDecrease") * 0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * 0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&6&lS&8&lP&6&lO&8&lO&6&lK&8&lY &6&lS&8&lE&6&lT &8&lB&6&lO&8&lN&6&lU&8&lS"));
        lore.add(CC.translate( "&6-&820&6% &8Incoming &6Damage"));
        lore.add(CC.translate( "&6+&835% &8Outgoing &6Damage"));
        lore.add(CC.translate( "&6Trick&8-&6or&8-&6Treat &8Ability"));
        lore.add(CC.translate("&6Bypass &8enemy &6Cage &8Ability &6Immunity"));
        lore.add(CC.translate( "&6Auto &8Bless"));
        lore.add(CC.translate("&7(&oRequires all 4 spooky items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.translate( "&6-&85&6% &8Incoming &6Damage"));
        message.add(CC.translate( "&6+&810% &8Outgoing &6Damage"));
        message.add(CC.translate( "&6Trick&8-&6or&8-&6Treat &8Ability"));
        message.add(CC.translate("&625% &8to &6bypass &8enemy &6Cage &8Ability &6Immunity"));
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&6&lS&8&lP&6&lO&8&lO&6&lK&8&lY &6&lS&8&lE&6&lT &8&lB&6&lO&8&lN&6&lU&8&lS"));
        message.add(CC.translate( "&6-&820&6% &8Incoming &6Damage"));
        message.add(CC.translate( "&6+&835% &8Outgoing &6Damage"));
        message.add(CC.translate( "&6Trick&8-&6or&8-&6Treat &8Ability"));
        message.add(CC.translate("&6Bypass &8enemy &6Cage &8Ability &6Immunity"));
        message.add(CC.translate( "&6Auto &8Bless"));
        message.add("");
        return message;
    }

    public void onEquip(Player player) {
        Blessed.blessPlayer(player);
        AutoBlessListener.blessedPlayers.add(player.getUniqueId());
    }

    public void onUnequip(Player player) {
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());

    }

}
