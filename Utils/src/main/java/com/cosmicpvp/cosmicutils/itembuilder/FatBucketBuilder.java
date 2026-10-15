/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FatBucketBuilder
        extends ItemBuilder {
    static double maxCharge = 50.0;
    int bucketUses = 0;
    int currentCharge = 0;

    public FatBucketBuilder() {
        super(Material.BUCKET);
    }

    public FatBucketBuilder(int uses) {
        super(uses > 0 ? Material.LAVA_BUCKET : Material.BUCKET);
        this.bucketUses = uses;
    }

    public FatBucketBuilder(int uses, int charge) {
        this(uses);
        this.currentCharge = charge;
    }

    @Override
    public ItemStack build() {
        String dataText = "EMPTY";
        if (this.bucketUses > 0) {
            dataText = String.valueOf(this.bucketUses) + " uses";
        } else {
            int chargePercent = (int) Math.round((double) this.currentCharge / maxCharge * 100.0);
            dataText = chargePercent + "% full";
        }
        ItemStack bucket = ItemUtils.buildItem((Material) this.getType(), (short) 0, (String) ChatColor.translateAlternateColorCodes('&', "&c&lFat Bucket &7(&f" + dataText + "&7)"), (String[]) new String[]{(Object) ((Object) ChatColor.GRAY) + "An extremely large bucket that", (Object) ((Object) ChatColor.GRAY) + "can hold up to 256 lava sources."});
        NBTWrapper wrapper = new NBTWrapper(bucket);
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("conaxData");
        wrapper.setString("cosmicType", "fatBucket");
        builder.setInt("fatBucketUses", this.bucketUses);
        builder.setInt("fatBucketCharges", this.currentCharge);
        wrapper.setNBTBase("conaxData", (NBTBase) builder.get());
        bucket = wrapper.build();
        return bucket;
    }
}

