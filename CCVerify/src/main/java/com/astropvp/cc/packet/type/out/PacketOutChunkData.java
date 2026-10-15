package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutChunkData implements Packet {
    @SerializedName(value="x")
    private int x;
    @SerializedName(value="z")
    private int z;
    @SerializedName(value="uuid")
    private String uuid;
    @SerializedName(value="name")
    private String name;
    @SerializedName(value="color")
    private String color;

    public PacketOutChunkData(int x, int z, String uuid, String name) {
        this.x = x;
        this.z = z;
        this.uuid = uuid;
        this.name = name;
    }

    public short getID() {
        return 0;
    }
}
