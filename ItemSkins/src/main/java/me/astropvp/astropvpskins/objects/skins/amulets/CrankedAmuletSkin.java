package me.astropvp.astropvpskins.objects.skins.amulets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class CrankedAmuletSkin extends ItemSkin {
    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (Math.random() < 0.03) {
            damager.setMetadata("noSoulProc", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            damager.sendMessage(CC.translate("&4&l* " + damaged.getName() + "'s CRANKED AMULET [&7Soul Enchants Silence&4&l] - &76s &4&l*"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damager.removeMetadata("noSoulProc", AstroItemSkinsPlugin.getInstance());
            }, 140L);
        }
    }
}
