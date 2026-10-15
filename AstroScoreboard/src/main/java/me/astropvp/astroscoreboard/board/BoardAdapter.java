package me.astropvp.astroscoreboard.board;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface BoardAdapter {
    List<String> getScoreboard(Player paramPlayer, Board paramBoard);

    String getTitle(Player paramPlayer);

    long getInterval();

    void onScoreboardCreate(Player paramPlayer, Scoreboard paramScoreboard);

    void preLoop();
}
