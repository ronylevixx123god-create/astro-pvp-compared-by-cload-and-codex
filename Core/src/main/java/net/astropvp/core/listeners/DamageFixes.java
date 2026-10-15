package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import net.astropvp.core.cooldown.ElfMaskCooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageFixes implements Listener {


/*    @EventHandler(priority = EventPriority.LOWEST)
    public void onHitP6(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player phurt = (Player) e.getEntity();

            e.setDamage(e.getDamage() * (1 - getProtectionReduction(phurt)));
        }
    }*/

    @EventHandler()
    public void elfMaskReHeal(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player phurt = (Player) e.getEntity();
            if (!(phurt.getHealth() <= 10)) return;
            if (CooldownUtils.isCooldownActive(phurt, "elfCooldown")) return;
            if (MaskUtils.hasMaskEquipped(phurt, "ELF") && Math.random() <= 0.15) {
                e.getEntity().sendMessage(CC.translate("&a&l * ELF MASK HEAL ABILITY &a&l*"));
                e.getDamager().sendMessage(CC.translate("&c&l * ELF MASK HEAL ABILITY &8[&4" + e.getEntity().getName() + "&8]&c&l*"));
                e.setCancelled(true);
                ((Player) e.getEntity()).setHealth(((Player) e.getEntity()).getMaxHealth() / 2);
                CooldownUtils.registerCooldown(phurt, new ElfMaskCooldown("elfCooldown", 300));
            }
        }
        
    }

}
