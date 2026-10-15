package com.cosmicpvp.cosmicutils.nbt;


import net.minecraft.server.v1_8_R3.*;

public class NBTTagListBuilder {
    private NBTTagList tagList;

    public NBTTagListBuilder(NBTTagList list) {
        this.tagList = list;
    }

    public NBTTagListBuilder(NBTBase comp) {
        if (comp instanceof NBTTagList) {
            this.tagList = (NBTTagList) comp;
        }
    }

    public NBTTagListBuilder() {
        this.tagList = new NBTTagList();
    }

    public NBTBase get(int index) {
        if (this.tagList == null) {
            return null;
        }
        return this.tagList.get(index);
    }

    public Integer getIntAt(int index) {
        if (this.tagList == null) {
            return null;
        }
        return ((NBTTagInt) this.get(index)).d();
    }

    public Double getDoubleAt(int index) {
        if (this.tagList == null) {
            return null;
        }
        return ((NBTTagDouble) this.get(index)).g();
    }

    public Float getFloatAt(int index) {
        if (this.tagList == null) {
            return null;
        }
        return ((NBTTagFloat) this.get(index)).h();
    }

    public String getStringAt(int index) {
        if (this.tagList == null) {
            return null;
        }
        return this.tagList.getString(index);
    }

    public boolean contains(String value) {
        if (this.tagList == null) {
            return false;
        }
        for (int i = 0; i < this.tagList.size(); ++i) {
            String val = this.tagList.getString(i);
            if (val == null || !val.equals(value)) continue;
            return true;
        }
        return false;
    }

    public NBTTagListBuilder addInt(int value) {
        return this.add((NBTBase) new NBTTagInt(value));
    }

    public NBTTagListBuilder addDouble(double value) {
        return this.add((NBTBase) new NBTTagDouble(value));
    }

    public NBTTagListBuilder addFloat(float value) {
        return this.add((NBTBase) new NBTTagFloat(value));
    }

    public NBTTagListBuilder addString(String value) {
        return this.add((NBTBase) new NBTTagString(value));
    }

    public NBTTagListBuilder add(NBTBase base) {
        if (this.tagList == null) {
            return this;
        }
        this.tagList.add(base);
        return this;
    }

    public int size() {
        return this.tagList != null ? this.tagList.size() : 0;
    }

    public NBTTagList build() {
        return this.tagList;
    }
}

