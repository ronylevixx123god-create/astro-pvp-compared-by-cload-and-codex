package me.astropvp.astrospawners.utils;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;

    private int coins;

    public PlayerData(UUID uuid, int coins) {
        this.uuid = uuid;
        this.coins = coins;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getCoins() {
        return this.coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
