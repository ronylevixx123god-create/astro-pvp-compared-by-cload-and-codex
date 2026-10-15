package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class PacketOutAllowedEmotes implements Packet {
    @SerializedName("allowedEmotes")
    private Set allowedEmotes;

    public PacketOutAllowedEmotes(Set allowedEmotes){
        this.allowedEmotes = allowedEmotes;
    }

    @Override
    public short getID() {
        return 25;
    }
}
