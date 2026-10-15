package me.astropvp.astropvpskins.objects.skins.axes;

import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.mastery.OpeningCombo;
import org.arkhamnetwork.enchantments.mastery.SoulMaster;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CalculatorSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (Math.random() < 0.05) {
            event.setDamage(event.getDamage()*0.85);
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (!(damaged instanceof Player)) return;
        (new OpeningCombo()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 5, event);
        (new SoulMaster()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 4, event);

    }
}