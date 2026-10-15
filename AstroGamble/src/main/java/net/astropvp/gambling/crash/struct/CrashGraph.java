package net.astropvp.gambling.crash.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @AllArgsConstructor
public class CrashGraph {

    private UUID playerUUID;
    @Setter
    private List<CrashGame> crashGames;

}
