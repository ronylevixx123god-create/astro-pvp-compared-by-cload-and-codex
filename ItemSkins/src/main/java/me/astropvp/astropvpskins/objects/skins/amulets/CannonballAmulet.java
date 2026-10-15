package me.astropvp.astropvpskins.objects.skins.amulets;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CannonballAmulet extends ItemSkin {

    public List<UUID> cooldown;
    public static List<UUID> active;
    public static List<Player> affected;


    public CannonballAmulet() {
        cooldown = new ArrayList<>();
        active = new ArrayList<>();
        affected = new ArrayList<>();
    }

    public void onSneak(Player player, PlayerToggleSneakEvent event) {
        if (cooldown.contains(player.getUniqueId())) {
            return;
        }

        if (event.isCancelled()) return;

        if (!CombatLog.inCombat(player)) {
            return;
        }

        active.add(player.getUniqueId());
        cooldown.add(player.getUniqueId());
        player.sendMessage(CC.translate("&e&l* CANNONBALL ABILITY &7[3s] &e&l*"));
        for (Entity entity : player.getNearbyEntities(8, 8, 8)) {
            if (entity instanceof Player) {
                affected.add((Player) entity);
                entity.sendMessage(CC.translate("&e&l* " + player.getName()+ "'s CANNONBALL ABILITY &7[3s] &e&l*"));

                Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                    affected.remove(entity);
                },3*20);
            }
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
            active.remove(player.getUniqueId());
            player.sendMessage(CC.translate("&e&l* CANNONBALL ABILITY &cExpired &e&l*"));

        },3*20);
        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
            cooldown.remove(player.getUniqueId());
            player.sendMessage(CC.translate("&e&l* CANNONBALL ABILITY COOLDOWN &7Expired &e&l*"));
        },300*20);
    }
}