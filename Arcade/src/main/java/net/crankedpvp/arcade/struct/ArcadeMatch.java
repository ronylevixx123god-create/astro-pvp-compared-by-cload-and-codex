package net.crankedpvp.arcade.struct;

import lombok.Getter;
import net.crankedpvp.arcade.utils.Pair;

import java.util.UUID;

@Getter
public class ArcadeMatch {
    private final Pair<UUID, UUID> players;

    public ArcadeMatch(UUID uuid1, UUID uuid2) {
        players = new Pair<>(uuid1, uuid2);
    }
}
