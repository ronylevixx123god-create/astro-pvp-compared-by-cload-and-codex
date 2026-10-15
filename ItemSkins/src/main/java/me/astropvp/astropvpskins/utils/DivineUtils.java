package me.astropvp.astropvpskins.utils;

import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DivineUtils {
    public static void divinePlayer(Player damaged, int enchantLevel) {
        double damage = Math.min(4, 4 + enchantLevel) / 4.0;
        damaged.damage(damage);
        ((Player) damaged).sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "** DIVINE IMMOLATION **");
        ((Player) damaged).playSound(damaged.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 0.6f, 0.8f);
        ((Player) damaged).playSound(damaged.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 0.3f);
        if (!damaged.hasPotionEffect(PotionEffectType.WITHER)) {
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 3 * 20, 1));
        }
    }
}
