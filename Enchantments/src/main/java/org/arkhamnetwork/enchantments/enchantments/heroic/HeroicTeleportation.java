/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.MaskUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.arkhamnetwork.Arkkit.net.astropvp.armorsets.utils.WorldGuardUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;

public class HeroicTeleportation
extends HeroicCustomEnchantment {
    public HeroicTeleportation() {
        super("Bidirectional Teleportation", "Teleportation", false);
        this.max = 5;
        this.setNaturalMaterials(new Material[]{Material.BOW});
        Bukkit.getPluginManager().registerEvents((Listener)new HeroicTeleportListener(this), (Plugin)Enchantments.getInstance());
    }

    public void applyProjectileEffect(LivingEntity user, int enchantLevel, ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow && !event.isCancelled()) {
            event.getEntity().setMetadata("bi_teleport_arrow", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)enchantLevel));
        }
    }

    public void grapplePlayer(Player target, Player damager, int level) {
        if (!target.getWorld().equals((Object)damager.getWorld())) {
            Bukkit.getLogger().info("unable to grapple to " + target.getName() + " because " + damager.getName() + " was in another world!");
            return;
        }
        Vector velocity = target.getLocation().toVector().subtract(damager.getLocation().toVector());
        if (velocity.length() != 0.0) {
            velocity.normalize();
        }
        double distance = target.getLocation().distanceSquared(damager.getLocation());
        double maxDistance = level * 10;
        if (distance > (maxDistance *= maxDistance)) {
            if (level == this.getMaxLevel()) {
                damager.sendMessage(CC.RedB + "(!) " + CC.Red + "Your Teleportation target is too far away to pull with Bidirectional Teleportation!");
            } else {
                damager.sendMessage(CC.RedB + "(!) " + CC.Red + "Your Teleportation target is too far away to pull with this Bidirectional Teleportation level!");
            }
            return;
        }
        double toMultiply = Math.min(Math.max(1.0, distance / 50.0), 6.0 + (double)level * 0.5);
        double y = velocity.getY();
        velocity.multiply(-toMultiply);
        velocity.setY(y * (-toMultiply / 1.75));
        if (velocity.getY() < 0.01 && velocity.getY() > -0.01) {
            velocity.multiply(new Vector(1.0, 7.5, 1.0));
        }
        target.setVelocity(velocity);
        target.playSound(target.getLocation(), Sound.ZOMBIE_METAL, 10.0f, 1.1f);
        target.playSound(target.getLocation(), Sound.WITHER_SHOOT, 10.0f, 1.5f);
        target.sendMessage(CC.RedB + "* BIDIRECTIONAL TELEPORT [towards: " + CC.Gray + damager.getName() + CC.RedB + "] *");
        damager.playSound(target.getLocation(), Sound.ZOMBIE_METAL, 10.0f, 1.1f);
        damager.playSound(target.getLocation(), Sound.WITHER_SHOOT, 10.0f, 1.5f);
        damager.sendMessage(CC.LightPurpleB + "* BIDIRECTIONAL TELEPORT [pulling: " + CC.Gray + target.getName() + CC.LightPurpleB + "] *");
    }

    public void trapTarget(Player player, Player damager, int level) {
        if (player.getWalkSpeed() < 0.2f) {
            return;
        }
        player.setWalkSpeed(0.0f);
        int duration = (int)(1.0 + (double)level * 0.125);
        player.sendMessage(CC.RedB + "* BIDIRECTIONAL TRAPPED [by: " + CC.Gray + damager.getName() + " (" + NumberUtils.formatSeconds((double)duration) + "s)]" + CC.RedB + " *");
        Enchantments.getInstance().getServer().getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> player.setWalkSpeed(0.2f), (long)(duration * 20));
    }

    class HeroicTeleportListener
    extends CustomEnchantListener<HeroicTeleportation> {
        public HeroicTeleportListener(HeroicTeleportation enchant) {
            super(enchant);
        }

        @EventHandler
        public void onPlayerDamageByArrow(EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player && ((Arrow)event.getDamager()).getShooter() instanceof Player) {
                Player playerHit;
                List meta = event.getDamager().getMetadata("bi_teleport_arrow");
                if (meta == null || meta.isEmpty()) {
                    return;
                }
                int level = ((MetadataValue)meta.get(0)).asInt();
                double chanceToProc = 0.066 * (double)level;
                Player shooter = (Player)((Arrow)event.getDamager()).getShooter();
                if (!shooter.canSee(playerHit = (Player)event.getEntity()) || WorldGuardUtils.isPvPDisabled((Location)playerHit.getLocation()) || WorldGuardUtils.isPvPDisabled((Location)shooter.getLocation())) {
                    return;
                }
                Relation relation = FactionUtils.getRelationTo(FactionUtils.getFPlayer(shooter), FactionUtils.getFPlayer((Player)((Player)event.getEntity())));
                if (relation == Relation.ENEMY || relation == Relation.NEUTRAL) {
                    if (MaskUtils.hasMaskEquipped((Player)playerHit, (String)"GLITCH")) {
                        return;
                    }
                    if (Math.random() <= chanceToProc) {
                        try {
                            ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getEyeLocation(), (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.5f, 35);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        try {
                            ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), playerHit.getEyeLocation(), (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.1f, 10);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        ((HeroicTeleportation)this.customEnchant).grapplePlayer(playerHit, shooter, level);
                        event.setCancelled(true);
                        return;
                    }
                    ((HeroicTeleportation)this.customEnchant).trapTarget(playerHit, shooter, level);
                    return;
                }
                if (relation == Relation.ALLY || relation == Relation.MEMBER) {
                    if (playerHit.getWorld().getName().equals("cosmic-station-1")) {
                        return;
                    }
                    event.setCancelled(true);
                    if (event.getDamager() instanceof Projectile) {
                        event.getDamager().remove();
                    }
                    Location tp = playerHit.getLocation();
                    tp.setPitch(shooter.getLocation().getPitch());
                    tp.setYaw(shooter.getLocation().getYaw());
                    if (!shooter.getWorld().getName().equals(playerHit.getWorld().getName()) || tp.distanceSquared(shooter.getLocation()) > Math.pow(30.0, 2.0)) {
                        shooter.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "Your ally is too far away to teleport to with this level of Bidirectional Teleportation.");
                        return;
                    }
                    if ((playerHit.getWorld().getEnvironment() == World.Environment.THE_END || playerHit.getWorld().getName().equals("world_koth")) && !WorldGuardUtils.isPvPDisabled((Location)shooter.getLocation()) && WorldGuardUtils.isPvPDisabled((Location)tp)) {
                        shooter.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot teleport from PvP-enabled to PvP-disabled with Bidirectional Teleportation in The End or KOTH.");
                        return;
                    }
                    shooter.setMetadata("ignore_combat_tag", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)tp));
                    shooter.teleport(tp, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                    shooter.removeMetadata("ignore_combat_tag", (Plugin)Enchantments.getInstance());
                    shooter.playSound(shooter.getLocation(), Sound.ORB_PICKUP, 0.75f, 0.341f);
                    Location loc = playerHit.getLocation().add(0.0, 0.5, 0.0);
                    try {
                        ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.5f, 35);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    try {
                        ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), loc, (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.1f, 10);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }
            }
        }
    }
}

