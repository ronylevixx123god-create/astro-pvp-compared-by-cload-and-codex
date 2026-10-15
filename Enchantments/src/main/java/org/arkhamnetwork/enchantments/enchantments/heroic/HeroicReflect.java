package org.arkhamnetwork.enchantments.enchantments.heroic;

import org.arkhamnetwork.enchantments.util.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;

public class HeroicReflect extends HeroicCustomEnchantment
{
    public HeroicReflect() {
        super("Heroic Enchant Reflect", "Enchant Reflect", GeneralUtil.armor, 10);
        this.max = 10;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        //player.setMetadata("heroic_reflect_enchant", new FixedMetadataValue((Plugin)Enchantments.getInstance(), enchantLevel));
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        //player.removeMetadata("heroic_reflect_enchant", (Plugin)Enchantments.getInstance());
    }
}
