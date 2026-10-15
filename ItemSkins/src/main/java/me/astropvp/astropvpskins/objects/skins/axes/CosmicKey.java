package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.utils.DivineUtils;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.swords.DivineImmolation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CosmicKey extends ItemSkin {
    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (damaged instanceof Player) {
            if (!damager.hasMetadata("frozenSkrrt") && !damaged.hasMetadata("frozenSkrrt")) {
                DivineUtils.divinePlayer((Player) damaged, 4);
            }
        }
        //code atlas 3 ability
        if (damager.getHealth() <= 15) {
            event.setDamage(event.getDamage()*1.1);
            damager.sendMessage(CC.translate("&9&l** COSMIC KEY [&7+10% Damage] &9&l**"));
        }
        //code kronos 8 ability
    }
}


