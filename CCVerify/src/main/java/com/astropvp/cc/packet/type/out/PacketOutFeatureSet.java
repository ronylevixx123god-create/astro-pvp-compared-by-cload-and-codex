package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.PacketContainer;
import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutFeatureSet implements Packet {
    @Override
    public short getID() {
        return 30;
    }

    @Override
    public String toJSON() {
        return PacketContainer.GSON.toJson(this);
    }

    @SerializedName("featureSet")
    private String featureSet;

    public PacketOutFeatureSet(String featureSet){
        this.featureSet = featureSet;
    }
}
