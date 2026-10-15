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

public class Tank extends CustomEnchantment
{
    public Tank() {
        super("Tank", GeneralUtil.armor, 2);
        this.max = 4;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (!(player instanceof Player)) {
            return;
        }

        if (player.hasMetadata("neutralized") && Math.random() < 0.1)
            return;

        if (attacker == null) return;
        if (!(attacker instanceof Player)) return;
        if ( AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.JULY_2021_BELT)) {
            return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.THANKSGIVING_SWORD_HARVESTER)) {
            return;
        }


        ItemStack i = null;
        if (attacker instanceof Player) {
            final Player pAttacker = (Player)attacker;
            i = pAttacker.getItemInHand();
        }
        double damage_negation = (double)level * 0.01875;
        if (i != null && i.getType().name().contains("_AXE") && e.getDamage() * (1.0 - damage_negation) > 1.0) {

            e.setDamage(e.getDamage() * (1.0 - damage_negation));
            final Location l = player.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), 42, 2001);
        }
    }
}
