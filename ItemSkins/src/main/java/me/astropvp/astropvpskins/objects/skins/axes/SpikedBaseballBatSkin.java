package me.astropvp.astropvpskins.objects.skins.axes;

import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpikedBaseballBatSkin extends ItemSkin {

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(!(damaged instanceof Player))
            return;
        if(((Player) damaged).getItemInHand().getType().toString().endsWith("_SWORD"))
            event.setDamage(event.getDamage()*1.1);
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(!(damager instanceof Player))
            return;
        if(((Player) damager).getItemInHand().getType().toString().endsWith("_SWORD"))
            event.setDamage(event.getDamage()*1.05);
    }
}