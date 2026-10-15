
package net.astropvp.lootboxes.gui.create;

import net.astropvp.lootboxes.lootbox.IncompleteLootbox;
import net.astropvp.lootboxes.manager.LootboxCreationManager;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.LootboxManager;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class LootboxCreateGui
implements InventoryProvider,
Listener {
    private final Map<UUID, String> settingProperties = Maps.newHashMap();
    private final LootboxManager lootboxManager;
    private final InventoryManager inventoryManager;
    private final LootboxCreationManager lootboxCreationManager;
    private final JavaPlugin plugin;
    private final SmartInventory inventory;

    public LootboxCreateGui(LootboxManager lootboxManager, InventoryManager inventoryManager, LootboxCreationManager lootboxCreationManager, JavaPlugin plugin) {
        this.lootboxManager = lootboxManager;
        this.inventoryManager = inventoryManager;
        this.lootboxCreationManager = lootboxCreationManager;
        this.plugin = plugin;
        this.inventory = SmartInventory.builder().title("AstroLootboxes - Create").provider(this).manager(inventoryManager).size(6, 9).type(InventoryType.CHEST).id("lootbox-create").build();
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public void init(Player player, InventoryContents contents) {
        IncompleteLootbox incompleteLootbox = this.lootboxCreationManager.getSession(player.getUniqueId()).get().getValue0();
        ItemStack paneItem = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta paneItemMeta = paneItem.getItemMeta();
        paneItemMeta.setDisplayName("");
        paneItem.setItemMeta(paneItemMeta);
        paneItem.setDurability((short)7);
        contents.fill(ClickableItem.empty(paneItem));
        contents.set(1, incompleteLootbox.isBundle() ? 2 : 1, ClickableItem.of(CommonsUtil.createItem(Material.ENDER_CHEST, "&a&lEdit Rewards", "&7&lCurrently: &f" + incompleteLootbox.getRewardItems().size()), event -> new LootboxRewardGui(this.plugin, this.inventoryManager, this, incompleteLootbox, player)));
        if (!incompleteLootbox.isBundle()) {
            contents.set(1, 2, ClickableItem.of(CommonsUtil.createItem(Material.CHEST, "&a&lEdit Bonus Rewards", "&7&lCurrently: &f" + incompleteLootbox.getBonusItems().size()), event -> new LootboxBonusGui(this.plugin, this.inventoryManager, this, incompleteLootbox, player)));
        }
        if (!incompleteLootbox.isBundle()) {
            contents.set(1, 3, ClickableItem.of(CommonsUtil.createItem(Material.GOLD_NUGGET, "&a&lEdit Reward count", "&7&lCurrently: &f" + incompleteLootbox.getRewardCount()), event -> {
                this.settingProperties.put(player.getUniqueId(), "rewardCount");
                player.closeInventory();
            }));
        }
        contents.set(1, 5, ClickableItem.of(CommonsUtil.createItem(Material.PAPER, "&a&lEdit Name", "&7&lCurrently: &f" + incompleteLootbox.getName()), event -> {
            if (incompleteLootbox.isEditing()) {
                player.sendMessage(CC.translate("&c&l(!) &cYou cannot rename a lootbox while editing it!"));
                return;
            }
            this.settingProperties.put(player.getUniqueId(), "name");
            player.closeInventory();
        }));
        contents.set(1, 6, ClickableItem.of(CommonsUtil.createItem(Material.PAPER, "&a&lEdit Display Name", "&7&lCurrently: &f" + incompleteLootbox.getDisplayName()), event -> {
            this.settingProperties.put(player.getUniqueId(), "displayName");
            player.closeInventory();
        }));
        contents.set(1, 7, ClickableItem.of(CommonsUtil.createItem(Material.PAPER, "&a&lEdit Lootbox Lore"), event -> new LootboxLoreGui(this.plugin, this.inventoryManager, this, incompleteLootbox, player)));
        contents.set(2, 2, ClickableItem.of(CommonsUtil.createItem(incompleteLootbox.getChosenMaterial(), "&a&lEdit chosen material", "&7&lCurrently: &f" + incompleteLootbox.getChosenMaterial().name()), event -> {
            this.settingProperties.put(player.getUniqueId(), "material");
            player.sendMessage(CC.translate("&a&l(&f!&a&l) &aHold and click any item to choose your material!"));
            player.closeInventory();
        }));
        contents.set(2, 3, ClickableItem.of(CommonsUtil.createItem(Material.ENCHANTED_BOOK, "&a&lGlowing?", "&7&lCurrently: " + CommonsUtil.formatBoolean(incompleteLootbox.isGlowing())), event -> {
            incompleteLootbox.setGlowing(!incompleteLootbox.isGlowing());
            this.init(player, contents);
        }));
        contents.set(2, 5, ClickableItem.of(CommonsUtil.createItem(Material.PAPER, "&a&lAnnounce opening?", "&7&lCurrently: " + CommonsUtil.formatBoolean(incompleteLootbox.isAnnounce())), event -> {
            incompleteLootbox.setAnnounce(!incompleteLootbox.isAnnounce());
            this.init(player, contents);
        }));
        contents.set(2, 6, ClickableItem.of(CommonsUtil.createItem(Material.WOOL, "&a&lIs Bundle?", 1, incompleteLootbox.isBundle() ? 5 : 14, "&7&lCurrently: &f" + CommonsUtil.formatBoolean(incompleteLootbox.isBundle())), event -> {
            incompleteLootbox.setBundle(!incompleteLootbox.isBundle());
            this.init(player, contents);
        }));
        contents.set(4, 2, ClickableItem.of(CommonsUtil.createItem(Material.REDSTONE_BLOCK, "&c&lDelete Lootbox Session", "&4&lWARNING: &7This will remove", "&7&nALL&7 of your progress"), event -> {
            player.closeInventory();
            this.lootboxCreationManager.deleteSession(player);
        }));
        contents.set(4, 6, ClickableItem.of(CommonsUtil.createItem(Material.NETHER_STAR, "&a&lCreate Lootbox"), event -> {
            if (!incompleteLootbox.isComplete()) {
                player.sendMessage(CC.translate("&c&l(!) &cThat lootbox is not completed!"));
                return;
            }
            player.closeInventory();
            (this.lootboxCreationManager.getSession(player.getUniqueId()).get().getValue1()).addNewLootbox(incompleteLootbox.convert(this.plugin, this.lootboxManager));
            player.sendMessage(CC.translate("&a&l(&f!&a&l) &aLootbox created!"));
            this.lootboxCreationManager.deleteSession(player);
        }));
        contents.set(4, 4, ClickableItem.of(new ItemBuilder(Material.TNT).name(CC.translate("&c&lDelete Lootbox")).lore(CC.translate("&4&lWARNING: &7This action can not be reversed.")).build(), event -> {
                boolean success = this.lootboxManager.removeLootbox(incompleteLootbox.getName());
                if (success) {
                    player.sendMessage(CC.translate("&a&lSUCCESS! &aLootbox &f" + incompleteLootbox.getName() + " &awas removed."));
                } else {
                    player.sendMessage(CC.translate("&c&lFAILURE! &cLootbox &f" + incompleteLootbox.getName() + " &ccould not be removed."));
                }
                player.closeInventory();
                this.lootboxCreationManager.deleteSession(player);
        }));
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        if (!this.settingProperties.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        String property = this.settingProperties.get(event.getPlayer().getUniqueId());
        IncompleteLootbox lootbox = this.lootboxCreationManager.getSession(event.getPlayer().getUniqueId()).get().getValue0();
        if (property.equalsIgnoreCase("name")) {
            if (this.lootboxManager.getLootbox(event.getMessage()).isPresent()) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!) &cA lootbox with that name already exists! Instead use lootbox edit."));
            } else {
                lootbox.setName(ChatColor.stripColor(CC.translate(event.getMessage())));
            }
            this.inventory.open(event.getPlayer());
        } else if (property.equalsIgnoreCase("displayName")) {
            lootbox.setDisplayName(event.getMessage());
            this.inventory.open(event.getPlayer());
        } else if (property.equalsIgnoreCase("rewardCount")) {
            try {
                lootbox.setRewardCount(Integer.parseInt(event.getMessage()));
                this.inventory.open(event.getPlayer());
            }
            catch (NumberFormatException e) {
                this.inventory.open(event.getPlayer());
            }
        }
        this.settingProperties.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!this.settingProperties.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }
        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }
        String property = this.settingProperties.get(event.getPlayer().getUniqueId());
        if (property.equalsIgnoreCase("material")) {
            event.setCancelled(true);
            IncompleteLootbox lootbox = this.lootboxCreationManager.getSession(event.getPlayer().getUniqueId()).get().getValue0();
            lootbox.setChosenMaterial(event.getItem().getType());
            this.inventory.open(event.getPlayer());
        }
        this.settingProperties.remove(event.getPlayer().getUniqueId());
    }

    public void update(Player player, InventoryContents contents) {
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

