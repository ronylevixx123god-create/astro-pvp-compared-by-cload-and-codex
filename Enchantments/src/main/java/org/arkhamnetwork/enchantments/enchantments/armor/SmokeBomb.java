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
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class SmokeBomb
extends CustomEnchantment {
    public SmokeBomb() {
        super("Smoke Bomb", GeneralUtil.helmets, 3);
        this.max = 8;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {
            Player p = (Player)player;
            double dmg = e.getFinalDamage();
           if (player.getHealth() - dmg <= 0.0) {
                if (p.hasMetadata("SmokeBomb") && System.currentTimeMillis() - ((MetadataValue)p.getMetadata("SmokeBomb").get(0)).asLong() <= 15000L) {
                    return;
                }
                p.setMetadata("SmokeBomb", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
                int radius = Math.round((float)level * 1.5f);
                ArrayList<LivingEntity> victims = new ArrayList<LivingEntity>();
                FPlayer fp = (FPlayer)FPlayers.getInstance().getByPlayer(p);
                for (Entity ent : player.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                    FPlayer fpEnt;
                    if (!(ent instanceof LivingEntity) || ent instanceof Player && fp.getRelationTo(FPlayers.getInstance().getByPlayer((Player) ent)).isAtLeast(Relation.TRUCE)) continue;
                    victims.add((LivingEntity)ent);
                }
                try {
                    //ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation().add(0.0, 1.0, 0.0), 1.0f, 1.0f, 1.0f, 0.1f, 75);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_TWINKLE2, 2.0f, 0.75f);
                for (LivingEntity victim : victims) {
                    if (!(victim instanceof Player)) continue;
                    ((Player)victim).sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + p.getName() + " has thrown a Smoke Bomb in an attempt to escape!");
                    ((Player)victim).getWorld().playSound(victim.getLocation(), Sound.FIREWORK_TWINKLE2, 1.6f, 0.75f);
/*                    ((Player)victim).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Math.max(level, 3) * 20, Math.min(level, 3)));
                    ((Player)victim).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Math.max(level, 3) * 20, Math.min(level, 3)));*/
                }
                //e.setDamage(0.0);
            }
        }
    }

    private Location getNearbyLocation(Location l, int radius, int y_boost) {
        Random rand = new Random();
        Location rand_loc = l.clone();
        rand_loc.add((double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + 1)), 0.0, (double)((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + 1)));
        rand_loc.add(0.5, (double)y_boost, 0.5);
        while ((rand_loc.getBlock().getType() != Material.AIR || rand_loc.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) && rand_loc.getY() < 255.0) {
            rand_loc = rand_loc.add(0.0, 1.0, 0.0);
        }
        return rand_loc;
    }
}

