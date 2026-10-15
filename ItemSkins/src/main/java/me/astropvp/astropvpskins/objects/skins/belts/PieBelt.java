package me.astropvp.astropvpskins.objects.skins.belts;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class PieBelt extends ItemSkin {
    private HashMap<Player, Integer> hypnosis;

    public PieBelt() {
        hypnosis = new HashMap<>();
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if(Math.random() <= 0.05) {
            if(hypnosis.containsKey(damaged)) {
                hypnosis.put(damaged, hypnosis.get(damaged) + 1);
                Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> { hypnosis.put(damaged, hypnosis.get(damaged) - 1);}, 400L);
            } else {
                hypnosis.put(damaged, 1);
                Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> { hypnosis.put(damaged, hypnosis.get(damaged) - 1);}, 400L);
            }
        }
        if(hypnosis.get(damaged) == 10) {
            damaged.setHealth(damaged.getHealth() + (damaged.getHealth() /3));
        }
    }

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if(hypnosis.containsKey(damager)) {
            event.setDamage(event.getDamage() * (1+hypnosis.get(damager)/100));

        }
    }
}
