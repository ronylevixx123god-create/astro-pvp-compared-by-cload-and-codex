package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.FrozenTropicsCooldown;
import net.astropvp.armorsets.tasks.FrozenTropicsAbility;
import net.astropvp.armorsets.utils.config.ConfigManager;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FrostyArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&b&lF&3&lr&b&lo&3&ls&b&lt&3&ly &b&lH&3&le&b&ll&3&lm&b&le&3&lt")).addLoreLines(CC.translate("&7Frosty helmet for winter.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&b&lF&3&lr&b&lo&3&ls&b&lt&3&ly &b&lC&3&lh&b&le&3&ls&b&lt&3&lp&b&ll&3&la&b&lt&3&le")).addLoreLines(CC.translate("&7Scary chestplate for winter.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&b&lF&3&lr&b&lo&3&ls&b&lt&3&ly &b&lL&3&le&b&lg&3&lg&b&li&3&ln&b&lg&3&ls")).addLoreLines(CC.translate("&7Frosty leggings for winter.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&b&lF&3&lr&b&lo&3&ls&b&lt&3&ly &b&lB&3&lo&b&lo&3&lt&b&ls")).addLoreLines(CC.translate("&7Frosty boots for winter."))
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 4;
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 35 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spookySetDamageIncrease") * 0.01));

        event.setDamage(event.getDamage() * (1 + (increasePercent * 0.01)));

        if (Math.random() < 0.03 && attacked instanceof Player) {
            if (CooldownUtils.isCooldownActive((Player) attacked, "frozenTropics")) return;
            CooldownUtils.registerCooldown((Player) attacked, new FrozenTropicsCooldown("frozenTropics", 60));
            FrozenTropicsAbility frozenTropicsAbility = new FrozenTropicsAbility((Player) attacker, 8, 8);
            frozenTropicsAbility.runTaskTimer(AstroArmorSets.getInstance(), 0L, 20L);
            attacked.sendMessage(CC.translate("&b&l*&f&l*&b&l* &3&lFROZEN TROPICS ABILITY &7[&c" + attacker.getName() + "&7] &b&l*&f&l*&b&l*"));
            attacker.sendMessage(CC.translate("&b&l*&f&l*&b&l* &3&lFROZEN TROPICS ABILITY &7[&a" + attacked.getName() + "&7] &b&l*&f&l*&b&l*"));
        }
    }


    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 10 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spookySetDamageIncrease") * 0.01));

        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));


        if (Math.random() < 0.01 && attacked instanceof Player) {
            if (CooldownUtils.isCooldownActive((Player) attacked, "frozenTropics")) return;
            CooldownUtils.registerCooldown((Player) attacked, new FrozenTropicsCooldown("frozenTropics", 60));
            FrozenTropicsAbility frozenTropicsAbility = new FrozenTropicsAbility((Player) attacker, 8, 8);
            frozenTropicsAbility.runTaskTimer(AstroArmorSets.getInstance(), 0L, 20L);
            attacked.sendMessage(CC.translate("&b&l*&f&l*&b&l* &3&lFROZEN TROPICS ABILITY &7[&c" + attacker.getName() + "&7] &b&l*&f&l*&b&l*"));
            attacker.sendMessage(CC.translate("&b&l*&f&l*&b&l* &3&lFROZEN TROPICS ABILITY &7[&a" + attacked.getName() + "&7] &b&l*&f&l*&b&l*"));
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
        lore.add(CC.translate("&8● &b&lF&3&lR&b&lO&3&lS&b&lT&3&lY &b&lS&3&lE&b&lT &3&lB&b&lO&3&lN&b&lU&3&lS"));
        lore.add(CC.translate( "&8● &b-&320&b% &3Incoming &bDamage"));
        lore.add(CC.translate( "&8● &b+&335% &3Outgoing &bDamage"));
        lore.add(CC.translate("&8● &bBypass &3enemy &bCage &3Ability &bImmunity"));
        lore.add(CC.translate( "&8● &bFrozen &3Tropics &bAbility"));
        lore.add(CC.translate( "&8● &bImmune to all freezes"));
        lore.add(CC.translate( "&8● &bAuto &3Bless"));
        lore.add(CC.translate("&7(&oRequires all 4 frosty items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.translate( "&b-&35&b% &3Incoming &bDamage"));
        message.add(CC.translate( "&b+&310% &3Outgoing &bDamage"));
        message.add(CC.translate( "&bFrozen &3Tropics &bAbility"));
        message.add(CC.translate( "&bImmune to all freezes"));
        message.add(CC.translate("&b25% &3to &bbypass &3enemy &bCage &3Ability &bImmunity"));
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&b&lF&3&lR&b&lO&3&lS&b&lT&3&lY &b&lS&3&lE&b&lT &3&lB&b&lO&3&lN&b&lU&3&lS"));
        message.add(CC.translate( "&8● &b-&320&b% &3Incoming &bDamage"));
        message.add(CC.translate( "&8● &b+&335% &3Outgoing &bDamage"));
        message.add(CC.translate( "&8● &bFrozen &3Tropics &bAbility"));
        message.add(CC.translate("&8● &bBypass &3enemy &bCage &3Ability &bImmunity"));
        message.add(CC.translate( "&8● &bImmune to all freezes"));
        message.add(CC.translate( "&8● &bAuto &3Bless"));
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
