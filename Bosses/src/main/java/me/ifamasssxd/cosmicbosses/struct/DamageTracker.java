package me.ifamasssxd.cosmicbosses.struct;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class DamageTracker extends HashMap<UUID, Integer> {
    private static final long serialVersionUID = 1L;

    public void trackDamage(Entity e, final EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (e instanceof Projectile) {
            final Projectile proj = (Projectile) e;
            if (proj.getShooter() instanceof Player) {
                e = (Entity) proj.getShooter();
            }
        }
        if (!(e instanceof Player)) {
            return;
        }
        int damage = this.containsKey(e.getUniqueId()) ? (this).get(e.getUniqueId()) : 0;
        damage += (int) event.getFinalDamage();
        this.put(e.getUniqueId(), damage);
    }

    public void trackDamage(Entity e, final int newDamage) {
        if (e instanceof Projectile) {
            final Projectile proj = (Projectile) e;
            if (proj.getShooter() instanceof Player) {
                e = (Entity) proj.getShooter();
            }
        }
        if (!(e instanceof Player)) {
            return;
        }
        int damage = this.containsKey(e.getUniqueId()) ? (this).get(e.getUniqueId()) : 0;
        damage += newDamage;
        this.put(e.getUniqueId(), damage);
    }
}
