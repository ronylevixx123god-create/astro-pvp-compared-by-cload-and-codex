package me.ifamasssxd.cosmicskycustoms.listeners;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.discord.CosmicDiscordChannels;
import com.cosmicpvp.cosmicutils.discord.DiscordAPI;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.Tuple;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.events.PlayerInteractNBTWrapperEvent;
import me.ifamasssxd.cosmicskycustoms.events.PostPlayerUseCosmicItemEvent;
import me.ifamasssxd.cosmicskycustoms.events.PrePlayerUseCosmicItemEvent;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import me.ifamasssxd.cosmicskycustoms.struct.PlaceableBlockItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CosmicItemListener
implements Listener {
    private final CosmicSkyCustomItems plugin;

    public CosmicItemListener(CosmicSkyCustomItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        Material type = item.getType();
        if (type == Material.AIR) {
            return;
        }
        PlayerInteractNBTWrapperEvent newEvent = new PlayerInteractNBTWrapperEvent(event.getPlayer(), event, item, new NBTWrapper(item));
        Bukkit.getPluginManager().callEvent(newEvent);
        if (newEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    private Tuple<CosmicItems, CosmicItem> fromItem(NBTWrapper wrapper) {
        String keyItem = wrapper.getString("cosmicType");
        if (!keyItem.isEmpty()) {
            CosmicItem customItem;
            CosmicItems item = CosmicItems.getCosmicItem(keyItem);
            CosmicItem cosmicItem = customItem = item == null ? CosmicItems.getCustomItem(keyItem) : item.getItem();
            if (customItem == null) {
                customItem = CosmicPersistentItem.getSampleVersion(keyItem);
            }
            return new Tuple(item, customItem);
        }
        return null;
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onInventoryClick(InventoryClickEvent event) {
        NBTWrapper clicked;
        if (event.getClickedInventory() == null || event.getWhoClicked().getOpenInventory() == null || !(event.getWhoClicked() instanceof Player) || event.getWhoClicked().getOpenInventory().getBottomInventory() == null || !event.getClickedInventory().equals(event.getWhoClicked().getOpenInventory().getBottomInventory())) {
            return;
        }
        NBTWrapper cursor = event.getCursor() != null && event.getCursor().getType() != Material.AIR ? new NBTWrapper(event.getCursor()) : null;
        NBTWrapper nBTWrapper = clicked = event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR ? new NBTWrapper(event.getCurrentItem()) : null;
        if (cursor != null) {
            this.callInventoryInteractable((Player)event.getWhoClicked(), cursor, clicked, event.getCursor(), event.getCurrentItem(), event, InventoryItemType.CURSOR);
        }
        if (clicked != null) {
            this.callInventoryInteractable((Player)event.getWhoClicked(), clicked, cursor, event.getCurrentItem(), event.getCursor(), event, InventoryItemType.CLICKED);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=false)
    public void onBlockPlace(BlockPlaceEvent event) {
        block4: {
            CosmicItem customItem;
            NBTWrapper wrapper;
            Tuple<CosmicItems, CosmicItem> pair;
            ItemStack placed = event.getItemInHand();
            if (placed != null && placed.getType() != Material.AIR && (pair = this.fromItem(wrapper = new NBTWrapper(placed))) != null && (customItem = pair.getRight()) instanceof PlaceableBlockItem) {
                PrePlayerUseCosmicItemEvent newEvent = new PrePlayerUseCosmicItemEvent(event.getPlayer(), (CosmicItems) pair.getLeft(), customItem, placed, wrapper);
                if (!newEvent.callEvent()) {
                    event.setCancelled(true);
                    return;
                }
                try {
                    ((PlaceableBlockItem) customItem).onPlayerPlace(event.getPlayer(), placed, wrapper, event);
                    new PostPlayerUseCosmicItemEvent(event.getPlayer(), (CosmicItems) pair.getLeft(), customItem, placed, wrapper).callEvent();
                }
                catch (CosmicException e) {
                    event.getPlayer().sendMessage(e.getMessage());
                    event.setCancelled(true);
                    if (!(e instanceof ItemDupedException)) break block4;
                    DiscordAPI.sendMessage(CosmicDiscordChannels.ADMIN_LOGS, event.getPlayer().getName() + " tried to use duped item from BlockPlace: " + customItem.getClass().getSimpleName() + " world: " + event.getPlayer().getWorld().getName() + " on " + CosmicUtils.get().getServerName());
                }
            }
        }
    }

    private void callInventoryInteractable(Player player, NBTWrapper wrapper, NBTWrapper otherWrapper, ItemStack itemStack, ItemStack otherItem, InventoryClickEvent event, InventoryItemType itemType) {
        block8: {
            CosmicItem customItem;
            Tuple<CosmicItems, CosmicItem> pair = this.fromItem(wrapper);
            if (pair != null && (customItem = pair.getRight()) instanceof InventoryInteractable) {
                InventoryInteractable inventoryInteractable = (InventoryInteractable) customItem;
                if (!inventoryInteractable.acceptsInventoryClickType(itemType)) {
                    return;
                }
                try {
                    PrePlayerUseCosmicItemEvent newEvent = new PrePlayerUseCosmicItemEvent(player, (CosmicItems) pair.getLeft(), customItem, itemStack, wrapper);
                    if (!newEvent.callEvent()) {
                        event.setCancelled(true);
                        return;
                    }
                    inventoryInteractable.onInventoryClick((Player)event.getWhoClicked(), itemStack, wrapper, otherWrapper, otherItem, event);
                    new PostPlayerUseCosmicItemEvent(player, (CosmicItems) pair.getLeft(), customItem, itemStack, wrapper).callEvent();
                }
                catch (CosmicException e) {
                    event.getWhoClicked().sendMessage(e.getMessage());
                    event.setCancelled(true);
                    if (!(e instanceof ItemDupedException)) break block8;
                    if (itemType == InventoryItemType.CURSOR) {
                        event.getWhoClicked().setItemOnCursor(null);
                    } else if (itemType == InventoryItemType.CLICKED) {
                        event.setCurrentItem(null);
                    }
                    DiscordAPI.sendMessage(CosmicDiscordChannels.ADMIN_LOGS, event.getWhoClicked().getName() + " tried to use duped item from me.astropvp.cratekeys.inventory click " + itemType + ": " + customItem.getClass().getSimpleName() + " world: " + event.getWhoClicked().getWorld().getName() + " on " + CosmicUtils.get().getServerName());
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteractNBT(PlayerInteractNBTWrapperEvent event) {
        block7: {
            if (event.getWrapper() == null) {
                return;
            }
            Tuple<CosmicItems, CosmicItem> pair = this.fromItem(event.getWrapper());
            if (pair != null) {
                CosmicItems item = pair.getLeft();
                CosmicItem customItem = pair.getRight();
                if (item != null || customItem != null) {
                    PrePlayerUseCosmicItemEvent newEvent = new PrePlayerUseCosmicItemEvent(event.getPlayer(), item, customItem, event.getItem(), event.getWrapper());
                    Bukkit.getPluginManager().callEvent(newEvent);
                    if (newEvent.isCancelled()) {
                        event.setCancelled(true);
                        event.getRawEvent().setUseItemInHand(Event.Result.DENY);
                        return;
                    }
                    try {
                        customItem.onItemUse(event.getPlayer(), event.getItem(), event.getWrapper(), event.getRawEvent());
                        if (customItem instanceof PlayerInteractable) {
                            ((PlayerInteractable) customItem).onPlayerInteractable(event.getPlayer(), event.getItem(), event.getWrapper(), event.getRawEvent());
                        }
                        Bukkit.getPluginManager().callEvent(new PostPlayerUseCosmicItemEvent(event.getPlayer(), item, customItem, event.getItem(), event.getWrapper()));
                    }
                    catch (CosmicException e) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(e.getMessage());
                        if (!(e instanceof ItemDupedException)) break block7;
                        event.getPlayer().getInventory().setItemInHand(null);
                        event.getPlayer().getInventory().removeItem(event.getItem());
                        event.getPlayer().updateInventory();
                        DiscordAPI.sendMessage(CosmicDiscordChannels.ADMIN_LOGS, event.getPlayer().getName() + " tried to use duped item: " + customItem.getClass().getSimpleName() + " world: " + event.getPlayer().getWorld().getName() + " on " + CosmicUtils.get().getServerName());
                    }
                }
            }
        }
    }
}

