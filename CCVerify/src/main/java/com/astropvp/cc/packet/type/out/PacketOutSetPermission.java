package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.PacketContainer;
import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutSetPermission implements Packet {
    @Override
    public short getID() {
        return 35;
    }

    @SerializedName("permission")
    private String permission;
    @SerializedName("value")
    private boolean value;

    public PacketOutSetPermission(String permission, boolean value){
        this.permission = permission;
        this.value = value;
    }

    @Override
    public String toJSON() {
        return PacketContainer.GSON.toJson(this);
    }
}
