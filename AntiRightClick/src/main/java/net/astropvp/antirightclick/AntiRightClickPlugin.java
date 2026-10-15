package net.astropvp.antirightclick;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AntiRightClickPlugin extends JavaPlugin implements Listener {

    private final Map<Player, Integer> flags = new HashMap<>();
    private final List<Player> cooldown = new ArrayList<>();

    private static final int MAX_POTS_PER_SECOND = 18;
    private static final int CHECK_FOR_CLICKS = 30;
    private static final long CLICK_INTERVAL_MS = 1000 / MAX_POTS_PER_SECOND;

    private final Map<UUID, Long> lastPotTime = new HashMap<>();
    public static final Map<UUID, Integer> clickCount = new HashMap<>();
    private final Map<UUID, Integer> potCount = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        new ClickCountResetTask().runTaskTimer(this, 0, 20);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTryPot(PlayerInteractEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(event.getItem() == null || event.getItem().getType() == Material.AIR) return;
            if(event.getItem().getType() != Material.POTION) return;

            UUID playerId = event.getPlayer().getUniqueId();
            long currentTime = System.currentTimeMillis();

            clickCount.put(playerId, clickCount.getOrDefault(playerId, 0) + 1);

            if (!lastPotTime.containsKey(playerId) || !clickCount.containsKey(playerId)) {
                lastPotTime.put(playerId, currentTime);
                clickCount.put(playerId, 1);
                return;
            }
            long lastClick = lastPotTime.get(playerId);
            if (currentTime - lastClick < CLICK_INTERVAL_MS) {
                event.setCancelled(true);
                return;
                //sendToStaff(event.getPlayer());
            }
            lastPotTime.put(playerId, currentTime);
        }
    }

    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRightClick(PotionSplashEvent event) {
        if(!(event.getPotion().getShooter() instanceof Player)) return;
        Player player = (Player) event.getPotion().getShooter();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        lastPotTime.put(playerId, currentTime);
    }

     */


    private void sendToStaff(Player player) {
        if(this.cooldown.contains(player)) return;
        this.flags.putIfAbsent(player, 0);
        this.flags.put(player, this.flags.get(player) + 1);
        this.cooldown.add(player);
        Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("astrocore.staff")).forEach(p -> p.sendMessage(ChatColor.RED + player.getName() + " may be auto-right clicking (" + this.flags.get(player) + " flags)"));
        Bukkit.getScheduler().runTaskLater(this, () -> this.cooldown.remove(player), 20L);
    }
    private class ClickCountResetTask extends BukkitRunnable {
        @Override
        public void run() {
            clickCount.clear();
            checkPlayersWith20Clicks();
        }

        private void checkPlayersWith20Clicks() {
            for (UUID uuid : clickCount.keySet()) {
                if(clickCount.get(uuid) > CHECK_FOR_CLICKS) {
                    sendToStaff(Bukkit.getPlayer(uuid));
                }
            }
        }
    }
}