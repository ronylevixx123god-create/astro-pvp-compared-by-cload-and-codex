package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutFocusedPlayer implements Packet {
    @SerializedName("target")
    private String target;

    public PacketOutFocusedPlayer(String target){
        this.target = target;
    }

    @Override
    public short getID() {
        return 31;
    }
}
