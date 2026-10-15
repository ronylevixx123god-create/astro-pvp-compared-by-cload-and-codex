package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.pets.InventoryWorldDestroyerPet;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.axes.Rogue;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicTrap;
import org.arkhamnetwork.enchantments.enchantments.swords.Silence;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PricklePear extends ItemSkin {
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation()))
            return;

        if (damaged instanceof Player) {
            event.setDamage(event.getDamage() * 1.04);
            (new Silence()).applyEffect((LivingEntity) damager, (LivingEntity) damaged, 5, event);

            if (Math.random() < 0.075) {
                prickleAbility(damager, (Player) damaged);
            }

        }
    }

    public void prickleAbility(Player player, Player enemy) {
        player.sendMessage(CC.translate("&a&l** PRICKLE CHAOS &7[" + enemy.getName() + "] &a&l**"));
        enemy.sendMessage(CC.translate("&a&l** PRICKLED &7[" + player.getName() + "] &a&l**"));

        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) AstroItemSkinsPlugin.getInstance(), (Runnable) new BukkitRunnable() {
            int count = 0;
            public void run() {
                if (this.count >= 5) {
                    this.cancel();
                }
                if (!enemy.isDead() && WorldGuardUtils.isPvPEnabled(enemy.getLocation())) {
                    final int dmg = 1;
                    AstroDamageHandler.getInstance().getDamageHandler().dealDamage(enemy, dmg, "&a&lPickle Ticks");
                }
                if (enemy.getHealth() <= 0.0 || enemy.isDead()) {
                    this.cancel();
                }
                if (!WorldGuardUtils.isPvPEnabled(enemy.getLocation())) {
                    this.cancel();
                }
                ++this.count;
            }
        }.runTaskTimer((Plugin) AstroItemSkinsPlugin.getInstance(), 20L, 20L));
    }


}
