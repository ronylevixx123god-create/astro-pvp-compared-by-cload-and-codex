/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.IronGolem
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.MetadataValue;

final class GuardiansListener
implements Listener {
    GuardiansListener() {
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager().hasMetadata("guardianSummoner")) {
            Player p = (Player)e.getEntity();
            String summoner = ((MetadataValue)e.getDamager().getMetadata("guardianSummoner").get(0)).asString();
            Player pSummoner = Bukkit.getPlayer((String)summoner);
            if (pSummoner != null && (summoner.equalsIgnoreCase(p.getName()) || GeneralUtil.isAtleastTruce(p, pSummoner))) {
                //e.setDamage(0.0);
                return;
            }
        }
        if (e.getEntity().hasMetadata("guardianSummoner")) {
            Entity damager = e.getDamager();
            if (damager instanceof Projectile) {
                damager = (LivingEntity)((Projectile)damager).getShooter();
            }
            if (damager instanceof Player) {
                Player p = (Player)damager;
                String summoner = ((MetadataValue)e.getEntity().getMetadata("guardianSummoner").get(0)).asString();
                Player pSummoner = Bukkit.getPlayer((String)summoner);
                if (pSummoner != null && (summoner.equalsIgnoreCase(p.getName()) || GeneralUtil.isAlly(p, pSummoner))) {
                    //e.setDamage(0.0);
                    return;
                }
                if (pSummoner != null && pSummoner.hasMetadata("bloodLink")) {
                    int bloodCurseLevel = ((MetadataValue)pSummoner.getMetadata("bloodLink").get(0)).asInt();
                    if (Math.random() < (double)bloodCurseLevel * 0.05 && pSummoner.getHealth() < pSummoner.getMaxHealth() && !pSummoner.isDead()) {
                        GeneralUtil.healPlayer((LivingEntity)pSummoner, bloodCurseLevel == 5 ? 2 : 1);
                        try {
                            //ParticleEffect.HEART.sendToPlayers(Bukkit.getOnlinePlayers(), pSummoner.getEyeLocation(), 0.2f, 0.2f, 0.2f, 0.1f, 1);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        try {
                            //ParticleEffect.HEART.sendToPlayers(Bukkit.getOnlinePlayers(), e.getEntity().getLocation().add(0.0, 0.3, 0.0).getBlock().getLocation(), 0.2f, 0.2f, 0.2f, 0.1f, 1);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
                ((IronGolem)e.getEntity()).setTarget((LivingEntity)p);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("guardianSummoner")) {
            e.getDrops().clear();
            e.getEntity().remove();
        }
        if (e.getEntity().hasMetadata("targetTaskID")) {
            Bukkit.getScheduler().cancelTask(((MetadataValue)e.getEntity().getMetadata("targetTaskID").get(0)).asInt());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && e.getEntity().hasMetadata("guardianSummoner")) {
            Player p = (Player)e.getTarget();
            if (((MetadataValue)e.getEntity().getMetadata("guardianSummoner").get(0)).asString().equalsIgnoreCase(p.getName())) {
                return;
            }
            String summonerName = ((MetadataValue)e.getEntity().getMetadata("guardianSummoner").get(0)).asString();
            Player summonerPlayer = Bukkit.getPlayer((String)summonerName);
            if (summonerPlayer != null && this.isAlly(p, summonerPlayer)) {
            }
            if (p.hasMetadata("spectator")) {
            }
        }
        if (e.getTarget() != null && e.getTarget().hasMetadata("combatNPC_PlayerName")) {
            e.setTarget(null);
        }
    }

    private boolean isAlly(Player p1, Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}

