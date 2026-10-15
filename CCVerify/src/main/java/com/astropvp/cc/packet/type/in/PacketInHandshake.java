package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;

public class PacketInHandshake implements PacketIn{
    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @SerializedName("handshake")
    private String handshake;
    @SerializedName("version")
    private String version;

    public PacketInHandshake(String handshake, String version){
        this.handshake = handshake;
        this.version = version;
    }

    @Override
    public short getID() {
        return 7;
    }

    public static class Handshake {
        @SerializedName("name")
        public String name;
        @SerializedName("uuid")
        public String uuid;
        @SerializedName("mojang_token_hash")
        public String mojang_token_hash;
        @SerializedName("version")
        public String version;
        @SerializedName("client_time")
        public Long client_time;
        @SerializedName("client_random")
        public Long client_random;
        @SerializedName("server_time")
        public Long server_time;
        @SerializedName("server_random")
        public Long server_random;
        @SerializedName("lifespan")
        public Long lifespan;
        @SerializedName("hmac")
        public String hmac;
        public Handshake(String name, String uuid, String mojang_token_hash, String version, Long client_time, Long client_random, Long server_time, Long server_random, Long lifespan, String hmac){
            this.name = name;
            this.uuid = uuid;
            this.mojang_token_hash = mojang_token_hash;
            this.version = version;
            this.client_time = client_time;
            this.client_random = client_random;
            this.server_time = server_time;
            this.server_random = server_random;
            this.lifespan = lifespan;
            this.hmac = hmac;
        }
    }
}
