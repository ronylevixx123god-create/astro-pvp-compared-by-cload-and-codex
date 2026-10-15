package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutCooldown implements Packet {
    @SerializedName(value="key")
    private String key;
    @SerializedName(value="category")
    private String category;
    @SerializedName(value="infinite")
    private boolean infinite;
    @SerializedName(value="color")
    private int color;
    @SerializedName(value="name")
    private String name;
    @SerializedName(value="duration")
    private int duration;
    @SerializedName(value="max")
    private int max;
    @SerializedName(value="item")
    private String item;
    @SerializedName(value="itemID")
    private int itemID;

    public PacketOutCooldown(String key, String category, boolean infinite, int color, String name, int duration, int max, String item, int itemID) {
        this.key = key;
        this.category = category;
        this.infinite = infinite;
        this.color = color;
        this.name = name;
        this.duration = duration;
        this.max = max;
        this.item = item;
        this.itemID = itemID;
    }

    public short getID() {
        return 22;
    }
}
