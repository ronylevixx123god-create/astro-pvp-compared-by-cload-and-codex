
package net.astropvp.lootboxes.gui;


import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.lootboxes.manager.LootboxCreationManager;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.inventory.content.SlotIterator;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LootboxSearchGui
implements InventoryProvider {
    private final LootboxManager lootboxManager;
    private final SmartInventory inventory;
    private final List<Lootbox> lootboxes;
    private final LootboxCreationManager lootboxCreationManager;
    private final Map<PluginLootboxManager, List<Lootbox>> pluginManagedLootboxes;

    public LootboxSearchGui(LootboxManager lootboxManager, InventoryManager inventoryManager, LootboxCreationManager lootboxCreationManager, List<Lootbox> lootboxes, Map<PluginLootboxManager, List<Lootbox>> pluginManagedLootboxes) {
        this.lootboxManager = lootboxManager;
        this.lootboxes = lootboxes;
        this.lootboxCreationManager = lootboxCreationManager;
        this.pluginManagedLootboxes = pluginManagedLootboxes;
        this.inventory = SmartInventory.builder().type(InventoryType.CHEST).size(6, 9).id(UUID.randomUUID().toString()).manager(inventoryManager).provider(this).title("AstroLootboxes - Search").build();
    }

    public void init(Player player, InventoryContents contents) {
        int totalPages = (int)Math.ceil((double)this.lootboxes.size() / 45.0);
        contents.pagination().setItemsPerPage(45);
        contents.set(5, 8, ClickableItem.of(CommonsUtil.createItem(Material.ARROW, CC.translate("&aForward"), CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")), event -> this.inventory.open(player, contents.pagination().next().getPage())));
        contents.set(5, 0, ClickableItem.of(CommonsUtil.createItem(Material.ARROW, CC.translate("&aBackward"), CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")), event -> this.inventory.open(player, contents.pagination().previous().getPage())));
        List<ClickableItem> items = Lists.newArrayList();
        this.lootboxes.forEach(lootbox -> items.add(ClickableItem.of(lootbox.getDisplayItem(), event -> {
            if (event.getClick() == ClickType.LEFT) {
                lootbox.giveItemTo(player, this.lootboxManager);
            } else if (event.getClick() == ClickType.RIGHT) {
                Bukkit.dispatchCommand(player, "dl edit " + lootbox.getName());
            }
        })));
        this.pluginManagedLootboxes.forEach((manager, lootboxes) -> lootboxes.forEach(lootbox -> items.add(ClickableItem.of(lootbox.getDisplayItem(), event -> {
            if (event.getClick() == ClickType.LEFT) {
                lootbox.giveItemTo(player, manager);
            } else if (event.getClick() == ClickType.RIGHT) {
                this.lootboxCreationManager.startEditingSession(player, lootbox, manager);
            }
        }))));
        contents.pagination().setItems(items.toArray(new ClickableItem[this.lootboxes.size()]));
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

