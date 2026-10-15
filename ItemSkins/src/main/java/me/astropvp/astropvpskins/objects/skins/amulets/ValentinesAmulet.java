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

import java.util.ArrayList;
import java.util.List;

public class ValentinesAmulet extends ItemSkin {
    private List<Player> bloodyAmuletDamage = new ArrayList<>();

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (this.bloodyAmuletDamage.contains(damaged)) {
            event.setDamage(event.getDamage()*0.9);
            return;
        }

        if (Math.random() < 0.025) {
            this.bloodyAmuletDamage.add(damaged);
            damaged.sendMessage(CC.translate("&c&l* DULLED STRIKE &7[8s] &c&l*"));
            damager.sendMessage(CC.translate("&c&l* " + damaged.getName() + "'s DULLED STRIKE &7[8s] &c&l*"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                this.bloodyAmuletDamage.remove(damaged);
            },8*20);
        }
    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {

        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (!(damaged instanceof Player))
            return;

        if (this.bloodyAmuletDamage.contains(damaged)) {
            event.setDamage(event.getDamage()*1.25);
        }
    }

}
