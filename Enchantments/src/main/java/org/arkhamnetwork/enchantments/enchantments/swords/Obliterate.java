/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Obliterate
        extends CustomEnchantment {
    public Obliterate() {
        super("Obliterate", GeneralUtil.weapons, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 6.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double random = Math.random();
        double chance = 0.1 * (double) enchantLevel;
        if (target.getWorld().getEnvironment() == World.Environment.THE_END) {
            return;
        }
        boolean inKoth = GeneralUtil.isKOTHWorld(target.getWorld());
        if (Enchantments.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getBoolean("obliterateEnabled")) {

            if (!target.hasMetadata("in_duel") && random < chance) {
                double d_enchantLevel = enchantLevel;
                try {
                    ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), target.getLocation().add(0.0, 1.0, 0.0), 1.0f, 1.0f, 1.0f, 0.1f, 3);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                if (inKoth) {
                    return;
                }

                pushAwayEntity(user, (Entity) target, 1.8 + d_enchantLevel * 0.5);
            }
        }
    }

    private void pushAwayEntity(LivingEntity center, Entity entity, double speed) {
        Vector unitVector = entity.getLocation().toVector().subtract(center.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}

