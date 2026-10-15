package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class PacketOutPlayerHeaders implements Packet {
    @Override
    public short getID() {
        return 38;
    }

    @SerializedName("playerHeaders")
    private Map<String, List<String>> playerHeaders;
    @SerializedName("patch")
    private boolean patch;

    public PacketOutPlayerHeaders(Map<String, List<String>> playerHeaders, boolean patch){
        this.patch = patch;
        this.playerHeaders = playerHeaders;
    }
}
