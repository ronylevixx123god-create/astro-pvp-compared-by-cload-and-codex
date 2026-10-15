package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;

public class PacketInChunkDataRequest implements PacketIn{
    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @SerializedName("x")
    private int x;
    @SerializedName("z")
    private int z;
    @SerializedName("bulk")
    private boolean bulk;

    @Override
    public short getID() {
        return 1;
    }
}
