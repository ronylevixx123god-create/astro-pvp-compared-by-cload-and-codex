
package net.astropvp.lootboxes.gui.create;

import net.astropvp.lootboxes.config.RarityConfig;
import net.astropvp.lootboxes.pojo.Rarity;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.RewardItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class LootboxRarityGui
implements InventoryProvider {
    private final RewardItem item;
    private final LootboxRewardGui parent;
    private final SmartInventory inventory;

    public LootboxRarityGui(Player player, RewardItem item, LootboxRewardGui parent, InventoryManager manager, JavaPlugin plugin, int page) {
        this.item = item;
        this.parent = parent;
        this.inventory = SmartInventory.builder().clickable(false).id(UUID.randomUUID().toString()).closeable(false).manager(manager).provider((InventoryProvider)this).type(InventoryType.CHEST).size(Math.min((int)Math.ceil((double) RarityConfig.getRarities().size() / 9.0), 6), 9).listener(new InventoryListener(InventoryCloseEvent.class, event -> Bukkit.getScheduler().runTaskLater((Plugin)plugin, () -> this.parent.getInventory().open(player, page), 1L))).build();
        this.inventory.open(player);
    }

    public void init(Player player, InventoryContents contents) {
        RarityConfig.getRarities().forEach(rarity -> contents.add(ClickableItem.of(CommonsUtil.createItem((Material)Material.EYE_OF_ENDER, (String)rarity.getDisplayName(), (String[])new String[]{"&fChance: &e" + rarity.getChance() + "%"}), event -> {
            this.item.setRarity((Rarity)rarity);
            this.parent.settingRarity = false;
            this.parent.settingCommand = false;
            this.parent.settingValueItem = null;
            this.inventory.close(player);
        })));
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }
}

