package me.astropvp.astropvpskins.listeners;

import me.astropvp.astropvpskins.objects.skins.swords.WetNoodleSword;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WetNoodleMutatedCrystal implements Listener {
    @EventHandler
    public void extraDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (WetNoodleSword.isWetNoodled((Player) event.getEntity())) {
                if (AstroArmorSetsAPI.hasMutatedCrystalEquipped((Player) event.getEntity())) {
                    event.setDamage(event.getDamage()*1.075);
                }
            }
        }
    }
}
