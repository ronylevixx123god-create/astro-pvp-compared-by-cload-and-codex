package org.arkhamnetwork.enchantments.mastery;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DivineLight extends MasteryCustomEnchantment {
    public DivineLight() {
        super("Divine Light", GeneralUtil.axe, 10);
        this.max = 5;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {

        if (event.isCancelled()) return;
        if (target instanceof Player && user instanceof Player) {


            if (GeneralUtil.isUsingAxe(user)) {
                event.setDamage(event.getDamage() * 1.15);
            }
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.EASTER_2022_AMULET) || AstroItemSkinsAPI.hasSkinEquipped((Player) target, SkinType.VALENTINES_2022_MUTATED_AMULET)) {
                return;
            }
            if (user.hasMetadata("blackout") && Math.random() < 0.5) {
                user.removeMetadata("blackout", Enchantments.getInstance());
                if (target.hasMetadata("blackout")) return;
                target.setMetadata("blackout", new FixedMetadataValue(Enchantments.getInstance(), true));
                Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> {
                            target.removeMetadata("blackout", Enchantments.getInstance());
                        }, 120L
                );
            }

        }
    }
}
