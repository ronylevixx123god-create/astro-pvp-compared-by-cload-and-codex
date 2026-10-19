package net.crankedpvp.staff.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import de.tr7zw.nbtapi.NBTItem;
import me.astropvp.proxypach.ProxyPatch;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomTeleportListener implements Listener {

    private final List<Player> cooldown = new ArrayList<>();

    private final AstroCore plugin;
    public RandomTeleportListener(AstroCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void randomTeleport(PlayerInteractEvent event) {
        if(event.getItem() == null || event.getItem().getType() == Material.AIR) return;

        ItemStack itemStack = event.getItem();
        NBTItem nbtItem = new NBTItem(itemStack);
        Player player = event.getPlayer();
        if(!nbtItem.getString("staffModeItem").equals("randomPlayerSelector")) return;
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        event.setCancelled(true);

        teleportToRandomPlayer(player);
    }

    void teleportToRandomPlayer(Player player) {
        if(cooldown.contains(player)) return;

        List<Player> possibleSelectedPlayers = player.getWorld().getPlayers()
                .stream()
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                //.filter(possiblePlayer -> !PLAYERS.contains(possiblePlayer.getName()))
                //.filter(loopPlayer -> WorldGuardUtils.canPvPInRegion(player))
                .collect(Collectors.toList());
        if (Bukkit.getPluginManager().isPluginEnabled("ProxyPatch")) {
            possibleSelectedPlayers.removeIf(checkPlayer -> ProxyPatch.playerNames.contains(checkPlayer.getName()));
        }
        if (possibleSelectedPlayers.isEmpty()) {
            player.sendMessage(CC.translate(AstroCore.prefix + "&cNo eligible players are available in this world."));
            return;
        }
        Player selectedPlayer = possibleSelectedPlayers.get(ThreadLocalRandom.current().nextInt(possibleSelectedPlayers.size()));
        if (!player.teleport(selectedPlayer, PlayerTeleportEvent.TeleportCause.UNKNOWN)) return;
        player.sendMessage(CC.translate(AstroCore.prefix + "&eYou have been teleported to &6" + selectedPlayer.getName()));
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);

        cooldown.add(player);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> cooldown.remove(player), 2L);
    }
}
