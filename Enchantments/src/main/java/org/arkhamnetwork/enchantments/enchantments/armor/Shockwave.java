/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class Shockwave
extends CustomEnchantment {
    public Shockwave() {
        super("Shockwave", GeneralUtil.chestplates, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (GeneralUtil.isKOTHWorld(player.getWorld())) {
            return;
        }
        if (player.hasMetadata("neutralized"))
            return;
        if (player.hasMetadata("noDefenseProcs"))
            return;
        double chance = (double)level * 0.01;
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && Math.random() < chance) {
            FPlayer fp = (FPlayer)FPlayers.getInstance().getByPlayer((Player) player);
            boolean KOTH = player.getWorld().getName().equals("world_koth");
            for (Entity ent : player.getNearbyEntities((double)level, (double)level, (double)level)) {
                if (ent.hasMetadata("no_knockback")) continue;
                FPlayer fpEnt;
                if (!(ent instanceof LivingEntity) || ent instanceof Player && (Enchantments.getEssentials().getUser((Player)ent).isVanished() || FPlayers.getInstance().getByPlayer((Player) ent).getRelationTo((RelationParticipator)fp).isAtLeast(Relation.ALLY)) || WorldGuardUtils.isPvPDisabled(ent.getLocation())) continue;
                if (KOTH) {
                    GeneralUtil.pushAwayEntity(player, ent, (double)1.35f);
                } else {
                    GeneralUtil.pushAwayEntity(player, ent, (double)1.7f);
                }
                Location l = ent.getLocation();
                try {
                    //ParticleEffect.ENCHANTMENT_TABLE.sendToPlayers(Bukkit.getOnlinePlayers(), l, (float)Math.random() * 2.0f, (float)Math.random() * 2.0f, (float)Math.random() * 2.0f, 0.73f, 100);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 0.1f);
            try {
                //ParticleEffect.ENCHANTMENT_TABLE.sendToPlayers(Bukkit.getOnlinePlayers(), player.getEyeLocation(), (float)Math.random() * 3.0f, (float)Math.random() * 3.0f, (float)Math.random() * 3.0f, 0.73f, 100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    private void pushAwayEntity(LivingEntity center, Entity entity, double speed) {
        Vector unitVector = entity.getLocation().toVector().subtract(center.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}

