/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.MapMaker
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.enchantments.swords.Blind;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class Clarity
extends CustomEnchantment {
    private Map<Player, Integer> claritiedPlayers = new MapMaker().weakKeys().concurrencyLevel(32).makeMap();

    public Clarity() {
        super("Clarity", GeneralUtil.armor, 2);
        this.max = 3;
        this.base = 10.0;
        this.interval = 5.0;
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)Enchantments.getInstance(), () -> this.claritiedPlayers.forEach((player, maxBlindLevelToRemove) -> {
            if (!player.isValid()) {
                return;
            }
            if (player.hasMetadata(Blind.RECENTLY_BLINDED_METADATA) && ((MetadataValue)player.getMetadata(Blind.RECENTLY_BLINDED_METADATA).get(0)).asLong() > System.currentTimeMillis()) {
                return;
            }
            for (PotionEffect pe : Lists.newArrayList(player.getActivePotionEffects())) {
                if (!pe.getType().getName().equals("BLINDNESS") || pe.getAmplifier() > maxBlindLevelToRemove) continue;
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                try {
                    ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), player.getEyeLocation(), 1f, 1f, 1f, 0.1f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                player.playSound(player.getLocation(), Sound.FIZZ, 1.0f, 0.6f);
                return;
            }
        }), 3L, 3L);
    }

    public void applyEquipEffect(Player player, int level) {
        int maxBlindLevelToRemove = level == 3 ? 3 : level - 1;
        this.claritiedPlayers.put(player, maxBlindLevelToRemove);
        player.setMetadata("clarityEnchant", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
    }

    public static boolean canBlindnessApply(Player player, int blindLevel) {
        if (!player.hasMetadata("clarityEnchant")) {
            return true;
        }
        int level = ((MetadataValue)player.getMetadata("clarityEnchant").get(0)).asInt();
        if (level <= 0) {
            return true;
        }
        int maxBlindLevelToRemove = level == 3 ? 3 : level - 1;
        return blindLevel > maxBlindLevelToRemove;
    }

    public void applyUnequipEffect(Player player, int level) {
        player.removeMetadata("clarityEnchant", (Plugin)Enchantments.getInstance());
        this.claritiedPlayers.remove((Object)player);
    }
}

