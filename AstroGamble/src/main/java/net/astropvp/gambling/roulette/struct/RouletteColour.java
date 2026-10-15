package net.astropvp.gambling.roulette.struct;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum RouletteColour {

    RED(2, (short)14, "&c"),
    BLACK(2, (short) 15, "&0"),
    GREEN(8, (short) 5, "&a"),
    ;


    private final int multiplier;
    private final short glassColour;
    private final String colour;
}
