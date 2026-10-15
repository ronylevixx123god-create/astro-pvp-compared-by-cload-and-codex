package org.arkhamnetwork.enchantments.mastery;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MasteryWebWalker extends MasteryCustomEnchantment {
    public MasteryWebWalker() {
        super("Web Walker", GeneralUtil.boots, 10);
        this.max = 1;
    }

    public void applyEquipEffect(Player player, int enchantLevel) {
        player.setMetadata("webWalkerEnchantment", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), Integer.valueOf(enchantLevel)));
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
        player.removeMetadata("webWalkerEnchantment", (Plugin) Enchantments.getInstance());
    }
}