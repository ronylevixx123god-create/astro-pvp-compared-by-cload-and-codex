package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Heavy extends CustomEnchantment {
    public Heavy() {
        super("Heavy", GeneralUtil.armor, 2);
        this.max = 5;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (!(player instanceof Player))
            return;
        ItemStack i = null;
        if (attacker instanceof Player) {
            Player pAttacker = (Player)attacker;
            i = pAttacker.getItemInHand();
        }
        double damage_negation = level * 0.02D;
        if (i != null && i.getType() == Material.BOW && e
                .getDamage() * (1.0D - damage_negation) > 1.0D) {
            e.setDamage(e.getDamage() * (1.0D - damage_negation));
            Location l = player.getLocation();
            WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)133, (int)2001);
        }
    }
}
