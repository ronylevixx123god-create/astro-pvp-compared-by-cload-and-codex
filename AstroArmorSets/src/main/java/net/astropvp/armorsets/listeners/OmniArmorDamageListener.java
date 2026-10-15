package net.astropvp.armorsets.listeners;

import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.cache.ArmorSetCache;
import net.astropvp.armorsets.utils.config.ConfigManager;
import net.astropvp.core.AstroCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OmniArmorDamageListener implements Listener {

    @EventHandler
    public void reduceDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;
        if (e.getDamager().getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (!AstroArmorSetsAPI.isInOmni(player)) return;
        double decrease = AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("omniSetDamageDecrease");
        e.setDamage(e.getDamage() * (1 - (decrease / 100)));
    }


    @EventHandler
    public void increaseDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;

        if (e.getDamager().getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }

        Player player = (Player) e.getDamager();
        if (!AstroArmorSetsAPI.isInOmni(player)) return;
        double increase = AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("omniSetDamageIncrease");
        e.setDamage(e.getDamage() * (1 + (increase / 100)));
    }
}
