package net.astropvp.gambling.roulette.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter @AllArgsConstructor
public class RoulettePlayer implements Serializable {
    private UUID playerUUID;
    private Double money;
    private RouletteColour rouletteColour;
    private RouletteColour gameEndedOn;

    public boolean isWinner() {
        if(gameEndedOn == null) return false;
        return rouletteColour == gameEndedOn;
    }

    public Double getWinnings() {
        if(!isWinner()) return null;
        return money * rouletteColour.getMultiplier();
    }
}
