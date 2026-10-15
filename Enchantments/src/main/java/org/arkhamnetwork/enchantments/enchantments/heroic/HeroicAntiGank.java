package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.UUID;

public class HeroicAntiGank
        extends HeroicCustomEnchantment {
    public HeroicAntiGank() {
        super("Righteous Anti Gank", "Anti Gank", GeneralUtil.axe, 7);
        this.max = 4;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        if (player.hasMetadata("HantiGank_nextReset")) {
            if ((long)MinecraftServerUtils.getCurrentServerTick() > player.getMetadata("HantiGank_nextReset").get(0).asLong()) {
                player.removeMetadata("HantiGank_uniqueEnemies", (Plugin)((Object)Enchantments.getInstance()));
                player.setMetadata("HantiGank_nextReset", new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), MinecraftServerUtils.getCurrentServerTick() + 120));
                return;
            }
        } else {
            player.setMetadata("HantiGank_nextReset", new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), MinecraftServerUtils.getCurrentServerTick() + 120));
        }
        HashSet<UUID> enemies = (HashSet<UUID>) (player.hasMetadata("antiGank_uniqueEnemies") ? player.getMetadata("antiGank_uniqueEnemies").get(0).value() : new HashSet<UUID>());
        if (enemies.size() >= 2) {
            if (Math.random() < 0.5) {
                Location l = playerHit.getLocation().add(0.0, 1.0, 0.0);
                WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)86, (int)2001);
                return;
            }
            Location l = playerHit.getLocation().add(0.0, 1.0, 0.0);
            WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)86, (int)2001);
        }
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (!(attacker instanceof Player)) {
            return;
        }
        if (player.hasMetadata("HantiGank_nextReset")) {
            if ((long)MinecraftServerUtils.getCurrentServerTick() > player.getMetadata("HantiGank_nextReset").get(0).asLong()) {
                player.removeMetadata("HantiGank_uniqueEnemies", (Plugin)((Object)Enchantments.getInstance()));
            }
        } else {
            player.setMetadata("HantiGank_nextReset", new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), MinecraftServerUtils.getCurrentServerTick() + 120));
        }
        HashSet<UUID> enemies = (HashSet<UUID>) (player.hasMetadata("antiGank_uniqueEnemies") ? player.getMetadata("antiGank_uniqueEnemies").get(0).value() : new HashSet<UUID>());
        Player pAttacker = (Player)attacker;
        enemies.add(pAttacker.getUniqueId());
        player.setMetadata("HantiGank_uniqueEnemies", new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), enemies));
    }
}