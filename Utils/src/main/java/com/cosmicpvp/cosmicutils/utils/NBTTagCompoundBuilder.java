/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 */
package com.cosmicpvp.cosmicutils.utils;


import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@Deprecated
public class NBTTagCompoundBuilder {
    private NBTTagCompound compound;

    public NBTTagCompoundBuilder(NBTTagCompound comp) {
        this.compound = comp;
    }

    public NBTTagCompoundBuilder(NBTBase comp) {
        if (comp instanceof NBTTagCompound) {
            this.compound = (NBTTagCompound) comp;
        }
    }

    public NBTTagCompoundBuilder() {
        this.compound = new NBTTagCompound();
    }

    public Integer getInt(String key) {
        if (this.compound == null) {
            return 0;
        }
        return this.compound.getInt(key);
    }

    public Short getShort(String key) {
        if (this.compound == null) {
            return (short) 0;
        }
        return this.compound.getShort(key);
    }

    public String getString(String key) {
        if (this.compound == null) {
            return "";
        }
        return this.compound.getString(key);
    }

    public NBTBase getNBTBase(String key) {
        if (this.compound == null) {
            return null;
        }
        return this.compound.get(key);
    }

    public Double getDouble(String key) {
        if (this.compound == null) {
            return null;
        }
        return this.compound.getDouble(key);
    }

    public Long getLong(String key) {
        if (this.compound == null) {
            return null;
        }
        return this.compound.getLong(key);
    }

    public NBTTagCompoundBuilder setString(String key, String value) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.setString(key, value);
        return this;
    }

    public NBTTagCompoundBuilder setInt(String key, Integer value) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.setInt(key, value.intValue());
        return this;
    }

    public NBTTagCompoundBuilder setShort(String key, Short value) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.setShort(key, value.shortValue());
        return this;
    }

    public NBTTagCompoundBuilder setNBTBase(String key, NBTBase base) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.set(key, base);
        return this;
    }

    public NBTTagCompoundBuilder setNBTBase(String key, NBTTagCompoundBuilder base) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.set(key, (NBTBase) base.get());
        return this;
    }

    public NBTTagCompoundBuilder setDouble(String key, Double value) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.setDouble(key, value.doubleValue());
        return this;
    }

    public NBTTagCompoundBuilder setLong(String key, Long value) {
        if (this.compound == null) {
            this.compound = new NBTTagCompound();
        }
        this.compound.setLong(key, value.longValue());
        return this;
    }

    public NBTTagCompound get() {
        return this.compound;
    }
}

