package net.astropvp.astroevents.game;

import lombok.Getter;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.games.ParkourMiniGame;
import net.astropvp.astroevents.game.games.SumoMiniGame;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.Map;

public class MiniGameRegister {

    private final AstroEvents plugin;
    @Getter
    private final Map<MiniGameType, MiniGame<?>> miniGames;

    public MiniGameRegister(AstroEvents plugin) {
        this.plugin = plugin;
        this.miniGames = new HashMap<>();
    }

    public void initialize() {
        this.registerAll(new ParkourMiniGame(plugin), new SumoMiniGame(plugin));
    }

    private void registerAll(MiniGame<?>... miniGames) {
        for (MiniGame<?> miniGame : miniGames) {
            register(miniGame);
        }
    }

    private void register(MiniGame<?> miniGame) {
        try {
            this.miniGames.put(miniGame.getMiniGameType(), miniGame);

            plugin.getServer().getPluginManager().registerEvents(miniGame, plugin);

        } catch (Exception exception) {
            System.err.println("Failed to register minigame: " + miniGame.getMiniGameType().name());
        }
    }

    public void unregisterAll() {
        this.miniGames.forEach(((miniGameType, miniGame) -> {
            miniGame.stopMiniGame();
            HandlerList.unregisterAll(miniGame);
        }));
        this.miniGames.clear();
    }

    private void unregister(MiniGame<?> miniGame) {
        miniGame.stopMiniGame();
        HandlerList.unregisterAll(miniGame);
        this.miniGames.remove(miniGame.getMiniGameType());
    }

    public void reloadMiniGames() {
        this.unregisterAll();
        this.initialize();
    }

    public MiniGame<?> getMiniGame(MiniGameType miniGameType) {
        return this.miniGames.get(miniGameType);
    }
}
