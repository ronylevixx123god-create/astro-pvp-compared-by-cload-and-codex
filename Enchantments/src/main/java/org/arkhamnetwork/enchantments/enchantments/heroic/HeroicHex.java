package org.arkhamnetwork.enchantments.enchantments.heroic;

import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.enchantments.axes.Hex;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.metadata.*;
import org.bukkit.plugin.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

import java.util.Random;

public class HeroicHex
        extends HeroicCustomEnchantment {
    public HeroicHex() {
        super("Bewitched Hex", "Hex");
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, final LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance;
        double random;
        if (playerHit instanceof Player && (random = Math.random()) < (chance = 0.03 * (double)level) && (!playerHit.hasMetadata("hexEnchantExpire") || ((MetadataValue)playerHit.getMetadata("hexEnchantExpire").get(0)).asLong() < System.currentTimeMillis())) {
            playerHit.setMetadata("hexEnchantExpire", new FixedMetadataValue(Enchantments.getInstance(), System.currentTimeMillis() + (long)((2 + level / 2) * 1000)));
            playerHit.setMetadata("hexEnchantLevel", new FixedMetadataValue(Enchantments.getInstance(), level));
            ((Player)playerHit).sendMessage(CC.translate( "&d&l* HEX DEBUFF [" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + (2 + level / 2) + "s" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "] *"));
            Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), new Runnable(){

                @Override
                public void run() {
                    ((Player)playerHit).sendMessage(CC.translate( "&d&l* HEX OFF *"));
                }
            }, (long)(2 + level / 2) * 20L);
        }
    }

    public static boolean isHexed(LivingEntity e) {
        return e.hasMetadata("hexEnchantExpire") && ((MetadataValue)e.getMetadata("hexEnchantExpire").get(0)).asLong() > System.currentTimeMillis();
    }

    public static int getHexLevel(LivingEntity e) {
        return !Hex.isHexed(e) ? 0 : ((MetadataValue)e.getMetadata("hexEnchantLevel").get(0)).asInt();
    }
}
