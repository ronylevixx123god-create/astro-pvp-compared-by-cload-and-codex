package me.astropvp.astropvpskins.objects.skins.helmets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MutatedSSFHelmet extends ItemSkin {


    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;

        if (Math.random() < 0.04) {
            damaged.setMetadata("reflectedEnglighted", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(),  true));
            damaged.sendMessage(CC.translate("&d&l** " + damager.getName() + "'s SACRILEGE ABILITY &7[8s] &d&l**"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damaged.removeMetadata("reflectedEnglighted", AstroItemSkinsPlugin.getInstance());
            },8*20);
        }
    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;

        if (Math.random() < 0.025) {
            damaged.setMetadata("reflectDivineTicks", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(),  true));
            damaged.sendMessage(CC.translate("&2&l** SACRIFICE ABILITY &7[8s] &2&l**"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damaged.removeMetadata("reflectDivineTicks", AstroItemSkinsPlugin.getInstance());
            },8*20);
        }
    }
}
