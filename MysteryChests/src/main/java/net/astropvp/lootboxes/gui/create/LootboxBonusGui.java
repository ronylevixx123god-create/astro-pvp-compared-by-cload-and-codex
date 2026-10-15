
package net.astropvp.lootboxes.gui.create;

import net.astropvp.lootboxes.lootbox.AstroRewardItem;
import net.astropvp.lootboxes.lootbox.IncompleteLootbox;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class LootboxBonusGui
implements InventoryProvider {
    private final IncompleteLootbox incompleteLootbox;
    private final SmartInventory inventory;

    public LootboxBonusGui(JavaPlugin plugin, InventoryManager inventoryManager, LootboxCreateGui parent, IncompleteLootbox lootbox, Player player) {
        this.incompleteLootbox = lootbox;
        this.inventory = SmartInventory.builder().id(UUID.randomUUID().toString()).type(InventoryType.CHEST).size(6, 9).manager(inventoryManager).title("AstroLootboxes - Bonus").provider(this).clickable(true).parent(parent.getInventory()).listener(new InventoryListener(InventoryCloseEvent.class, event -> {
            this.incompleteLootbox.setBonusItems(Lists.newArrayList());
            for (ItemStack content : ((InventoryCloseEvent)event).getInventory().getContents()) {
                if (content == null || content.getType() == Material.AIR) continue;
                this.incompleteLootbox.getBonusItems().add(new AstroRewardItem(content));
            }
            Bukkit.getScheduler().runTask((Plugin)plugin, () -> parent.getInventory().open((Player)((InventoryCloseEvent)event).getPlayer()));
        })).build();
        this.inventory.open(player);
    }

    public void init(Player player, InventoryContents contents) {
        try {
            this.incompleteLootbox.getBonusItems().forEach(item -> contents.add(ClickableItem.empty((ItemStack)item.getReward())));
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    public void update(Player player, InventoryContents contents) {
    }
}

