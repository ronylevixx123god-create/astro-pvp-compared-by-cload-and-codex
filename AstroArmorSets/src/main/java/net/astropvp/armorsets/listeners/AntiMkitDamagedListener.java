package net.astropvp.armorsets.listeners;

import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.antiMkit.AntiMkit;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class AntiMkitDamagedListener implements Listener {



    @EventHandler
    public void onEnchantProc(CustomEnchantProcOnEntityEvent event) {
        if (event.getEnchant() != null && event.getTarget() instanceof Player) {
            String name = event.getEnchant().name();
            if (!name.equals("Silence") && !name.equals("IceAspect")) {
                Player target;
                Map<AntiMkit, Integer> equipped;
                String armorSetFrom;
                if (event.getEnchant() instanceof MasteryCustomEnchantment && event.getTarget() instanceof Player && (armorSetFrom = ((MasteryCustomEnchantment) event.getEnchant()).getArmorSet()) != null && !armorSetFrom.isEmpty() && (equipped = AstroArmorSetsAPI.getAntiMkitCrystalsEquipped(target = (Player) event.getTarget())) != null && equipped.containsKey(armorSetFrom)) {
                    if (target.hasMetadata("revoked")) {
                        return;
                    }

                    String customName = event.getPlayer() instanceof Player ? event.getPlayer().getName() : event.getPlayer().getCustomName();
                    event.setCancelled(true);
                }
            }
        }
    }



}

