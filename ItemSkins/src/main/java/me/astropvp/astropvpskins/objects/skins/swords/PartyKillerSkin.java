package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class PartyKillerSkin extends ItemSkin {


    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (event.isCancelled())
            return;
        damager.setLastDamage(damager.getLastDamage()*1.05);

        if (Math.random()<0.05 && damaged instanceof Player) {
            grapplePlayer((Player) damaged, damager);
        }
    }


    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        damaged.setLastDamage(damaged.getLastDamage()*0.95);
    }

    public void grapplePlayer(Player target, Player damager) {
        final Vector velocity = target.getLocation().toVector().subtract(damager.getLocation().toVector());
        if (velocity.length() != 0.0) {
            velocity.normalize();
        }
        final double distance = target.getLocation().distanceSquared(damager.getLocation());
        final double toMultiply = Math.min(Math.max(1.0, distance / 50.0), 6.0 + 3 * 0.5);
        final double y = velocity.getY();
        velocity.multiply(-toMultiply);
        velocity.setY(y * (-toMultiply / 1.75));
        if (velocity.getY() < 0.01 && velocity.getY() > -0.01) {
            velocity.multiply(new Vector(1.0, 7.5, 1.0));
        }
        target.setVelocity(velocity);
    }
}
