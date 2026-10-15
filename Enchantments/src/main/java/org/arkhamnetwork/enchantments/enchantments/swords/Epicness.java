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
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Epicness
extends CustomEnchantment {
    public Epicness() {
        super("Epicness", GeneralUtil.swords, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance;
        double random = Math.random();
        if (random < (chance = 0.5)) {
            if (level == 1) {
                this.playSoundToEnemies(Sound.CHICKEN_HURT, 1.0f, 0.85f, playerHit);
                try {
                    ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getEyeLocation(), (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            } else if (level == 2) {
                this.playSoundToEnemies(Sound.MAGMACUBE_WALK2, 1.2f, 0.8f, playerHit);
                try {
                    ParticleEffect.SLIME.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getEyeLocation(), (float)Math.random(), (float)Math.random(), (float)Math.random(), 1.0f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            } else if (level == 3) {
                this.playSoundToEnemies(Sound.GHAST_SCREAM, 1.0f, 1.25f, playerHit);
                try {
                    ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getEyeLocation(), (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.95f, 15);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        }
    }

    private void playSoundToEnemies(Sound s, float volume, float pitch, LivingEntity le) {
        if (!(le instanceof Player)) {
            return;
        }
        Player p = (Player)le;
        FPlayer fp = (FPlayer)FPlayers.getInstance().getByPlayer(p);
        for (Entity ent : p.getNearbyEntities(16.0, 16.0, 16.0)) {
            if (!(ent instanceof Player)) continue;
            Player pEnt = (Player)ent;
            FPlayer fpEnt = (FPlayer)FPlayers.getInstance().getByPlayer(pEnt);
            if (fp.getFaction().getRelationTo((RelationParticipator)fpEnt.getFaction()).isAtLeast(Relation.TRUCE)) continue;
            pEnt.playSound(pEnt.getLocation(), s, volume, pitch);
        }
    }
}

