package net.astropvp.gambling.bombs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum DifficultyType {
    EASY(4),
    MEDIUM(18),
    HARD(49),
    ;

    private final int bombs;
}
