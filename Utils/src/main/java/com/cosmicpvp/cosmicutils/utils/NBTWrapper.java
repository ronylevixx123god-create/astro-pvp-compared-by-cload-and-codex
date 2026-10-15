/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.craftbukkit.v1_7_R4.me.astropvp.cratekeys.inventory.CraftItemStack
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagListBuilder;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTWrapper {
    private net.minecraft.server.v1_8_R3.ItemStack nmsItem;
    private NBTTagCompound compound;
    private ItemStack defaultItem;
    private Long foundIID;

    public NBTWrapper(ItemStack item, boolean useDefault) {
        this.defaultItem = item;
        if (!useDefault) {
            this.nmsItem = item instanceof CraftItemStack ? ((CraftItemStack) item).getHandle() : CraftItemStack.asNMSCopy(item);
            this.compound = this.nmsItem.getTag();
        }
    }

    public NBTWrapper(ItemStack item) {
        this.defaultItem = item;
        this.load();
    }

    private void load() {
        this.nmsItem = this.defaultItem instanceof CraftItemStack ? ((CraftItemStack) this.defaultItem).getHandle() : CraftItemStack.asNMSCopy((ItemStack) this.defaultItem);
        this.compound = this.nmsItem.getTag();
    }

    public NBTWrapper remove(String key) {
        if (this.compound == null) {
            return this;
        }
        this.compound.remove(key);
        return this;
    }

    public NBTTagCompound getOrCreateTag() {
        if (this.nmsItem == null) {
            if (this.defaultItem == null) {
                return null;
            }
            this.nmsItem = CraftItemStack.asNMSCopy((ItemStack) this.defaultItem);
        }
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        return this.compound;
    }

    public long getIID() {
        if (!CosmicUtils.get().isArkkitEnabled() || !this.isIIDEnabled()) {
            return -1L;
        }
        if (this.foundIID != null) {
            return this.foundIID;
        }
        this.foundIID = ItemID.getItemID((ItemStack) this.defaultItem);
        return this.foundIID;
    }

    public NBTTagCompoundBuilder getconaxData() {
        return this.getCompound("conaxData");
    }

    public boolean isBlacklistedIID() {
        if (!CosmicUtils.get().isArkkitEnabled()) {
            return false;
        }
        if (!this.isIIDEnabled()) {
            return false;
        }
        long id = this.getIID();
        if (id == -1L) {
            return false;
        }
        return ItemID.blacklist_iid.contains(id);
    }

    public NBTWrapper blacklistIID(String reason) {
        if (!this.isIIDEnabled()) {
            return this;
        }
        long id = this.getIID();
        if (id == -1L) {
            return this;
        }
        ItemID.blacklistIID((long) id, (String) reason);
        return this;
    }

    public NBTWrapper assignIID() {
        if (!CosmicUtils.get().isArkkitEnabled() || !this.isIIDEnabled()) {
            return this;
        }
        ItemStack item = this.build();
        ItemID.assignItemID(null, (ItemStack) item);
        this.foundIID = ItemID.getItemID((ItemStack) item);
        this.defaultItem = item;
        this.load();
        return this;
    }

    public ItemStack getDefault() {
        return this.defaultItem;
    }

    public ItemStack build() {
        if (this.nmsItem == null) {
            return null;
        }
        this.nmsItem.setTag(this.compound);
        return CraftItemStack.asCraftMirror(this.nmsItem);
    }

    public String getString(String key) {
        if (this.nmsItem == null) {
            return "";
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return "";
        }
        return tag.getString(key);
    }

    public Long getLong(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getLong(key);
    }

    public Double getDouble(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getDouble(key);
    }

    public Float getFloat(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return Float.valueOf(tag.getFloat(key));
    }

    public Short getShort(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getShort(key);
    }

    public Integer getInt(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getInt(key);
    }

    public Boolean getBoolean(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getBoolean(key);
    }

    public NBTBase getNBTBase(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompound tag = this.getTag();
        if (tag == null) {
            return null;
        }
        return tag.get(key);
    }

    public NBTTagCompoundBuilder getCompound(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(this.getNBTBase(key));
        if (!builder.hasCompound()) {
            return null;
        }
        return builder;
    }

    public NBTTagCompoundBuilder getCompoundOrCreate(String key) {
        if (this.nmsItem == null) {
            return null;
        }
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(this.getNBTBase(key));
        if (!builder.hasCompound()) {
            return new NBTTagCompoundBuilder();
        }
        return builder;
    }

    public boolean hasKey(String key) {
        if (this.nmsItem == null) {
            return false;
        }
        NBTTagCompound comp = this.getTag();
        return comp != null && comp.hasKey(key);
    }

    public NBTTagCompound getTag() {
        if (this.nmsItem == null) {
            return null;
        }
        return this.nmsItem.getTag();
    }

    public NBTWrapper setString(String key, String value) {
        this.getOrCreateTag().setString(key, value);
        return this;
    }

    public NBTWrapper setDouble(String key, Double value) {
        this.getOrCreateTag().setDouble(key, value.doubleValue());
        return this;
    }

    public NBTWrapper setFloat(String key, Float value) {
        this.getOrCreateTag().setFloat(key, value.floatValue());
        return this;
    }

    public NBTWrapper setNBTBase(String key, NBTBase value) {
        this.getOrCreateTag().set(key, value);
        return this;
    }

    public NBTWrapper setLong(String key, Long value) {
        this.getOrCreateTag().setLong(key, value.longValue());
        return this;
    }

    public NBTWrapper setShort(String key, Short value) {
        this.getOrCreateTag().setShort(key, value.shortValue());
        return this;
    }

    public NBTWrapper setInteger(String key, Integer value) {
        this.getOrCreateTag().setInt(key, value.intValue());
        return this;
    }

    public NBTWrapper setBoolean(String key, Boolean value) {
        this.getOrCreateTag().setBoolean(key, value.booleanValue());
        return this;
    }

    public NBTWrapper setGlowing() {
        this.getOrCreateTag().set("ench", (NBTBase) new NBTTagListBuilder().build());
        return this;
    }

    private boolean isIIDEnabled() {
        return ArkPlugins.ITEM_ID.getArkPlugin().isRunning();
    }
}

