
package net.astropvp.lootboxes.gui;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.lootboxes.lootbox.AstroRewardItem;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;

import com.cosmicpvp.cosmicutils.inventory.content.SlotIterator;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class LootboxPlayerViewGui
implements InventoryProvider {
    private final Map<UUID, Lootbox> playerViewMap = Maps.newHashMap();
    private final Set<UUID> notClosing = Sets.newHashSet();
    private final LootboxManager lootboxManager;
    private final SmartInventory inventory;

    public LootboxPlayerViewGui(InventoryManager inventoryManager, LootboxManager lootboxManager) {
        this.lootboxManager = lootboxManager;
        this.inventory = SmartInventory.builder().id("lootbox-playerview").manager(inventoryManager).provider(this).size(6, 9).type(InventoryType.CHEST).title("Viewing Lootbox").build();
    }

    public void init(Player player, InventoryContents contents) {
        Lootbox lootbox = this.playerViewMap.get(player.getUniqueId());
        int totalPages = (int)Math.ceil((double)lootbox.getRandomRewards().size() / 45.0);
        contents.pagination().setItemsPerPage(45);
        contents.set(5, 8, ClickableItem.of(CommonsUtil.createItem(Material.ARROW, CC.translate("&aForward"), CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")), event -> this.inventory.open(player, contents.pagination().next().getPage())));
        contents.set(5, 0, ClickableItem.of(CommonsUtil.createItem(Material.ARROW, CC.translate("&aBackward"), CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")), event -> this.inventory.open(player, contents.pagination().previous().getPage())));
        ArrayList<ClickableItem> items = Lists.newArrayList();
        lootbox.getRandomRewards().forEach(rewardItem -> items.add(ClickableItem.empty(AstroRewardItem.removeMetaLines(rewardItem.generatePlayerViewableStack(this.lootboxManager), true))));
        contents.pagination().setItems(items.toArray(new ClickableItem[lootbox.getRandomRewards().size()]));
        contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }

    public Map<UUID, Lootbox> getPlayerViewMap() {
        return this.playerViewMap;
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

