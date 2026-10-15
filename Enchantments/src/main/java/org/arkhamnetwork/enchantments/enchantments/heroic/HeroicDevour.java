package org.arkhamnetwork.enchantments.enchantments.heroic;

import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import org.arkhamnetwork.enchantments.util.*;
import org.arkhamnetwork.enchantments.*;
import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.enchantments.axes.*;
import org.bukkit.metadata.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;

public class HeroicDevour extends CustomEnchantment
{
    public HeroicDevour() {
        super("Vampiric Devour", GeneralUtil.axe, 2);
        this.max = 4;
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> {
            this.heroicEnchantment = true;
            this.nonHeroicVersion = EnchantmentAPI.getEnchantment("Devour");
        }, 2L);
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamage() > 0.0 && user instanceof Player && Bleed.getBleedStack(target) > 0) {
            /*if (GeneralUtil.isEffectedByRage(target)) {
                return;
            }*/
            if (SinkOrSwipFlippers.insanityDevourSilenced.contains(user)) {
                return;
            }
            final double mod = Bleed.getBleedStack(target) * 0.015 + enchantLevel * 0.1 + 1.0;
            event.setDamage(event.getDamage() * 1.2);
            target.setMetadata("effectedByDevour", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) System.currentTimeMillis()));
            final Location l = target.getLocation();
            WorldUtil.sendWorldEffect(l.getBlock(), Material.MYCEL.getId(), 2001);
        }

    }
}
