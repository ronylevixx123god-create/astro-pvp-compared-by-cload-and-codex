package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicSolitude;
import org.arkhamnetwork.enchantments.enchantments.swords.Silence;
import org.arkhamnetwork.enchantments.mastery.OpeningCombo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

    public class DarkstarAxe extends ItemSkin {
        public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
            if (DuelAPI.isSpectator(damager)) return;
            if (damager.hasMetadata("frozenSkrrt")) return;

            if (event.isCancelled()) return;
            if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
                return;

            if (damaged instanceof Player) {
                if (((Player) damaged).getItemInHand() != null && ((Player) damaged).getItemInHand().getType().name().endsWith("_SWORD")) {
                    event.setDamage(event.getDamage() * 1.15);
                }
                (new OpeningCombo()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 6, event);
                (new HeroicSolitude()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 4, event);


            }
        }
}
