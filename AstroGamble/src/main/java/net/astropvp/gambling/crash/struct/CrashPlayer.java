package net.astropvp.gambling.crash.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter @AllArgsConstructor
public class CrashPlayer {
    private UUID playerUUID;
    private Double money;
    private Double cashout;
    private Double crashGameCrashX;

    public boolean isWinner() {
        if(cashout == null) return false;
        return cashout <= crashGameCrashX;
    }

    public Double getWinnings() {
        if(!isWinner()) return null;
        return money * cashout;
    }
}
