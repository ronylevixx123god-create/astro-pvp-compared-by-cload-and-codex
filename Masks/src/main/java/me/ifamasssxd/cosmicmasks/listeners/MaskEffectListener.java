/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.math.LongHash
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.MaskUtils
 *  com.google.common.collect.MapMaker
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.bukkit.Chunk
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicmasks.listeners;

import com.cosmicpvp.cosmicutils.math.LongHash;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.google.common.cache.CacheBuilder;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class MaskEffectListener
implements Listener {
    public static ConcurrentMap<Object, Object> expiringChunkMap = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.SECONDS).build().asMap();

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            Chunk c = e.getEntity().getLocation().getChunk();
            long chunkKey = LongHash.toLong((int)c.getX(), (int)c.getZ());
            if (!this.expiringChunkMap.containsKey(chunkKey)) {
                for (Entity ent : c.getEntities()) {
                    if (!(ent instanceof Player) || !MaskUtils.hasMaskEquipped((Player)((Player)ent), (String)MaskType.BUNNY.name())) continue;
                    this.expiringChunkMap.put(LongHash.toLong((int)c.getX(), (int)c.getZ()), true);
                    return;
                }
                this.expiringChunkMap.put(LongHash.toLong((int)c.getX(), (int)c.getZ()), false);
            } else {
                boolean bunnyMask = (boolean) this.expiringChunkMap.get(chunkKey);
                if (bunnyMask && Math.random() < 0.65) {
                    e.getEntity().setMetadata("monsterAmount", (MetadataValue)new FixedMetadataValue((Plugin)Arkkit.get(), (Object)(e.getEntity().hasMetadata("monsterAmount") ? ((MetadataValue)e.getEntity().getMetadata("monsterAmount").get(0)).asInt() + 2 : 2)));
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onPlayerDamageBoss(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Projectile) {
            LivingEntity ps = (LivingEntity) ((Projectile)damager).getShooter();
            if (ps == null) {
                return;
            }
            damager = (Entity)ps;
        }
        if (e.getEntity().hasMetadata("boss")) {
            if (damager instanceof Player && MaskUtils.hasMaskEquipped((Player)((Player)damager), (String)"BOSS")) {
                e.setDamage(e.getDamage() * 1.1);
            }
        } else if (damager.hasMetadata("boss") && e.getEntity() instanceof Player && MaskUtils.hasMaskEquipped((Player)((Player)e.getEntity()), (String)"BOSS")) {
            e.setDamage(e.getDamage() * 0.75);
        }
    }

    private long toLong(int msw, int lsw) {
        return LongHash.toLong((int)msw, (int)lsw);
    }
}

