package me.astropvp.astropvpskins.objects.skins.amulets;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MomentoMoriSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {

    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (damaged instanceof Player && !ItemUtils.isAirOrNull(((Player) damaged).getItemInHand())) {
            if (((Player) damaged).getItemInHand().getType().name().contains("DIAMOND_")) {
                event.setDamage(event.getDamage() * 1.05);
            }
        }
    }
}