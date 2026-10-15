package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class PacketOutTooltip implements Packet {
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("item")
    private String item;
    @SerializedName("itemID")
    private int itemId;
    @SerializedName("itemDamage")
    private int itemDamage;

    public PacketOutTooltip(String title, String text, String item, int itemId, int itemDamage){
        this.title = title;
        this.text = text;
        this.item = item;
        this.itemId = itemId;
        this.itemDamage = itemDamage;
    }

    @Override
    public short getID() {
        return 70;
    }
}
