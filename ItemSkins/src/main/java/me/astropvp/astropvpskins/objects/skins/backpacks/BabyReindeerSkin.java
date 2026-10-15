package me.astropvp.astropvpskins.objects.skins.backpacks;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
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

import java.util.ArrayList;
import java.util.List;

public class BabyReindeerSkin extends ItemSkin {
    private final List<Player> cooldowns = new ArrayList<>();

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;

        if (damaged.getHealth() - event.getFinalDamage() <= 20 && (!cooldowns.contains(damaged))) {
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 8, 4));
            cooldowns.add(damaged);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                cooldowns.remove(damaged);
            }, 600L);
        }
        if (damager instanceof Player && damaged instanceof Player) {
            List<Entity> nearby = damaged.getNearbyEntities(8, 8, 8);
            nearby.removeIf(entity -> !(entity instanceof Player));
            nearby.removeIf(entity -> !GeneralUtil.isAtleastTruce(damaged, (Player) damager));
            if (nearby.size() == 0 && damaged.getHealth() <= 6) {
                event.setDamage(event.getDamage()*0.75);
            } else if (Math.random() > 0.5) {
                if (event.getFinalDamage() - damaged.getHealth() < 6) {
                    if (nearby.size() == 0)
                        return;
                    Player target = (Player) nearby.get(0);
                    if (FactionUtils.isInEnemyFaction((Player) damager, target)) {
                        target.damage(event.getFinalDamage());
                    }
                    //event.setDamage(0);
                }
            }
        }
    }
}
