package net.astropvp.showcasegui.struct;

import com.google.common.collect.Lists;
import net.astropvp.showcasegui.ShowcaseAPI;
import net.astropvp.showcasegui.ShowcaseGUI;
import net.astropvp.showcasegui.custominventory.*;
import net.astropvp.showcasegui.utils.ItemBuilder;
import net.astropvp.showcasegui.utils.ItemSerialization;
import org.bukkit.inventory.ItemStack;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Showcase {
    private LinkedList<CustomInventory> inventories = new LinkedList();
    private transient Player player;
    UUID uuid;
    String name;
    int size = 9;
    boolean editted = false;
    boolean closeExempt = false;
    private CustomInventory firstClickedInventory;
    private CustomInventory secondClickedInventory;
    private Integer firstSlotToMove;
    private Integer secondSlotToMove;

    public Showcase(Player p, UUID uuid, String name, String serializedItems, int size) {
        this.uuid = uuid;
        this.size = size;
        this.player = p;
        this.name = name;
        this.inventories.add(this.createInventory(1));
        this.updateInventories();
        if (serializedItems != null) {
            Iterator var6 = ItemSerialization.itemListFromBase64(serializedItems).iterator();

            while(var6.hasNext()) {
                ItemStack is = (ItemStack)var6.next();
                this.addItem(is);
            }
        }

    }

    public void setSize(int size) {
        this.editted = true;
        this.size = size;
    }

    public void addInventory(CustomInventory inv) {
        this.editted = true;
        this.inventories.add(inv);
    }

    public CustomInventoryItem getItem(ItemStack is) {
        CustomInventoryItem item = new CustomInventoryItem(is);
        item.setCallback((inventory, customItem, event) -> {
            event.setCancelled(true);
            Player p = (Player)event.getWhoClicked();
            if (this.isOwner(p) || p.isOp() || event.isShiftClick()) {
                if (customItem != null) {
                    if (event.getClick() == ClickType.LEFT) {
                        if (this.firstSlotToMove == null) {
                            this.secondSlotToMove = null;
                            this.secondClickedInventory = null;
                            this.firstSlotToMove = event.getRawSlot();
                            this.firstClickedInventory = inventory;
                            p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.4F);
                            p.sendMessage((String)(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "Please click the second item to swap places with."));
                        } else if (this.firstSlotToMove != event.getRawSlot() || !this.firstClickedInventory.equals(inventory)) {
                            this.secondClickedInventory = inventory;
                            this.secondSlotToMove = event.getRawSlot();
                            CustomInventoryItem firstItem = this.firstClickedInventory.getItem(this.firstSlotToMove);
                            if (firstItem != null) {
                                this.secondClickedInventory.insertItem(this.secondSlotToMove, firstItem);
                            }

                            this.firstClickedInventory.insertItem(this.firstSlotToMove, customItem);
                            this.firstClickedInventory.updateInventoryItems();
                            if (!this.secondClickedInventory.equals(this.firstClickedInventory)) {
                                this.secondClickedInventory.updateInventoryItems();
                            }

                            this.firstSlotToMove = null;
                            this.secondSlotToMove = null;
                            this.secondClickedInventory = null;
                            this.firstClickedInventory = null;
                            this.editted = true;
                            p.sendMessage((String)(ChatColor.GREEN + "Item's Swapped."));
                            p.playSound(this.player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.4F);
                            p.updateInventory();
                        }
                    } else if (event.getRawSlot() <= event.getInventory().getSize()) {
                        this.createConfirmInventory(is, true, event.getRawSlot(), 0).openInventory(p, true);
                        p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.4F);
                    }
                }

            }
        });
        return item;
    }

    public void addItem(ItemStack is) {
        Iterator var2 = this.inventories.iterator();

        CustomInventory inv;
        do {
            if (!var2.hasNext()) {
                if (this.inventories.size() < this.getPages()) {
                    CustomInventory nextPage = this.createInventory(this.inventories.size() + 1);
                    this.addInventory(nextPage);
                    nextPage.addItem(this.getItem(is));
                }

                return;
            }

            inv = (CustomInventory)var2.next();
        } while(inv.getEmptySlots() == 0);

        inv.addItem(this.getItem(is));
    }

    public boolean removeItem(CustomInventoryItem item) {
        Iterator var2 = this.inventories.iterator();

        while(var2.hasNext()) {
            CustomInventory inv = (CustomInventory)var2.next();

            for(int slot = 0; slot < inv.getInventorySize(); ++slot) {
                ItemStack is = inv.getItem(slot);
                if (is != null && is.getType() != Material.AIR) {
                    if (is.equals(item)) {
                        inv.removeItem(slot, true);
                        return true;
                    }

                    if (is.getType() == Material.SKULL_ITEM && item.getType() == Material.SKULL_ITEM && item.getAmount() == is.getAmount() && is.hasItemMeta() && item.hasItemMeta()) {
                        SkullMeta sm = (SkullMeta)is.getItemMeta();
                        SkullMeta other = (SkullMeta)item.getItemMeta();
                        String owner = sm.getOwner() != null ? sm.getOwner() : null;
                        String otherOwner = other.getOwner() != null ? other.getOwner() : null;
                        if (owner != null && owner.equalsIgnoreCase(otherOwner)) {
                            try {
                                if (sm.getDisplayName().equals(other.getDisplayName()) && sm.getLore().equals(other.getLore())) {
                                    inv.removeItem(slot, true);
                                    return true;
                                }
                            } catch (NullPointerException var11) {
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public int getNextInventorySize(int page) {
        if (this.getSize() <= 54) {
            return this.getSize();
        } else {
            int size = this.getSize();

            for(int i = 0; i < this.inventories.size(); ++i) {
                if (i + 1 != page) {
                    CustomInventory inv = (CustomInventory)this.inventories.get(i);
                    size -= inv.getInventorySize();
                }
            }

            if (size < 9) {
                size = 9;
            }

            if (size > 54) {
                size = 54;
            }

            return size;
        }
    }

    public int getPages() {
        return (int)Math.max(1.0D, Math.ceil((double)this.getSize() * 1.0D / 54.0D));
    }

    public CustomInventory getPage(int page) {
        if (this.inventories.size() != this.getPages()) {
            this.updateInventories();
        }

        if (page > this.inventories.size() && page <= this.getPages()) {
            this.updateInventories();
        }

        return (CustomInventory)this.inventories.get(page - 1);
    }

    public void updateInventories() {
        int currentSize = this.inventories.size();

        for(int i = currentSize + 1; i <= this.getPages(); ++i) {
            this.inventories.add(i - 1, this.createInventory(i));
            if (!this.editted) {
                this.setEditted(true);
            }
        }

    }

    public CustomInventory createInventory(int page) {
        String invName = this.name + "'s Showcase" + (this.getPages() >= 1 ? " (" + page + " / " + this.getPages() + ")" : "");
        if (invName.length() > 32) {
            invName = invName.substring(0, 32);
        }

        CustomInventory inv = new SimpleCustomInventory(invName, this.getNextInventorySize(page) / 9);
        if (page == this.getPages()) {
            if (page > 1) {
                inv.insertItem(inv.getInventorySize() - 9, this.getPageButton(page, true));
                inv.updateInventoryItems();
            }
        } else if (page < this.getPages()) {
            if (page == 1) {
                inv.insertItem(inv.getInventorySize() - 1, this.getPageButton(page, false));
                inv.updateInventoryItems();
            } else {
                inv.insertItem(inv.getInventorySize() - 9, this.getPageButton(page, true));
                inv.insertItem(inv.getInventorySize() - 1, this.getPageButton(page, false));
                inv.updateInventoryItems();
            }
        }

        inv.setBottomCallback((inventory, itemClicked, event) -> {
            event.setCancelled(true);
            Player p = (Player)event.getWhoClicked();
            if (!this.isOwner(p) && !p.isOp() && !event.isShiftClick()) {
                p.sendMessage((String)(ChatColor.RED + "This is not your showcase!"));
            } else if (event.getClick() == ClickType.LEFT) {
                if (itemClicked != null && itemClicked.getType() != Material.AIR) {
                    if (inv.getEmptySlots() == 0) {
                        p.sendMessage((String)(ChatColor.RED + "This showcase does not have enough space for that item!"));
                        if (this.getEmptySlots() > 0) {
                            p.sendMessage((String)(ChatColor.GREEN + "You have more pages available that this item can be added to."));
                        } else if (this.getSize() < 558) {
                            p.sendMessage((String)(ChatColor.GRAY + "You can purchase more showcase slots at " + ChatColor.RED + ChatColor.UNDERLINE.toString() + "buy.astropvp.net"));
                        } else {
                            p.sendMessage((String)(ChatColor.GRAY + "You already have the max amount of Showcase slots available!"));
                        }

                    } else {
                        p.getInventory().setItem(event.getSlot(), (ItemStack)null);
                        CustomInventory inv2 = this.createConfirmInventory(itemClicked, false, -1, page);
                        inv2.openInventory(p, false);
                        p.sendMessage((String)(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Please confirm your decision."));
                        p.playSound(p.getLocation(), Sound.WITHER_IDLE, 1.0F, 1.3F);
                    }
                }
            }
        });
        inv.setCallback((inventory, event) -> {
            if (this.closeExempt) {
                this.closeExempt = false;
            } else {
                this.firstSlotToMove = null;
                this.secondSlotToMove = null;
                this.firstClickedInventory = null;
                this.secondClickedInventory = null;
            }

            if (this.editted) {
                if (event.getPlayer() != null && this.player != null && this.isOwner((Player)event.getPlayer())) {
                    this.player.sendMessage((String)(ChatColor.GREEN + "Showcase updates saved."));
                }

                Bukkit.getScheduler().scheduleAsyncDelayedTask(ShowcaseGUI.get(), () -> {
                    ShowcaseAPI.saveShowcase(this.uuid, false, false);
                    this.editted = false;
                });
            }

        });
        return inv;
    }

    public CustomInventory createConfirmInventory(final ItemStack is, final boolean destroy, int slotClicked, int previousPage) {
        CustomInventory confirm = new SimpleCustomInventory("Confirm" + (destroy ? " Item Destruction" : " Item Addition"), 1);
        CustomInventoryItem item = new CustomInventoryItem(is);
        final AtomicBoolean success = new AtomicBoolean(false);
        confirm.setCallback(new CustomInventoryCallback() {
            public void inventoryCloseEvent(CustomInventory inventory, InventoryCloseEvent event) {
                if (!success.get()) {
                    success.set(true);
                    Player p = (Player)event.getPlayer();
                    if (destroy) {
                        p.sendMessage((String)(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Showcase item destruction cancelled."));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ShowcaseGUI.get(), (Runnable)(() -> {
                            Showcase.this.getPage(1).openInventory(p, false);
                        }));
                    } else {
                        p.sendMessage((String)(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Showcase item cancelled."));
                        if (p.getInventory().firstEmpty() == -1) {
                            p.getWorld().dropItem(p.getLocation(), is);
                            p.sendMessage((String)(ChatColor.GRAY + "Your showcase item has been dropped on the ground."));
                        } else {
                            p.getInventory().addItem(new ItemStack[]{is});
                            p.closeInventory();
                            p.updateInventory();
                            p.sendMessage((String)(ChatColor.GRAY + "Your showcase item has been added back into your inventory."));
                        }

                    }
                }
            }
        });
        CustomInventoryItem confirmPane = new CustomInventoryItem(this.getConfirmButton(destroy));
        CustomInventoryItem cancelPane = this.getCancelButton(destroy);
        confirmPane.setCallback(new CustomInventoryItemCallback() {
            public void inventoryClickEvent(CustomInventory inventory, CustomInventoryItem customItem, InventoryClickEvent event) {
                event.setCancelled(true);
                if (event.getClick() == ClickType.LEFT) {
                    Player p = (Player)event.getWhoClicked();
                    CustomInventoryItem item = Showcase.this.getItem(is);
                    if (destroy) {
                        Showcase.this.removeItem(item);
                        Showcase.this.editted = true;
                        p.sendMessage((String)(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Your showcase item has been destroyed!"));
                        p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 1.3F);
                        p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 1.0F, 0.8F);
                    } else {
                        ItemMeta im = item.getItemMeta();
                        List<String> lore = im.hasLore() ? im.getLore() : Lists.newArrayList();
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
                        ((List)lore).add(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + format.format(date));
                        im.setLore((List)lore);
                        item.setItemMeta((ItemMeta)im);
                        Showcase.this.addItem(item);
                        Showcase.this.editted = true;
                        p.playSound(p.getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 1.4F);
                    }

                    success.set(true);
                    Showcase.this.getFirstPage().openInventory((Player)event.getWhoClicked(), false);
                }
            }
        });
        confirm.insertItem(4, item, false);

        int i;
        for(i = 0; i < 4; ++i) {
            if (confirm.getItem(i) == null) {
                confirm.insertItem(i, confirmPane, true);
            }
        }

        for(i = 5; i < confirm.getInventorySize(); ++i) {
            if (confirm.getItem(i) == null) {
                confirm.insertItem(i, cancelPane, true);
            }
        }

        return confirm;
    }

    public CustomInventoryItem getCancelButton(boolean destroy) {
        CustomInventoryItem item = new CustomInventoryItem(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)DyeColor.RED.getData(), ChatColor.RED + ChatColor.BOLD.toString() + "Cancel " + (destroy ? "Item Destruction" : "Item Addition"), ChatColor.GRAY + "Click to cancel your action."));
        item.setCallback(new CustomInventoryItemCallback() {
            public void inventoryClickEvent(CustomInventory inventory, CustomInventoryItem customItem, InventoryClickEvent event) {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }
        });
        return item;
    }

    public CustomInventoryItem getPageButton(final int page, final boolean back) {
        CustomInventoryItem item = new CustomInventoryItem(ItemBuilder.buildItem(Material.ARROW, back ? ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Previous Page" : ChatColor.AQUA + ChatColor.BOLD.toString() + "Next Page", ChatColor.GRAY + (back ? "Click to go back to the previous page." : "Click to go to the next page.")));
        item.setCallback(new CustomInventoryItemCallback() {
            public void inventoryClickEvent(CustomInventory inventory, CustomInventoryItem customItem, InventoryClickEvent event) {
                event.setCancelled(true);
                if (event.getClick() == ClickType.LEFT) {
                    int newPage = page;
                    if (back) {
                        --newPage;
                    } else {
                        ++newPage;
                    }

                    if (newPage < 1) {
                        newPage = Showcase.this.getPages();
                    } else if (newPage > Showcase.this.getPages()) {
                        newPage = 1;
                    }

                    Player p = (Player)event.getWhoClicked();
                    p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.3F);
                    Showcase.this.closeExempt = true;
                    p.closeInventory();
                    Showcase.this.getPage(newPage).openInventory(p, false);
                }
            }
        });
        return item;
    }

    public ItemStack getConfirmButton(boolean destroy) {
        return ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)DyeColor.LIME.getData(), ChatColor.GREEN + ChatColor.BOLD.toString() + "Confirm " + (destroy ? "Item Destruction" : "Item Addition"), ChatColor.GRAY + "Click to confirm your decision.", "", ChatColor.RED + ChatColor.BOLD.toString() + "WARNING: " + (destroy ? ChatColor.GRAY + "This item will be deleted " + ChatColor.RED + ChatColor.UNDERLINE.toString() + "forever." : ChatColor.GRAY + "Once placed inside your//" + ChatColor.GRAY + "Showcase this item can never//" + ChatColor.GRAY + "be withdrawn and it can only be//" + ChatColor.GRAY + "destroyed."));
    }

    public CustomInventory getFirstPage() {
        return this.inventories.size() > 0 ? (CustomInventory)this.inventories.get(0) : this.createInventory(1);
    }

    public LinkedList<ItemStack> getShowcaseItems() {
        LinkedList<ItemStack> items = new LinkedList();

        for (CustomInventory inv : this.inventories) {

            for (CustomInventoryItem customInventoryItem : inv.getItems().values()) {
                ItemStack is = (ItemStack) customInventoryItem;
                if (is != null && is.getType() != Material.AIR && !isPageButton(is)) {
                    items.add(is);
                }
            }
        }

        return items;
    }

    public static boolean isPageButton(ItemStack is) {
        return is != null && is.getType() == Material.ARROW && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().contains("Page");
    }

    public int getEmptySlots() {
        AtomicInteger size = new AtomicInteger(0);
        this.inventories.forEach((inv) -> {
            size.addAndGet(inv.getEmptySlots());
        });
        return size.get();
    }

    public String getSerializedItems() {
        return ItemSerialization.itemListToBase64(this.getShowcaseItems());
    }

    public boolean isOwner(Player p) {
        return p.getUniqueId().equals(this.uuid);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT IGNORE INTO showcases(uuid, size, showcase) VALUES ('" + this.uuid.toString() + "', '");
        builder.append(this.getSize() + "', '");
        builder.append(this.getSerializedItems());
        builder.append("') ON DUPLICATE KEY UPDATE showcase = '");
        builder.append(this.getSerializedItems());
        builder.append("', size = '" + this.getSize() + "';");
        return builder.toString();
    }

    public LinkedList<CustomInventory> getInventories() {
        return this.inventories;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEditted() {
        return this.editted;
    }

    public boolean isCloseExempt() {
        return this.closeExempt;
    }

    public void setEditted(boolean editted) {
        this.editted = editted;
    }

    public void setCloseExempt(boolean closeExempt) {
        this.closeExempt = closeExempt;
    }
}
