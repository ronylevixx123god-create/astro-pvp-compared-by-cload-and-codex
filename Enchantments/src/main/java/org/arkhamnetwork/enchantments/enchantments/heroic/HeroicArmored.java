package org.arkhamnetwork.enchantments.enchantments.heroic;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.enchantments.axes.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.inventory.*;
import org.bukkit.*;

public class HeroicArmored extends HeroicCustomEnchantment {
    public HeroicArmored() {
        super("Paladin Armored", "Armored");
    }

    @Override
    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (!(player instanceof Player)) {
            return;
        }
        if (player.hasMetadata("neutralized") && Math.random() < 0.1) {
            return;
        }
        ItemStack i = null;
        if (attacker instanceof Player) {
            final Player pAttacker = (Player) attacker;
            if (AstroItemSkinsAPI.hasSkinEquipped(pAttacker, SkinType.VALENTINES_2021_BELT) && !AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.PRISONS_DARKSTAR_PICKAXE_BLUE)) {
                return;
            }
            i = pAttacker.getItemInHand();
            if (Math.random() < level * 0.01) {
                Blessed.blessPlayer((Player) player, false);
            }
        }
        double damage_negation = (double)level * 0.005;
        // 0.02
        if (i != null && i.getType().name().contains("_SWORD") && e.getDamage() * (1.0D - damage_negation) > 1.0D) {
            e.setDamage(e.getDamage() * (1.0D - damage_negation));
            Location l = player.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), 57, 2001);

        }
    }
}
