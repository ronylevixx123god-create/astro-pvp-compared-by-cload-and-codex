package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutTimeResponse implements Packet {
    @SerializedName("epoch")
    private long epoch;

    public PacketOutTimeResponse(long epoch){
        this.epoch = epoch;
    }

    @Override
    public short getID() {
        return 46;
    }
}
