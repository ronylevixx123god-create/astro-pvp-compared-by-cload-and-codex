package me.astropvp.astropvpskins.objects.skins.helmets;

import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AntiCoronaMaskSkin extends ItemSkin {


    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(Math.random() < 0.02)
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 500));
    }
}