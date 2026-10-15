/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.IronGolem
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class Ragdoll
        extends CustomEnchantment {
    public Ragdoll() {
        super("Ragdoll", GeneralUtil.armor, 3);
        this.max = 4;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player && attacker instanceof LivingEntity) {
            if (attacker instanceof IronGolem) {
                return;
            }


            Player p = (Player) player;
            if (Math.random() < (double) level * 0.125) {
                if (attacker.hasMetadata("no_knockback")) return;
                if (player.getWorld().getEnvironment() == World.Environment.THE_END || GeneralUtil.isKOTHWorld(player.getWorld())) {
                    return;
                }

                if ((AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.CHRISTMAS_2021_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.CHRISTMAS_2021_MUTATED_HELMET)) && Math.random() < 0.75) {
                    return;
                }

                if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.BACK_TO_SCHOOL_2022_BACKPACK)) {
                    return;
                }

                if (player.hasMetadata("noRagdoll")) return;

                if (UndertowHelmet.undertowField.contains(player)) {
                    return;
                }
                GeneralUtil.pushAwayEntity(attacker, (Entity) p, 1.5 + 0.5 * (double) level);
            }
        }
    }

    private void pushAwayEntity(LivingEntity center, Entity entity, double speed) {
        Vector unitVector = entity.getLocation().toVector().subtract(center.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}

