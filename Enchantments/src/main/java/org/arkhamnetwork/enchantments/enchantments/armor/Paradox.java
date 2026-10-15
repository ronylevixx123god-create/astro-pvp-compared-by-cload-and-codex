/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicOutposts.CosmicOutpostsAPI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.UpgradeUtils
 *  com.earth2me.essentials.Essentials
 *  com.rit.sucy.CustomEnchantment
 *  me.ifamasssxd.factionupgrades.FactionUpgradeAPI
 *  me.ifamasssxd.factionupgrades.net.astropvp.armorsets.struct.FactionUpgrade
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.earth2me.essentials.Essentials;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.cmds.CommandEnchDust;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class Paradox
extends CustomEnchantment {
    int soulCost;
    private boolean factionUpgrades;
    Essentials ess;

    public Paradox() {
        super("Paradox", GeneralUtil.armor, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 3.0;
        this.soulCost = 5;
        this.factionUpgrades = Bukkit.getPluginManager().isPluginEnabled("FactionUpgrades");
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (!CommandEnchDust.soulEnabled) {
            return;
        }
        if(player.hasMetadata("witcher_ability"))
            return;
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        if (player.hasMetadata("neutralized"))
            return;
        double random = Math.random();
        double chance = 0.1 * (double)level;
        int radius = 8 + level * 4;
        ArrayList<Player> affectedPlayers = new ArrayList<Player>();
        if (random < chance) {
            Player p = (Player)player;
            double amountToHeal = e.getDamage() * (double)(level / 10);
            int cost = this.soulCost;


            if (KillTrackerAPI.hasSoulAmount((Player)p, (int)cost)) {
                int highImpactPlayers = 0;
                for (Entity ent : p.getNearbyEntities((double)radius, (double)(radius + 128), (double)radius)) {
                    Player pEnt;
                    if (ent.hasMetadata("spectator") || !(ent instanceof Player) || !GeneralUtil.isAlly(p, pEnt = (Player)ent) || pEnt.getGameMode() != GameMode.SURVIVAL) continue;
                    affectedPlayers.add(pEnt);
                    if (!(pEnt.getHealth() + amountToHeal <= pEnt.getMaxHealth())) continue;
                    ++highImpactPlayers;
                }
                if (highImpactPlayers > 0) {
                    KillTrackerAPI.removeSoulsFromGems((Player)p, (int)cost);
                    for (Player pEnt : affectedPlayers) {
                        try {
                            //ParticleEffect.HAPPY_VILLAGER.sendToPlayers(Bukkit.getOnlinePlayers(), pEnt.getLocation().add(0.0, 1.0, 0.0), 0.7f, 0.7f, 0.7f, 0.1f, 20);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        //GeneralUtil.healPlayer((LivingEntity)pEnt, amountToHeal);
                        pEnt.playSound(pEnt.getLocation(), Sound.EAT, 1.0f, 2.0f);
                        pEnt.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "** PARADOX [" + p.getName() + "] (+" + amountToHeal + "HP) **");
                    }
                    p.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "** PARADOX [" + (double)affectedPlayers.size() * amountToHeal + " -> HP]  **");
                    if (KillTrackerAPI.getAllSouls((Player)p) % 100 == 0 || KillTrackerAPI.getAllSouls((Player)p) < 10) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + cost + " Soul Gems");
                        p.sendMessage((Object)ChatColor.GRAY + "You have " + (Object)ChatColor.UNDERLINE + KillTrackerAPI.getAllSouls((Player)p) + (Object)ChatColor.GRAY + " souls left.");
                    }
                }
            } else {
                try {
                    //ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), p.getEyeLocation(), 0.4f, 0.4f, 0.4f, 0.1f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                p.playSound(p.getLocation(), Sound.ITEM_BREAK, 0.7f, 0.4f);
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "** OUT OF SOULS **");
            }
        }
    }
}

