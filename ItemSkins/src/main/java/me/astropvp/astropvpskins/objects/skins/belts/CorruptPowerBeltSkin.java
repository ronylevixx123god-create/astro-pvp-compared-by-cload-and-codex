package me.astropvp.astropvpskins.objects.skins.belts;

import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.SpecificCustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class CorruptPowerBeltSkin extends ItemSkin {

    public void onEquip(Player player) {

        String world = player.getWorld().getName();

        switch (world) {
            case "world_the_end":
            case "world_outpost_pluto":
            case "space":
            case "mars":
                return;
        }
        EnchantmentAPI.getEnchantment("Godly Overload").applyEquipEffect(player, 5);
    }

    public void onUnequip(Player player) {
        System.out.println("[PowerBelt] Attempting to remove godly overload 5]");
        EnchantmentAPI.getEnchantment("Godly Overload").applyUnequipEffect(player, 5);
        PlayerPotionEffects.untrackPotionEffect(player, new SpecificCustomEnchantment(EnchantmentAPI.getEnchantment("Godly Overload"), 5));
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);

    }
}

