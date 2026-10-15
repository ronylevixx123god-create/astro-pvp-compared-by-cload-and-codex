/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.enchantments.libs.AbstractPotionEffect
 *  org.arkhamnetwork.enchantments.libs.PlayerPotionEffects
 *  org.arkhamnetwork.enchantments.mastery.MasteryMortalCoil
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.ifamasssxd.cosmicmasks.struct;

import org.arkhamnetwork.enchantments.libs.AbstractPotionEffect;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.mastery.MasteryMortalCoil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class SantaMask extends CosmicMask {
    public void onEquip(Player player) {
        if (player.getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }
        if (!modifyStoredEffect(player, PotionEffectType.HEALTH_BOOST, false))

            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, 0), true);
    }

    public void onUnequip(Player player) {
        if (modifyStoredEffect(player, PotionEffectType.HEALTH_BOOST, true)) {
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            Bukkit.getLogger().info("[Santa Mask] Unequipping health boost from santa mask for " + player.getName());
        }
    }

    public int getMaxActivePotionLevel(Player player) {
        PlayerPotionEffects effects = PlayerPotionEffects.getTrackedPotionEffects(player);
        int maxLevel = -1;
        if (effects != null)
            for (Collection<AbstractPotionEffect> e : effects.getEffects().values()) {
                for (AbstractPotionEffect eff : e) {
                    if (eff.getEffectType().getId() == PotionEffectType.HEALTH_BOOST.getId() &&
                            eff.getPotionLevel() > maxLevel) {
                        maxLevel = eff.getPotionLevel();
                        Bukkit.getLogger().info(
                                "Removing santa bonus due to health boost being detected: " + eff.getEffectType());
                    }
                }
            }
        return maxLevel;
    }

    public void onUpdate(Player player) {
        try {
            if (MasteryMortalCoil.isAffected(player))
                return;
        } catch (Exception exception) {}
        if (!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
            int storedMmax = getMaxActivePotionLevel(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, (storedMmax == -1) ? 0 : 0),
                    true);
            return;
        }
        PlayerPotionEffects effects = PlayerPotionEffects.getTrackedPotionEffects(player);
        boolean detectedNew = false;
        if (effects != null)
            for (Collection<AbstractPotionEffect> e : effects.getEffects().values()) {
                for (AbstractPotionEffect eff : e) {
                    if (eff.getEffectType().getId() == PotionEffectType.HEALTH_BOOST.getId() &&
                            eff.getPotionLevel() == eff.getOriginalLevel()) {
                        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                        eff.setPotionLevel(eff.getPotionLevel() + 1);
                        Bukkit.getLogger().info(
                                "Removing santa bonus due to health boost being detected: " + eff.getEffectType());
                        detectedNew = true;
                    }
                }
            }
        if ((!detectedNew && hasEffect(player, PotionEffectType.HEALTH_BOOST, 0, false)) || (
                !detectedNew && !player.hasPotionEffect(PotionEffectType.HEALTH_BOOST))) {
            int stored = getMaxActivePotionLevel(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, (stored == -1) ? 0 : 0), true);
        }
    }
}

