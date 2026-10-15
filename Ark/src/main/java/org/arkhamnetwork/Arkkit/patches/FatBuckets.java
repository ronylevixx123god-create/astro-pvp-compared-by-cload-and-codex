/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.itembuilder.FatBucketBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.itembuilder.FatBucketBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class FatBuckets
        extends ArkPlugin {
    protected static int maxBucketCharges = 50;

    public FatBuckets(Plugin p) {
        super(p);
    }

    public static boolean isFatBucket(ItemStack i) {
        return i != null && i.getType() != Material.AIR && new NBTWrapper(i).getString("cosmicType").equals("fatBucket");
    }

    public static int getFatBucketUses(ItemStack i) {
        if (FatBuckets.isFatBucket(i)) {
            return new NBTWrapper(i).getCompound("conaxData").getInt("fatBucketUses");
        }
        return 0;
    }

    public static int getFatBucketCharges(ItemStack i) {
        if (FatBuckets.isFatBucket(i)) {
            return new NBTWrapper(i).getCompound("conaxData").getInt("fatBucketCharges");
        }
        return 0;
    }

    public static ItemStack setFatBucketUses(ItemStack i, int uses) {
        if (!FatBuckets.isFatBucket(i)) {
            return i;
        }
        return new FatBucketBuilder(uses).build();
    }

    public static ItemStack setFatBucketCharges(ItemStack i, int charges) {
        if (!FatBuckets.isFatBucket(i)) {
            return i;
        }
        return new FatBucketBuilder(0, charges).build();
    }

    public static ItemStack getFatBucket() {
        return new FatBucketBuilder().build();
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandFatBucket("fatbucket", "/<command>", "Admin command to spawn fat buckets."));
        this.registerListener(new FatBucketsListener());
    }

    @Override
    public void disable() {
    }
}

