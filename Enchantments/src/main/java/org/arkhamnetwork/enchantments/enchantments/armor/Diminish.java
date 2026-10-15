package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;

public class Diminish extends CustomEnchantment {
    private final DecimalFormat df;

    public Diminish() {
        super("Diminish", GeneralUtil.chestplates, 2);
        this.df = new DecimalFormat("#.##");
        this.max = 6;
        this.base = 15.0;
        this.interval = 3.0;
        Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), () -> Diminish.this.heroicVersion = EnchantmentAPI.getEnchantment("Vengeful Diminish"), 2L);
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double procChance;
        double random;
        if (attacker instanceof Player) {
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.VALENTINES_2021_SWORD)) {
                return;
            }
        }

        if (attacker == null) return;

        if (player.hasMetadata("diminishEffect")) {
            double maxDmg = player.getMetadata("diminishEffect").get(0).asDouble();
            if (e.getDamage(EntityDamageEvent.DamageModifier.BASE) > maxDmg) {
                e.setDamage(maxDmg);
                player.removeMetadata("diminishEffect", Enchantments.getInstance());
                return;
            }
        }
        if ((random = Math.random()) < (procChance = 0.0015 * (double)level) && e.getDamage(EntityDamageEvent.DamageModifier.BASE) > 0.0) {
            player.setMetadata("diminishEffect", new FixedMetadataValue(Enchantments.getInstance(), e.getDamage(EntityDamageEvent.DamageModifier.BASE)));
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "* DIMINISH [" + ChatColor.YELLOW + "DMG: " + this.df.format(Math.max(0.0, e.getDamage())) + ChatColor.BOLD + "] *");
        }
    }
}
