/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.IronGolem
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.enchantments.armor.Guardians;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;

public class Hijack
extends CustomEnchantment {
    public Hijack() {
        super("Hijack", new Material[]{Material.BOW}, 2);
        this.max = 4;
        this.base = 10.0;
        this.interval = 8.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double chance;
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }

        double random = Math.random();
        if (random < (chance = (double)enchantLevel * 0.08)) {
            Player pUser = (Player)user;
            if (target.getType() == EntityType.IRON_GOLEM && target.hasMetadata("guardianSummoner")) {
                String oldSummoner = ((MetadataValue)target.getMetadata("guardianSummoner").get(0)).asString();
                target.remove();
                IronGolem ig = Guardians.spawnGuardianEntity(target.getLocation().add(0.0, 2.0, 0.0), pUser, enchantLevel * 2);
                try {
                    de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), ig.getLocation().add(0.0, 1.0, 0.0), 1.2f, 1.2f, 1.2f, 0.1f, 60);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                for (Entity ent : ig.getNearbyEntities(24.0, 24.0, 24.0)) {
                    if (!(ent instanceof Player)) continue;
                    ((Player)ent).sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "*** HIJACK (" + (Object)ChatColor.GRAY + (Object)ChatColor.STRIKETHROUGH + oldSummoner + (Object)ChatColor.DARK_PURPLE + (Object)ChatColor.BOLD + " -> " + (Object)ChatColor.WHITE + pUser.getName() + (Object)ChatColor.DARK_PURPLE + (Object)ChatColor.BOLD + ") ***");
                    ((Player)ent).playSound(ent.getLocation(), Sound.IRONGOLEM_DEATH, 0.8f, 1.2f);
                }
            }
        }
    }
}

