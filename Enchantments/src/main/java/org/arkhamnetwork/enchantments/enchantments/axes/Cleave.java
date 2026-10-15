package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;

public class Cleave extends CustomEnchantment {
    boolean mcmmo;

    public Cleave() {
        super("Cleave", GeneralUtil.axe, 2);
        this.mcmmo = false;
        this.max = 7;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        final double radius = 0.45 * level;
        final double damage = (level <= 3) ? 1.0 : ((level <= 6) ? 2.0 : ((level == 7) ? 3.0 : 0.0));
        if (player.hasMetadata("previous_cleave_use") && System.currentTimeMillis() - player.getMetadata("previous_cleave_use").get(0).asLong() <= 1500L) {
            return;
        }
        if (player.hasMetadata("skullSplitterProc") && System.currentTimeMillis() - player.getMetadata("skullSplitterProc").get(0).asLong() <= 1000L) {
            return;
        }
        player.setMetadata("previous_cleave_use", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) System.currentTimeMillis()));
        if (damage > 0.0 && e.getDamage() > 0.0) {
            final List<Entity> aoe = (List<Entity>) e.getEntity().getNearbyEntities(radius, radius, radius);
            for (final Entity aoe_ent : aoe) {
                if (aoe_ent.equals(player)) {
                    continue;
                }
                if (aoe_ent.hasMetadata("spectator")) {
                    continue;
                }
                if (!(aoe_ent instanceof LivingEntity)) {
                    continue;
                }
                if (aoe_ent instanceof Player && this.isAlly((Player) player, (Player) aoe_ent)) {
                    continue;
                }
                final LivingEntity le_ent = (LivingEntity) aoe_ent;
                if (le_ent.hasMetadata("lastCleaveDamage") && System.currentTimeMillis() - le_ent.getMetadata("lastCleaveDamage").get(0).asLong() < 1000L) {
                    continue;
                }
                le_ent.setMetadata("lastCleaveDamage", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) System.currentTimeMillis()));
                le_ent.damage(damage);
            }
        }
    }

    private boolean isAlly(final Player p1, final Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }

    public void pushAwayEntity(final Entity pusher, final Entity entity, final double speed) {
        if (entity instanceof Horse) {
            return;
        }
        final Vector unitVector = entity.getLocation().toVector().subtract(pusher.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}
