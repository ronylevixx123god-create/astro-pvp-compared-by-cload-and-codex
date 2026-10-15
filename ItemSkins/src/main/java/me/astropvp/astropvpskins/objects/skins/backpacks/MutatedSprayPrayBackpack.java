package me.astropvp.astropvpskins.objects.skins.backpacks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class MutatedSprayPrayBackpack extends ItemSkin {

    public static List<Player> sprayed;
    public List<Player> sprayer;


    public MutatedSprayPrayBackpack() {
        sprayed = new ArrayList<>();
        sprayer = new ArrayList<>();
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (!(damaged instanceof Player)) return;
        if (sprayed.contains(damaged)) {
            event.setDamage(event.getDamage()*1.06);
        }
        if (Math.random() < 0.02) {
            damager.sendMessage(CC.translate("&a&l*&e&l* &a&lSPRAY AND PRAY ABILITY &7[10s - " + damaged.getName() + "] &e&l*&a&l*"));
            damaged.sendMessage(CC.translate("&a&l*&e&l* &a&l" + damager.getName() + "'s SPRAY AND PRAY ABILITY &7[10s] &e&l*&a&l*"));
            sprayed.add((Player) damaged);
            sprayer.add(damager);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                sprayed.remove((damaged));
                sprayer.remove(damager);
            }, 10*20);

        }
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (event.isCancelled()) return;


        if (sprayer.contains(damaged)) {
            event.setDamage(event.getDamage()*0.94);
        }
    }
}
