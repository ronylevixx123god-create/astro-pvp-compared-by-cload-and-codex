package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutClearTooltip implements Packet {
    @SerializedName("item")
    private String item;

    public PacketOutClearTooltip(String item){
        this.item = item;
    }

    @Override
    public short getID() {
        return 71;
    }
}
