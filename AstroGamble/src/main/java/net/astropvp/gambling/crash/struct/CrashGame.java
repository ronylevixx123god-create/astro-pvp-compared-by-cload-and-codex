package net.astropvp.gambling.crash.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter @AllArgsConstructor
public class CrashGame {
    private UUID crashGameID;
    private List<CrashPlayer> players;
    private double crashX;
}
