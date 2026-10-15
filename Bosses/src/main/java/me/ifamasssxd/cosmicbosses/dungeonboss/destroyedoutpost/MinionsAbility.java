package me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost;

import com.avaje.ebean.validation.NotNull;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.struct.EntityContainer;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.arkhamnetwork.Arkkit.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0002§\u0006\u0002\u0010\u0005J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0016J\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001d\u001a\u00020\u0015H\u0016J\u0010\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u000fH\u0002R \u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0086\u000e§\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR \u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0086\u000e§\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013§\u0006 "}, d2 = {"Lme/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MinionsAbility;", "Lme/ifamasssxd/cosmicbosses/net.astropvp.armorsets.struct/BossAbility;", "Lme/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MotherOfYijki;", "Lme/ifamasssxd/cosmicbosses/net.astropvp.armorsets.struct/EntityContainer;", "boss", "(Lme/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MotherOfYijki;)V", "entitySet", "", "Lorg/bukkit/entity/Entity;", "getEntitySet", "()Ljava/util/Set;", "setEntitySet", "(Ljava/util/Set;)V", "locationLists", "", "Lorg/bukkit/Location;", "getLocationLists", "()Ljava/util/List;", "setLocationLists", "(Ljava/util/List;)V", "clearEntities", "", "getAbilityName", "", "getCooldown", "", "getEntities", "hasBlazesAlive", "", "onAbility", "spawnBlaze", "location", "CosmicBosses"})
public final class MinionsAbility extends BossAbility<MotherOfYijki> implements EntityContainer {
    @NotNull
    private Set<Entity> entitySet;
    @NotNull
    private List<? extends Location> locationLists;

    @NotNull
    public final Set<Entity> getEntitySet() {
        return this.entitySet;
    }

    public final void setEntitySet(final Set<Entity> f) {
        Intrinsics.checkParameterIsNotNull((Object) f, "<set-?>");
        this.entitySet = f;
    }

    @NotNull
    public final List<Location> getLocationLists() {
        return (List<Location>) this.locationLists;
    }

    public final void setLocationLists(final List<? extends Location> f) {
        Intrinsics.checkParameterIsNotNull((Object) f, "<set-?>");
        this.locationLists = f;
    }

    @Override
    public void clearEntities() {
        final Iterable $receiver$iv = this.getEntities();
        for (final Object element$iv : $receiver$iv) {
            final Entity ent = (Entity) element$iv;
            ent.remove();
        }
        this.getEntities().clear();
    }

    @NotNull
    @Override
    public Set<Entity> getEntities() {
        return this.entitySet;
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(100L);
    }

    @NotNull
    @Override
    public String getAbilityName() {
        return "Siphon";
    }

    public final boolean hasBlazesAlive() {
        final Iterable $receiver$iv = this.entitySet;
        boolean b;
        if ($receiver$iv instanceof Collection && ((Collection) $receiver$iv).isEmpty()) {
            b = false;
        } else {
            for (final Object element$iv : $receiver$iv) {
                final Entity it = (Entity) element$iv;
                if (!it.isDead()) {
                    b = true;
                    return b;
                }
            }
            b = false;
        }
        return b;
    }

