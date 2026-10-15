/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralCooldown;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Paralyze
        extends CustomEnchantment {
    public Paralyze() {
        super("Paralyze", GeneralUtil.swords, 2);
        this.max = 4;
        this.base = 25.0;
        this.interval = 0.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (!(playerHit instanceof Player)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        final double random = Math.random();
        final double chance = (level == 3) ? 0.02 : (0.01 * level);
        if (random < chance) {

            if (GeneralCooldown.isOnGeneralCooldown((Player) e.getDamager())) return;


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2021_HELMET)) {
                playerHit.sendMessage(CC.translate("&d&l* NEON EMOTIONS &7Blocked Slow Effect &d&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_BOOTS_SLIPPERS)) {
                playerHit.sendMessage(CC.translate("&c&l* SNOWFLAKE FLIPPERS &7Blocked Slow Effect &c&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.JULY_2021_BOOTS)) {
                playerHit.sendMessage(CC.translate("&c&l** BARRY's BOLTS [&7Blocked Paralyze&c&l] **"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_MUTATED_BOOTS)) {
                return;
            }



            playerHit.getLocation().getWorld().strikeLightningEffect(player.getLocation());
            DamageIndicatorUtils.sendDamageIndicator((Player) playerHit, CC.translate("&b&l* PARALYZE *"), "PARALYZE");
            playerHit.damage((double)(level));
            GeneralCooldown.enterCooldown((Player) player);

        }
    }
}

