/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.DurabilityUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Healing
extends CustomEnchantment {
    public Healing() {
        super("Healing", new Material[]{Material.BOW}, 3);
        this.max = 4;
        this.base = 20.0;
        this.interval = 10.0;
        Bukkit.getPluginManager().registerEvents((Listener)new HealingEvents(), (Plugin)Enchantments.getInstance());
    }

    private static class HealingEvents
    implements Listener {
        private HealingEvents() {
        }

        @EventHandler(priority=EventPriority.LOWEST)
        public void onArrowHit(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Projectile && e.getDamager().hasMetadata("healing_level")) {
                Player player = null;
                Player playerHit = null;
                if (((Projectile)e.getDamager()).getShooter() instanceof Player) {
                    player = (Player)((Projectile)e.getDamager()).getShooter();
                }
                if (e.getEntity() instanceof Player) {
                    playerHit = (Player)e.getEntity();
                }
                if (player == null || playerHit == null) {
                    return;
                }
                int level = ((MetadataValue)e.getDamager().getMetadata("healing_level").get(0)).asInt();
                if (player.equals((Object)playerHit)) {
                    e.setCancelled(true);
                    if (player instanceof Player) {
                        player.playSound(player.getLocation(), Sound.CAT_HISS, 0.85f, 0.2f);
                    }
                    return;
                }
                if (!(player instanceof Player) || !(playerHit instanceof Player)) {
                    return;
                }
                FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
                Faction f = fPlayer.getFaction();
                Faction fply = FPlayers.getInstance().getByPlayer(playerHit).getFaction();
                if (f.isNormal() && fply.isNormal() && (fply.getRelationTo((RelationParticipator)f).isAlly() || fply.equals((Object)f))) {
                    double current_hp;
                    int min_heal = 1 * level;
                    int max_heal = 3 * level;
                    double heal = new Random().nextInt(max_heal - min_heal) + min_heal;
                    boolean absorption = level >= 3 ? Math.random() < 0.15 * (double)(level - 2) : false;
                    e.setCancelled(true);
                    if (e.getDamager() instanceof Projectile) {
                        e.getDamager().remove();
                    }
                    if ((current_hp = playerHit.getHealth()) + heal > playerHit.getMaxHealth()) {
                        GeneralUtil.setPlayerHealth((LivingEntity)playerHit, playerHit.getMaxHealth());
                        if (!absorption) {
                            playerHit.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * (4 + level), level - 1));
                        }
                    } else {
                        GeneralUtil.healPlayer((LivingEntity)playerHit, heal);
                    }
                    if (Math.random() < 0.25 * (double)level) {
                        DurabilityUtil.healMostDamagedArmorPeice(player, (short)1);
                    }
                    if (absorption) {
                        playerHit.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * (1 + level), level - 3));
                    }
                    if (playerHit instanceof Player) {
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.75f, 0.341f);
                    }
                    Location loc = playerHit.getLocation().add(0.0, 0.5, 0.0);
                    WorldUtil.sendWorldEffect((Block)loc.getBlock(), (int)Material.EMERALD_BLOCK.getId(), (int)2001);
                }
            }
        }

        @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
        public void onPlayerShootBow(EntityShootBowEvent e) {
            if (e.getBow() != null && EnchantmentAPI.itemHasEnchantment((ItemStack)e.getBow(), (String)"Healing")) {
                Location loc = e.getProjectile().getLocation();
                e.getProjectile().setMetadata("healing_level", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), EnchantmentAPI.getEnchantments((ItemStack)e.getBow()).get((Object)EnchantmentAPI.getEnchantment((String)"Healing"))));
                WorldUtil.sendWorldEffect((Block)loc.getBlock(), (int)Material.EMERALD_BLOCK.getId(), (int)2001);
            }
        }
    }
}

