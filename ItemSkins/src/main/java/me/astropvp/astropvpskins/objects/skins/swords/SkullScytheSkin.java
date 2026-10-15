package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class SkullScytheSkin extends ItemSkin {

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (DuelAPI.isSpectator(damager)) return;


        event.setDamage(event.getDamage()*1.025);
        if(Math.random() <= 0.1) {
            damaged.setMetadata("scytheSilence", (MetadataValue)new FixedMetadataValue((Plugin) AstroItemSkinsPlugin.getInstance(), System.currentTimeMillis() + 10000));
        }
    }

    public static boolean isScytheSilenced(Player player) {
        return (player.hasMetadata("scytheSilence") && player.getMetadata("scytheSilence").get(0).asLong() > System.currentTimeMillis());
    }
}
