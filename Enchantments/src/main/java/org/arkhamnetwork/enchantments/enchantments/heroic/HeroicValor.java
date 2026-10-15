package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class HeroicValor extends HeroicCustomEnchantment
{
    public HeroicValor() {
        super("Martyr Valor", "Valor", GeneralUtil.armor, 2);
        this.max = 5;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (!(player instanceof Player)) {
            return;
        }
        if (player.hasMetadata("neutralized") && Math.random() < 0.1)
            return;


        if (attacker instanceof Player) {
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.BACK_TO_SCHOOL_2021_AXE) && Math.random() < 0.5) {
                return;
            }
        }
        //double damage_negation = level * 0.015D;
        double damage_negation = level * 0.013D;
        double hp_threshold = (level * 5 + 6);

        if (attacker instanceof Player && AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.BACK_TO_SCHOOL_2021_BELT)) {
            damage_negation /=2;
        }

        if (player.getHealth() <= hp_threshold && (((Player)player).getItemInHand().getType() == Material.GOLD_SWORD || ((Player)player).getItemInHand().getType() == Material.DIAMOND_SWORD) && e.getDamage() * (1.0 - damage_negation) > 1.0) {
            e.setDamage(e.getDamage() * (1.0D - damage_negation));
            final Location l = player.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), 41, 2001);
        }
    }
}
