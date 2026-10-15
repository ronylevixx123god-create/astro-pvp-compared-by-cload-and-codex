package org.arkhamnetwork.enchantments.mastery;

import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class MasteryDeathPact extends MasteryCustomEnchantment {
    public MasteryDeathPact() {
        super("Death Pact", GeneralUtil.armor, 10);
        this.max = 3;
        this.armorSet = "DEATH_KNIGHT";
    }

    @Override
    public void applyDamagedOtherPlayer(final Player damager, final Player damaged, final EntityDamageByEntityEvent event, final int enchantLevel) {
        if (!new CustomEnchantProcOnEntityEvent((LivingEntity) damager, (LivingEntity) damaged, (Event) event, this, enchantLevel).callEvent()) {
            return;
        }
        final double percentMissing = this.getMissingHealthPercent((LivingEntity) damager);
        final double increments = 2 + enchantLevel * 2;
        final double healthChunksGone = percentMissing / 0.1;
        final double toDecrement = Math.min(25.0, increments * healthChunksGone);
        //event.setDamage(event.getDamage() * (1.0 - 0.01 * toDecrement));
    }

    public void applyDefenseEffect(final LivingEntity damaged, final LivingEntity target, final int enchantLevel, EntityDamageEvent event) {
        final double percentMissing = this.getMissingHealthPercent(damaged);
        final double increments = enchantLevel * 2;
        final double healthChunksGone = percentMissing / 0.1;
        final double toDecrement = Math.min(50.0, increments * healthChunksGone);
        final double finalMod = 1.0 - 0.01 * toDecrement;
        //event.setDamage(event.getDamage() * finalMod);
    }
}
