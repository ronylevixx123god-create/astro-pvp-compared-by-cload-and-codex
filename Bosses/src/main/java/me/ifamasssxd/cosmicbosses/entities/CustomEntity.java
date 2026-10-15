package me.ifamasssxd.cosmicbosses.entities;

import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.bosses.xmasbattlefield.BattlefieldSnowmanEntity;
import me.ifamasssxd.cosmicbosses.bosses.yijki.WitherBoss;
import me.ifamasssxd.cosmicbosses.dungeonboss.DeathCurseBlaze;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijkiEntity;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijkiImage;
import me.ifamasssxd.cosmicbosses.dungeonboss.YijkiRunnerEntity;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.HealingBlazeEntity;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijkiEntity;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.YijkiDiscipleEntity;
import me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonautEntity;
import me.ifamasssxd.cosmicbosses.evilabomination.EvilAbominationEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum CustomEntity {
    GHAST("GhastMinion", 56, EntityType.GHAST, (Class<? extends Entity>) EntityGhast.class, (Class<? extends EntityInsentient>) GhastMinion.class),
    SPIDER("SpiderBoss", 52, EntityType.SPIDER, (Class<? extends Entity>) EntitySpider.class, (Class<? extends EntityInsentient>) SpiderBoss.class),
    CAVE_SPIDER("Cave Spider", 59, EntityType.CAVE_SPIDER, (Class<? extends Entity>) EntityCaveSpider.class, (Class<? extends EntityInsentient>) CaveSpiderMinion.class),
    SKELETON_BOSS("SkeletonBoss", 51, EntityType.SKELETON, (Class<? extends Entity>) EntitySkeleton.class, (Class<? extends EntityInsentient>) SkeletonBoss.class),
    WITHER("Wither", 64, EntityType.WITHER, (Class<? extends Entity>) EntityWither.class, (Class<? extends EntityInsentient>) WitherBoss.class),
    ABOMINATION_ZOMBIE("ZombieAbomination", (int) EntityType.ZOMBIE.getTypeId(), EntityType.ZOMBIE, (Class<? extends Entity>) EntityZombie.class, (Class<? extends EntityInsentient>) EvilAbominationEntity.class),
    ENDERMAN("Endermans", (int) EntityType.ENDERMAN.getTypeId(), EntityType.ENDERMAN, (Class<? extends Entity>) EntityEnderman.class, (Class<? extends EntityInsentient>) SoulReaperEnderman.class),
    SNOWMAN("HolidaySpirit", (int) EntityType.SNOWMAN.getTypeId(), EntityType.SNOWMAN, (Class<? extends Entity>) EntitySnowman.class, (Class<? extends EntityInsentient>) HolidaySpiritEntity.class),
    DUNGEON_YIJKI("DungeonYijki", (int) EntityType.WITHER.getTypeId(), EntityType.WITHER, (Class<? extends Entity>) EntityWither.class, (Class<? extends EntityInsentient>) DungeonYijkiEntity.class),
    DUNGEON_MOTHER_OF_YIJKI("MotherOfYijki", (int) EntityType.WITHER.getTypeId(), EntityType.WITHER, (Class<? extends Entity>) EntityWither.class, (Class<? extends EntityInsentient>) MotherOfYijkiEntity.class),
    CORRUPT_COSMONAUT("CorruptCosmonaut", (int) EntityType.SKELETON.getTypeId(), EntityType.SKELETON, (Class<? extends Entity>) EntitySkeleton.class, (Class<? extends EntityInsentient>) CorruptCosmonautEntity.class),
    DUNGEON_HEALING_BLAZE("DungeonBlaze", (int) EntityType.BLAZE.getTypeId(), EntityType.BLAZE, (Class<? extends Entity>) EntityBlaze.class, (Class<? extends EntityInsentient>) HealingBlazeEntity.class),
    DUNGEON_YIJKI_IMAGE("DungeonImage", (int) EntityType.WITHER.getTypeId(), EntityType.WITHER, (Class<? extends Entity>) EntityWither.class, (Class<? extends EntityInsentient>) DungeonYijkiImage.class),
    DUNGEON_DISCIPEL("DungeonDisciple", (int) EntityType.SKELETON.getTypeId(), EntityType.SKELETON, (Class<? extends Entity>) EntitySkeleton.class, (Class<? extends EntityInsentient>) YijkiDiscipleEntity.class),
    DUNGEON_BLAZE("DeathCurseBlaze", (int) EntityType.BLAZE.getTypeId(), EntityType.BLAZE, (Class<? extends Entity>) EntityBlaze.class, (Class<? extends EntityInsentient>) DeathCurseBlaze.class),
    DUNGEON_YIJKI_RUNNER("DungeonYijkiRunner", (int) EntityType.WITHER.getTypeId(), EntityType.WITHER, (Class<? extends Entity>) EntityWither.class, (Class<? extends EntityInsentient>) YijkiRunnerEntity.class),
    ENDER_DRAGON("EndDragon", (int) EntityType.ENDER_DRAGON.getTypeId(), EntityType.ENDER_DRAGON, (Class<? extends Entity>) EntityEnderDragon.class, (Class<? extends EntityInsentient>) EndDragonEntity.class),
    BATTLEFIELD_SNOW("ChristmasSpirit", (int) EntityType.SNOWMAN.getTypeId(), EntityType.SNOWMAN, (Class<? extends Entity>) EntitySnowman.class, (Class<? extends EntityInsentient>) BattlefieldSnowmanEntity.class);

    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends Entity> nmsClass;
    private Class<? extends EntityInsentient> customClass;

    private CustomEntity(final String name, final int id, final EntityType entityType, final Class<? extends Entity> nmsClass, final Class<? extends EntityInsentient> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Class<? extends Entity> getNMSClass() {
        return this.nmsClass;
    }

    public Class<? extends EntityInsentient> getCustomClass() {
        return this.customClass;
    }

    public static void registerEntities() {
        for (final CustomEntity entity : values()) {
            if (entity != CustomEntity.ENDERMAN) {
                a(entity.getCustomClass(), entity.getName(), entity.getID());
            }
        }
    }

    public void registerEntity() {
        a(this.getCustomClass(), this.getName(), this.getID());
    }

    public void unregisterEntity() {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(this.getCustomClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(this.getCustomClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        a(this.getNMSClass(), this.getName(), this.getID());
    }

    public static void unregisterEntities() {
        for (final CustomEntity entity : values()) {
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (final CustomEntity entity : values()) {
            try {
                a(entity.getNMSClass(), entity.getName(), entity.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc) {
            return;
        }
        for (final BiomeBase biomeBase : biomes) {
            if (biomeBase == null) {
                break;
            }
            /*for (final String field : new String[] { "as", "at", "au", "av" }) {
                try {
                    final Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    final List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>)list.get(biomeBase);
                    for (final BiomeBase.BiomeMeta meta : mobList) {
                        for (final CustomEntity entity2 : values()) {
                            if (entity2.getCustomClass().equals(meta.b)) {
                                meta.b = entity2.getNMSClass();
                            }
                        }
                    }
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }*/
        }
    }

    private static Object getPrivateStatic(final Class clazz, final String f) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    @Deprecated
    private static void a(final Class clazz, final String name, final int id) {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(name, clazz);
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(clazz, name);
            ((Map) getPrivateStatic(EntityTypes.class, "e")).put(id, clazz);
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(clazz, id);
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(name, id);
        } catch (Exception ex) {
        }
    }

    public static void registerEntityNEW(final String name, final int id, final Class<? extends EntityInsentient> customClass) {
        try {
            final List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
            for (final Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMaps.add((Map<?, ?>) f.get(null));
                }
            }
            if (dataMaps.get(2).containsKey(id)) {
                dataMaps.get(0).remove(name);
                dataMaps.get(2).remove(id);
            }
            final Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
