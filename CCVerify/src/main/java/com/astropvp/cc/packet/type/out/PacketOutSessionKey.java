package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.PacketContainer;
import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutSessionKey implements Packet {
    @Override
    public short getID() {
        return 33;
    }

    @Override
    public String toJSON() {
        return PacketContainer.GSON.toJson(this);
    }

    @SerializedName("sessionKey")
    private String sessionKey;

    public PacketOutSessionKey(String sessionKey){
        this.sessionKey = sessionKey;
    }
}
