package me.astropvp.astroscoreboard.board.tab;
import com.google.common.collect.*;
import org.bukkit.entity.*;
import me.neznamy.tab.api.*;
import me.neznamy.tab.api.scoreboard.*;
import java.util.*;
import java.util.regex.*;

public class MainScoreboard {

    private final Scoreboard scoreboard;

    public MainScoreboard() {
        this.scoreboard = TabAPI.getInstance().getScoreboardManager().getRegisteredScoreboards().get("main_scoreboard");
    }

    public void addPlayer(final Player player) {
        final TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
        TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, this.scoreboard);
    }

    public void removePlayer(final Player player) {
        final TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
        TabAPI.getInstance().getScoreboardManager().resetScoreboard(tabPlayer);
    }

    public void updateBoard() {
        for (Map.Entry<Integer, String> entry : customLines().entrySet()) {
            int pos = entry.getKey();
            String customLine = entry.getValue();
            this.scoreboard.getLines().get(pos).setText(customLine);
        }
    }

    private Map<Integer, String> customLines() {
        final Map<Integer, String> customLines = new HashMap<>();

        customLines.put(11, "Line 3");
        customLines.put(12, "Line 4");
        customLines.put(13, "Line 4");

        return customLines;
    }


}
