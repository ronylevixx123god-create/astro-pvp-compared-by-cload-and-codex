package net.astropvp.gambling.crash.manager;

import lombok.Getter;
import lombok.Setter;

import  net.astropvp.gambling.crash.struct.CrashGraph;
import net.astropvp.gambling.utils.Pair;
import org.bukkit.entity.Player;
import net.astropvp.gambling.GamblingPlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CrashManager {

    private final GamblingPlugin plugin;
    public CrashManager(GamblingPlugin plugin) {
        this.plugin = plugin;
    }

    @Getter
    private final Map<UUID, CrashGraph> playerCrashGraphMap = new HashMap<>();

    @Getter @Setter
    private boolean active;

    @Getter @Setter
    private double currentCrashX;

    @Getter @Setter
    private double finalCrashX;

    @Getter
    private final Map<Player, Double> bets = new HashMap<>();

    @Getter
    private final Map<Player, Double> waitingToJoinCrash = new HashMap<>();

    @Getter
    private final Map<Player, Pair<Double, Double>> cashedAt = new HashMap<>();

    @Getter
    private final LinkedList<Double> recentCrashes = new LinkedList<>();

    @Getter
    private final Map<Player, Double> lastBets = new HashMap<>();

    @Getter
    private final Map<Integer, Integer> lastBigCrashes = new HashMap<>();


    public void setupLastBigCrashes() {
        this.lastBigCrashes.put(1000, 0);
        this.lastBigCrashes.put(500, 0);
        this.lastBigCrashes.put(250, 0);
        this.lastBigCrashes.put(100, 0);
        this.lastBigCrashes.put(50, 0);
        this.lastBigCrashes.put(25, 0);
        this.lastBigCrashes.put(10, 0);
        this.lastBigCrashes.put(5, 0);
    }

    public void randomizeFinalCrashX() {
        if(Math.random() > 0.97) {
            this.setFinalCrashX(1.0D);
            return;
        }

        if(Math.random() > 0.9997) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(351, 1250));
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 90) {
            this.setFinalCrashX(1 + (Math.random() / 3));
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 50) {
            this.setFinalCrashX(1 + Math.random());
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 50) {
            this.setFinalCrashX(2 + Math.random());
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 45) {
            this.setFinalCrashX(3 + Math.random());
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 40) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(4, 7));
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 35) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(8, 17));
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 30) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(18, 41) + Math.random());
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 25) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(42, 101) + Math.random());
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 20) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(102, 254) + Math.random());
            return;
        }

        if(ThreadLocalRandom.current().nextInt(100) > 15) {
            this.setFinalCrashX(ThreadLocalRandom.current().nextDouble(255, 498) + Math.random());
            return;
        }

        this.setFinalCrashX(ThreadLocalRandom.current().nextInt(499, 1250) + Math.random());
    }

    public boolean hasBetted(Player player) {
        return bets.containsKey(player);
    }


    /*
    public void save() {
        for (Map.Entry<UUID, CrashGraph> entry : playerCrashGraphMap.entrySet()) {
            UUID uuid = entry.getKey();
            CrashGraph graph = entry.getValue();
            plugin.getGambleManager().getGambleUser(uuid).setCrashGraph(graph);
        }
    }

    public void load() {
        for (GambleUser gambleUser : plugin.getGambleManager().getCacheValues()) {
            playerCrashGraphMap.put(gambleUser.getUuid(), gambleUser.getCrashGraph());
        }
    }
     */



}
