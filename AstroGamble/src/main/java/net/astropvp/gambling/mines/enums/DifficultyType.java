package net.astropvp.gambling.mines.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DifficultyType {
    EASY(8,  new double[] { 1D, 1.08D, 1.16D, 1.26D, 1.43D, 1.68D, 1.831D, 2.38D, 3.1D, 3.96D, 4.56D, 5.24D, 6.3D, 7.1D, 7.93D, 9.16D, 10.5D, 12D, 15D, 21D, 30D, 50D, 75D, 125D}),
    MEDIUM(17, new double[] { 1D, 1.85D, 2.43D, 3.73D, 5.22D, 7.74D, 10.69D, 14.14D, 21D, 30D, 50D, 90D, 145D, 200D, 270D, 360D, 450D, 600D}),
    HARD(25, new double[] { 1D, 2.7D, 5.4D, 9.2D, 14.6D, 20.5D, 28.7D, 40D, 60.5D, 100D, 120D, 210D, 330D, 475D, 665D, 1000D}),
    ;

    private final int mines;
    private final double[] multiplier;
}
