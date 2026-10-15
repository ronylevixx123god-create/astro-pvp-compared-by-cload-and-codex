package net.astropvp.weaponbonuses.listener;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import net.astropvp.weaponbonuses.api.WeaponBonusesAPI;
import net.astropvp.weaponbonuses.bonus.BonusType;
import net.astropvp.weaponbonuses.bonus.BonusWeapon;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BonusListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (WorldGuardUtils.isPvPEnabled(event.getDamager().getLocation())) {
            if (event.getDamager() instanceof Player) {
                if (event.getEntity() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    Player damaged = (Player) event.getEntity();
                    if (WeaponBonusesAPI.hasWeaponBonuses(damager.getItemInHand())) {
                        if (WeaponBonusesAPI.hasBonus(damager.getItemInHand(), BonusType.BASE_PVP_DMG)) {
                            event.setDamage(event.getDamage() + WeaponBonusesAPI.getBonusValue(damager.getItemInHand(), BonusType.BASE_PVP_DMG) / 100.0D);
                        }

                        if (Math.random() < 0.2D) {
                            BonusWeapon bonusWeapon = new BonusWeapon(damager.getItemInHand());
                            if (bonusWeapon.getLevel() != 3) {
                                bonusWeapon.addExperience(3 + (new Random()).nextInt(3));
                                bonusWeapon.applyVisualChanges();
                            }
                        }
                    }

                    if (WeaponBonusesAPI.hasWeaponBonuses(damaged.getItemInHand()) && WeaponBonusesAPI.hasBonus(damaged.getItemInHand(), BonusType.LESS_INCOMING_DMG)) {
                        event.setDamage(event.getDamage() - WeaponBonusesAPI.getBonusValue(damager.getItemInHand(), BonusType.LESS_INCOMING_DMG) / 100.0D);
                    }

                }
            }
        }
    }

    @EventHandler
    public void onPot(PotionEffectAddEvent e) {
        if (e.getEntity() instanceof Player && !e.isCancelled()) {
            int bonusLevel = 0;
            Player player = (Player) e.getEntity();
            for (ItemStack item : player.getInventory()) {
                if (WeaponBonusesAPI.hasWeaponBonuses(item) && WeaponBonusesAPI.hasBonus(item, BonusType.EXTRA_HP_FROM_POTIONS)) {
                    if (e.getEffect().getType() == PotionEffectType.HEAL && e.getCause() == PotionEffectAddEvent.EffectCause.POTION_SPLASH) {
                        int bonusValue = (int) WeaponBonusesAPI.getBonusValue(item, BonusType.EXTRA_HP_FROM_POTIONS);
                        if (bonusValue > bonusLevel) {
                            bonusLevel = bonusValue;
                        }
                        if (bonusLevel > 0) {
                            player.setHealth(Math.max(player.getMaxHealth(), player.getHealth() + (double) bonusLevel));
                            return;
                        }
                    }
                }
            }
        }
    }
}
