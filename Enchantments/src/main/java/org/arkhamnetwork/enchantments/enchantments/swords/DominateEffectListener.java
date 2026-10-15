package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

final class DominateEffectListener
        implements Listener {
    DominateEffectListener() {
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onEntityDamageByEntiy(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata(Dominate.dominateEffectLevel_metadata)) {
            long expireTick = e.getDamager().getMetadata(Dominate.dominateEffectExpire_metadata).get(0).asLong();
            if ((long)MinecraftServerUtils.getCurrentServerTick() < expireTick) {
                int dominateLevel = e.getDamager().getMetadata(Dominate.dominateEffectLevel_metadata).get(0).asInt();
                double mod = 1.0 - (double)(dominateLevel * 2 / 100);
                e.setDamage(e.getDamage() * mod);
            } else {
                e.getDamager().removeMetadata(Dominate.dominateEffectExpire_metadata, Enchantments.getInstance());
                e.getDamager().removeMetadata(Dominate.dominateEffectLevel_metadata, Enchantments.getInstance());
            }
        }
    }
}
