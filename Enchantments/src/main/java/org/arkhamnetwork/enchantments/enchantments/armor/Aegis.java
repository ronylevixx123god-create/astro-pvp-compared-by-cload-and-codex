package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.UUID;

public class Aegis extends CustomEnchantment {
    private static final String aegisNextReset_MetadataKey = "aegis_nextReset";

    private static final String aegisUniqueEnemies_MetadataKey = "aegis_uniqueEnemies";

    public Aegis() {
        super("Aegis", GeneralUtil.armor, 4);
        this.max = 6;
        this.base = 20.0D;
        this.interval = 5.0D;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (!(attacker instanceof Player))
            return;
        /*int ticksToCheck = 100;
        if (player.hasMetadata("aegis_nextReset")) {
            if (MinecraftServerUtils.getCurrentServerTick() > ((MetadataValue)player.getMetadata("aegis_nextReset").get(0)).asLong()) {
                player.removeMetadata("aegis_uniqueEnemies", (Plugin) Enchantments.getInstance());
                player.setMetadata("aegis_nextReset", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), MinecraftServerUtils.getCurrentServerTick() + ticksToCheck));
            }
        } else {
            player.setMetadata("aegis_nextReset", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), MinecraftServerUtils.getCurrentServerTick() + ticksToCheck));
        }
        HashSet<UUID> enemies = player.hasMetadata("aegis_uniqueEnemies") ? (HashSet<UUID>) player.getMetadata("aegis_uniqueEnemies").get(0).value() : new HashSet<>();
        Player pAttacker = (Player)attacker;
        if (enemies.size() > 8 - level &&
                !enemies.contains(pAttacker.getUniqueId())) {
            //e.setDamage(e.getDamage() * 0.9D);
            return;
        }
        enemies.add(pAttacker.getUniqueId());
        player.setMetadata("aegis_uniqueEnemies", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), enemies));*/
    }
}
