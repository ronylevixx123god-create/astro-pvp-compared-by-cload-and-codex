/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.enums.ArmorSet
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.google.common.collect.Lists
 *  com.rit.sucy.CustomEnchantment
 *  net.minecraft.server.v1_7_R4.MinecraftServer
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Blessed
extends CustomEnchantment {
    private static List<PotionEffectType> debuffs;

    public Blessed() {
        super("Blessed", GeneralUtil.axe, 7);
        this.max = 4;
        this.base = 30.0;
        this.interval = 10.0;
        debuffs = Lists.newArrayList(PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER);
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double chance;
        if (!(target instanceof Player) || !(user instanceof Player)) {
            return;
        }
        Player playerAttacker = (Player)user;
        double random = Math.random();
        if (random < (chance = 0.03 * (double)enchantLevel)) {
            if (user.hasMetadata("deepWounds") && ((MetadataValue)user.getMetadata("deepWounds").get(0)).asLong() > System.currentTimeMillis()) {
                playerAttacker.sendMessage((Object)ChatColor.DARK_RED + "** DEEP WOUNDS " + (Object)ChatColor.GRAY + "(NO BLESS)" + (Object)ChatColor.DARK_RED + " **");
                return;
            }
            Bleed.clearBleedEffect((LivingEntity)playerAttacker);
            playerAttacker.playSound(playerAttacker.getLocation(), Sound.SPLASH, 1.2f, 2.0f);
            Blessed.blessPlayer(playerAttacker);
            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(playerAttacker, "BLESS", new Object[]{target}));
        }
    }

    public static void blessPlayer(Player player) {
        Blessed.blessPlayer(player, false);
    }

    public static void blessPlayer(Player player, boolean respectDeepWounds) {
        List<PotionEffectType> playerDebuffs = Blessed.getActiveDebuffs(player);
        if (playerDebuffs.size() > 0) {
            if (respectDeepWounds && player.hasMetadata("deepWounds") && ((MetadataValue)player.getMetadata("deepWounds").get(0)).asLong() > System.currentTimeMillis()) {
                player.sendMessage((Object)ChatColor.DARK_RED + "** DEEP WOUNDS " + (Object)ChatColor.GRAY + "(NO BLESS)" + (Object)ChatColor.DARK_RED + " **");
                return;
            }
            ArmorSetType as = AstroArmorSetsAPI.getCurrentArmorSet((Player)player);
            if (as != null && as.equals((Object)ArmorSetType.KOTH)) {
                long nextBlessedTextTick;
                long l = nextBlessedTextTick = player.hasMetadata("nextBlessedTextTick") ? ((MetadataValue)player.getMetadata("nextBlessedTextTick").get(0)).asLong() : 0L;
                if ((long) MinecraftServer.currentTick >= nextBlessedTextTick) {
                    player.setMetadata("nextBlessedTextTick", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)(MinecraftServer.currentTick + 160)));
                    player.sendMessage(ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + "** BLESSED **");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + "** BLESSED **");
            }
            PlayerPotionEffects.untrackPotionEffect(player, playerDebuffs.get(0));
        }
    }

    public static List<PotionEffectType> getActiveDebuffs(Player p) {
        ArrayList<PotionEffectType> result = new ArrayList<PotionEffectType>();
        for (PotionEffect pe : p.getActivePotionEffects()) {
            if (!debuffs.contains((Object)pe.getType())) continue;
            result.add(pe.getType());
        }
        return result;
    }
}

