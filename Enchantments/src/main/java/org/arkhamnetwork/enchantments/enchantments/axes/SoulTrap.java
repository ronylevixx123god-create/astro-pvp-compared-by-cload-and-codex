/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import de.inventivegames.particle.ParticleEffect;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.cmds.CommandEnchDust;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class SoulTrap
extends CustomEnchantment {
    public SoulTrap() {
        super("Soul Trap", GeneralUtil.axe, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (!CommandEnchDust.soulEnabled) {
            return;
        }
        if (!(playerHit instanceof Player)) {
            return;
        }
        Player p = (Player)player;
        Player pHit = (Player)playerHit;
        double radius = 1.0 * (double)level;
        double chance = 0.07 * (double)level;
        long duration = level * 4L;
        ItemStack used = p.getItemInHand();
        if (used == null || !EnchantmentAPI.itemHasEnchantment((ItemStack)used, (String)this.enchantName)) {
            return;
        }
        if (p.hasMetadata("lastItemHeldChange") && (long)MinecraftServerUtils.getCurrentServerTick() - ((MetadataValue)p.getMetadata("lastItemHeldChange").get(0)).asLong() <= 5L) {
            return;
        }
        if (p.hasMetadata("soulTrap") && ((MetadataValue)p.getMetadata("soulTrap").get(0)).asLong() > System.currentTimeMillis()) {
            return;
        }
        if (KillTrackerAPI.hasGodMode((Player)p) && KillTrackerAPI.getAllSouls((Player)p) > 0 && Math.random() < chance) {
            int souls;
            int toTake;
            if (pHit.hasMetadata("soulTrap") && System.currentTimeMillis() < ((MetadataValue)pHit.getMetadata("soulTrap").get(0)).asLong() + 5000L) {
                return;
            }
            if (!p.hasMetadata("lastSoulRemoveEvent") || System.currentTimeMillis() - ((MetadataValue)p.getMetadata("lastSoulRemoveEvent").get(0)).asLong() > 1000L) {
                KillTrackerAPI.removeSoulsFromGems((Player)p, (int)5);
            }
            if ((toTake = 10 * level) > (souls = KillTrackerAPI.getAllSouls((Player)pHit))) {
                toTake = souls;
            }
            if (toTake > 0) {
                KillTrackerAPI.removeSoulsFromGems((Player)pHit, (int)toTake);
                Bukkit.getLogger().info("removing " + toTake + " souls from " + pHit.getName() + " form " + p.getName() + "'s Soul Trap!");
            } else {
                pHit.damage((double)(1 + level));
            }
            pHit.setMetadata("soulTrap", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)(System.currentTimeMillis() + duration * 1000L)));
            pHit.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "** SOUL TRAP " + (Object)ChatColor.GRAY + "[" + duration + "s]" + (Object)ChatColor.BLUE + (Object)ChatColor.BOLD + "**");
            if (KillTrackerAPI.hasGodMode((Player)pHit)) {
                KillTrackerAPI.toggleGodMode((Player)pHit);
            }
            pHit.playSound(pHit.getLocation(), Sound.ENDERMAN_SCREAM, 1.0f, 0.3f);
            try {
                //ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), pHit.getLocation().add(0.0, 1.0, 0.0), 0.7f, 0.7f, 0.7f, 0.1f, 60);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                //ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), pHit.getLocation().add(0.0, 1.0, 0.0), 0.4f, 0.4f, 0.4f, 0.1f, 25);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
}

