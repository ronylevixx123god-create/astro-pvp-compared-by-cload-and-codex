package me.astropvp.astropvpskins.objects.skins.amulets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SilentDeathSkin extends ItemSkin {
    HashMap<Player, Integer> silentDeath;

    public SilentDeathSkin() {
        silentDeath = new HashMap<>();
    }


    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        event.setDamage(event.getDamage()*1.03);
        if(!(damaged instanceof Player))
            return;
        if(silentDeath.containsKey(damaged)) {
            silentDeath.put((Player) damaged, silentDeath.get(damaged) + 1);
            event.setDamage(event.getDamage()*1.2);
        }
        if(Math.random() <= 0.05) {
            if(AstroItemSkinsAPI.hasSkinEquipped((Player) damaged, SkinType.HALLOWEEN_AMULET))
                return;
            silentDeath.put((Player) damaged, 0);

            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                if(silentDeath.containsKey(damaged))
                    silentDeath.remove(damaged);
                }, 300L);
            if(silentDeath.get(damaged) > 4) {
                silentDeath.remove(damaged);
            }
        }
    }


}
