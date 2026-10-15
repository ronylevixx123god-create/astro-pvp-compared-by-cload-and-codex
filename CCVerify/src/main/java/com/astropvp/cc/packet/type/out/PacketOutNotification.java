package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutNotification implements Packet {
    @SerializedName("text")
    private String text;
    @SerializedName("timeToDisplay")
    private int timeToDisplay;

    public PacketOutNotification(String text, int timeToDisplay){
        this.text = text;
        this.timeToDisplay = timeToDisplay;
    }

    @Override
    public short getID() {
        return 34;
    }
}
