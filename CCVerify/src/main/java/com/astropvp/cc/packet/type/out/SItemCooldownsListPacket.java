package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SItemCooldownsListPacket implements Packet {
    @Override
    public short getID() {
        return 32;
    }

    @SerializedName("cooldowns")
    private Map<String, List<Double>> cooldowns;

    public SItemCooldownsListPacket(Map<String, List<Double>> cooldowns){
        this.cooldowns = cooldowns;
    }
}
