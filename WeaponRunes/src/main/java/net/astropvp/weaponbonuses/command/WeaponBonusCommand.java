package net.astropvp.weaponbonuses.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.weaponbonuses.api.WeaponBonusesAPI;
import net.astropvp.weaponbonuses.bonus.BonusType;
import net.astropvp.weaponbonuses.bonus.BonusWeapon;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class WeaponBonusCommand extends BaseCommand {
    private ItemStack build;

    @CommandAlias("givebonusweapon")
    @CommandCompletion("@players @bonusWeaponTypes")
    @Syntax("<player> <type>")
    @CommandPermission("astroweaponbonuses.admin")
    public void onGiveCommand(Player player, OnlinePlayer target, @Default("") String inputType) {
        String type = inputType.toLowerCase();
        if (!type.toLowerCase().equals("champion") && !type.toLowerCase().equals("ultra") && !type.toLowerCase().equals("superior")) {
            player.sendMessage("§cUsage: §7/givebonusweapon <player> <type>");
            player.sendMessage("§cExample: §7/givebonusweapon bryside champion");
        } else {
            List<String> bonuses = new ArrayList(Arrays.asList(BonusType.BASE_PVP_DMG.name(), BonusType.BONUS_RAGE_STACK_DMG.name()));

            if (type.equals("superior")) {
                bonuses.add(BonusType.LESS_INCOMING_DMG.name());
                bonuses.add(BonusType.EXTRA_HP_FROM_POTIONS.name());
            }

            if (type.equals("ultra")) {
                bonuses.add(BonusType.LESS_INCOMING_DMG.name());
            }

            String name;
            if (type.equals("champion")) {
                name = "&6&k:&f&k:&7 &f&l&k✘&6&l Champion Admin Item &f&l✘ &f&k:&6&k:&7";
            } else if (type.equals("ultra")) {
                name = "&8&k:&f&k:&7 &f&l&k✘&7&l Ultra Admin Item &f&l✘ &f&k:&8&k:&7";
            } else {
                name = "&5&k:&f&k:&7 &f&l&k✘&5&l Superior Admin Item &f&l✘ &f&k:&5&k:&7";
            }

            String color;
            if (type.equals("champion")) {
                color = "6";
            } else if (type.equals("ultra")) {
                color = "7";
            } else {
                color = "5";
            }

            List<String> lore = Lists.newArrayList();
            lore.add("");
            lore.add(CC.translate("&" + color + "&l(!) &fThis is an &" + color + "Admin Item &fWeapon."));
            ItemStack sword = CommonsUtil.createItem(Material.DIAMOND_SWORD, name, lore);
            sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            sword.addEnchantment(Enchantment.DURABILITY, 3);
            sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);

            CustomEnchantment rage = EnchantmentAPI.getEnchantment("Rage");
            if (rage != null) {
                rage.addToItem(sword, 6);
            }

            CustomEnchantment hero_killer = EnchantmentAPI.getEnchantment("Hero Killer");
            if (hero_killer != null) {
                hero_killer.addToItem(sword, 3);
            }

            CustomEnchantment blackout = EnchantmentAPI.getEnchantment("Blackout");
            if (blackout != null) {
                blackout.addToItem(sword, 4);
            }

            CustomEnchantment neutralize = EnchantmentAPI.getEnchantment("Neutralize");
            if (neutralize != null) {
                neutralize.addToItem(sword, 5);
            }

            sword = AstroArmorSetsAPI.convertItemToHeroic(sword);

            NBTWrapper wrapper = new NBTWrapper(sword);
            NBTTagCompoundBuilder compoundOrCreate = wrapper.getCompoundOrCreate("weaponBonusData");
            compoundOrCreate.setInt("weaponBonusLevel", 1);
            compoundOrCreate.setInt("weaponBonusExperience", 0);
            compoundOrCreate.setString("weaponBonuses", String.join("@", bonuses));
            wrapper.setNBTBase("weaponBonusData", compoundOrCreate.get());
            this.build = wrapper.build();
            (new BonusWeapon(this.build)).applyVisualChanges("6");



            player.getInventory().addItem(new ItemStack[]{this.build});
        }
    }

    public static boolean isSkitItem(@Nullable ItemStack weapon) {
        if (weapon != null && weapon.getType() != Material.AIR) {
            int index = -1;
            Iterator var2 = weapon.getItemMeta().getLore().iterator();

            while (var2.hasNext()) {
                String lore = (String) var2.next();
                ++index;
                if (CC.strip(lore).equalsIgnoreCase("(!) This is an Admin Item Weapon.")) {
                    return true;
                }
            }
        }

        return false;
    }

    @CommandAlias("levelupbonusweapon")
    @CommandPermission("astroweaponbonuses.levelupcommand")
    public void onGiveCommand(Player player) {
        ItemStack weapon = player.getItemInHand();
        if (!WeaponBonusesAPI.hasWeaponBonuses(weapon)) {
            player.sendMessage("§c§l(!) §cYou must be holding a weapon with bonuses on it to level it up!");
        } else {
            BonusWeapon bonusWeapon = new BonusWeapon(weapon);
            bonusWeapon.levelUp();
            bonusWeapon.applyVisualChanges();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            player.sendMessage("§a§l(!) §aLeveled up!");
        }
    }
}
