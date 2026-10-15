package net.astropvp.gambling.roulette.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RouletteGame implements Serializable {
    private UUID rouletteGameID;
    private List<RoulettePlayer> players;
    private RouletteColour rouletteColour;
}
