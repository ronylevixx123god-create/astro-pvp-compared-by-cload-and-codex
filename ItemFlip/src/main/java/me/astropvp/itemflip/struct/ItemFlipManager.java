package me.astropvp.itemflip.struct;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import java.util.List;

public class ItemFlipManager
{
    private static final List<ItemFlipMatch> matches;

    public static boolean isInMatch(final Player player) {
        return getMatch(player) != null;
    }

    public static ItemFlipMatch getMatch(final Player player) {
        return ItemFlipManager.matches.stream().filter(m -> m.containsPlayer(player)).findFirst().orElse(null);
    }

    public static void addMatch(final ItemFlipMatch match) {
        ItemFlipManager.matches.add(match);
    }

    public static List<ItemFlipMatch> getMatches() {
        return ItemFlipManager.matches;
    }

    static {
        matches = Lists.newArrayList();
    }
}
