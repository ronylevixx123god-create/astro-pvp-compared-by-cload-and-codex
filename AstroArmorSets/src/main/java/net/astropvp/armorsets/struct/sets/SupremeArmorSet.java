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

public class SupremeArmorSet extends ArmorSet implements ArmorSetWeapon {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&4&lSupreme Headgear")).addLoreLines(CC.translate("&4A lightweight headpiece fit fot take off.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&4&lSupreme Vest")).addLoreLines(CC.translate("&4A lightweight headpiece fit fot take off.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&4&lSupreme Chaps")).addLoreLines(CC.translate("&4A lightweight headpiece fit fot take off.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&4&lSupreme Thruster Boots")).addLoreLines(CC.translate("&4A lightweight headpiece fit fot take off.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 15;
        event.setDamage(event.getDamage() * (1 + (increasePercent  * 0.01)));    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 3;
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));    }

    @Override
    public int getLadderTier() {
        return 0;
    }

    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 10;
        event.setDamage(event.getDamage() * (1-(reductionPercent*0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&4&lSUPREME SET BONUS"));
        lore.add(CC.translate("&8● &4Gears IV."));
        lore.add(CC.translate("&8● &4Deal +15% damage to all enemies."));
        lore.add(CC.translate("&8● &4Enemy arrows deal 10% less to you."));

        lore.add(CC.translate("&7(&oRequires all 4 supreme items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.DarkRed + "+3% Outgoing Damage");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&4&lSUPREME SET BONUS"));
        message.add(CC.translate("&8● &4Gears IV."));
        message.add(CC.translate("&8● &4Deal +15% damage to all enemies."));
        message.add(CC.translate("&8● &4Enemy arrows deal 10% less to you."));
        message.add("");
        return message;
    }

    public List<ItemStack> getWeapons() {
        List<ItemStack> weapons = new ArrayList<>();
        ItemStack sword = new NBTWrapper(new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(CC.DarkRedB + "Supreme Fanny Pack")
                .addLoreLines(CC.Gray + "They laughed at you, they"
                        ,CC.Gray + "told you a fanny pack is not"
                        , CC.Gray + "a practical piece of clothing..."
                        , CC.Gray + "now they will pay for their ignorance."
                        , ""
                        , CC.DarkRedB + "SUPREME WEAPON BONUS"
                        , CC.translate("&8● &4Deal +20% damage to all enemies.")
                        , CC.translate("&8● &4Enemies deal 20% less to you.")
                        , CC.Gray + "(" + CC.GrayI + "Requires all 4 supreme items." + CC.Gray + ")")
                .addEnchantment(Enchantment.DAMAGE_ALL, 5).addEnchantment(Enchantment.DURABILITY, 3).build()).setString("cosmicType", "SUPREME_SWORD").build();

        weapons.add(sword);
        return weapons;
    }


}
