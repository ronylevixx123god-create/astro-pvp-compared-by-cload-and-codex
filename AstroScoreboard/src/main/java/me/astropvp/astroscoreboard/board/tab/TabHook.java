package me.astropvp.astroscoreboard.board.tab;
import org.bukkit.*;

public class TabHook
{
    private final boolean tabExists;
    private MainScoreboard mainScoreboard;

    public TabHook() {
        this.tabExists = Bukkit.getPluginManager().isPluginEnabled("TAB");
        if (this.tabExists) {
            this.mainScoreboard = new MainScoreboard();
        }
    }

    public boolean isTabExists() {
        return this.tabExists;
    }

    public MainScoreboard getMainScoreboard() {
        return this.mainScoreboard;
    }
}
