/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class HeroicDodge
        extends HeroicCustomEnchantment {
    public HeroicDodge() {
        super("Ethereal Dodge", "Dodge", GeneralUtil.boots, 2);
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
            if (player.getWorld().getName().toLowerCase().contains("dungeon_planet_nul")) {
                return;
            }
            if (attacker instanceof Player && (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.VALENTINES_2022_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.VALENTINES_2022_MUTATED_HELMET)) && Math.random() < 0.15)
                return;

        double chance = 0.03 * (double)level;
        final boolean sneaking = ((Player) player).isSneaking();
            if (sneaking) {
                chance += 0.05;
            }
            if (Math.random() < chance) {
                if (Math.random() < 0.03 && !player.hasMetadata("carver_damage")) {
                    player.setMetadata("carver_damage", new FixedMetadataValue(Enchantments.getInstance(), true));
                    player.sendMessage(CC.translate("&e&l* CARVER DODGE BUFF [&7+50% DMG&e&l] *"));
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> {
                        player.removeMetadata("carver_damage", Enchantments.getInstance());
                    }, 5 * 20);
                }

                e.setDamage(0);
                //e.setCancelled(true);
                player.sendMessage(ChatColor.YELLOW + "*DODGE*");
                player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.75f);
            }
    }
}

