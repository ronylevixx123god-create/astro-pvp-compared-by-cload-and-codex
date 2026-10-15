package me.astropvp.astropvpskins.objects.skins.boots;

import com.rit.sucy.EnchantmentAPI;
import lombok.Getter;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BarrysBoltsSkin extends ItemSkin {
    private List<Player> currentFlashPlayers = new ArrayList<>();

    private List<Player> currentSilencedPlayers = new ArrayList<>();

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player))
            return;
        if(!this.currentFlashPlayers.contains(damaged) && Math.random() < 0.1) {
            this.currentFlashPlayers.add(damaged);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> this.currentFlashPlayers.remove(damaged), 100L);
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player))
            return;
        if(this.currentFlashPlayers.contains(damager) && !this.currentSilencedPlayers.contains(damaged)) {
            this.currentSilencedPlayers.add((Player) damaged);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> this.currentSilencedPlayers.remove(damaged), 200L);
        }
    }

    public void onEquip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
        AutoBlessListener.blessedPlayers.add(player.getUniqueId());

    }

    public void onUnequip(Player player) {
        EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());

    }


}
