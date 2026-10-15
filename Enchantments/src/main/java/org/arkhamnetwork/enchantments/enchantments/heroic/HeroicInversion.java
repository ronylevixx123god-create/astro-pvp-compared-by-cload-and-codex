package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import lombok.SneakyThrows;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.enchantments.axes.Corrupt;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class HeroicInversion extends HeroicCustomEnchantment {
    public HeroicInversion() {
        super("Reality Inversion","Inversion", GeneralUtil.swords, 2);
        this.max = 4;
    }

    @SneakyThrows
    public void applyDefenseEffect(final LivingEntity entityHit, final LivingEntity attacker, int level, final EntityDamageEvent e) {
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) entityHit, SkinType.VALENTINES_2021_SWORD)) {
            level = 5;
        }

        final float procChance = (float) (0.08 * level);
        if (Math.random() <= procChance) {
            final Player playerHit = (Player) entityHit;
            final double heal = new Random().nextInt(4) + 1;
            if (heal >= 1.0) {
                if (Corrupt.isCorrupted((LivingEntity) playerHit) && Math.random() < Corrupt.getCorruptLevel((LivingEntity) playerHit) * 0.2f) {
                    playerHit.damage(heal);
                    playerHit.sendMessage(ChatColor.DARK_PURPLE + "* CORRUPTED [" + ChatColor.RED + heal + ChatColor.DARK_PURPLE + " DMG] *");
                    return;
                }
                //GeneralUtil.healPlayer((LivingEntity) playerHit, heal);
                try {
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayer(playerHit,playerHit.getLocation(), 0,1, 0,1.0f,4);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                playerHit.playSound(playerHit.getLocation(), Sound.PISTON_EXTEND, 0.8f, 2.0f);
            }
        }
    }
}