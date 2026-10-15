package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class PacketInVoice implements PacketIn{
    @SerializedName("channel")
    @Getter
    @Setter
    private String channel;
    @SerializedName("audio")
    @Getter
    private byte[] audio;

    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @Override
    public short getID() {
        return 18;
    }
}
