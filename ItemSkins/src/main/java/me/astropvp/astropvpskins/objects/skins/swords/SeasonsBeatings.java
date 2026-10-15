package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.axes.Hex;
import org.arkhamnetwork.enchantments.mastery.MasteryMarkOfTheBeast;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SeasonsBeatings extends ItemSkin {
    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (!(damaged instanceof Player)) return;
        if (((Player) damaged).getItemInHand() != null &&  EnchantmentAPI.itemHasEnchantment(((Player) damaged).getItemInHand(), "Hero Killer")) {
            event.setDamage(event.getDamage() * 1.07);
        }

        if (KillTrackerAPI.hasGodMode((Player) damaged)) {
            event.setDamage(event.getDamage() * 1.1);
        }



    }



}


