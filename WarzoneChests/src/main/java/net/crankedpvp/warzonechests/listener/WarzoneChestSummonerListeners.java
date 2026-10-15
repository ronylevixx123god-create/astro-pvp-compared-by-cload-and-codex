package net.crankedpvp.warzonechests.listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import net.crankedpvp.warzonechests.WarzoneChestsPlugin;
import net.crankedpvp.warzonechests.struct.WarzoneChestManager;
import net.crankedpvp.warzonechests.struct.items.WarzoneChestSummoner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class WarzoneChestSummonerListeners implements Listener {

    private final WarzoneChestManager warzoneChestManager;

    public WarzoneChestSummonerListeners(WarzoneChestsPlugin plugin) {
        this.warzoneChestManager = plugin.getWarzoneChestManager();
    }

    @EventHandler
    public void onWarzoneChestSummonerInteract(PlayerInteractEvent event) {
        if (!WarzoneChestSummoner.isWarzoneChestPlace(event.getItem())) return;
        event.getPlayer().setItemInHand(ItemUtils.decreaseItemStackAmountByOne(event.getItem()));
        this.warzoneChestManager.resetWarzoneChestsForPlayer(event.getPlayer());
        event.getPlayer().sendMessage(CC.translate("&e&lWarzone Chests &8➥ &7All your Warzone Chests have been reset, you can now go loot them again"));
    }
}
