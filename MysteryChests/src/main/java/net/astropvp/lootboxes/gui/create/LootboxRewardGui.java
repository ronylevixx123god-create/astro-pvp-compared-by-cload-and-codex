
package net.astropvp.lootboxes.gui.create;

import net.astropvp.lootboxes.lootbox.AstroRewardItem;
import net.astropvp.lootboxes.lootbox.IncompleteLootbox;
import net.astropvp.lootboxes.util.NBTUtil;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.RewardItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LootboxRewardGui
implements InventoryProvider,
Listener {
    boolean settingCommand = false;
    boolean settingRarity = false;
    int settingPage = 0;
    boolean switchingPages = false;
    RewardItem settingValueItem = null;
    private final JavaPlugin plugin;
    private final IncompleteLootbox incompleteLootbox;
    private final Player player;
    private final SmartInventory inventory;
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final ItemStack INFO_ITEM = CommonsUtil.createItem((Material)Material.REDSTONE_TORCH_ON, (String)"&a&lInfo", (String[])new String[]{"&7Right click any item to set it's command.", "&7Right click an empty slot to set a command without item.", "&7Middle click an item remove it's command.", "&7Drop an item to set it's rarity.", "&7Left click this item to sort items on your page by rarity.", "&7Right click this item to sort ALL items on pages by rarity.", "&7Drop this item to shuffle ALL pages and sort all by rarity."});
    private static final ClickableItem PANE_ITEM = ClickableItem.of((ItemStack)CommonsUtil.createItem((Material)Material.STAINED_GLASS_PANE, (String)"", (String[])new String[0]), event -> event.setCancelled(true));

    public LootboxRewardGui(JavaPlugin plugin, InventoryManager inventoryManager, LootboxCreateGui parent, IncompleteLootbox incompleteLootbox, Player player) {
        this.plugin = plugin;
        this.incompleteLootbox = incompleteLootbox;
        this.player = player;
        this.inventory = SmartInventory.builder().provider((InventoryProvider)this).manager(inventoryManager).type(InventoryType.CHEST).size(6, 9).title("AstroLootboxes - Rewards").id(UUID.randomUUID().toString()).clickable(true).parent(parent.getInventory()).listener(new InventoryListener(InventoryClickEvent.class, event -> {
            if (((InventoryClickEvent)event).getClickedInventory() == null) {
                return;
            }
            if (!((InventoryClickEvent)event).getClickedInventory().getTitle().equalsIgnoreCase("AstroLootboxes - Rewards")) {
                return;
            }
            if (((InventoryClickEvent)event).getSlot() >= 45) {
                return;
            }
            if (((InventoryClickEvent)event).getClick() == ClickType.RIGHT || ((InventoryClickEvent)event).getClick() == ClickType.SHIFT_RIGHT) {
                ((InventoryClickEvent)event).setCancelled(true);
                if (((InventoryClickEvent)event).getClickedInventory().getItem(((InventoryClickEvent)event).getSlot()) == null || ((InventoryClickEvent)event).getClickedInventory().getItem(((InventoryClickEvent)event).getSlot()).getType() == Material.AIR) {
                    this.saveInventoryToRewards(((InventoryClickEvent)event).getClickedInventory(), 0, this.getCurrentPage(((InventoryClickEvent)event).getClickedInventory()));
                    this.settingCommand = true;
                    this.settingValueItem = null;
                    this.player.closeInventory();
                    player.sendMessage(CC.translate("&a&l(&f!&a&l) &aEnter the command item in the format; <displayname>,<command>"));
                } else {
                    int index = this.saveInventoryToRewards(((InventoryClickEvent)event).getClickedInventory(), ((InventoryClickEvent)event).getSlot(), this.getCurrentPage(((InventoryClickEvent)event).getClickedInventory()));
                    this.settingCommand = true;
                    this.settingValueItem = this.incompleteLootbox.getRewardPages().get(this.settingPage).get(index);
                    player.closeInventory();
                    player.sendMessage(CC.translate("&a&l(&f!&a&l) &aEnter the command in chat."));
                }
            } else if (((InventoryClickEvent)event).getClick() == ClickType.DROP || ((InventoryClickEvent)event).getClick() == ClickType.CONTROL_DROP) {
                int page = this.getCurrentPage(((InventoryClickEvent)event).getClickedInventory());
                int index = this.saveInventoryToRewards(((InventoryClickEvent)event).getClickedInventory(), ((InventoryClickEvent)event).getSlot(), page);
                this.settingRarity = true;
                this.settingValueItem = this.incompleteLootbox.getRewardPages().get(this.settingPage).get(index);
                ((InventoryClickEvent)event).setCancelled(true);
                Bukkit.getScheduler().runTask((Plugin)plugin, () -> new LootboxRarityGui(player, this.settingValueItem, this, inventoryManager, plugin, page));
            } else if (((InventoryClickEvent)event).getClick() == ClickType.MIDDLE) {
                ((InventoryClickEvent)event).setCancelled(true);
                int index = this.saveInventoryToRewards(((InventoryClickEvent)event).getClickedInventory(), ((InventoryClickEvent)event).getSlot(), this.settingPage);
                RewardItem rewardItem = this.incompleteLootbox.getRewardPages().get(this.settingPage).get(index);
                if (rewardItem.isCommand() && !rewardItem.isItem()) {
                    this.incompleteLootbox.getRewardPages().get(this.settingPage).remove(index);
                    this.switchingPages = true;
                    this.getInventory().open(player, this.settingPage);
                } else {
                    this.incompleteLootbox.getRewardPages().get(this.settingPage).set(index, new AstroRewardItem(rewardItem.getDisplayName(), rewardItem.getReward()));
                    this.switchingPages = true;
                    this.getInventory().open(player, this.settingPage);
                }
            }
        })).listener(new InventoryListener(InventoryCloseEvent.class, event -> {
            if (!(this.settingCommand || this.settingRarity || this.switchingPages)) {
                this.saveInventoryToRewards(((InventoryCloseEvent)event).getInventory(), 0, this.getCurrentPage(((InventoryCloseEvent)event).getInventory()));
                Bukkit.getScheduler().runTask((Plugin)plugin, () -> parent.getInventory().open((Player)((InventoryCloseEvent)event).getPlayer()));
                HandlerList.unregisterAll((Listener)this);
            }
            for (int i = 0; i < player.getInventory().getContents().length; ++i) {
                ItemStack content = player.getInventory().getContents()[i];
                if (content == null || content.getType() == Material.AIR || !NBTUtil.hasKey(content, "reward-uuid")) continue;
                content = NBTUtil.removeKey(content, "reward-uuid");
                player.getInventory().setItem(i, content);
            }
        })).build();
        this.inventory.open(player);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    private int getCurrentPage(Inventory inventory) {
        ItemStack item = inventory.getItem(48);
        return Integer.parseInt(ChatColor.stripColor((String)item.getItemMeta().getDisplayName()).split("Current Page: ")[1]);
    }

    private int saveInventoryToRewards(Inventory inventory, int slot, int page) {
        ArrayList savedItems = Lists.newArrayList();
        boolean anyItems = false;
        int foundSlot = 0;
        for (int i = 0; i < inventory.getSize(); ++i) {
            ItemStack item;
            if (i >= 45 || (item = inventory.getItem(i)) == null || item.getType() == Material.AIR) continue;
            savedItems.add(new AstroRewardItem(item));
            anyItems = true;
            if (i != slot) continue;
            foundSlot = i;
        }
        if (anyItems) {
            this.incompleteLootbox.getRewardPages().put(page, savedItems);
        } else {
            this.incompleteLootbox.getRewardPages().remove(page);
        }
        this.incompleteLootbox.getRewardItems().clear();
        this.incompleteLootbox.getRewardPages().forEach((rewardPage, items) -> this.incompleteLootbox.getRewardItems().addAll((Collection<RewardItem>)items));
        return foundSlot;
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        if (!this.settingCommand) {
            return;
        }
        if (!event.getPlayer().getUniqueId().equals(this.player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        if (this.settingValueItem != null) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                ListIterator<RewardItem> items = this.incompleteLootbox.getRewardPages().get(this.settingPage).listIterator();
                RewardItem foundItem = null;
                while (items.hasNext()) {
                    RewardItem rewardItem = items.next();
                    if (!rewardItem.getUUID().equals(this.settingValueItem.getUUID())) continue;
                    items.remove();
                    foundItem = rewardItem;
                    break;
                }
                items.add(new AstroRewardItem(foundItem.getDisplayName(), foundItem.getReward(), event.getMessage(), foundItem.getRarity()));
                this.settingCommand = false;
                this.settingValueItem = null;
                this.inventory.open(this.player, this.settingPage);
                this.settingPage = 0;
            });
        } else {
            String[] split = COMMA_PATTERN.split(event.getMessage());
            if (split.length != 2) {
                event.getPlayer().sendMessage(CC.translate("&c&l(!) &cPut in command item in format: <displayname>,<command>"));
                return;
            }
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                int page = this.incompleteLootbox.addToFirstAvailablePage(new AstroRewardItem(split[0], split[1]));
                this.settingCommand = false;
                this.inventory.open(this.player, page);
            });
        }
    }

    public void init(Player player, InventoryContents contents) {
        this.settingPage = contents.pagination().getPage();
        this.switchingPages = false;
        contents.fill(null);
        for (int i = 0; i < 9; ++i) {
            contents.set(5, i, PANE_ITEM);
        }
        contents.set(5, 3, ClickableItem.of((ItemStack)CommonsUtil.createItem((Material)Material.PAPER, (String)("&aCurrent Page: &7" + contents.pagination().getPage()), (String[])new String[0]), event -> event.setCancelled(true)));
        contents.set(5, 4, ClickableItem.of((ItemStack)INFO_ITEM, event -> {
            this.saveInventoryToRewards(event.getClickedInventory(), 0, contents.pagination().getPage());
            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                try {
                    List<RewardItem> items = Lists.newArrayList(this.incompleteLootbox.getRewardPages().get(contents.pagination().getPage())).stream().sorted(Comparator.comparingDouble(rewardItem -> rewardItem.getRarity().getChance())).collect(Collectors.toList());
                    this.incompleteLootbox.getRewardPages().put(contents.pagination().getPage(), items);
                }
                catch (NullPointerException items) {}
            } else if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                HashMap<Integer, List<RewardItem>> pages = Maps.newHashMap(this.incompleteLootbox.getRewardPages());
                HashSet<Integer> pageSet = Sets.newHashSet(pages.keySet());
                pageSet.forEach(pageInt -> {
                    List<RewardItem> items = Lists.newArrayList((pages.get(pageInt))).stream().sorted(Comparator.comparingDouble(rewardItem -> rewardItem.getRarity().getChance())).collect(Collectors.toList());
                    pages.put(pageInt, items);
                });
                this.incompleteLootbox.getRewardPages().putAll(pages);
            } else if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
                event.setCancelled(true);
                List items = Lists.newArrayList(this.incompleteLootbox.getRewardItems()).stream().sorted(Comparator.comparingDouble(rewardItem -> rewardItem.getRarity().getChance())).collect(Collectors.toList());
                this.incompleteLootbox.getRewardItems().clear();
                this.incompleteLootbox.getRewardItems().addAll(items);
                this.incompleteLootbox.shufflePagesBack();
                this.switchingPages = true;
                this.inventory.open(player, 0);
                return;
            }
            this.incompleteLootbox.getRewardItems().clear();
            this.incompleteLootbox.getRewardPages().forEach((pageInt, rewards) -> this.incompleteLootbox.getRewardItems().addAll((Collection<RewardItem>)rewards));
            event.setCancelled(true);
            event.setCursor(null);
            this.init(player, contents);
        }));
        contents.set(5, 5, ClickableItem.of((ItemStack) CommonsUtil.createItem((Material)Material.ARROW, (String)"&aStore all items from inventory", (String[])new String[0]), event -> {
            event.setCancelled(true);
            event.setCursor(null);
            ArrayList openSlots = Lists.newArrayList();
            for (int i = 0; i < 45; ++i) {
                if (event.getClickedInventory().getItem(i) != null && event.getClickedInventory().getItem(i).getType() != Material.AIR) continue;
                openSlots.add(i);
            }
            for (ItemStack item : player.getInventory()) {
                if (openSlots.isEmpty() || item == null || item.getType() == Material.AIR) continue;
                int slot = (Integer)openSlots.get(0);
                openSlots.remove(0);
                event.getClickedInventory().setItem(slot, item);
                player.getInventory().removeItem(new ItemStack[]{item});
            }
            this.saveInventoryToRewards(event.getClickedInventory(), 0, contents.pagination().getPage());
            this.init(player, contents);
        }));
        int totalPages = this.incompleteLootbox.getHighestPage() + 1;
        contents.pagination().setItemsPerPage(45);
        contents.set(5, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name(CC.translate("&aForward")).lore(CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")).build(), event -> {

                boolean hasItems = false;
            for (int i = 0; i < 45; ++i) {
                ItemStack item = event.getClickedInventory().getItem(i);
                if (item == null || item.getType() == Material.AIR) continue;
                hasItems = true;
            }
            if (hasItems) {
                this.saveInventoryToRewards(event.getClickedInventory(), 0, contents.pagination().getPage());
                event.setCursor(null);
                event.setCancelled(true);
                this.switchingPages = true;
                this.inventory.open(player, contents.pagination().next().getPage() + 1);
            } else {
                event.setCursor(null);
                event.setCancelled(true);
            }
        }));
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name(CC.translate("&aBackward")).lore(CC.translate("&7(" + (contents.pagination().getPage() + 1) + "/" + totalPages + ")")).build(), event -> {

                this.saveInventoryToRewards(event.getClickedInventory(), 0, contents.pagination().getPage());
                event.setCursor(null);
                event.setCancelled(true);
                this.switchingPages = true;
                this.inventory.open(player, Math.max(0, contents.pagination().previous().getPage()));
            }));
        try {
            this.incompleteLootbox.getRewardPages().get(contents.pagination().getPage()).forEach(rewardItem -> contents.add(ClickableItem.empty((ItemStack)rewardItem.generateViewableStack())));
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    public void update(Player player, InventoryContents contents) {
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

