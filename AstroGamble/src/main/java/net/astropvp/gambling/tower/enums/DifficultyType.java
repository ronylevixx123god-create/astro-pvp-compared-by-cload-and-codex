package net.astropvp.gambling.tower.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DifficultyType {
    EASY(new double[] { 1D, 1.5D, 2.1D, 2.75D, 3.5D, 4.2D, 5D}),
    MEDIUM(new double[] { 1D, 1.85D, 3.63D, 7.44D, 15.52D, 31.64D, 65.39D}),
    HARD(new double[] { 1D, 2.8D, 6.5D, 14.3D, 28.6D, 57.5D, 115.7D}),
    ;

    private final double[] multiplier;
}
