/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.ArmorSlot;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;

public class EagleEye
extends CustomEnchantment {
    public EagleEye() {
        super("Eagle Eye", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (Math.random() < 0.05 * (double)level + 0.2) {
            if (e.isCancelled()) {
                return;
            }
            if (!(playerHit instanceof Player)) {
                return;
            }
            if (playerHit.hasMetadata("lastArrowDamageEvent")) {
                long minDelayMilliseconds = level * 200;
                long last = ((MetadataValue)playerHit.getMetadata("lastArrowDamageEvent").get(0)).asLong();
                if (System.currentTimeMillis() - last <= minDelayMilliseconds) {
                    return;
                }
            }
            double duraDamage = 1.0;
            double dist = player.getLocation().distanceSquared(playerHit.getLocation());
            if (dist >= 100.0) {
                duraDamage += 1.0;
            }
            if (dist >= 400.0) {
                duraDamage += 1.0;
            }
            if (dist >= 1024.0) {
                duraDamage += 1.0;
            }
            Player pTarget = (Player)playerHit;
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.HELMET, -1);
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.CHESTPLATE, -1);
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.LEGGINGS, -1);
            ArmorUtil.modifyPlayerArmor(pTarget, ArmorSlot.BOOTS, -1);
            try {
                //ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getLocation(), (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.45f, 30);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            ((Player)player).playSound(playerHit.getLocation(), Sound.ANVIL_BREAK, 0.3f, 0.8f);
        }
    }
}

