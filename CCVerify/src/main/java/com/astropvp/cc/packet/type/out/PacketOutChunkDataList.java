package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.PacketContainer;
import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PacketOutChunkDataList implements Packet {
    @SerializedName(value = "chunkDataList")
    private List<PacketOutChunkData> chunkDataList;

    public PacketOutChunkDataList(List<PacketOutChunkData> chunkDataList) {
        this.chunkDataList = chunkDataList;
    }

    public String toJSON() {
        return PacketContainer.GSON.toJson(this);
    }

    public short getID() {
        return 20;
    }
}
