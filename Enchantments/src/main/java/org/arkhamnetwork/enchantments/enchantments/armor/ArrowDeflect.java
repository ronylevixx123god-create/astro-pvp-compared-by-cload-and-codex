package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;

public class ArrowDeflect extends CustomEnchantment
{
    public ArrowDeflect() {
        super("Arrow Deflect", GeneralUtil.armor, 4);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent)e;
            if (edbee.getDamager() instanceof Projectile) {
                final Projectile shooter = (Projectile)edbee.getDamager();
                if (shooter != null && shooter.hasMetadata("lethalSniper")) {
                    final int sniper = shooter.getMetadata("lethalSniper").get(0).asInt();
                    final double chance = 0.1 * sniper;
                    if (Math.random() <= chance) {
                        return;
                    }
                }
                if (player.hasMetadata("lastArrowDamageEvent")) {
                    final long minDelayMilliseconds = level * 500;
                    final long last = player.getMetadata("lastArrowDamageEvent").get(0).asLong();
                    if (System.currentTimeMillis() - last <= minDelayMilliseconds) {
                        ((Player)player).playSound(player.getLocation(), Sound.ITEM_BREAK, 0.7f, 0.2f);
                        e.setCancelled(true);
                        edbee.getDamager().remove();
                        player.removeMetadata("lastArrowDamageEvent", (Plugin)Enchantments.getInstance());
                        return;
                    }
                }
                player.setMetadata("lastArrowDamageEvent", new FixedMetadataValue((Plugin)Enchantments.getInstance(), System.currentTimeMillis()));
            }
        }
    }
}
