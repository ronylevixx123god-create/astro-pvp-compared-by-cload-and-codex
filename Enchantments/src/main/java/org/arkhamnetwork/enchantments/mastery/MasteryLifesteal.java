package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class MasteryLifesteal extends MasteryCustomEnchantment {
    public MasteryLifesteal() {
        super("Chain Lifesteal", GeneralUtil.armor, 10);
        this.max = 5;
        this.armorSet = "DEATH_KNIGHT";
    }

    @Override
    public void applyDamagedOtherPlayer(final Player damager, final Player damaged, final EntityDamageByEntityEvent event, final int enchantLevel) {
        /*if (Math.random() <= enchantLevel * 0.05) {
            if (MaskType.hasEquipped(damaged, "NECROMANCER") || !this.canEffectPlayer(damaged)) {
                return;
            }
            final double radius = 1 + enchantLevel / 2;
            final int maxTargets = (int) (1.0 + Math.ceil(enchantLevel / 2));
            int scanned = 0;
            final List<Entity> nearby = (List<Entity>) Lists.newArrayList((Iterable) damaged.getNearbyEntities(radius, radius, radius));
            nearby.add((Entity) damaged);
            for (final Entity ent : nearby) {
                if (ent instanceof Player) {
                    final Player nearbyPlayer = (Player) ent;
                    if (nearbyPlayer.equals(damager)) {
                        continue;
                    }
                    if (!this.canEffectPlayer(nearbyPlayer) || MaskType.hasEquipped(nearbyPlayer, "NECROMANCER")) {
                        continue;
                    }
                    if (GeneralUtil.isAtleastTruce(damaged, nearbyPlayer)) {
                        continue;
                    }
                    if (!new CustomEnchantProcOnEntityEvent((LivingEntity) damager, (LivingEntity) damaged, (Event) event, this, enchantLevel).callEvent()) {
                        continue;
                    }
                    if (scanned >= maxTargets) {
                        break;
                    }
                    ++scanned;
                    final double toSteal = 0.5 + this.random.nextDouble() * enchantLevel;
                    GeneralUtil.reducePlayerHealth(nearbyPlayer, toSteal);
                    GeneralUtil.healPlayer((LivingEntity) damaged, toSteal);
                    if (nearbyPlayer.isDead()) {
                        return;
                    }
                    nearbyPlayer.playEffect(EntityEffect.HURT);
                }
            }
        }
    }*/
    }
}
