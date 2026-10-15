package me.astropvp.astropvpskins.objects.skins.axes;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import de.inventivegames.particle.ParticleEffect;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicTrap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MutatedWhiskTaker extends ItemSkin {

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (!(damager instanceof Player)) return;
        if (!(damaged instanceof Player)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;
        if (damager.hasMetadata("whiskDamage")) {
            event.setDamage(event.getDamage() * 1.2);
        }

        event.setDamage(event.getDamage() * 1.055);

        if (Math.random() < 0.03) {
            try {
                whiskAbility(damager, (Player) damaged);
                damager.sendMessage(CC.translate("&c&l* WHISK TAKER [&76s&c&l] *"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (Math.random() < 0.04) {
            damaged.setMetadata("noDivine", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), ()-> {
                damaged.removeMetadata("noDivine", AstroItemSkinsPlugin.getInstance());
                damaged.sendMessage(CC.translate("&c&l* WHISK TAKER &7Expired &c&l*"));
            }, 20 * 8);
        }
    }
    public void whiskAbility(Player attacker, Player attacked) throws Exception {
        for (Entity p : attacker.getNearbyEntities(5, 5 , 5)) {
            if (p instanceof Player) {
                ((Player) p).damage(10);
                ParticleEffect.EXPLOSION_LARGE.sendToPlayer((Player) p, p.getLocation(),0, 1, 0, 0.5f, 55);
            }
            attacker.setMetadata("whiskDamage", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), true));
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), ()-> {
                attacker.removeMetadata("whiskDamage", AstroItemSkinsPlugin.getInstance());
                attacker.sendMessage(CC.translate("&c&l* WHISK TAKER &7Expired &c&l*"));

            }, 20 * 6);
        }

    }
}
