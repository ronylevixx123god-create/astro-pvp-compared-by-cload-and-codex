package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import lombok.Getter;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Carver extends ItemSkin {
    private final List<String> rogueSilenced = new ArrayList<>();

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;

        if (Math.random() < 0.04) {
            if (!rogueSilenced.contains(damaged.getName())) {
                rogueSilenced.add(damaged.getName());
                damaged.sendMessage(CC.translate("&e&l* ROGUE SILENCED [&7" + damager.getName() + "'s Carver &e&l] *"));
                Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                    rogueSilenced.remove(damaged.getName());
                },8*20);
            }
        }



    }

    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {

    }
}
