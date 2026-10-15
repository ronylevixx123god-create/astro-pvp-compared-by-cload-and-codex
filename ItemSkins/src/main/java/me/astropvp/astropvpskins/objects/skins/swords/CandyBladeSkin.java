package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.arkhamnetwork.enchantments.mastery.MasteryLifesteal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class CandyBladeSkin extends ItemSkin {
    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (DuelAPI.isSpectator(damager)) return;

        if(!(damaged instanceof Player)) {
            return;
        }

        if (Math.random() < 0.1) {
            damager.setMetadata("candyBladeAbility", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), ()-> {
                damager.removeMetadata("candyBladeAbility", AstroItemSkinsPlugin.getInstance());
            },60L );
        }


        if (damager.hasMetadata("candyBladeAbility") && Math.random() < 0.15) {
            damager.sendMessage(CC.translate("&c&l* SUGAR RUSH&7 [+20% DMG] &c&l*"));
            event.setDamage(event.getDamage()*1.2);
        }



    }
}
