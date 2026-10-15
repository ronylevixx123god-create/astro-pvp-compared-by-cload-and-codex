package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutHealthBar implements Packet {
    @SerializedName("currentHealth")
    private long currentHealth;

    @SerializedName("maxHealth")
    private long maxHealth;

    @SerializedName("name")
    private String name;

    public short getID() {
        return 72;
    }

    public PacketOutHealthBar(long currentHealth, long maxHealth, String name) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.name = name;
    }
}
