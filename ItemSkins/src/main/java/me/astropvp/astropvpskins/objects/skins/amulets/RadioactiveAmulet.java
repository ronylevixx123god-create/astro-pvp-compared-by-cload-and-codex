package me.astropvp.astropvpskins.objects.skins.amulets;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RadioactiveAmulet extends ItemSkin {
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;

        if (damager instanceof Player) return;
        if (!damaged.getLocation().getWorld().getName().equals("space")) return;
        if (!damaged.getLocation().getWorld().getName().equals("mars")) return;
        if (!AstroSpaceGrindingPlugin.activeMonsters.containsKey(damager.getUniqueId())) return;
        event.setDamage(event.getDamage()*0.50);
    }
}
