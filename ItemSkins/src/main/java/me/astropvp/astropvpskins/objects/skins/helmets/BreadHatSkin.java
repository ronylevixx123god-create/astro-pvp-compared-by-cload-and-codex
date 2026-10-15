package me.astropvp.astropvpskins.objects.skins.helmets;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BreadHatSkin extends ItemSkin {
    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if(!(damager instanceof Player))
            return;
        if(Math.random() < 0.015) {
            double damagerHealth = ((Player) damager).getHealth();
            double reducedHealth = damagerHealth - (Math.floor(damagerHealth / 10));
            ((Player) damager).setHealth(reducedHealth);
            damager.sendMessage(CC.translate("&8&l** " + damaged.getName() + "'s Bread Hat &7[" + Math.floor(damagerHealth / 10) + "DMG] &8&l**"));
        }
        if(Math.random() < 0.01) {
            damager.setMetadata("noSoulProc", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damager.removeMetadata("noSoulProc", AstroItemSkinsPlugin.getInstance());
            }, 140L);
        }
    }
}
