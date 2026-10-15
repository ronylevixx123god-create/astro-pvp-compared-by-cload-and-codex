package org.arkhamnetwork.enchantments.enchantments.heroic;

import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralCooldown;
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

public class HeroicCleave extends HeroicCustomEnchantment {
    boolean mcmmo;

    public HeroicCleave() {
        super("Mighty Cleave", "Cleave", GeneralUtil.axe, 5);
        this.mcmmo = false;
        this.max = 5;
        this.mcmmo = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        double radius = 3.0 + 0.25 * (double)level;
        double damage = 7;
        if (player.hasMetadata("previous_cleave_use") && System.currentTimeMillis() - ((MetadataValue)player.getMetadata("previous_cleave_use").get(0)).asLong() <= 1500L) {
            return;
        }
        if (player.hasMetadata("skullSplitterProc") && System.currentTimeMillis() - ((MetadataValue)player.getMetadata("skullSplitterProc").get(0)).asLong() <= 1000L) {
            return;
        }
        player.setMetadata("previous_cleave_use", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
        if (damage > 0.0 && e.getDamage() > 0.0) {
            List<Entity> aoe = e.getEntity().getNearbyEntities(radius, radius, radius);
            for (Entity aoe_ent : aoe) {
                LivingEntity le_ent;
                if (aoe_ent.equals(player) || aoe_ent.hasMetadata("spectator") || !(aoe_ent instanceof LivingEntity) || aoe_ent instanceof Player && this.isAlly((Player)player, (Player)aoe_ent) || (le_ent = (LivingEntity)aoe_ent).hasMetadata("lastCleaveDamage") && System.currentTimeMillis() - ((MetadataValue)le_ent.getMetadata("lastCleaveDamage").get(0)).asLong() < 1000L) continue;
                le_ent.setMetadata("lastCleaveDamage", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
                le_ent.damage(damage);
            }
        }
    }

    private boolean isAlly(Player p1, Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }

    public void pushAwayEntity(Entity pusher, Entity entity, double speed) {
        if (entity instanceof Horse) {
            return;
        }
        Vector unitVector = entity.getLocation().toVector().subtract(pusher.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}
