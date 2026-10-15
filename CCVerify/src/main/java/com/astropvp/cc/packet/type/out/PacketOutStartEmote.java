package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutStartEmote implements Packet {
    @Override
    public short getID() {
        return 45;
    }

    @SerializedName("entityID")
    private int entityID;
    @SerializedName("emote")
    private String emote;

    public PacketOutStartEmote(int entityID, String emote){
        this.entityID = entityID;
        this.emote = emote;
    }
}
