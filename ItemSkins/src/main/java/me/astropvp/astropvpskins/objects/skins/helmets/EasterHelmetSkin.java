 package me.astropvp.astropvpskins.objects.skins.helmets;
 
 import me.astropvp.astropvpskins.enums.SkinType;
 import me.astropvp.astropvpskins.objects.ItemSkin;
 import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicDodge;
 import org.bukkit.entity.Entity;
 import org.bukkit.entity.LivingEntity;
 import org.bukkit.entity.Player;
 import org.bukkit.event.entity.EntityDamageByEntityEvent;

 public class EasterHelmetSkin extends ItemSkin {

     public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
             (new HeroicDodge()).applyDefenseEffect(damaged, (LivingEntity) damager, 6, (event)); //IMPORT ENCHANTS PLUGIN
     }
 }
