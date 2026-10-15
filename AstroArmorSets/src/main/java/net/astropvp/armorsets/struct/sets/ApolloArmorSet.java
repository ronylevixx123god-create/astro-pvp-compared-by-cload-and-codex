package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
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

public class ApolloArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&f&lApollo Crown")).addLoreLines(CC.translate("&fApollo God Crown.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&f&lApollo Torso")).addLoreLines(CC.translate("&fApollo God Torso.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&f&lApollo Jeans")).addLoreLines(CC.translate("&fApollo God Jeans.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&f&lApollo Kicks")).addLoreLines(
                CC.translate("&fApollo God Kicks."))
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 5;
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 40 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("apolloSetDamageIncrease") * 0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent * 0.01)));
    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 15 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("apolloSetDamageIncrease") * 0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));
    }

    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 10 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("apolloSetDamageDecrease") * 0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount * 0.01)));
    }

    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 25 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("apolloSetDamageDecrease") * 0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * 0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&f&lAPOLLO SET BONUS"));
        lore.add(CC.translate("&8● &fDeal +40% damage to all enemies."));
        lore.add(CC.translate("&8● &fEnemies deal 25% less damage to you."));
        lore.add(CC.translate("&8● &f1.5x Pet Duration."));
        lore.add(CC.translate("&8● &fImmune to Witcher Pet."));
        lore.add(CC.translate("&8● &fBypass Enemy Silence Negation"));
        lore.add(CC.translate("&8● &fAuto Bless"));
        lore.add(CC.translate("&7(&oRequires all 4 apollo items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.White + "-10% Incoming Damage");
        message.add(CC.White + "+15% Outgoing Damage");
        message.add(CC.White + "20% Immune To Witcher Pet");
        message.add(CC.White + "25% Bypass Enemy Silence Negation");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&f&lAPOLLO SET BONUS"));
        message.add(CC.translate("&8● &fDeal +40% damage to all enemies."));
        message.add(CC.translate("&8● &fEnemies deal 25% less damage to you."));
        message.add(CC.translate("&8● &f1.5x Pet Duration."));
        message.add(CC.translate("&8● &fImmune to Witcher Pet."));
        message.add(CC.translate("&8● &fBypass Enemy Silence Negation"));
        message.add(CC.translate("&8● &fAuto Bless"));
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
