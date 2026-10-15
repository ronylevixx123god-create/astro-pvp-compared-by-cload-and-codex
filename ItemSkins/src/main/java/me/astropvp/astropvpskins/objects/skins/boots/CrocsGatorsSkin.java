package me.astropvp.astropvpskins.objects.skins.boots;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CrocsGatorsSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (damager instanceof Player) {
            if (Math.random() < 0.1) {
                event.setDamage(event.getDamage() * 0.95);
            }
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (Math.random() < 0.1) {
            event.setDamage(event.getDamage() * 1.1);
        }
    }

    public void onEquip(Player player) {
        AutoBlessListener.blessedPlayers.add(player.getUniqueId());
    }

    public void onUnequip(Player player) {
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());
    }
}