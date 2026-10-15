package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.arkhamnetwork.Arkkit.*;
import org.bukkit.metadata.*;
import org.bukkit.plugin.*;

public class ThunderingBlow extends CustomEnchantment
{
    public ThunderingBlow() {
        super("Thundering Blow", GeneralUtil.swords, 2);
        this.max = 3;
        this.base = 25.0;
        this.interval = 0.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        final double random = Math.random();
        final double chance = (level == 3) ? 0.05 : (0.0175 * level);
        if (random < chance) {
            if (playerHit.hasMetadata("lastThunderingBlowProc") && System.currentTimeMillis() - playerHit.getMetadata("lastThunderingBlowProc").get(0).asLong() <= 2500L) {
                return;
            }
            playerHit.setMetadata("lastThunderingBlowProc", new FixedMetadataValue((Plugin)Arkkit.get(), System.currentTimeMillis()));
            playerHit.getWorld().strikeLightningEffect(playerHit.getLocation());
            playerHit.damage(1.0);
        }
    }
}
