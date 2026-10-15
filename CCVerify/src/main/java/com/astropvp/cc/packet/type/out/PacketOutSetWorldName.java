package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.PacketContainer;
import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PacketOutSetWorldName implements Packet {
    @Override
    public short getID() {
        return 44;
    }

    @SerializedName("worldName")
    private String worldName;
    @SerializedName("pois_new")
    private List pois_new;
    @SerializedName("skybox")
    private byte skybox;
    @SerializedName("precipitationType")
    private byte precipitationType;

    public PacketOutSetWorldName(String worldName, List pois_new, byte skybox, byte precipitationType){
        this.worldName = worldName;
        this.pois_new = pois_new;
        this.skybox = skybox;
        this.precipitationType = precipitationType;
    }

    @Override
    public String toJSON() {
        return PacketContainer.GSON.toJson(this);
    }
}
