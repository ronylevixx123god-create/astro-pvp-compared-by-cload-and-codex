package org.arkhamnetwork.enchantments.mastery;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MasteryPoltergeist extends MasteryCustomEnchantment {
    public MasteryPoltergeist() {
        super("Poltergeist", GeneralUtil.armor, 1);
        this.max = 3;
        this.armorSet = "GHOST";
    }

    public static double getChanceToProc(final Player player) {
        if (!player.hasMetadata("poltergeist")) {
            return 0.0;
        }
        return player.getMetadata("poltergeist").get(0).asInt() * 12.5;
    }

    public void applyDefenseEffect(final LivingEntity user, final LivingEntity target, final int enchantLevel, final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        player.setMetadata("poltergeist", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) enchantLevel));
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        player.removeMetadata("poltergeist", (Plugin) Enchantments.getInstance());
    }
}
