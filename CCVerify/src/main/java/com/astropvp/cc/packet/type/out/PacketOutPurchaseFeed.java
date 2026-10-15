package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutPurchaseFeed implements Packet {
    @SerializedName("name")
    private String name;
    @SerializedName("text")
    private String text;
    @SerializedName("iconURL")
    private String iconURL;

    public PacketOutPurchaseFeed(String name, String text, String iconURL){
        this.name = name;
        this.text = text;
        this.iconURL = iconURL;
    }

    @Override
    public short getID() {
        return 69;
    }
}
