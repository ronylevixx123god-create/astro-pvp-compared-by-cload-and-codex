/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.User
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.zencode.shortninja.staffplus.StaffPlus
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.earth2me.essentials.User;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

final class FireballListener
implements Listener {
    FireballListener() {
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("hellfire")) {
            e.setCancelled(true);
            //e.setDamage(0.0);
            if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player) {
                ((Player)((EntityDamageByEntityEvent)e).getDamager()).playSound(e.getEntity().getLocation(), Sound.FIRE, 1.0f, 0.7f);
                e.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata(Hellfire.hellfireEncahntmentMetadata)) {
            Location loc = e.getEntity().getLocation();
            if (e.getEntity().getPassenger() != null) {
                e.getEntity().getPassenger().remove();
            }
            if (WorldGuardUtils.isPvPDisabled(loc)) {
                return;
            }
            loc.getWorld().playSound(loc, Sound.FIRE, 1.0f, 0.85f);
            final int level = ((MetadataValue)e.getEntity().getMetadata(Hellfire.hellfireEncahntmentMetadata).get(0)).asInt();
            final boolean duelWorld = loc.getWorld().getName().equals("world_duels2");
            int radius = level * 2;
            if (duelWorld) {
                radius /= 2;
            }
            String enchantment_owner = e.getEntity().hasMetadata(Hellfire.hellfireEncahntmentOwnerMetadata) ? ((MetadataValue)e.getEntity().getMetadata(Hellfire.hellfireEncahntmentOwnerMetadata).get(0)).asString() : null;
            Player owner = Bukkit.getPlayer(enchantment_owner);
            for (Entity ent : e.getEntity().getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                Player pEnt;
                User u;
                if (ent.hasMetadata("spectator") || !(ent instanceof LivingEntity) || ent.hasMetadata("do_not_clear") || enchantment_owner != null && ent instanceof Player && (((Player)ent).getName().equals(enchantment_owner) || this.isAlly((Player)ent, owner) || !this.canHurt((Player)ent, owner)) || !(ent instanceof Player) || ent instanceof Player && ((u = Hellfire.ess3.getUser(pEnt = (Player)ent)).isGodModeEnabled() || u.isVanished() || WorldGuardUtils.isPvPDisabled(ent.getLocation()) || ent instanceof EnderDragon)) continue;
                if (ent instanceof LivingEntity) {
                    ((LivingEntity)ent).setFireTicks(((LivingEntity)ent).getFireTicks() + level * 20);
                }
                final LivingEntity le = (LivingEntity)ent;
                if (Bukkit.getPlayer((String)enchantment_owner) != null) {
                    Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

                        @Override
                        public void run() {
                            if (!le.isDead()) {
                                int dmg = 1 + level;
                                le.damage(duelWorld ? (double)dmg / 1.5 : (double)dmg);
                            }
                        }
                    }, 1L);
                }
                Location eloc = ent.getLocation().add(0.0, 1.0, 0.0);
                try {
                    de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), eloc, (float) Math.random(), (float) Math.random(), (float)Math.random(), 0.5f, 16);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            try {
                de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float) Math.random(), (float) Math.random(), (float)Math.random(), 0.5f, 8);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                de.inventivegames.particle.ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float) Math.random(), (float) Math.random(), (float)Math.random(), 0.25f, 45);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            e.getEntity().remove();
        }
    }

    private boolean isAlly(Player p1, Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }

    private boolean canHurt(Player p1, Player p2) {
        return GeneralUtil.isEnemy(p1, p2);
    }
}

