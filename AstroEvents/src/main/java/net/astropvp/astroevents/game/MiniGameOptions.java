package net.astropvp.astroevents.game;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MiniGameOptions {

    @Builder.Default
    private final boolean foodLoss = false;
    @Builder.Default
    private final boolean blockPlace = false;
    @Builder.Default
    private final boolean blockBreak = false;
    @Builder.Default
    private final boolean fallDamage = false;
    @Builder.Default
    private final boolean itemPickUp = false;
    @Builder.Default
    private final boolean itemDrop = false;
    @Builder.Default
    private final boolean blockCommands = true;
    @Builder.Default
    private final boolean inventoryInteract = false;
    @Builder.Default
    private final String[] excludedBlockedCommands = {"parkour"};
}
