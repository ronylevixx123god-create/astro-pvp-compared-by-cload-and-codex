package me.astropvp.astropvpskins.objects.skins.helmets;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class LampShadeSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (damager instanceof Player && Math.random() < 0.03) {
            damager.setMetadata("noSoulProc", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
            damager.sendMessage(CC.translate("&2&l * " + damaged.getName() + "'s LAMP SHADE &7[10 Seconds no Soul Enchants] &2&l*"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), ()-> {
                damager.removeMetadata("noSoulProc", CosmicSkyInventoryPets.get());
            },10*20L);
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {

    }
}