    @Override
    public void onAbility() {
        if (this.boss == null) return;

        MotherOfYijki casted = this.boss;

        boolean istore_1 = casted.inDifferentDimension;

        if (!istore_1) {
            for (Location loc : this.locationLists) {
                this.spawnBlaze(loc);
            }
        }

        int toSpawn = ThreadLocalRandom.current().nextInt(2) + 1;

        for (int i = 0; i < toSpawn; i++) {
            Location nearbyLocation = LocationUtils.getNearbyLocation(casted.entity.getLocation(), 1, 2);
            org.bukkit.block.Block block = nearbyLocation.getBlock();

            if (block.isEmpty()) {
                this.spawnBlaze(block.getLocation());
            }
        }

        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     1: getfield        me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MinionsAbility.boss:Lme/ifamasssxd/cosmicbosses/net.astropvp.armorsets.struct/Boss;
        //     4: dup
        //     5: ifnonnull       18
        //     8: new             Lkotlin/TypeCastException;
        //    11: dup
        //    12: ldc             "null cannot be cast to non-null type me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki"
        //    14: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    17: athrow
        //    18: checkcast       Lme/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MotherOfYijki;
        //    21: getfield        me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MotherOfYijki.inDifferentDimension:Z
        //    24: istore_1        /* otherDimension */
        //    25: iload_1         /* otherDimension */
        //    26: ifne            79
        //    29: aload_0         /* this */
        //    30: getfield        me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MinionsAbility.locationLists:Ljava/util/List;
        //    33: checkcast       Ljava/lang/Iterable;
        //    36: astore_2        /* $receiver$iv */
        //    37: aload_2         /* $receiver$iv */
        //    38: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    43: astore_3
        //    44: aload_3
        //    45: invokeinterface java/util/Iterator.hasNext:()Z
        //    50: ifeq            78
        //    53: aload_3
        //    54: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    59: astore          element$iv
        //    61: aload           element$iv
        //    63: checkcast       Lorg/bukkit/Location;
        //    66: astore          loc
        //    68: aload_0         /* this */
        //    69: aload           loc
        //    71: invokespecial   me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MinionsAbility.spawnBlaze:(Lorg/bukkit/Location;)V
        //    74: nop
        //    75: goto            44
        //    78: nop
        //    79: iload_1         /* otherDimension */
        //    80: ifeq            87
        //    83: iconst_1
        //    84: goto            96
        //    87: invokestatic    java/util/concurrent/ThreadLocalRandom.current:()Ljava/util/concurrent/ThreadLocalRandom;
        //    90: iconst_2
        //    91: invokevirtual   java/util/concurrent/ThreadLocalRandom.nextInt:(I)I
        //    94: iconst_1
        //    95: iadd
        //    96: istore_2        /* toSpawn */
        //    97: iconst_0
        //    98: istore_3
        //    99: iload_2         /* toSpawn */
        //   100: istore          4
        //   102: iload_3
        //   103: iload           4
        //   105: if_icmpgt       194
        //   108: aload_0         /* this */
        //   109: getfield        me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MinionsAbility.boss:Lme/ifamasssxd/cosmicbosses/net.astropvp.armorsets.struct/Boss;
        //   112: checkcast       Lme/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MotherOfYijki;
        //   115: getfield        me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MotherOfYijki.entity:Lorg/bukkit/entity/LivingEntity;
        //   118: dup
        //   119: ldc             "boss.entity"
        //   121: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   124: invokeinterface org/bukkit/entity/LivingEntity.getLocation:()Lorg/bukkit/Location;
        //   129: iconst_1
        //   130: iconst_2


        //   131: invokestatic    org/arkhamnetwork/Arkkit/me.astropvp.cratekeys.net.astropvp.armorsets.utils/LocationUtils.getNearbyLocation:(Lorg/bukkit/Location;II)Lorg/bukkit/Location;
        //   134: astore          nearbyLocation
        //   136: aload           nearbyLocation
        //   138: dup
        //   139: ldc             "nearbyLocation"
        //   141: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   144: invokevirtual   org/bukkit/Location.getBlock:()Lorg/bukkit/block/Block;
        //   147: astore          block
        //   149: aload           block
        //   151: dup

        //   152: ldc             "block"
        //   154: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   157: invokeinterface org/bukkit/block/Block.isEmpty:()Z
        //   162: ifeq            182
        //  if (block.isEmpty())
        //   165: aload_0         /* this */
        //   166: aload           block
        //   168: invokeinterface org/bukkit/block/Block.getLocation:()Lorg/bukkit/Location;
        //   173: dup
        //   174: ldc             "block.location"
        //   176: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   179: invokespecial   me/ifamasssxd/cosmicbosses/dungeonboss/destroyedoutpost/MinionsAbility.spawnBlaze:(Lorg/bukkit/Location;)V
        // if (!block.isEmpty())   182: iload_3         /* i */
        //   183: iload           4
        //   185: if_icmpeq       194
        //   188: iinc            i, 1
        //   191: goto            108
        //   194: return
        //    StackMapTable: 00 09 52 07 00 BA FE 00 19 01 07 00 2B 07 00 31 21 F9 00 00 07 48 01 FE 00 0B 01 01 01 FD 00 49 07 00 81 07 00 AE F9 00 0B
        //
        // The error that occurred was:
        //
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        //
    }

