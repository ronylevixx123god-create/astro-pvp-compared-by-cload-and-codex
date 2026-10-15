package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;

public class PacketInPingAllies implements PacketIn{
    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;
    @SerializedName("z")
    private double z;
    @SerializedName("dimension")
    private int dimension;
    @SerializedName("allies")
    private boolean allies;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean isAllies() {
        return allies;
    }

    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @Override
    public short getID() {
        return 12;
    }
}
