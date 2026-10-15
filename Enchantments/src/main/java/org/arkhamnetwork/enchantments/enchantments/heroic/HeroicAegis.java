package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import lombok.SneakyThrows;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.UUID;

public class HeroicAegis extends HeroicCustomEnchantment {
    public HeroicAegis() {
        super("Holy Aegis", "Aegis", GeneralUtil.armor, 2);
        this.max = 6;
    }

    @SneakyThrows
    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
/*        if (!(attacker instanceof Player))
            return;
        if (player.hasMetadata("aegis_nextReset")) {
            if (MinecraftServerUtils.getCurrentServerTick() > player.getMetadata("aegis_nextReset").get(0).asLong()) {
                player.removeMetadata("aegis_uniqueEnemies", Enchantments.getInstance());
                player.setMetadata("aegis_nextReset", new FixedMetadataValue(Enchantments.getInstance(), Integer.valueOf(MinecraftServerUtils.getCurrentServerTick() + 100)));
            }
        } else {
            player.setMetadata("aegis_nextReset", new FixedMetadataValue(Enchantments.getInstance(), Integer.valueOf(MinecraftServerUtils.getCurrentServerTick() + 100)));
        }
        HashSet<UUID> enemies = player.hasMetadata("aegis_uniqueEnemies") ? (HashSet<UUID>) player.getMetadata("aegis_uniqueEnemies").get(0).value() : new HashSet<>();
        Player pAttacker = (Player)attacker;
        if (enemies.size() > 8 - level && !enemies.contains(pAttacker.getUniqueId())) {
            //e.setDamage(e.getDamage());
            return;
        }
        enemies.add(pAttacker.getUniqueId());
        player.setMetadata("aegis_uniqueEnemies", new FixedMetadataValue(Enchantments.getInstance(), enemies));*/
    }
}
