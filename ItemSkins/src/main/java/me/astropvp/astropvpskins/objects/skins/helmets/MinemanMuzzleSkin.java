package me.astropvp.astropvpskins.objects.skins.helmets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import io.netty.util.internal.ThreadLocalRandom;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class MinemanMuzzleSkin extends ItemSkin {
    //TODO: 3.5% chance to negate cosmic cape for 10s
    //TODO: Banshee RKO

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;
        if(Math.random() < 0.075) {
            final List<Entity> nearby = damaged.getNearbyEntities(6, 6, 6);
            if (nearby == null) return;
            if (nearby.isEmpty()) return;
            for (int i = 0; i < nearby.size(); i++) {
                Entity entity = nearby.get(i);
                if (entity == null) continue;
                if(!(entity instanceof Player)) {
                    nearby.remove(entity);
                }
            }

            double dmg = ThreadLocalRandom.current().nextInt(3, 6 + 1);
            nearby.forEach(player -> {
                if (player instanceof Player) {
                    if (FactionUtils.isInEnemyFaction(damaged, (Player) player)) {
                        ((Player) player).damage(dmg);

                        player.sendMessage(CC.translate("&8&l** " + damaged.getName() + "'s Mineman Muzzle &7[" + dmg + "DMG] &8&l**"));
                    }
                }
            });
        }

        if (Math.random() < 0.035 && !damager.hasMetadata("noCape")) {
            damager.setMetadata("noCape", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                damager.removeMetadata("noCape", AstroItemSkinsPlugin.getInstance());
            }, 10*20);
        }
    }


    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damaged.hasMetadata("frozenSkrrt")) return;
        if (damaged instanceof Player) {
            if (damaged.hasMetadata("muzzle")) {
                event.setDamage(event.getDamage() * 1.33);
            }

        }

    }

    }
