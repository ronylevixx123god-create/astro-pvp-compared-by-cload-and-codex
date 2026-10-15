package me.astropvp.astropvpskins.objects;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ItemSkin {

    public void onEquip(Player player) {
    }

    public void onUnequip(Player player) {
    }

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {

    }

    public void onKill(Player killer, Entity killed, EntityDeathEvent event) {
    }

    public void onKilled(Player killed, Entity killer, EntityDeathEvent event) {
    }

    public void onSneak(Player player, PlayerToggleSneakEvent event) {

    }


}
