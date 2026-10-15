package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.utils.config.ConfigManager;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UltraArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&7&lUltra Hat")).addLoreLines(CC.translate("&7The Ultra Helmet.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&7&lUltra Jacket")).addLoreLines(CC.translate("&7The Ultra Jacket.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&7&lUltra Short")).addLoreLines(CC.translate("&7The Ultra Short.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&7&lUltra Boots.")).addLoreLines(
                CC.translate("&7The Ultra Boots."))
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 4;
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 35 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ultraSetDamageIncrease") * 0.01));
        if (Math.random() < 0.2) {
            activateUltraAbility(attacker, (Player) attacked, event);
        }
        event.setDamage(event.getDamage() * (1 + (increasePercent * 0.01)));
    }


    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 10 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ultraSetDamageIncrease") * 0.01));
        if (Math.random() < 0.1) {
            activateUltraAbility(attacker, (Player) attacked, event);
        }
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));

    }

    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 5 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ultraSetDamageDecrease") * 0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount * 0.01)));
    }

    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 20 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ultraSetDamageDecrease") * 0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * 0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7&lULTRA SET BONUS"));
        lore.add(CC.translate("&8● &7Deal +35% damage to all enemies."));
        lore.add(CC.translate("&8● &7Enemies deal 20% less damage to you."));
        lore.add(CC.translate("&8● &740% Holy Ultra Negation."));
        lore.add(CC.translate("&8● &7Ultra Rage Ability"));
        lore.add(CC.translate("&8● &740% Chance to bypass Utility Belt"));
        lore.add(CC.translate("&7(&oRequires all 4 ultra items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.Gray + "-5% Incoming Damage");
        message.add(CC.Gray + "+10% Outgoing Damage");
        message.add(CC.Gray + "Ultra Rage Ability");
        message.add(CC.Gray + "10% Chance to bypass Utility Belt");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&7&lULTRA SET BONUS"));
        message.add(CC.translate("&8● &7Deal +35% damage to all enemies."));
        message.add(CC.translate("&8● &7Enemies deal 20% less damage to you."));
        message.add(CC.translate("&8● &740% Holy Ultra Negation."));
        message.add(CC.translate("&8● &7Ultra Rage Ability"));
        message.add(CC.translate("&8● &740% Chance to bypass Utility Belt"));
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

    public void activateUltraAbility(Player player, final Player target, EntityDamageByEntityEvent event) {
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.THANKSGIVING_2022_BELT) && Math.random() < 0.25) {
            target.sendMessage(CC.translate("&e&l* MATRIX MASTER BELT &7[Blocked Ultra Rage] &e&l*"));
            return;
        }
        event.setDamage(event.getDamage() * 1.15);
        target.sendMessage(CC.translate((String) ("&e&l*&4&l*&e&l* &7&lULTRA STRIKED &f[&c" + player.getName() + "&f] &c&l*&4&l*&e&l*")));
        player.sendMessage(CC.translate((String) "&e&l*&4&l*&e&l* &f&lULTRA STRIKE &7x2 &f&lDMG &e&l*&4&l*&e&l*"));
        target.playEffect(EntityEffect.VILLAGER_ANGRY);
    }
}
