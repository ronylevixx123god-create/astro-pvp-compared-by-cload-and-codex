package me.astropvp.astropvpskins.objects.skins.belts;

import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.SpecificCustomEnchantment;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CowboyChaps extends ItemSkin {
    public void onEquip(Player player) {

        if (player.getLocation().getWorld().getName().equals("world_the_end"))
            return;
        if (player.getLocation().getWorld().getName().equals("space"))
            return;
        if (player.getLocation().getWorld().getName().equals("mars"))
            return;
        if (player.getLocation().getWorld().getName().equals("world_outpost_pluto"))
            return;
        EnchantmentAPI.getEnchantment("Godly Overload").applyEquipEffect(player, 4);

            Blessed.blessPlayer(player);
            AutoBlessListener.blessedPlayers.add(player.getUniqueId());

    }

    public void onUnequip(Player player) {
        System.out.println("[CowboyChaps] Attempting to remove godly overload 4]");
        EnchantmentAPI.getEnchantment("Godly Overload").applyUnequipEffect(player, 4);
        PlayerPotionEffects.untrackPotionEffect(player, new SpecificCustomEnchantment(EnchantmentAPI.getEnchantment("Godly Overload"), 4));
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());

    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
            event.setDamage(event.getDamage() * 1.06);
    }

}
