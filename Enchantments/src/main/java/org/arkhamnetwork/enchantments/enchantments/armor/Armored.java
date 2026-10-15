package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Armored extends CustomEnchantment
{
    public Armored() {
        super("Armored", GeneralUtil.armor, 4);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (!(player instanceof Player)) {
            return;
        }

        if (player.hasMetadata("neutralized") && Math.random() < 0.3) {
            return;
        }

        ItemStack i = null;
        if (attacker instanceof Player) {
            final Player pAttacker = (Player)attacker;
            if (AstroItemSkinsAPI.hasSkinEquipped(pAttacker, SkinType.VALENTINES_2021_BELT) && !AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.PRISONS_DARKSTAR_PICKAXE_BLUE)) return;
            if (AstroItemSkinsAPI.hasSkinEquipped(pAttacker, SkinType.EASTER_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped(pAttacker, SkinType.EASTER_2022_MUTATED_SWORD)) return;
            i = pAttacker.getItemInHand();
        }
        double damage_negation = (double)level * 0.01875;
        if (i != null && i.getType().name().contains("_SWORD") && e.getDamage() * (1.0 - damage_negation) > 1.0) {
            //e.setDamage(e.getDamage() * (1.0D - damage_negation));
            final Location l = player.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), 57, 2001);
        }
    }
}
