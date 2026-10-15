package org.demonicpvp.demonictrials.manager;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TrialManager {
    private static final Set<TrialSession> trials = Sets.newHashSet();
    public static int NEXT_TRIAL_ID = 0;

    public static Optional<TrialSession> getTrialSession(Player player) {
        return trials.stream().filter(trial -> trial.getPlayers().contains(player)).findFirst();
    }



    public static void addTrial(TrialSession session) {
        trials.add(session);
        Bukkit.getServer().getPluginManager().registerEvents(session, DemonicTrials.getInstance());
    }

    public static void removeTrial(TrialSession session) {
        trials.remove(session);
        HandlerList.unregisterAll(session);
    }

    public static Optional<TrialSession> getTrial(int id) {
        return trials.stream().filter(trial -> trial.getId() == id).findFirst();
    }

    public static Optional<TrialSession> getTrial(Player player) {
        if (!player.isDead() && !player.getWorld().getName().startsWith("trials_")) {
            return Optional.empty();
        }

        return trials.stream().filter(trial -> trial.getPlayers().contains(player)).findFirst();
    }

    public static Set<TrialSession> getTrials() {
        return trials;
    }




    public static List<World> getAvailableWorlds() {
        return Bukkit.getWorlds()
                .stream()
                .filter(w -> w.getName().startsWith("trials_") && trials.stream().noneMatch(trial -> trial.getWorld().getName().equals(w.getName())))
                .collect(Collectors.toList());
    }
}
