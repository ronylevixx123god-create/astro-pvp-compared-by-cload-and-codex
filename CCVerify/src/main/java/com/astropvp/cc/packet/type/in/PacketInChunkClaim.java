package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PacketInChunkClaim implements PacketIn{
    @Override
    public void handle(PacketHandler handler, Player sender) { handler.handle(this, sender); }

    public PacketInChunkClaim(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Getter
    @SerializedName(value = "x")
    private int x;

    @Getter
    @SerializedName(value = "z")
    private int z;

    @Override
    public short getID() {
        return 2;
    }


}
