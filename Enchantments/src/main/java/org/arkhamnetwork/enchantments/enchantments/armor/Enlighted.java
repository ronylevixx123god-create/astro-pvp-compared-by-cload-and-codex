package org.arkhamnetwork.enchantments.enchantments.armor;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.*;
import org.arkhamnetwork.enchantments.*;
import com.rit.sucy.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

public class Enlighted extends CustomEnchantment
{
    public Enlighted() {
        super("Enlighted", GeneralUtil.armor, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 8.0;
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable() {
            @Override
            public void run() {
                Enlighted.this.heroicVersion = EnchantmentAPI.getEnchantment("Divine Enlighted");
            }
        }, 2L);
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, int level, final EntityDamageEvent e) {

        final double random = Math.random();
        if (level > 3) {
            level = 3;
        }
        double chance = 0.075 * level;
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.VALENTINES_2022_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.VALENTINES_2022_MUTATED_BACKPACK)) {
            chance*=1.1;
        }
        if (random < chance) {
            if (player.hasMetadata("neutralized"))
                return;

            Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (player.hasMetadata("reflectedEnglighted")) {
                        player.damage(1);
                    } else {
                        GeneralUtil.healPlayer(player, 1.0);
                    }
                }
            }, 1L);
        }
    }
}
