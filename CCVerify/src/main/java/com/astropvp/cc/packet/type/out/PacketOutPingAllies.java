package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutPingAllies implements Packet {
    @SerializedName("name")
    private String name;
    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;
    @SerializedName("z")
    private double z;
    @SerializedName("dimension")
    private int dimension;
    @SerializedName("allies")
    private boolean allies;

    public PacketOutPingAllies(String name, int dimension, double x, double y, double z, boolean allies){
        this.name = name;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.allies = allies;
    }

    @Override
    public short getID() {
        return 36;
    }
}
