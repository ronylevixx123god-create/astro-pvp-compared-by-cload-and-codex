package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.*;
import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

public class Devour extends CustomEnchantment
{
    public Devour() {
        super("Devour", GeneralUtil.axe, 2);
        this.max = 4;
        this.base = 10.0;
        this.interval = 8.0;
    }

    public void applyEffect(final LivingEntity user, final LivingEntity target, final int enchantLevel, final EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamage() > 0.0 && user instanceof Player && Bleed.getBleedStack(target) > 0) {
            /*if (GeneralUtil.isEffectedByRage(target)) {
                return;
            }*/

                if (SinkOrSwipFlippers.insanityDevourSilenced.contains(user)) {
                    return;
                }

            double mod = (double)Bleed.getBleedStack(target) * 0.01D + ((double)enchantLevel * 0.1D + 1.0D)/2;
            event.setDamage(event.getDamage() * 1.1);
            target.setMetadata("effectedByDevour", new FixedMetadataValue((Plugin)Enchantments.getInstance(), System.currentTimeMillis()));
            final Location l = target.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), Material.MYCEL.getId(), 2001);
        }
    }
}
