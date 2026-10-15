package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PacketInStartEmote implements PacketIn{
    @SerializedName("emote")
    @Getter
    private String emote;

    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @Override
    public short getID() {
        return 16;
    }
}
