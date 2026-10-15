package me.kingsavaqe.astroenvoy.listeners;


import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OpenEnvoyCrateListener implements Listener {

    private final EnvoyPlugin plugin;
    public OpenEnvoyCrateListener(EnvoyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void openEnvoyCrate(PlayerInteractEvent event) {
        if(event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.CHEST) return;
        Location clickedBlockLocation = event.getClickedBlock().getLocation();
        if(!this.plugin.getEnvoyManager().getLocations().contains(clickedBlockLocation)) return;
        event.setCancelled(true);
        this.plugin.getEnvoyManager().removeCrateAt(clickedBlockLocation);
        this.plugin.getEnvoyManager().setRemainingCrates(this.plugin.getEnvoyManager().getRemainingCrates() - 1);
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.FIZZ, 2.0F, 2.0F);
        PlayerUtil.giveOrDropProtectedItem(player, LootTableAPI.getRandomLootItem(LootTable.ENVOY), 15);
        PlayerUtil.giveOrDropProtectedItem(player, LootTableAPI.getRandomLootItem(LootTable.ENVOY), 15);
        //PlayerUtil.giveOrDropProtectedItem(player, LootTableAPI.getRandomLootItem(LootTableType.ENVOY), 15);
        if(this.plugin.getEnvoyManager().getRemainingCrates() <= 0) {
            this.plugin.getEnvoyManager().endEnvoyEvent();
        }

    }
}
