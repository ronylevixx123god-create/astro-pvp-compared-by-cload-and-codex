package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutVoice implements Packet {
    @SerializedName("label")
    private String label;
    @SerializedName("channel")
    private String channel;
    @SerializedName("audio")
    private byte[] audio;

    public PacketOutVoice(String label, String channel, byte[] audio){
        this.label = label;
        this.audio = audio;
        this.channel = "local";
    }

    @Override
    public short getID() {
        return 47;
    }
}
