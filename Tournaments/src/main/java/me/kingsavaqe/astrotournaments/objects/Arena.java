package me.kingsavaqe.astrotournaments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.kingsavaqe.astrotournaments.utils.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter @AllArgsConstructor @Setter
public class Arena {

    private String id;
    private Pair<Location, Location> locations;
    private Pair<TournamentPlayer, TournamentPlayer> currentlyFighting;
    private int currentTimeStamp;
    private boolean inUse;

    public void reset() {
        this.currentlyFighting = new Pair<>(null, null);
        this.inUse = false;
        this.currentTimeStamp = 0;
    }

}
