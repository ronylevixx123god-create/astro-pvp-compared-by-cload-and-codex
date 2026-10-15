/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frozen
extends CustomEnchantment {
    public Frozen() {
        super("Frozen", GeneralUtil.armor, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 3.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        int amp;
        double random = Math.random();
        double chance = 0.0005 * (double)level;
        int time = (int)(Math.random() * (double)level) + 1 * level;
        int n = amp = level > 2 ? 1 : 0;
        if (random < chance && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && attacker != null) {
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.VALENTINES_2021_HELMET)) {
                attacker.sendMessage(CC.translate("&d&l* NEON EMOTIONS &7Blocked Slow Effect &d&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.JULY_2021_BOOTS)) {
                attacker.sendMessage(CC.translate("&c&l** BARRY's BOLTS [&7Blocked Frozen&c&l] **"));
                return;
            }

            if (((Player)attacker).getInventory().getBoots() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.HALLOWEEN_2021_BOOTS))
                return;

            if (((Player)attacker).getInventory().getChestplate() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.BACK_TO_SCHOOL_2021_AMULET))
                return;

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.CHRISTMAS_BOOTS_SLIPPERS)) {
                attacker.sendMessage(CC.translate("&c&l* SNOWFLAKE SLIPPERS &7Blocked Slow Effect &c&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.EASTER_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.EASTER_2022_MUTATED_BOOTS)) {
                return;
            }


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.SPRING_BREAK_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.SPRING_BREAK_2022_MUTATED_BOOTS)) {
                SinkOrSwipFlippers.addSinkStack((Player) attacker);
                attacker.sendMessage(CC.translate("&a&l* BLOCKED FREEZE &7[+1 Stack] &a&l*"));
                return;
            }
            //attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, amp));
            Location l = player.getLocation();
            DamageIndicatorUtils.sendDamageIndicator((Player) attacker, CC.AquaB + "* FROZEN " + CC.Gray + "[" + time + "s]" + CC.AquaB + " *" + CC.AquaB +  "*", "FROZEN");
            WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)57, (int)2001);
        }
    }
}

