package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PacketInSetChannel implements PacketIn{
    @SerializedName("channel")
    @Getter
    private String channel;

    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @Override
    public short getID() {
        return 15;
    }
}
