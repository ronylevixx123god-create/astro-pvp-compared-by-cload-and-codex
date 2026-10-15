/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.enchantments.libs.AbstractPotionEffect
 *  org.arkhamnetwork.enchantments.libs.PlayerPotionEffects
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.ifamasssxd.cosmicmasks.struct;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import org.arkhamnetwork.enchantments.libs.AbstractPotionEffect;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
public abstract class CosmicMask {
    public boolean modifyStoredEffect(Player player, PotionEffectType type, boolean subtract) {
        AtomicBoolean retr = new AtomicBoolean();
        PlayerPotionEffects effects = PlayerPotionEffects.getTrackedPotionEffects((Player)player);
        if (effects != null) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            effects.getEffects().forEach((enchantment, effect) -> {
                for (AbstractPotionEffect pEffect : effect) {
                    if (pEffect.getEffectType().getId() != type.getId()) continue;
                    Bukkit.getLogger().info("[" + this.getClass().getSimpleName() + "] Found equipped health boost: " + pEffect.getPotionLevel());
                    if (subtract) {
                        pEffect.resetOriginalLevel();
                        player.removePotionEffect(type);
                    } else if (pEffect.getPotionLevel() == pEffect.getOriginalLevel()) {
                        pEffect.setPotionLevel(pEffect.getPotionLevel() + 1);
                        player.removePotionEffect(type);
                    }
                    atomicBoolean.set(true);
                }
            });
        }
        return retr.get();
    }

    public boolean hasEffect(Player player, PotionEffectType type, int level, boolean greater) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().getId() != type.getId() || (greater ? effect.getAmplifier() < level : effect.getAmplifier() != level)) continue;
            return true;
        }
        return false;
    }

    public abstract void onEquip(Player var1);

    public abstract void onUnequip(Player var1);

    public abstract void onUpdate(Player var1);
}

*/

public abstract class CosmicMask {
    public boolean modifyStoredEffect(Player player, PotionEffectType type, boolean subtract) {
        AtomicBoolean retr = new AtomicBoolean();
        PlayerPotionEffects effects = PlayerPotionEffects.getTrackedPotionEffects(player);
        if (effects != null)
            effects.getEffects().forEach((enchantment, effect) -> {
                Iterator<AbstractPotionEffect> iterator = effect.iterator();
                while (iterator.hasNext()) {
                    AbstractPotionEffect pEffect = iterator.next();
                    if (pEffect.getEffectType().getId() == type.getId()) {
                        Bukkit.getLogger().info("[" + getClass().getSimpleName() + "] Found equipped health boost: " + pEffect.getPotionLevel());
                        if (subtract) {
                            pEffect.resetOriginalLevel();
                            player.removePotionEffect(type);
                        } else if (pEffect.getPotionLevel() == pEffect.getOriginalLevel()) {
                            pEffect.setPotionLevel(pEffect.getPotionLevel() + 1);
                            player.removePotionEffect(type);
                        }
                        retr.set(true);
                    }
                }
            });
        return retr.get();
    }

    public boolean hasEffect(Player player, PotionEffectType type, int level, boolean greater) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().getId() != type.getId() || (
                    greater ? (
                            effect.getAmplifier() < level) : (

                            effect.getAmplifier() != level)))
                continue;
            return true;
        }
        return false;
    }

    public abstract void onEquip(Player paramPlayer);

    public abstract void onUnequip(Player paramPlayer);

    public abstract void onUpdate(Player paramPlayer);
}

