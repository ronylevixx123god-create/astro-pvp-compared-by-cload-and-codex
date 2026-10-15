package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.CacheUtils;
import org.arkhamnetwork.enchantments.util.RageStacksCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

final class RageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player attacked = (Player) event.getEntity();

            RageStacksCache victimRageStack = CacheUtils.getRageStackCache(attacked);
            if (victimRageStack.isSameVictim(attacker)) {
                if (victimRageStack.getRageStack() > 0) {

                    victimRageStack.resetRageStack();
                }
            }
            if (victimRageStack.getRageStack() != 0) {
                if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.SPRING_2021_BELT) && Math.random() < 0.1) {
                    victimRageStack.decrementRageStack();
                    attacked.sendMessage(CC.translate("&a&l* PARTY BELT &7[-1 Rage Stack] &a&l*"));
                }
            }


        }
    }
}
