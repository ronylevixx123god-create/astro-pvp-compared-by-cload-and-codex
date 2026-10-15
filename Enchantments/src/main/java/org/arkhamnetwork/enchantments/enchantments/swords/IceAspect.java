/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IceAspect
        extends CustomEnchantment {
    public IceAspect() {
        super("IceAspect", GeneralUtil.swords, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 3.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (playerHit.hasPotionEffect(PotionEffectType.SLOW)) {
            return;
        }
        double random = Math.random();
        double chance = 0.035 * (double)level;
        int time = 2 * level;
        if (random < chance && playerHit instanceof Player) {
            Location l = playerHit.getLocation();
            if (!new CustomEnchantProcOnEntityEvent(player, playerHit, (Event) e, this, level).callEvent()) {
                return;
            }
            if (playerHit.hasMetadata("immune_freeze")) {
                ((Player) playerHit).sendMessage(CC.DarkGrayB + "* DRAGON SLAYER [" + (Object) ChatColor.GRAY + "Ice Aspect blocked!" + CC.DarkGrayB + "] *");
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_MUTATED_BOOTS)) {
                return;
            }

            if (((Player) playerHit).getInventory().getBoots() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.HALLOWEEN_2021_BOOTS))
                return;

            if (((Player) playerHit).getInventory().getChestplate() != null && AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.BACK_TO_SCHOOL_2021_AMULET))
                return;

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_BOOTS_SLIPPERS)) {
                playerHit.sendMessage(CC.translate("&c&l* SNOWFLAKE SLIPPERS &7Blocked Slow Effect &c&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.JULY_2021_BOOTS)) {
                playerHit.sendMessage(CC.translate("&c&l** BARRY's BOLTS [&7Blocked Ice Aspect&c&l] **"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.SPRING_BREAK_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.SPRING_BREAK_2022_MUTATED_BOOTS)) {
                SinkOrSwipFlippers.addSinkStack((Player) playerHit);
                playerHit.sendMessage(CC.translate("&a&l* BLOCKED FREEZE &7[+1 Stack] &a&l*"));
                return;
            }

            if (MaskType.hasEquipped((Player) playerHit, "HOLY")) {
                playerHit.sendMessage(CC.translate("&6&l* HOLY MASK [&7Ice Aspect blocked!&6&l] *"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2021_HELMET)) {
                playerHit.sendMessage(CC.translate("&d&l* NEON EMOTIONS &7Blocked Slow Effect &d&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VIRUS_HELMET_HAZMAT)) {
                ((Player) playerHit).sendMessage(CC.translate("&2&L* PRISON BREAK &7(Hazmat Helmet) &2&L*"));
                ASound.ANVIL_LAND.play((Player) playerHit, 2.0f, 1.4f);
                return;
            }
            DamageIndicatorUtils.sendDamageIndicator((Player) playerHit, CC.DarkPurpleB + "* ICE ASPECT " + CC.Gray + "[" + time + "]" + CC.DarkPurpleB + "*", "ICE_ASPECT");
            //playerHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, 5));
            //playerHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, 2));
        }
    }
}

