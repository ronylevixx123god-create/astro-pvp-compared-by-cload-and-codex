package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MasteryRevoke extends MasteryCustomEnchantment {
    public MasteryRevoke() {
        super("Revoke", GeneralUtil.swords, 10);
        this.max = 3;
        this.base = 20.0D;
        this.interval = 5.0D;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double random = Math.random();
        double chance = 0.001D * enchantLevel;
        long time = (long) (enchantLevel * 1.5D);
        if (target.hasMetadata("revoked"))
            return;
        if (random < chance) {
            if (user.hasMetadata("RevokeCooldown") && System.currentTimeMillis() - user.getMetadata("RevokeCooldown").get(0).asLong() <= 60000L)
                return;
            user.setMetadata("RevokeCooldown", new FixedMetadataValue(Enchantments.getInstance(), Long.valueOf(System.currentTimeMillis())));
            target.setMetadata("revoked", new FixedMetadataValue(Enchantments.getInstance(), user.getName()));
            user.sendMessage(CC.translate("&4&l** &cRevoked &7" + target.getName() + " &c[&75s&c] &4&l**"));
            target.sendMessage(CC.translate("&4&l** &cRevoked by &7" + user.getName() + "&c[&75s&c] &4&l**"));
            Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), () -> target.removeMetadata("revoked", Enchantments.getInstance()), 20L * time);
        }
    }
}

