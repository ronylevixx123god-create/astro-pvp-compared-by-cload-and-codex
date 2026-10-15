package com.astropvp.cc.packet.type;

import com.astropvp.cc.packet.PacketContainer;

public interface Packet {
    short getID();

    default String toJSON(){
        return PacketContainer.GSON.toJson(this);
    }
}
