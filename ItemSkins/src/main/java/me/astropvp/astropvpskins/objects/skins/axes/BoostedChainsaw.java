package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import lombok.Getter;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.enchantments.swords.DoubleStrike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class BoostedChainsaw extends ItemSkin {
    @Getter
    private final List<String> chainSawedPlayers = new ArrayList<>();

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        new DoubleStrike().applyEffect(damager, (LivingEntity) damaged, 4, event);

        if (damaged instanceof Player) {
            if (Math.random() < 0.025) {
                chainSawedPlayers.add(damaged.getName());
                damaged.sendMessage(CC.translate("&8&l* CHAINSAW SILENCED [&78s&8&l] *"));
                Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                    chainSawedPlayers.remove(damaged.getName());
                },8*20);
            }

            //code kronos 8 ability
        }
    }
}



