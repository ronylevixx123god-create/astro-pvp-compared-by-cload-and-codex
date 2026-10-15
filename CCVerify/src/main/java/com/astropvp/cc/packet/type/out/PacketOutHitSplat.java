package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.io.Serializable;

public class PacketOutHitSplat implements Packet {
    @Override
    public short getID() {
        return 49;
    }

    @SerializedName("hitsplat")
    private HitSplatData hitsplat;

    public PacketOutHitSplat(HitSplatData hitsplat){
        this.hitsplat = hitsplat;
    }

    public static class HitSplatData implements Serializable {
        @SerializedName("entityId")
        private int entityId;
        @SerializedName("text")
        private String text;
        @SerializedName("type")
        @Since(1.1D)
        private String type;
        @SerializedName("x")
        @Since(1.2D)
        private double x;
        @SerializedName("y")
        @Since(1.2D)
        private double y;
        @SerializedName("z")
        @Since(1.2D)
        private double z;

        public HitSplatData(int entityId, String text, String type, double x, double y, double z){
            this.entityId = entityId;
            this.text = text;
            this.type = type;
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }
}
