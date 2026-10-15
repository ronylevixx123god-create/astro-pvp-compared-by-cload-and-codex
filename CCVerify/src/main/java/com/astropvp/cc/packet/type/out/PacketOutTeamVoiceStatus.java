package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class PacketOutTeamVoiceStatus implements Packet {
    @Override
    public short getID() {
        return 43;
    }

    @SerializedName("members")
    private Set members;
    @SerializedName("voiceEnabled")
    private Set voiceEnabled;

    public PacketOutTeamVoiceStatus(Set members, Set voiceEnabled){
        this.members = members;
        this.voiceEnabled = voiceEnabled;
    }
}
