package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.utils.DivineUtils;
import org.arkhamnetwork.enchantments.enchantments.swords.DivineImmolation;
import org.arkhamnetwork.enchantments.mastery.MasteryBlackout;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class StormBreaker extends ItemSkin {
    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;

        if (damaged instanceof Player) {
            if (!damager.hasMetadata("frozenSkrrt") && !damaged.hasMetadata("frozenSkrrt")) {

                DivineUtils.divinePlayer((Player) damaged, 1);
            }
        }

        new MasteryBlackout().applyEffect(damager, (LivingEntity) damaged, 4, event);
        //code kronos 8 ability
    }
}


