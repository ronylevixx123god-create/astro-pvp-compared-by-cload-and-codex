package net.crankedpvp.shards;

import net.crankedpvp.shards.data.MobcoinsUser;
import net.crankedpvp.shards.managers.MobcoinManager;
import org.bukkit.entity.Player;

public class MobcoinAPI {
    private static final MobcoinPlugin plugin = MobcoinPlugin.getInstance();
    private static final MobcoinManager shardsManager = plugin.getShardsManager();

    public static void addMobcoins(Player player, double amount) {
        if (player == null) return;
        shardsManager.getShardsUser(player.getUniqueId()).addShards(amount);
    }

    public static void removeMobcoins(Player player, double amount) {
        if (player == null) return;
        shardsManager.getShardsUser(player.getUniqueId()).removeShards(amount);
    }

    public static void resetMobcoins(Player player) {
        if (player == null) return;
        shardsManager.getShardsUser(player.getUniqueId()).resetShards();
    }

    public static long getBalance(Player player) {
        if (player == null) return 0;
        return shardsManager.getShardsUser(player.getUniqueId()).getBalance();
    }

    public static MobcoinsUser getMobcoinUser(Player player) {
        return shardsManager.getShardsUser(player.getUniqueId());
    }
}
