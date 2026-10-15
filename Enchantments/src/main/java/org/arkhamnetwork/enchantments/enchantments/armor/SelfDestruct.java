/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class SelfDestruct
extends CustomEnchantment {

    public static Map<UUID, Player> tntList= new HashMap<>();


    public SelfDestruct() {
        super("Self Destruct", GeneralUtil.armor, 3);
        this.max = 3;
        this.base = 25.0;
        this.interval = 10.0;
        Enchantments.getInstance().getServer().getPluginManager().registerEvents((Listener)new SelfDestructListener(), (Plugin)Enchantments.getInstance());
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {
            if (player.hasMetadata("neutralized"))
                return;
            Player p = (Player)player;
            double dmg = e.getFinalDamage();
            if (player.getHealth() - dmg <= 0.0) {
                if (p.hasMetadata("SelfDestructing") && System.currentTimeMillis() - ((MetadataValue)p.getMetadata("SelfDestructing").get(0)).asLong() <= 10000L) {
                    return;
                }
                p.setMetadata("SelfDestructing", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) System.currentTimeMillis()));
                Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> {
                    p.removeMetadata("SelfDestructing", Enchantments.getInstance());
                }, 180*20);                int radius = (int)((float)level * 2.5f);
                FPlayer fp = (FPlayer)FPlayers.getInstance().getByPlayer(p);
                ArrayList<LivingEntity> victims = new ArrayList<LivingEntity>();
                for (Entity ent : player.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                    Player pEnt;
                    FPlayer fpEnt;
                    if (!(ent instanceof LivingEntity) || ent instanceof Player && FPlayers.getInstance().getByPlayer((Player)ent).getRelationTo(fp).isAtLeast(Relation.ALLY)) continue;
                    victims.add((LivingEntity)ent);
                }
                p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 2.0f, 0.75f);
                for (LivingEntity victim : victims) {
                    if (!(victim instanceof Player)) continue;
                    ((Player)victim).sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + p.getName() + "'s Self-Destruct was activated, RUN!");
                }
                int fuseTicks = 120 - level * 20;
                for (int tntAmount = radius; tntAmount > 0; --tntAmount) {
                    this.spawnExplosion(p, this.getNearbyLocation(player.getLocation(), 3, 1), fuseTicks);
                }
            }
        }
    }

    private void spawnExplosion(Player exploder, Location l, int fuseTicks) {
        if (l.getBlock().getType().isSolid()) {
            return;
        }
        TNTPrimed tnt = (TNTPrimed)l.getWorld().spawnEntity(l, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(fuseTicks);
        //tnt.setMetadata("SelfDestructTNT", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)exploder.getName()));
        tntList.put(tnt.getUniqueId(), exploder);
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

