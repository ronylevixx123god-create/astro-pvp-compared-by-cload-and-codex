/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import net.minecraft.server.v1_8_R3.GenLayerBiome;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.listeners.VanillaDamageListener;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralCooldown;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DoubleStrike
        extends CustomEnchantment {
    public DoubleStrike() {
        super("Double Strike", GeneralUtil.swords, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance;
        double random = Math.random();
        if (random < (chance = 0.02 * (double)level)) {
            double dmg = VanillaDamageListener.getLastFinalDamage((Entity)playerHit);
            if (dmg == 0.0) {
                dmg = e.getDamage();
            }
            final double fdmg = dmg;
            final Player pp = (Player)e.getDamager();
            Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

                @Override
                public void run() {
                    if (!playerHit.isDead() && playerHit.getHealth() > 0.0) {
                        if (GeneralCooldown.isOnGeneralCooldown(pp)) return;
                        playerHit.setMetadata("doubleStriked", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
                        playerHit.damage(fdmg/2.5, (Entity)player);
                        pp.sendMessage("§6§l** DOUBLE STRIKE **");
                        if (playerHit instanceof Player) {
                            DamageIndicatorUtils.sendDamageIndicator((Player) playerHit, CC.translate("&6&l* DOUBLE STRIKE *"), "DOUBLE_STRIKE");
                        }
                        GeneralCooldown.enterCooldown(pp);
                    }
                }
            }, 2L);
            try {
                de.inventivegames.particle.ParticleEffect.RED_DUST.sendToPlayers(Bukkit.getOnlinePlayers(),playerHit.getLocation(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.5F, 20);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

