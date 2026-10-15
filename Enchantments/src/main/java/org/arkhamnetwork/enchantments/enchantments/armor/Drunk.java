/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.SpecificCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Drunk
extends CustomEnchantment {
    public Drunk() {
        super("Drunk", GeneralUtil.helmets, 5);
        this.max = 4;
        this.base = 15.0;
        this.interval = 6.0;
    }

    public void applyEquipEffect(Player player, int level) {
        if (player.hasPotionEffect(PotionEffectType.SLOW) && this.getPotionEffectLevel(player, PotionEffectType.SLOW) <= level / 2) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && this.getPotionEffectLevel(player, PotionEffectType.SLOW_DIGGING) <= level / 2) {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
        PlayerPotionEffects.trackPotionEffect(player, new SpecificCustomEnchantment(this, level), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, level / 2));
        PlayerPotionEffects.trackPotionEffect(player, new SpecificCustomEnchantment(this, level), new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, Math.max(level - 2, 0)));
        PlayerPotionEffects.trackPotionEffect(player, new SpecificCustomEnchantment(this, level), new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, level / 2));
    }

    public void applyUnequipEffect(Player player, int level) {
        PlayerPotionEffects.untrackPotionEffect(player, new SpecificCustomEnchantment(EnchantmentAPI.getEnchantment((String)"Drunk"), level));
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double blackoutChance = (double)level * 0.01;
        if (Math.random() < blackoutChance) {
            Player p = (Player)player;
            p.sendMessage((Object)ChatColor.YELLOW + "You're starting to feel very dizzy...");
        }
    }

    private int getPotionEffectLevel(Player p, PotionEffectType pet) {
        for (PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType() != pet) continue;
            return pe.getAmplifier();
        }
        return 0;
    }
}

