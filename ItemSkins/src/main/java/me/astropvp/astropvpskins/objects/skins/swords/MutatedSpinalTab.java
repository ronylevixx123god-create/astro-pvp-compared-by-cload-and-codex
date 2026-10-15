package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.axes.Rogue;
import org.arkhamnetwork.enchantments.mastery.MasteryBlackout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class MutatedSpinalTab extends ItemSkin {
    private List<String> playersStruck = new ArrayList<>();

    private List<String> witchSilencedPlayers = new ArrayList<>();


    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (!(damaged instanceof Player)) return;
        if(Math.random() <= 0.02) {
            (new Rogue()).applyEffect((LivingEntity)damager, (LivingEntity)damaged, 2, event);
        }

        if (!playersStruck.contains(damaged.getName()) && playersStruck.size() < 5) {
            damager.sendMessage(CC.translate("&6&l+ &6" + damaged.getName() + " &6&l[&7Spinal Tap&6&l]"));
            playersStruck.add(damaged.getName());
        }

        if (playersStruck.size() >= 5) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                playersStruck.clear();
            },4*20);
        }

        if (playersStruck.size() == 5) {
            (new MasteryBlackout()).applyEffect((LivingEntity)damager, (LivingEntity)damaged, 2, event);
        }

        if (!playersStruck.isEmpty()) {
            event.setDamage(event.getDamage() * (1+(playersStruck.size()*5/100)));
        }



        if (Math.random() < 0.04) {
            witchSilencedPlayers.add(damaged.getName());
            damaged.sendMessage(CC.translate("&e&l* WITCH HAT SILENCED [&7" + damager.getName() + "'s Spinal Tab&e&l] *"));
            damager.sendMessage(CC.translate("&e&l* WITCH HAT SILENCE [&7" + damaged.getName() + "&e&l] *"));

            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                witchSilencedPlayers.remove(damaged.getName());
                damaged.sendMessage(CC.translate("&e&l* WITCH HAT SILENCE Expired! *"));
            },10*20);
        }
    }
}




