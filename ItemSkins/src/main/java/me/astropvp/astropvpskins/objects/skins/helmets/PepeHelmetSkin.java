package me.astropvp.astropvpskins.objects.skins.helmets;

import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PepeHelmetSkin extends ItemSkin {

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(damaged instanceof Player) {
            event.setDamage(event.getDamage() * 1.075);
        }

    }

    public void onEquip(Player player) {
        AutoBlessListener.blessedPlayers.add(player.getUniqueId());
    }

    public void onUnequip(Player player) {
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());
    }
}

