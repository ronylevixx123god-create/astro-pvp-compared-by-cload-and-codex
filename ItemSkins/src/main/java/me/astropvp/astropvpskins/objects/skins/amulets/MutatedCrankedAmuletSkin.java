package me.astropvp.astropvpskins.objects.skins.amulets;

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

public class MutatedCrankedAmuletSkin extends ItemSkin {
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
        if (Math.random() < 0.035) {
            if (damaged.hasMetadata("mageCD")) return;
            damaged.sendMessage(CC.translate("&2&l* MAGE THIEF ABILITY [&7Mutated Cranked Amulet&2&l] *"));
            damaged.setMetadata("mageCD", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            damaged.setMetadata("mageDamage", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            for (Entity p : damaged.getNearbyEntities(7, 7, 7)) {
                if (p instanceof Player) {
                    if (p.hasMetadata("noSoulProc")) continue;
                    p.setMetadata("noSoulProc", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
                    p.sendMessage(CC.translate("&2&l* " + damaged.getName() + "'s MAGE THIEF [&710s &2&l] *"));
                    Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                        p.removeMetadata("noSoulProc", AstroItemSkinsPlugin.getInstance());
                    }, 10 * 20L);
                }
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damaged.removeMetadata("mageCD", AstroItemSkinsPlugin.getInstance());
            }, 120 * 20L);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damaged.removeMetadata("mageDamage", AstroItemSkinsPlugin.getInstance());
            }, 10 * 20L);
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damager.hasMetadata("mageDamage")) {
            event.setDamage(event.getDamage()*1.2);
        }
    }

}
