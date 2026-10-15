package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class HeroicOverwhelm extends HeroicCustomEnchantment{
    public HeroicOverwhelm() {
        super("Bitter Overwhelm","Overwhelm", GeneralUtil.axe, 2);
        this.max = 4;
        this.base = 10.0;
        this.interval = 8.0;
    }

    public void applyEffect(final LivingEntity user, final LivingEntity target, final int enchantLevel, final EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (GeneralUtil.isUsingAxe(user)) {
            event.setDamage(event.getDamage() * 1.05);
        }


        if (target.hasMetadata("overwhelm_effect")) return;

        if (Math.random() < 0.08) {
            target.setMetadata("overwhelm_effect", new FixedMetadataValue(Enchantments.getInstance(), true));
            target.sendMessage(CC.translate("&6&l* STRUCK BY OVERWHELM EFFECT &7[" + user.getName() + "&7]&6&l *"));
            event.setDamage(event.getDamage()*1.15);

            Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> {
                target.removeMetadata("overwhelm_effect", Enchantments.getInstance());
                target.sendMessage(CC.translate("&6&l* OVERWHELM EFFECT &c&lEXPIRED &6&l*"));
            }, 120L);
        }
    }
}
