package net.crankedpvp.hypeboxes.listeners;

import com.cosmicpvp.cosmicutils.utils.StringUtils;
import de.tr7zw.nbtapi.NBTItem;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.inventory.PreviewLootboxInventory;
import net.crankedpvp.hypeboxes.language.Language;
import net.crankedpvp.hypeboxes.struct.HypeBoxManager;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HypeboxPreviewListener implements Listener {

    private final HypeboxesPlugin plugin;
    public HypeboxPreviewListener(HypeboxesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getItem() == null) return;
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (HypeBoxManager.isHypebox(event.getItem())) {
                event.setCancelled(true);
                final NBTItem nbtItem = new NBTItem(event.getItem());
                final String name = nbtItem.getCompound("cosmicData").getString("hypeboxID");
                PreviewLootboxInventory previewLootInventory = new PreviewLootboxInventory(event.getPlayer(), name, this.plugin);
                previewLootInventory.getUI().open(event.getPlayer());
                event.getPlayer().sendMessage(Language.PREFIX.getMessage() + Language.PREVIEW_LOOT.getMessage().replace("%lootbox%", StringUtils.toNiceString(name)));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.CHEST_OPEN, 1.0F, 2.0F);
            }
        }
    }

}