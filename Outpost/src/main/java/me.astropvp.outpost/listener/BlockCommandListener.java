package me.astropvp.outpost.listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class BlockCommandListener implements Listener {
    private static List<String> allowedCommands = Lists.newArrayList();

    static {
        allowedCommands.addAll(Arrays.asList("msg", "message", "staffmode", "staff", "freeze", "lms", "ft", "filter", "r", "bless", "blessme", "response", "space", "bless", "heal", "cf", "coinflip", "jackpot",
                "feed", "f", "fix", "filter edit", "filter toggle", "ft", "bless", "r", "reply", "spawn", "warp", "pots", "filters", "outpost", "outposts", "pin"));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().contains("outpost")) {
            return;
        }

        if (player.isOp()) {
            return;
        }

        if (player.hasPermission("astrocore.staffmode")) {
            return;
        }
        if (allowedCommands.contains(event.getMessage().split(" ")[0].replace("/", "").toLowerCase())) {
            return;
        }
        player.sendMessage(CC.translate(  "&c&l(!) &c" + event.getMessage() + " is denied in this world!"));
        event.setMessage("/null");
        event.setCancelled(true);
    }

}
