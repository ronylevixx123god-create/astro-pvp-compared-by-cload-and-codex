package net.astropvp.armorsets.listeners;

import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class RangerSetListener implements Listener {

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        Projectile proj = (Projectile)event.getDamager();
        if (!(proj.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) proj.getShooter();

        if (AstroArmorSetsAPI.getCurrentArmorSet(shooter) != ArmorSetType.RANGER) return;

        event.setDamage(event.getDamage() * 1.30);
    }

    @EventHandler
    public void onShot(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && AstroArmorSetsAPI.getCurrentArmorSet((Player) event.getEntity()) == ArmorSetType.RANGER) {
            event.setDamage(event.getDamage() * 0.65);
        }
    }
}
