/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.GoldenAdmirer;
import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.util.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Trap
extends CustomEnchantment {
    public Trap() {
        super("Trap", GeneralUtil.swords, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 3.0;
        Bukkit.getPluginManager().registerEvents((Listener)new TrapEvents(), (Plugin)Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity player, LivingEntity target, int level, EntityDamageByEntityEvent e) {
        double chance;
        if (!(target instanceof Player)) {
            return;
        }
        if (((Player)player).getWalkSpeed() < 0.2f) {
            return;
        }
        double random = Math.random();
        if (random < (chance = 0.04 * (double)level)) {
            int metaphysicalLevel;
            final Player ptarget = (Player)target;
            if (target.hasMetadata("metaphysicalEnchant") && random > (chance -= (double)(metaphysicalLevel = ((MetadataValue)target.getMetadata("metaphysicalEnchant").get(0)).asInt()) * 0.025)) {
                ptarget.sendMessage((Object)ChatColor.DARK_GRAY + "" + (Object)ChatColor.BOLD + "** METAPHYSICAL (" + (Object)ChatColor.DARK_GRAY + "Trap blocked!" + (Object)ChatColor.BOLD + ") **");
                return;
            }
            if (!new CustomEnchantProcOnEntityEvent(player, target, (Event)e, this, level).callEvent()) {
                return;
            }
            if (target.hasMetadata("immune_freeze")) {
                ptarget.sendMessage(CC.DarkGrayB + "* DRAGON SLAYER [" + (Object)ChatColor.GRAY + "Trap blocked!" + CC.DarkGrayB + "] *");
                return;
            }
            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.THANKSGIVING_HELMET_WITCH) && (!GoldenAdmirer.isAdmirerSilenced(ptarget))) {
                ptarget.sendMessage(CC.translate("&e&l* WITCH HAT &7Blocked &eTrap &e&l*"));
                return;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(ptarget, SkinType.HALLOWEEN_2022_HELMET)) {
                ptarget.sendMessage(CC.translate("&4&l* GRIM HOOD BLOCKED &7[Titan Trap] &4&l*"));
                return;
            }


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) ptarget, SkinType.SPRING_BREAK_2022_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped((Player) ptarget, SkinType.SPRING_BREAK_2022_MUTATED_BOOTS)) {
                SinkOrSwipFlippers.addSinkStack((Player) ptarget);
                ptarget.sendMessage(CC.translate("&a&l* BLOCKED FREEZE &7[+1 Stack] &a&l*"));
                return;
            }

            ptarget.setWalkSpeed(0.0f);
            ptarget.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You have been trapped by " + ((Player)player).getName() + "!");
            DamageIndicatorUtils.sendDamageIndicator((Player) ptarget, CC.AquaB + "* TRAP " + CC.Gray + "[" + 3.25 + "]" + CC.AquaB + "*" + CC.DarkPurpleB +  "*", "TRAP");

            Enchantments.getInstance().getServer().getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

                @Override
                public void run() {
                    ptarget.setWalkSpeed(0.2f);
                    ptarget.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.GREEN + "You are no longer trapped!");
                }
            }, 35L);
            Location l = ptarget.getLocation();
        }
    }

    private static class TrapEvents
    implements Listener {
        private TrapEvents() {
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent e) {
            Player pl = e.getPlayer();
            pl.setWalkSpeed(0.2f);
        }

        @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
        public void onEntityDamage(EntityDamageEvent e) {
            Player p;
            if (e.getEntity() instanceof Player && (p = (Player)e.getEntity()).getWalkSpeed() == 0.0f && !p.isOp()) {
                Location l = p.getLocation();
            }
        }
    }
}

