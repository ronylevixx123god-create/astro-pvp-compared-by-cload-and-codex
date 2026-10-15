package me.astropvp.astropvpskins.objects.skins.helmets;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class DaftHelmetSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (damager instanceof Player && Math.random() < 0.02) {
            damager.setMetadata("noSoulProc", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
            damager.setMetadata("soulSilenced", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));

            damager.sendMessage(CC.translate("&6&l* " + damaged.getName() + "'s DAFT HELMET &7[5s Soul Enchants Silence] &6&l*"));

            Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), ()-> {
                damager.removeMetadata("noSoulProc", CosmicSkyInventoryPets.get());
                damager.removeMetadata("soulSilenced", CosmicSkyInventoryPets.get());

            },5*20L);
        }

        if (damager instanceof Player && Math.random() < 0.02) {
            damaged.setMetadata("reflectDivineTicks", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
            damaged.sendMessage(CC.translate("&6&l* DAFT HELMET &7[5s Divine Reflection] &6&l*"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), ()-> {
                damaged.removeMetadata("reflectDivineTicks", CosmicSkyInventoryPets.get());

            },5*20L);
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {

    }
}