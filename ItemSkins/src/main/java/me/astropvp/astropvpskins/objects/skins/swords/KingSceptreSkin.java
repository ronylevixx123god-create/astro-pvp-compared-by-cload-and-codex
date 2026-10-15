package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicInquisitive;
import org.arkhamnetwork.enchantments.mastery.MasteryAutoSell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class KingSceptreSkin extends ItemSkin {
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        //(new HeroicInquisitive()).applyEffect(damager, (LivingEntity)damaged, 5, event);
    }

    public void onKill(Player killer, Entity killed, EntityDeathEvent event) {
    }
}
