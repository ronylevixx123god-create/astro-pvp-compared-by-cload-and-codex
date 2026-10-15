package me.astropvp.astrospawners.utils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.data.SpawnerData;
import me.astropvp.astrospawners.data.SpawnerManager;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.SpawnerSpawnEvent;


public class MobStackerUtils {

    private final HashMap<Entity, Integer> mobMap;

    public MobStackerUtils() {
        this.mobMap = new HashMap<>();
        int delayS = 15;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(AstroSpawners.getInstance(), () -> {
            for (int i = 0; i < mobMap.keySet().size(); i++) {
                Entity entity  = (Entity) mobMap.keySet().toArray()[i];
                if (!entity.isValid() || entity.isDead()) {
                     mobMap.remove(entity);
                    continue;
                }
                stackMobs(entity);
            }
            System.out.println("[Spawner] Attempting to stack nearby entities!");
        }, 40L, delayS * 20L);
    }

    public static void onEnable() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Player) && !(entity instanceof ArmorStand)) {
                    entity.remove();
                }
            }
        }
    }

    public HashMap<Entity, Integer> getMobMap() {
        return mobMap;
    }

    public int getStackSize(Entity entity) {
        return mobMap.getOrDefault(entity, 1);
    }

    public void setStackSize(Entity entity, int size) {
        entity.setCustomName(CC.GoldB + EntityUtils.getEntityName(entity.getType()) + " " + CC.Gray + "[" + CC.White + "x " + CC.Underline + size + CC.Gray + "]");
        mobMap.put(entity, size);
    }

    public boolean isStackedMob(Entity entity) {
        return mobMap.containsKey(entity);
    }

    public void removeStackedMob(Entity entity) {
        if (!entity.isDead()) entity.remove();
        mobMap.remove(entity);
    }

    public void stackMobs(SpawnerSpawnEvent event) {
        Chunk chunk = ChunkUtils.getChunkIfLoaded((int) Math.floor(event.getEntity().getLocation().getChunk().getX()), (int) Math.floor(event.getEntity().getLocation().getChunk().getZ()), event.getLocation().getWorld());
        //Chunk chunk = event.getLocation().getChunk();

        if (chunk == null) {
            System.out.println("[Spawners] Skipping because chunk is null");
            event.setCancelled(true);
            return;
        }
        List<CraftEntity> entities = ChunkUtils.getEntitiesInChunk(chunk);
        int entityStack = 0;
        Entity entity = event.getEntity();
        Location location = event.getSpawner().getLocation();
        SpawnerData data = AstroSpawners.getInstance().getSpawnerManager().getSpawnerData(location);

        int size;

        if (entity.getType() == EntityType.BLAZE) {
            size = entityStack + (data != null ? (data.getSpawnerAmount() * 10) + 100 : 100);


        } else {
            size = entityStack + (data != null ? data.getSpawnerAmount() : 100);
        }

        int stackSize = 0;
        Entity highestStackedEntity = null;


        for (Entity nearbyEntity : entities) {
            if (nearbyEntity.getType() != entity.getType()) continue;
            if (!isStackedMob(nearbyEntity)) continue;
            if (getStackSize(nearbyEntity) <= stackSize) continue;
            highestStackedEntity = nearbyEntity;
            stackSize = getStackSize(nearbyEntity);
        }
        if (highestStackedEntity == null) {
            setStackSize(entity, size);
        } else {
            setStackSize(highestStackedEntity, stackSize + size);
            removeStackedMob(entity);
            event.setCancelled(true);
        }
    }

    public void stackMobs(Entity entity) {
        int stack = getStackSize(entity);
        Entity highestStackEntity = entity;

        for (Entity nearbyEntity : entity.getNearbyEntities(10, 4, 10)) {
            if (nearbyEntity.getType() != entity.getType()) continue;
            if (!isStackedMob(nearbyEntity)) continue;
            int nearbyStack = getStackSize(nearbyEntity);
            if (nearbyStack >= stack) {
                highestStackEntity = nearbyEntity;
            }

        }
        if (highestStackEntity.getUniqueId() != entity.getUniqueId()) {
            setStackSize(highestStackEntity, stack + getStackSize(highestStackEntity));
            removeStackedMob(entity);
        }
    }

    /*public void stackMobs(SpawnerSpawnEvent event) {

        Chunk chunk = event.getLocation().getChunk();
        Entity[] entities = chunk.getEntities();


        int entityStack = 0;
        Entity entity = event.getEntity();
        Location location = event.getSpawner().getLocation();
        SpawnerData data = SpawnerPlugin.getInstance().getSpawnerManager().getSpawnerData(location);

        int size = 0;

        if (entity.getType() == EntityType.BLAZE) {
            size = entityStack + (data != null ? (data.getSpawnerAmount()*10)+100 : 100);
        } else {
            size = entityStack + (data != null ? data.getSpawnerAmount() : 100);
        }



        int stackSize = 0;
        Entity highestStackedEntity = null;


        for (Entity nearbyEntity : entities) {
            if (nearbyEntity.getType() != entity.getType()) continue;
            if (!isStackedMob(nearbyEntity)) continue;
            if (getStackSize(nearbyEntity) <= stackSize) continue;
            highestStackedEntity = nearbyEntity;
            stackSize = getStackSize(nearbyEntity);
        }
        if (highestStackedEntity == null) {
            setStackSize(entity, size);
        } else {
            setStackSize(highestStackedEntity, stackSize + size);
            removeStackedMob(entity);
            event.setCancelled(true);
        }
    }*/

}