    private final void spawnBlaze(final Location location) {
        final org.bukkit.World world2 = location.getWorld();
        if (world2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type org.bukkit.craftbukkit.v1_8_R3.CraftWorld");
        }
        final WorldServer world = ((CraftWorld) world2).getHandle();
        final Boss boss = this.boss;
        if (boss == null) {
            throw new TypeCastException("null cannot be cast to non-null type me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki");
        }
        final HealingBlazeEntity healingEntity = new HealingBlazeEntity((MotherOfYijki) boss, (World) world);
        healingEntity.setPosition(location.getX(), location.getY(), location.getZ());
        world.addEntity(healingEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final CraftEntity bukkitEntity = healingEntity.getBukkitEntity();
        if (bukkitEntity == null) {
            throw new TypeCastException("null cannot be cast to non-null type org.bukkit.entity.LivingEntity");
        }
        final LivingEntity livingEntity = (LivingEntity) bukkitEntity;
        livingEntity.setHealth(livingEntity.getMaxHealth());
        livingEntity.setCustomName(CC.AquaB + "Mothers Fallen Angel");
        livingEntity.setCustomNameVisible(true);
        final LivingEntity livingEntity2 = livingEntity;
        final String s = "minion";
        final Plugin plugin = (Plugin) CosmicBosses.get();
        final LivingEntity entity = ((MotherOfYijki) this.boss).entity;
        Intrinsics.checkExpressionValueIsNotNull((Object) entity, "boss.entity");
        livingEntity2.setMetadata(s, (MetadataValue) new FixedMetadataValue(plugin, (Object) entity.getUniqueId()));
        final EntityEquipment equipment2;
        final EntityEquipment equipment = equipment2 = livingEntity.getEquipment();
        Intrinsics.checkExpressionValueIsNotNull((Object) equipment2, "equipment");
        equipment2.setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.THORNS, 2).build());
        equipment.setChestplateDropChance(0.0f);
        this.getEntities().add((Entity) livingEntity);
    }

    public MinionsAbility(final MotherOfYijki boss) {
        super(boss);
        Intrinsics.checkParameterIsNotNull((Object) boss, "boss");
        final Set<Entity> setFromMap = Collections.newSetFromMap(new WeakHashMap<Entity, Boolean>());
        Intrinsics.checkExpressionValueIsNotNull((Object) setFromMap, "Collections.newSetFromMap(WeakHashMap())");
        this.entitySet = setFromMap;
        final Location[] array = new Location[3];
        final int n = 0;
        final Location location = boss.location;
        Intrinsics.checkExpressionValueIsNotNull((Object) location, "boss.location");
        array[n] = new Location(location.getWorld(), -100.0, 82.0, -656.0);
        final int n2 = 1;
        final Location location2 = boss.location;
        Intrinsics.checkExpressionValueIsNotNull((Object) location2, "boss.location");
        array[n2] = new Location(location2.getWorld(), -94.0, 82.0, -628.0);
        final int n3 = 2;
        final Location location3 = boss.location;
        Intrinsics.checkExpressionValueIsNotNull((Object) location3, "boss.location");
        array[n3] = new Location(location3.getWorld(), -66.0, 82.0, -663.0);
    }
}
