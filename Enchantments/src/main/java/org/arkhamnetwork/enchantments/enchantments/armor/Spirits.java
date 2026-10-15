/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.gmail.nossr50.mcMMO
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.Factions
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Blaze
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
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
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class Spirits
extends CustomEnchantment {
    public static Map<Entity, Integer> spirits = new WeakHashMap<Entity, Integer>();
    public static Map<Integer, Integer> ticks = new HashMap<Integer, Integer>();

    public Spirits() {
        super("Spirits", GeneralUtil.armor, 2);
        this.max = 10;
        this.base = 20.0;
        this.interval = 5.0;
        Bukkit.getPluginManager().registerEvents((Listener)new acenstoralListener(), (Plugin)Enchantments.getInstance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)Enchantments.getInstance(), () -> spirits.forEach((ent, level) -> {
            if (!ent.isValid()) {
                return;
            }
            int required = (int)((double)(20 + (10 - level) * 4) * 1.5);
            Integer currentTick = ticks.computeIfAbsent(ent.getEntityId(), k -> 0);
            if (currentTick >= required) {
                ticks.put(ent.getEntityId(), 0);
                Faction faction = Factions.getInstance().getFactionById(((MetadataValue)ent.getMetadata("faction").get(0)).asInt() + "");
                if (faction == null || faction.getFPlayers().size() <= 0) {
                    return;
                }
                int healed = 0;
                int maxHeal = level > 6 ? 2 : 1;
                int radius = 8 + level;
                int toHeal = level <= 5 ? 2 : (level <= 8 ? 4 : 5);
                toHeal /= 2;
                try {
                    //ParticleEffect.HEART.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation().clone().add(0.0, 1.0, 1.0), 0.5f, 0.5f, 0.5f, 0.01f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                for (Entity nearby : ent.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                    if (healed < maxHeal) {
                        FPlayer fpl;
                        long cd;
                        Player player;
                        if (!(nearby instanceof Player) || (player = (Player)nearby).hasMetadata("heal_cd") && (cd = ((MetadataValue)player.getMetadata("heal_cd").get(0)).asLong()) > System.currentTimeMillis() || (fpl = FPlayers.getInstance().getByPlayer(player)) == null || !fpl.hasFaction() || fpl.getRelationTo((RelationParticipator)faction) != Relation.ALLY || player.getHealth() >= player.getMaxHealth() || player.isDead()) continue;
                        ++healed;
                        //GeneralUtil.healPlayer((LivingEntity)player, toHeal);
                        player.setMetadata("heal_cd", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)((double)System.currentTimeMillis() + (double)required / 20.0 * 1000.0)));
                        try {
                         //   ParticleEffect.HEART.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation().clone().add(0.0, 1.0, 1.0), 0.75f, 0.75f, 0.75f, 0.01f, 15);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    break;
                }
            } else {
                currentTick = currentTick + 1;
                ticks.put(ent.getEntityId(), currentTick);
            }
        }), 20L, 1L);
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player) {
            if (player.hasMetadata("acenstoralEvent") && System.currentTimeMillis() < ((MetadataValue)player.getMetadata("acenstoralEvent").get(0)).asLong()) {
                return;
            }
            double chance = 0.01 + (double)level * 0.05;
            if (chance > 0.02) {
                chance = 0.02;
            }
            if (Math.random() < chance) {
                String facName;
                player.setMetadata("acenstoralEvent", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)(System.currentTimeMillis() + 10000L)));
                int acenstoral_count = level / 10 + 1;
                Player p = (Player)player;
                Location spawnLoc = player.getLocation().add(0.0, 1.0, 0.0);
                try {
                    //ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), spawnLoc, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.64f, 45);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                try {
                    //ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), spawnLoc, (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.154f, 35);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                FPlayer fp = FPlayers.getInstance().getByPlayer((Player) player);
                String string = facName = fp.hasFaction() ? fp.getFaction().getId() : "";
                while (acenstoral_count > 0) {
                    --acenstoral_count;
                    if (spawnLoc.getChunk().getEntities().length >= 50) continue;
                    Blaze blaze = (Blaze)player.getWorld().spawnEntity(spawnLoc, EntityType.BLAZE);
                    spirits.put((Entity)blaze, level);
                    blaze.setMetadata("faction", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)facName));
                    blaze.setMetadata("acenstoralSummoner", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)p.getName()));
                    blaze.setMetadata("no_loot", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)true));
                    blaze.setCanPickupItems(false);
                    blaze.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD.toString() + p.getName() + "'s Spirit");
                    blaze.setCustomNameVisible(true);
                    double customHp = 50 + level * 10;
                    blaze.setMaxHealth(customHp);
                    blaze.setHealth(customHp);
                    blaze.getWorld().playSound(blaze.getLocation(), Sound.IRONGOLEM_DEATH, 1.0f, 0.55f);
                    blaze.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                    if (level >= 4) {
                        blaze.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
                    }
                    if (level >= 6) {
                        blaze.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
                    }
                    if (level >= 8) {
                        blaze.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                    }
                    if (level == 10) {
                        blaze.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
                    }
                    Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> {
                        blaze.damage(blaze.getHealth() * 10.0);
                        if (!blaze.isDead()) {
                            blaze.remove();
                        }
                    }, 200L);
                }
            }
        }
    }
}

