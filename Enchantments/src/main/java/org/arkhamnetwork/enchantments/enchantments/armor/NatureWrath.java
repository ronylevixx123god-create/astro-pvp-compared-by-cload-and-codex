/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicOutposts.CosmicOutpostsAPI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
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
 *  org.bukkit.entity.EnderCrystal
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.zencode.shortninja.staffplus.StaffPlus
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.cosmicpvp.cosmicutils.utils.UpgradeUtils;
import com.earth2me.essentials.Essentials;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.cmds.CommandEnchDust;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.mastery.MasteryPoltergeist;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;

public class NatureWrath
extends CustomEnchantment {
    int soulCost;
    Essentials ess;
    boolean factionUpgrades;

    public NatureWrath() {
        super("Nature Wrath", GeneralUtil.armor, 2);
        this.max = 4;
        this.base = 15.0;
        this.interval = 3.0;
        this.soulCost = 75;
        this.ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        this.factionUpgrades = Bukkit.getPluginManager().isPluginEnabled("FactionUpgrades");
        Bukkit.getPluginManager().registerEvents((Listener)new NatureWrathListener(), (Plugin)Enchantments.getInstance());
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (!CommandEnchDust.soulEnabled) {
            return;
        }
        if (!(attacker instanceof Player)) return;
        if (!(player instanceof Player)) return;

        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        double random = Math.random();
        double chance = 0.001 * (double)level;
        boolean KOTH = player.getWorld().getName().equals("world_koth");
        if (random < chance) {
            int cost;
            Player p = (Player)player;
            if (p.hasMetadata("soulTrap") && ((MetadataValue)p.getMetadata("soulTrap").get(0)).asLong() > System.currentTimeMillis()) {
                return;
            }
            if (MaskType.hasEquipped((Player) attacker, "THANOS")) {
                return;
            }
            if (MaskType.hasEquipped((Player) attacker, "ZEUS")) {
                return;
            }
            if(p.hasMetadata("witcher_ability"))
                return;
            int n = cost = KOTH ? this.soulCost * 2 : this.soulCost;


            if (KillTrackerAPI.hasSoulAmount((Player)p, (int)cost)) {
                KillTrackerAPI.removeSoulsFromGems((Player)p, (int)cost);
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "** NATURE'S WRATH **");
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + cost + " Soul Gems");
                p.sendMessage((Object)ChatColor.GRAY + "You have " + (Object)ChatColor.UNDERLINE + KillTrackerAPI.getAllSouls((Player)p) + (Object)ChatColor.GRAY + " souls left.");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.65f);
                p.sendMessage("");
                this.procNaturesWrath(p, attacker, level, e, 1.0);
                if (attacker != null) {
                    if (attacker instanceof Player) {
                        DamageIndicatorUtils.sendDamageIndicator((Player) attacker, CC.RedB + "* NATURE'S WRATH *" + CC.DarkPurpleB + "*", "NATURES_WRATH");
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

    public int procNaturesWrath(Player playerWhoProced, LivingEntity affected, int level, EntityDamageEvent e, double durationModifier) {
        int radius = 8 + level * 5;
        ArrayList<String> affectedPlayers = new ArrayList<String>();
        boolean KOTH = playerWhoProced.getWorld().getName().equals("world_koth");
        for (Entity ent : playerWhoProced.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
            Player pEnt;
            if (WorldGuardUtils.isPvPDisabled(ent.getLocation()) || ent.hasMetadata("spectator") || ent instanceof EnderCrystal) continue;
            if (ent instanceof LivingEntity && !(ent instanceof Player) && !ent.hasMetadata("do_not_clear")) {
                ent.getWorld().strikeLightningEffect(ent.getLocation());
                try {
                    //ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation().add(0.0, 1.0, 0.0), 0.6f, 0.6f, 0.6f, 0.1f, 10);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                try {
                    //ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation().add(0.0, 1.0, 0.0), 0.4f, 0.4f, 0.4f, 0.1f, 35);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ent.remove();
                continue;
            }
            if (!(ent instanceof Player) || GeneralUtil.isAtleastTruce(playerWhoProced, pEnt = (Player)ent) || pEnt.hasMetadata("natureWrathTask") || pEnt.getGameMode() != GameMode.SURVIVAL || this.ess.getUser(pEnt).isGodModeEnabled()) continue;
            CustomEnchantProcOnEntityEvent event = new CustomEnchantProcOnEntityEvent((LivingEntity)playerWhoProced, (LivingEntity)pEnt, (Event)e, this, level);
            boolean freeze = true;
            if (!event.callEvent()) {
                if (event.getEvent() == null) continue;
                freeze = false;
            }
            affectedPlayers.add(pEnt.getName());
            pEnt.getWorld().strikeLightningEffect(pEnt.getLocation());
            double dodge = MasteryPoltergeist.getChanceToProc(pEnt);
            if (dodge > 0.0 && freeze) {
                freeze = false;
                pEnt.sendMessage(CC.DarkRedB + "* POLTERGEIST [" + CC.Gray + "Immune: Nature's Wrath" + CC.DarkRedB + "] *");
            }
            if (freeze) {
                pEnt.setWalkSpeed(0.0f);
                pEnt.removePotionEffect(PotionEffectType.JUMP);
                pEnt.removePotionEffect(PotionEffectType.SLOW);
                pEnt.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (7 + level) * 20, 128));
                pEnt.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (7 + level) * 20, 128));
                pEnt.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (7 + level) * 20, 2));
            }
            pEnt.playSound(pEnt.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0f, 2.0f);
            NatureWrathTask nwt = new NatureWrathTask(pEnt, level);
            nwt.runTaskTimer((Plugin)Enchantments.getInstance(), 20L, 20L);
            pEnt.setMetadata("lastNatureWrathEffect", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
            pEnt.setMetadata("natureWrathTask", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)nwt));
        }
        final String[] players = affectedPlayers.toArray(new String[affectedPlayers.size()]);
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

            @Override
            public void run() {
                for (String s : players) {
                    Player p;
                    if (Bukkit.getPlayer((String)s) == null || !(p = Bukkit.getPlayer((String)s)).hasMetadata("natureWrathTask")) continue;
                    NatureWrathTask nwt = (NatureWrathTask)((Object)((MetadataValue)p.getMetadata("natureWrathTask").get(0)).value());
                    nwt.cancel();
                    p.setWalkSpeed(0.2f);
                    p.removeMetadata("natureWrathTask", (Plugin)Enchantments.getInstance());
                }
            }
        }, (long)((double)(KOTH ? (long)(7 + level) * 5L : (long)(7 + level) * 20L) * durationModifier));
        return affectedPlayers.size();
    }
}

