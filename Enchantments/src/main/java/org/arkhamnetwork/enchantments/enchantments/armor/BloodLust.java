package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.*;
import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;

public class BloodLust extends CustomEnchantment
{
    public BloodLust() {
        super("Blood Lust", GeneralUtil.chestplates, 2);
        this.max = 6;
        this.base = 17.0;
        this.interval = 4.0;
    }

    public void applyDefenseEffect(final LivingEntity player, final LivingEntity attacker, final int level, final EntityDamageEvent e) {
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        player.setMetadata("bloodLust", new FixedMetadataValue((Plugin)Enchantments.getInstance(), enchantLevel));
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        player.removeMetadata("bloodLust", (Plugin)Enchantments.getInstance());
    }
}
