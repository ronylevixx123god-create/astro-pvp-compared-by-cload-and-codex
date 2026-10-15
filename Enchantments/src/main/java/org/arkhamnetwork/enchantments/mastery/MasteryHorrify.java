package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class MasteryHorrify extends MasteryCustomEnchantment {
    public MasteryHorrify() {
        super("Horrify", GeneralUtil.armor, 1);
        this.max = 4;
        this.base = 15.0D;
        this.interval = 15.0D;
        this.armorSet = "GHOST";
        Bukkit.getPluginManager().registerEvents((Listener) new HorrifiedListener(this), (Plugin) Enchantments.getInstance());
    }

    public void applyDefenseEffect(LivingEntity damaged, LivingEntity attacker, int enchantLevel, EntityDamageEvent event) {
        /*if (!(event instanceof EntityDamageByEntityEvent) || !(attacker instanceof Player))
            return;
        if ((enchantLevel * 2) * 0.01D >= Math.random()) {
            Player player = (Player) damaged;
            long duration = (2 + enchantLevel / 2);
            List<Player> affected = Lists.newArrayList();
            for (Entity near : player.getNearbyEntities(32.0D, 32.0D, 32.0D)) {
                if (near instanceof Player && !GeneralUtil.isAtleastTruce((Player) near, player) &&
                        !WorldGuardUtils.isPvPDisabled(near.getLocation()) &&
                        !VanishUtils.isVanished((Player) near) &&
                        !EssentialsUtils.isInGodMode((Player) near) &&
                        !near.hasMetadata("spectator") && ((Player) near)
                        .getGameMode() != GameMode.CREATIVE) {
                    Player nearby = (Player) near;
                    if (nearby.hasMetadata("immune_freeze")) {
                        nearby.sendMessage(CC.DarkGrayB + "* DRAGON SLAYER [" + ChatColor.GRAY + "Horrify blocked!" + CC.DarkGrayB + "] *");
                        return;
                    }

                    if (!(new CustomEnchantProcOnEntityEvent((LivingEntity) player, (LivingEntity) nearby, (Event) event, (CustomEnchantment) this, enchantLevel)).callEvent())
                        continue;
                    nearby.sendMessage(CC.RedB + "* HORRIFIED " + CC.Red + "[" + CC.RedB + NumberUtils.formatSeconds(duration) + "s" + CC.Red + "] " + CC.RedB + "*");
                    nearby.playSound(nearby.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.4F);
                    affected.add(nearby);
                    Vector from = nearby.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector());
                    if (from.length() != 0.0D)
                        from.normalize();
                    nearby.setWalkSpeed(0.001F);
                    nearby.setMetadata("horrified", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), Long.valueOf(System.currentTimeMillis() + duration * 1000L)));
                    nearby.setMetadata("noFallDamageHorrify", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), Long.valueOf(System.currentTimeMillis() + 10000L)));
                    nearby.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int) (20L * duration), 128), true);
                    Location newLocation = nearby.getLocation();
                    newLocation.setDirection(from);
                    nearby.teleport(newLocation, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                }
            }
            if (affected.size() > 0) {
                player.sendMessage(CC.GreenB + "* HORRIFIED " + affected.size() + " player(s) [" + CC.Gray + NumberUtils.formatSeconds(duration) + "s" + CC.GreenB + "] *");
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Enchantments.getInstance(), () -> {
                    for (Player pl : affected)
                        pl.setWalkSpeed(0.2F);
                }, duration * 20L);
            }
        }*/
    }

    class HorrifiedListener extends CustomEnchantListener<MasteryHorrify> {
        private HashSet<UUID> pendingHighestToUncancel = new HashSet<>();

        public HorrifiedListener(MasteryHorrify enchant) {
            super(enchant);
        }

        private boolean isHoriffied(Player player) {
            List<MetadataValue> val = player.getMetadata("horrified");
            if (val == null || val.isEmpty())
                return false;
            return (((MetadataValue) val.get(0)).asLong() > System.currentTimeMillis());
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onWorldChange(PlayerChangedWorldEvent event) {
            if (isHoriffied(event.getPlayer())) {
                event.getPlayer().removeMetadata("horrified", (Plugin) Enchantments.getInstance());
                event.getPlayer().removeMetadata("noFallDamageHorrify", (Plugin) Enchantments.getInstance());
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityByAlly(EntityDamageEvent e) {
            if (e instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    if (!isHoriffied(damager))
                        return;
                    Player entity = (Player) event.getEntity();
                    Faction fac = FactionUtils.getFaction(damager, false);
                    Faction faction = FactionUtils.getFaction(entity, false);
                    if (fac != null && faction != null && WorldGuardUtils.isPvPEnabled(entity.getLocation())) {
                        Relation rel = fac.getRelationTo((RelationParticipator) faction);
                        if (rel != Relation.ENEMY) {
                            this.pendingHighestToUncancel.add(damager.getUniqueId());
                        }
                    }
                }
            } else if (e.getCause() == EntityDamageEvent.DamageCause.FALL &&
                    e.getEntity().hasMetadata("noFallDamageHorrify")) {
                long time = ((MetadataValue) e.getEntity().getMetadata("noFallDamageHorrify").get(0)).asLong();
                if (time > System.currentTimeMillis()) {
                }
                e.getEntity().removeMetadata("noFallDamageHorrify", (Plugin) Enchantments.getInstance());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlayerDamagePlayer(EntityDamageEvent event) {
            if (event instanceof EntityDamageByEntityEvent && event.isCancelled()) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if (e.getDamager() instanceof Player) {
                    UUID uuid = e.getDamager().getUniqueId();
                    if (this.pendingHighestToUncancel.remove(uuid)) {
                        if (event.getDamage() <= 0.0D)
                            return;
                        if (!WorldGuardUtils.isPvPEnabled(e.getEntity().getLocation()) ||
                                !WorldGuardUtils.isPvPEnabled(e.getDamager().getLocation()))
                            return;
                    }
                }
            }
        }
    }
}
