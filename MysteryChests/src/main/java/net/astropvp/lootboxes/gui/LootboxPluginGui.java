
package net.astropvp.lootboxes.gui;

import net.astropvp.lootboxes.manager.ForeignLootboxManager;
import net.astropvp.lootboxes.manager.LootboxCreationManager;
import net.astropvp.lootboxes.pojo.PluginInformation;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.UUID;

public class LootboxPluginGui
implements InventoryProvider {
    private final AstroLootboxes plugin;
    private final ForeignLootboxManager foreignLootboxManager;
    private final LootboxCreationManager lootboxCreationManager;
    private final InventoryManager inventoryManager;
    private final SmartInventory inventory;

    public LootboxPluginGui(AstroLootboxes plugin, ForeignLootboxManager foreignLootboxManager, LootboxCreationManager lootboxCreationManager, InventoryManager inventoryManager, Player player) {
        this.plugin = plugin;
        this.foreignLootboxManager = foreignLootboxManager;
        this.lootboxCreationManager = lootboxCreationManager;
        this.inventoryManager = inventoryManager;
        this.inventory = SmartInventory.builder().type(InventoryType.CHEST).size(CommonsUtil.roundInventorySize((int)foreignLootboxManager.getPluginManagers().size()) / 9, 9).provider((InventoryProvider)this).manager(inventoryManager).title("AstroLootboxes - Plugin").id(UUID.randomUUID().toString()).build();
        this.inventory.open(player);
    }

    public void init(Player player, InventoryContents contents) {
        this.foreignLootboxManager.getDescribedPluginManagers().forEach(pair -> {
            PluginInformation pluginInformation = pair.getValue0();
            PluginLootboxManager lootboxManager = pair.getValue1();
            boolean enabled = Bukkit.getPluginManager().isPluginEnabled(pluginInformation.getPluginName());
            contents.add(ClickableItem.of(CommonsUtil.createItem(Material.BOOK, (enabled ? "&a" : "&c") + pluginInformation.getPluginName(), "&aManaged Lootboxes: &7" + lootboxManager.getLootboxes().size(), "&aVersion: &7" + pluginInformation.getPluginVersion(), "&aAuthor(s): &7" + StringUtils.join(pluginInformation.getAuthors(), (String)","), "&aDependencies: &7" + StringUtils.join(pluginInformation.getDependencies(), (String)",")), event -> new LootboxPluginListGui(this.plugin, this.foreignLootboxManager, lootboxManager, this.lootboxCreationManager, this.inventoryManager, this.inventory, player)));
        });
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }
}

