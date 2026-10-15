
package net.astropvp.lootboxes.gui;


import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.inventory.content.SlotIterator;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.LootboxManager;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LootboxViewGui
implements InventoryProvider {
    private final LootboxManager lootboxManager;
    private final SmartInventory inventory;

    public LootboxViewGui(LootboxManager lootboxManager, InventoryManager inventoryManager) {
        this.lootboxManager = lootboxManager;
        this.inventory = SmartInventory.builder().type(InventoryType.CHEST).size(6, 9).id("lootbox-view").manager(inventoryManager).provider(this).title("AstroLootboxes - View").build();
    }

    public void init(Player player, InventoryContents contents) {
        int totalPages = (int)Math.ceil((double)this.lootboxManager.getLootboxes().size() / 45.0);
        contents.pagination().setItemsPerPage(45);
        contents.set(5, 8, ClickableItem.of(CommonsUtil.createItem(Material.ARROW, CC.translate("&aForward"), CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")), event -> this.inventory.open(player, contents.pagination().next().getPage())));
        contents.set(5, 0, ClickableItem.of(CommonsUtil.createItem(Material.ARROW, CC.translate("&aBackward"), CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")), event -> this.inventory.open(player, contents.pagination().previous().getPage())));
        ArrayList<ClickableItem> items = Lists.newArrayList();
        this.lootboxManager.getLootboxes().forEach(lootbox -> items.add(ClickableItem.of(lootbox.getDisplayItem(), event -> {
            if (event.getClick() == ClickType.LEFT) {
                lootbox.giveItemTo(player, this.lootboxManager);
            } else if (event.getClick() == ClickType.RIGHT) {
                Bukkit.dispatchCommand(player, "lootbox edit " + lootbox.getName());
            }
        })));
        contents.pagination().setItems(items.toArray(new ClickableItem[this.lootboxManager.getLootboxes().size()]));
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

