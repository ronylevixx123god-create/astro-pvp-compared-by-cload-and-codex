package net.astropvp.gambling.roulette.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RouletteStats implements Serializable {

    private UUID playerUUID;

    @Setter
    private List<RouletteGame> rouletteGames;

}
