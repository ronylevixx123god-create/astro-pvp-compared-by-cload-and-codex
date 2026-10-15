package me.astropvp.astropvpskins.objects.skins.belts;

import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class JellyRollSkin extends ItemSkin {
    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(damager.getItemInHand().getType().name().endsWith("_AXE"))
            event.setDamage(event.getDamage() * 1.05);
    }
}
