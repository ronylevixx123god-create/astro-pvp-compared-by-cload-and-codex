package org.arkhamnetwork.enchantments.enchantments.swords;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class SilenceListener implements Listener {
    @EventHandler(
            priority = EventPriority.NORMAL,
            ignoreCancelled = true
    )
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getDamager().hasMetadata("banshee_active")) return;
        if (e.getDamager().hasMetadata("mutated_banshee")) return;
        if (e.getEntity().hasMetadata("neutralized")) {
            e.setDamage(e.getDamage() * 1.05D);
            return;
        }

        if (e.getDamage() > 0.0D && e.getEntity() instanceof Player && e.getEntity().hasMetadata("noDefenseProcs") && ((Player) e.getEntity()).getKiller() != null) {
            //int stacks = ((MetadataValue)e.getEntity().getMetadata("noDefenseProcs").get(0)).asInt();
            //double extraStackDmg = 0.07 * (double)stacks;
            if (AstroItemSkinsAPI.hasSkinEquipped(((Player) e.getEntity()).getKiller(), SkinType.BACK_TO_SCHOOL_2021_HELMET) || AstroItemSkinsAPI.hasSkinEquipped(((Player) e.getEntity()).getKiller(), SkinType.SPRING_BREAK_2022_MUTATED_BELT)) {
                e.setDamage(e.getDamage() * (1.15D));
            } else {
                e.setDamage(e.getDamage() * (1.1));

            }

           /* for(int i = 0; i < stacks; ++i) {

                try {
                    de.inventivegames.particle.ParticleEffect.CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), e.getEntity().getLocation().clone().add(0.0D, 1.0D, 0.0D), 1.0f, 1.0f, 1.0f, 0.1f, 15);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }*/
        }

    }
}