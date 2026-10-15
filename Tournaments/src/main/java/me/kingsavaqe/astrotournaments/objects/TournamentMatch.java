package me.kingsavaqe.astrotournaments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter @Setter @AllArgsConstructor
public class TournamentMatch {

    private TournamentPlayer playerOne;
    private TournamentPlayer playerTwo;
    private Arena arena;

}
