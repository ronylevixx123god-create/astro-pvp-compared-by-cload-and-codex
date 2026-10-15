package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
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

public class HeroicDiminish extends CustomEnchantment {
    private final DecimalFormat df;

    public HeroicDiminish() {
        super("Vengeful Diminish", GeneralUtil.chestplates, 2);
        this.df = new DecimalFormat("#.##");
        this.max = 6;
        this.base = 15.0;
        this.interval = 3.0;
        Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), new Runnable(){

            @Override
            public void run() {
                HeroicDiminish.this.heroicEnchantment = true;
                HeroicDiminish.this.nonHeroicVersion = EnchantmentAPI.getEnchantment("Diminish");
            }
        }, 2L);
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double random;
        double procChance;

        double maxDmg;

        if (attacker instanceof Player) {


            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.VALENTINES_2021_SWORD)) {
                return;
            }
        }

        if (attacker == null) return;

        if (player.hasMetadata("diminishEffect")) {
            maxDmg = player.getMetadata("diminishEffect").get(0).asDouble();
            if (maxDmg > 0.0 && attacker instanceof Player) {
                //attacker.damage(maxDmg);
                Player attackerPlayer = (Player)attacker;
                Bukkit.getLogger().info("[Heroic Diminish] Damaging attacker " + attackerPlayer.getName() + " for " + maxDmg + " due to being then diminishEffect: " + maxDmg + " maxDmg: " + e.getDamage());
            }
            if (e.getFinalDamage() > maxDmg) {
                e.setDamage(maxDmg);
                player.removeMetadata("diminishEffect", Enchantments.getInstance());
                return;
            }
        }
        if ((random = Math.random()) < (procChance = 0.001 * (double)level)) {
            e.setDamage(e.getDamage() * 0.9);
            player.setMetadata("diminishEffect", new FixedMetadataValue(Enchantments.getInstance(), e.getFinalDamage() / 2.0));
            player.sendMessage(CC.translate( "&d&l* VENGEFUL DIMINISH ["  + "&fMAX DMG: " + this.df.format(e.getFinalDamage() / 2.0) + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "] *"));
        }
    }

}
