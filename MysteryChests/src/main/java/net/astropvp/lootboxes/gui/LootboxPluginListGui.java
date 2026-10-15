
package net.astropvp.lootboxes.gui;


import net.astropvp.lootboxes.manager.ForeignLootboxManager;
import net.astropvp.lootboxes.manager.LootboxCreationManager;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class LootboxPluginListGui
implements InventoryProvider {
    private final ForeignLootboxManager foreignLootboxManager;
    private final PluginLootboxManager pluginLootboxManager;
    private final LootboxCreationManager lootboxCreationManager;
    private final InventoryManager inventoryManager;
    private final Player player;
    private boolean isMoving = false;
    private final SmartInventory inventory;

    public LootboxPluginListGui(AstroLootboxes plugin, ForeignLootboxManager foreignLootboxManager, PluginLootboxManager pluginLootboxManager, LootboxCreationManager lootboxCreationManager, InventoryManager inventoryManager, SmartInventory parent, Player player) {
        this.foreignLootboxManager = foreignLootboxManager;
        this.pluginLootboxManager = pluginLootboxManager;
        this.lootboxCreationManager = lootboxCreationManager;
        this.inventoryManager = inventoryManager;
        this.player = player;
        this.inventory = SmartInventory.builder().title(pluginLootboxManager.getManagingPlugin().getName()).type(InventoryType.CHEST).size(CommonsUtil.roundInventorySize((int)pluginLootboxManager.getLootboxes().size()) / 9, 9).provider((InventoryProvider)this).manager(inventoryManager).id(UUID.randomUUID().toString()).listener(new InventoryListener(InventoryCloseEvent.class, event -> {
            if (this.isMoving) {
                this.isMoving = false;
                return;
            }
            Bukkit.getScheduler().runTask((Plugin)plugin, () -> parent.open(player));
        })).build();
        this.inventory.open(player);
    }

    public void init(Player player, InventoryContents contents) {
        this.pluginLootboxManager.getLootboxes().forEach(lootbox -> contents.add(ClickableItem.of((ItemStack) CommonsUtil.createItem((Material)lootbox.getDisplayItem().getType(), (String)lootbox.getDisplayName(), (String[])new String[]{"&7Left click to edit", "&7Right click to give"}), event -> {
            if (event.getClick() == ClickType.LEFT) {
                this.isMoving = true;
                this.lootboxCreationManager.startEditingSession(player, (Lootbox)lootbox, this.pluginLootboxManager);
            } else if (event.getClick() == ClickType.RIGHT) {
                if (!this.pluginLootboxManager.isHandleable()) {
                    player.sendMessage(CC.translate("&c&l(!) &cYou are unable to handle that lootbox!"));
                    return;
                }
                lootbox.giveItemTo(player, this.pluginLootboxManager);
            }
        })));
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }
}

