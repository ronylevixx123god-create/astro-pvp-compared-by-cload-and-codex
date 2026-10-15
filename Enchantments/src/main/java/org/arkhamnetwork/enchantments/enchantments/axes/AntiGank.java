// Decompiled with: CFR 0.151
// Class Version: 8
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import java.util.HashSet;
import java.util.UUID;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class AntiGank
        extends CustomEnchantment {
    private static final String antiGankNextReset_MetadataKey = "antiGank_nextReset";
    private static final String antiGankUniqueEnemies_MetadataKey = "antiGank_uniqueEnemies";
    private static final int ticksToCheck = 120;

    public AntiGank() {
        super("Anti Gank", GeneralUtil.axe, 7);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        /*if (player.hasMetadata(antiGankNextReset_MetadataKey)) {
            if ((long)MinecraftServerUtils.getCurrentServerTick() > player.getMetadata(antiGankNextReset_MetadataKey).get(0).asLong()) {
                player.removeMetadata(antiGankUniqueEnemies_MetadataKey, (Plugin)((Object)Enchantments.getInstance()));
                player.setMetadata(antiGankNextReset_MetadataKey, new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), MinecraftServerUtils.getCurrentServerTick() + 120));
                return;
            }
        } else {
            player.setMetadata(antiGankNextReset_MetadataKey, new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), MinecraftServerUtils.getCurrentServerTick() + 120));
        }
         HashSet<UUID> enemies = (HashSet<UUID>) (player.hasMetadata("antiGank_uniqueEnemies") ? player.getMetadata("antiGank_uniqueEnemies").get(0).value() : new HashSet<UUID>());
        if (enemies.size() >= 6 - level) {
            Location l = playerHit.getLocation().add(0.0, 1.0, 0.0);
            WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)86, (int)2001);
        }*/
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        /*if (!(attacker instanceof Player)) {
            return;
        }
        if (player.hasMetadata(antiGankNextReset_MetadataKey)) {
            if ((long)MinecraftServerUtils.getCurrentServerTick() > player.getMetadata(antiGankNextReset_MetadataKey).get(0).asLong()) {
                player.removeMetadata(antiGankUniqueEnemies_MetadataKey, (Plugin)((Object)Enchantments.getInstance()));
            }
        } else {
            player.setMetadata(antiGankNextReset_MetadataKey, new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), MinecraftServerUtils.getCurrentServerTick() + 120));
        }
         HashSet<UUID> enemies = (HashSet<UUID>) (player.hasMetadata("antiGank_uniqueEnemies") ? player.getMetadata("antiGank_uniqueEnemies").get(0).value() : new HashSet<UUID>());
        Player pAttacker = (Player)attacker;
        enemies.add(pAttacker.getUniqueId());
        player.setMetadata(antiGankUniqueEnemies_MetadataKey, new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), enemies));
    }*/
    }
}
