package me.ifamasssxd.cosmicduel.listeners;

import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitsListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            if (DuelAPI.hasDuel(damager) && DuelAPI.hasDuel(damaged)) {
                NewDuel duel = DuelAPI.getDuel(damager);
                assert duel != null;

                if (duel.getDamageMap().containsKey(damager.getUniqueId())) {
                    duel.getDamageMap().put(damager.getUniqueId(), duel.getDamageMap().get(damager.getUniqueId())+event.getFinalDamage());
                } else {
                    duel.getDamageMap().put(damager.getUniqueId(), event.getFinalDamage());
                }

                if (duel.getHitsMap().containsKey(damager.getUniqueId())) {
                    duel.getHitsMap().put(damager.getUniqueId(), duel.getHitsMap().get(damager.getUniqueId())+1);
                } else {
                    duel.getHitsMap().put(damager.getUniqueId(), 1);
                }
                duel.getCurrentComboMap().put(damaged.getUniqueId(), 0);

                if (duel.getCurrentComboMap().containsKey(damager.getUniqueId())) {
                    duel.getCurrentComboMap().put(damager.getUniqueId(), duel.getCurrentComboMap().get(damager.getUniqueId()) + 1);
                    int longestCombo = duel.getLongestComboMap().getOrDefault(damager.getUniqueId(), 0);
                    if (longestCombo < duel.getCurrentComboMap().get(damager.getUniqueId())) {
                        duel.getLongestComboMap().put(damager.getUniqueId(),duel.getCurrentComboMap().get(damager.getUniqueId()));
                    }
                } else {
                    duel.getCurrentComboMap().put(damager.getUniqueId(), 1);
                }
            }
        }
    }
}
