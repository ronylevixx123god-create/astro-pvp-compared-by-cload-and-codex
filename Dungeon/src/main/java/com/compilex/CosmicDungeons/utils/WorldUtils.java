/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  org.bukkit.Location
 *  org.jetbrains.annotations.NotNull
 */
package com.compilex.CosmicDungeons.utils;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 10}, bv={1, 0, 2}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u0006\u0002\u0010\u0002\u0006\u0004"}, d2={"Lcom/compilex/CosmicDungeons/net.astropvp.armorsets.utils/WorldUtils;", "", "()V", "Companion", "CosmicDungeons"})
public final class WorldUtils {
    public static final Companion Companion = new Companion();

    @Metadata(mv={1, 1, 10}, bv={1, 0, 2}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u0006\u0002\u0010\u0002J*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00040\n\u0006\f"}, d2={"Lcom/compilex/CosmicDungeons/net.astropvp.armorsets.utils/WorldUtils$Companion;", "", "()V", "scanBlocks", "", "start", "Lorg/bukkit/Location;", "radius", "", "blocks", "Lkotlin/Function1;", "Lorg/bukkit/block/Block;", "CosmicDungeons"})
    public static final class Companion {
        private Companion() {
        }

        public final void scanBlocks(@NotNull Location start, int radius, @NotNull Function1 blocks) {
            Intrinsics.checkParameterIsNotNull((Object)start, (String)"start");
            Intrinsics.checkParameterIsNotNull((Object)blocks, (String)"blocks");
            int i = -radius;
            int j = radius;
            if (i <= j) {
                while (true) {
                    int m;
                    int k;
                    if ((k = -radius) <= (m = radius)) {
                        while (true) {
                            int i1;
                            int n;
                            if ((n = -radius) <= (i1 = radius)) {
                                while (true) {
                                    Intrinsics.checkExpressionValueIsNotNull((Object)start.getBlock().getRelative(i, n, k), (String)"start.block.getRelative(x, y, z)");
                                    blocks.invoke((Object)start.getBlock().getRelative(i, n, k));
                                    if (n == i1) break;
                                    ++n;
                                }
                            }
                            if (k == m) break;
                            ++k;
                        }
                    }
                    if (i == j) break;
                    ++i;
                }
            }
        }
    }
}

