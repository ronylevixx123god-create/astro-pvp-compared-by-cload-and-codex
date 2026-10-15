package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Snowflake extends ItemSkin {
    private static List<Player> snowflakeCooldown;

    private HashMap<Player, Integer> stacks;

    public Snowflake() {
        snowflakeCooldown = new ArrayList<>();
        stacks = new HashMap<>();
    }

    public static List<Player> getSnowflakeCooldown() { return snowflakeCooldown; }

    public static void addSnowflakeCooldown(Player player) {
        snowflakeCooldown.add(player);
        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {snowflakeCooldown.remove(player);}, 160L);
    }

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if(!(damaged instanceof Player))
            return;
        if(stacks.containsKey(damager)) {
            stacks.put(damager, stacks.get(damager) + 1);
        } else {
            stacks.put(damager, 1);
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {stacks.put(damager, stacks.get(damager) - 1);}, 200L);
        if(Math.random() <= 0.04) {
            ((Player) damaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
            event.setDamage(event.getDamage()*1.05);
            damager.sendMessage(CC.AquaB + "** DEEP FREEZE " + CC.Gray + "[" + damaged.getName() + "]" + CC.AquaB + " **");
        }
        if(Math.random() <= 0.33) {
            List<Entity> entityList = damager.getNearbyEntities(5, 5, 5);
            entityList.removeIf(entity -> !(entity instanceof Player));
            for(Entity player : entityList) {
                if(GeneralUtil.isAtleastTruce((Player) player, damager))
                    return;
                grapplePlayer((Player) player, damager);
            }
        }
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(stacks.containsKey(damaged) && stacks.get(damaged) > 0) {
            event.setDamage(event.getDamage()*(1-stacks.get(damaged)/100));

        }
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
