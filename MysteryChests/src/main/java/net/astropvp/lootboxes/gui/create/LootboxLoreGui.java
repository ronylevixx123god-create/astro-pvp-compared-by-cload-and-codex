
package net.astropvp.lootboxes.gui.create;

import net.astropvp.lootboxes.lootbox.IncompleteLootbox;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.inventory.content.SlotPos;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class LootboxLoreGui
implements InventoryProvider,
Listener {
    private final UUID uuid;
    private final IncompleteLootbox incompleteLootbox;
    private SmartInventory inventory;
    private List<String> lore;
    private Integer settingNum = null;
    private boolean inserting = false;
    private boolean removingLore = false;
    private final BiConsumer<InventoryClickEvent, Integer> loreClickConsumer = (event, num) -> {
        if (num > this.lore.size()) {
            ((CommandSender) event.getWhoClicked()).sendMessage(CC.translate("&c&l(!) &cYou cannot set a lore before setting all it's previous!"));
            return;
        }
        if (event.getClick().isLeftClick()) {
            this.settingNum = num;
            event.getWhoClicked().closeInventory();
            ((CommandSender) event.getWhoClicked()).sendMessage(CC.translate("&a&l(&f!&a&l) &aType in your line of lore, type \"empty\" for an empty line."));
        } else if (event.getClick().isRightClick()) {
            try {
                this.lore.remove((int)num);
                this.removingLore = true;
                this.inventory.open((Player)event.getWhoClicked());
            }
            catch (IndexOutOfBoundsException | NullPointerException runtimeException) {}
        } else if (event.getClick() == ClickType.MIDDLE || event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
            this.inserting = true;
            this.settingNum = num;
            event.getWhoClicked().closeInventory();
            ((CommandSender) event.getWhoClicked()).sendMessage(CC.translate("&a&l(&f!&a&l) &aType in your line of lore, type \"empty\" for an empty line."));
        }
    };

    public LootboxLoreGui(JavaPlugin plugin, InventoryManager inventoryManager, LootboxCreateGui parent, IncompleteLootbox lootbox, Player player) {
        this.incompleteLootbox = lootbox;
        this.uuid = player.getUniqueId();
        this.inventory = SmartInventory.builder().id(UUID.randomUUID().toString()).type(InventoryType.CHEST).size(1, 9).manager(inventoryManager).title("AstroLootboxes - Item Lore").provider((InventoryProvider)this).parent(parent.getInventory()).listener(new InventoryListener(InventoryCloseEvent.class, event -> {
            if (this.isListEmpty(this.lore)) {
                this.incompleteLootbox.setSpecialLore(null);
            } else {
                this.incompleteLootbox.setSpecialLore(this.lore);
            }
            if (this.removingLore) {
                this.removingLore = false;
                return;
            }
            if (this.settingNum != null) {
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> parent.getInventory().open((Player)((InventoryCloseEvent)event).getPlayer()));
        })).build();
        this.lore = lootbox.getSpecialLore();
        if (this.lore == null) {
            this.lore = Lists.newArrayList();
        }
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.inventory.open(player);
    }

    public void init(Player player, InventoryContents contents) {
        for (int i = 0; i < 8; ++i) {
            int finalI = i;
            try {
                contents.set(SlotPos.of((int)0, (int)i), ClickableItem.of((ItemStack) CommonsUtil.createItem((Material)(this.lore.get(i).equalsIgnoreCase("empty") ? Material.SNOW_BLOCK : Material.PAPER), (String)("&aLore line #" + (i + 1)), (String[])new String[]{"&7&lCurrently: &5" + (this.lore.get(i).equalsIgnoreCase("empty") ? "&fEmpty Line" : this.lore.get(i))}), event -> this.loreClickConsumer.accept((InventoryClickEvent)event, finalI)));
                continue;
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                contents.set(SlotPos.of((int)0, (int)i), ClickableItem.of((ItemStack)CommonsUtil.createItem((Material)Material.REDSTONE_BLOCK, (String)("&a&lLore Line #" + (i + 1)), (String[])new String[0]), event -> this.loreClickConsumer.accept((InventoryClickEvent)event, finalI)));
            }
        }
        contents.set(SlotPos.of((int)0, (int)8), ClickableItem.empty((ItemStack)CommonsUtil.createItem((Material)Material.REDSTONE_TORCH_ON, (String)"&a&lInfo", (String[])new String[]{"&7Left click a lore item to set it's value.", "&7Right click a lore line to remove it.", "&7For a blank line, type in \"empty\".", "&7To insert a line between two, drop the item."})));
    }

    @Override
    public void update(Player var1, InventoryContents var2) {

    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().getUniqueId().equals(this.uuid)) {
            return;
        }
        if (this.settingNum == null) {
            return;
        }
        event.setCancelled(true);
        try {
            if (this.inserting) {
                this.lore.add(this.settingNum, event.getMessage());
            } else {
                this.lore.set(this.settingNum, event.getMessage());
            }
        }
        catch (IndexOutOfBoundsException e) {
            this.lore.add(event.getMessage());
        }
        this.inserting = false;
        this.settingNum = null;
        this.inventory.open(event.getPlayer());
    }

    private boolean isListEmpty(List<String> list) {
        boolean isEmpty = true;
        for (String s : list) {
            if (s == null || s.equalsIgnoreCase("")) continue;
            isEmpty = false;
        }
        return isEmpty;
    }
}

