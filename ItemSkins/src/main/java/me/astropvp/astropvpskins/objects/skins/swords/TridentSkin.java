package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.enchantments.axes.Hex;
import org.arkhamnetwork.enchantments.mastery.MasteryMarkOfTheBeast;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TridentSkin extends ItemSkin {
    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (DuelAPI.isSpectator(damager)) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if(Math.random() <= 0.04) {
            damaged.sendMessage("");
            damaged.sendMessage(CC.translate("&3&l* TRIDENT OF DEEP &7[" + damager.getName() + "] &3&l*"));
            damaged.sendMessage(CC.translate("&3&l * &7+ Hex"));
            damaged.sendMessage(CC.translate("&3&l * &7+ Mark Of the Beast"));
            damaged.sendMessage(CC.translate("&7"));
            try {
                MasteryMarkOfTheBeast.applyMarkEffect((LivingEntity) damaged, damager, 6, event);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            (new Blessed()).applyEffect(damager, (LivingEntity)damager,4, event);

        }
    }
}

