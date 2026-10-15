package net.astropvp.astroevents.game.players;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.astroevents.game.MiniGamePlayer;
import org.bukkit.entity.Player;

@Getter
public class ParkourPlayer extends MiniGamePlayer {

    @Setter
    private Byte currentCheckpoint;

    public ParkourPlayer(Player player) {
        super(player);
        this.currentCheckpoint = 0;
    }
}