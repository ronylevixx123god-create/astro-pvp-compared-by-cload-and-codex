package org.arkhamnetwork.enchantments.enchantments.heroic;

import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;

public class HeroicLuck extends HeroicCustomEnchantment
{
    public HeroicLuck() {
        super("Infinite Luck", "Lucky", GeneralUtil.armor, 2);
        this.max = 5;
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        player.setMetadata("infiniteLucky", new FixedMetadataValue((Plugin)Enchantments.getInstance(), enchantLevel));
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        player.removeMetadata("infiniteLucky", (Plugin)Enchantments.getInstance());
    }
}
