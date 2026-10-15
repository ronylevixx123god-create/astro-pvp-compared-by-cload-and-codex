package com.cosmicpvp.cosmicutils.utils;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

final class ParticleEffectPacket {
    ParticleEffect effect;
    Location l;
    float speed;
    int amount;
    float offsetX;
    float offsetY;
    float offsetZ;
    Player p;
    private Packet cachedPacket;

    ParticleEffectPacket(ParticleEffect pe, Location l, float speed, int amount) {
        this.effect = pe;
        this.l = l;
        this.speed = speed;
        this.amount = amount;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.offsetZ = 0.0f;
    }

    ParticleEffectPacket(Player pReciever, ParticleEffect pe, Location l, float speed, int amount) {
        this.p = pReciever;
        this.effect = pe;
        this.l = l;
        this.speed = speed;
        this.amount = amount;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.offsetZ = 0.0f;
    }

    ParticleEffectPacket(Player pReciever, ParticleEffect pe, Location l, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        this.p = pReciever;
        this.effect = pe;
        this.l = l;
        this.speed = speed;
        this.amount = amount;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    ParticleEffectPacket(Player pReciever, Location location, Packet cachedParticle) {
        this.p = pReciever;
        this.cachedPacket = cachedParticle;
        this.l = location;
    }

    public Packet getPacket() throws Exception {
        if (this.cachedPacket != null) {
            return this.cachedPacket;
        }
        return (Packet) ParticleEffect.createPacket(this.effect, this.l, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.amount);
    }

    public ParticleEffect getEffect() {
        return this.effect;
    }

    public Location getL() {
        return this.l;
    }

    public float getSpeed() {
        return this.speed;
    }

    public int getAmount() {
        return this.amount;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public float getOffsetZ() {
        return this.offsetZ;
    }

    public Player getP() {
        return this.p;
    }
}

