package org.arkhamnetwork.enchantments.mastery;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MasteryExplosivesExpert extends MasteryCustomEnchantment {
    public MasteryExplosivesExpert() {
        super("Explosives Expert", GeneralUtil.armor, 1);
        this.max = 1;
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        player.setMetadata("explosivesExpertEnchantment", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) enchantLevel));
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        player.removeMetadata("explosivesExpertEnchantment", (Plugin) Enchantments.getInstance());
    }
}
