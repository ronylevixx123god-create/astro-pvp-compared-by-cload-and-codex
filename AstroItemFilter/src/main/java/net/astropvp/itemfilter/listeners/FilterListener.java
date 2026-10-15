package net.astropvp.itemfilter.listeners;

import net.astropvp.itemfilter.ItemFilterPlugin;
import net.astropvp.itemfilter.data.FilterUser;
import net.astropvp.itemfilter.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FilterListener
        implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null)
            return;

        Inventory localInventory = event.getInventory();
        Player localPlayer = (Player) event.getWhoClicked();
        if (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.title")))) {
            event.setCancelled(true);
            int i = event.getRawSlot();
            if ((i <= localInventory.getSize()) &&
                    (event.getCurrentItem() != null)) {
                if (i == ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Equipment.slot")) {
                    EquipmentInventory.getInstance().equipmentInventory(localPlayer);
                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-open.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-open.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-pitch"));
                    }
                }
                if (i == ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Potion.slot")) {
                    PotionInventory.getInstance().potionInventory(localPlayer);
                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-open.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-open.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-pitch"));
                    }
                }
                if (i == ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Raiding.slot")) {
                    RaidingInventory.getInstance().raidingInventory(localPlayer);
                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-open.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-open.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-pitch"));
                    }
                }
                if (i == ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Food.slot")) {
                    FoodInventory.getInstance().foodInventory(localPlayer);
                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-open.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-open.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-pitch"));
                    }
                }
                if (i == ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Specialty.slot")) {
                    SpecialtyInventory.getInstance().specialtyInventory(localPlayer);
                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-open.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-open.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-pitch"));
                    }
                }
                if (i == ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.items.Ores.slot")) {
                    OresInventory.getInstance().oresInventory(localPlayer);
                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-open.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-open.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-open.sound-pitch"));
                    }
                }
            }
        }
        if ((localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.equipment-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.ores-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.specialty-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.food-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.raiding-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.potion-inventory.title"))))) {
            event.setCancelled(true);
            ItemStack localItemStack1 = event.getCurrentItem();
            List<String> localList1 = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(localPlayer.getUniqueId()).getFilter();

            if ((localItemStack1.hasItemMeta()) &&
                    (localItemStack1.getItemMeta().hasDisplayName())) {
                if (localList1.contains(String.valueOf(localItemStack1.getType().name()))) {
                    ItemStack localItemStack2 = localItemStack1;

                    ItemMeta localItemMeta = localItemStack1.getItemMeta();
                    String str = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.WillNot.ItemName").replace("{2}", ChatColor.stripColor(localItemStack1.getItemMeta().getDisplayName())));
                    List<String> localList2 = ItemFilterPlugin.colorList(ItemFilterPlugin.getInstance().getConfig().getStringList("settings.WillNot.ItemLore"));
                    localItemMeta.setDisplayName(str);
                    localItemMeta.removeEnchant(Enchantment.DURABILITY);
                    localItemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                    localItemMeta.setLore(localList2);
                    localItemStack2.setItemMeta(localItemMeta);
                    localPlayer.updateInventory();

                    localList1.remove(String.valueOf(localItemStack1.getType().name()));

                    if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.filter-item-select.enable")) {
                        localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.filter-item-select.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.filter-item-select.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.filter-item-select.sound-pitch"));
                    }
                    return;
                }

                ItemStack localItemStack2 = localItemStack1;
                ItemMeta localItemMeta = localItemStack2.getItemMeta();
                String str = ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.Will.ItemName").replace("{2}", ChatColor.stripColor(localItemStack1.getItemMeta().getDisplayName())));
                List<String> localList2 = ItemFilterPlugin.colorList(ItemFilterPlugin.getInstance().getConfig().getStringList("settings.Will.ItemLore"));
                localItemMeta.setDisplayName(str);
                localItemMeta.setLore(localList2);
                localItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                localItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                localItemStack2.setItemMeta(localItemMeta);
                localPlayer.updateInventory();

                localList1.add(String.valueOf(localItemStack1.getType().name()));
                if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.filter-item-select.enable")) {
                    localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.filter-item-select.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.filter-item-select.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.filter-item-select.sound-pitch"));
                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(!e.getMessage().toLowerCase().equalsIgnoreCase("/ft")) {
            return;
        }

        e.setCancelled(true);
        e.getPlayer().chat("/filters toggle");
    }



    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent paramPlayerPickupItemEvent) {
        Player localPlayer = paramPlayerPickupItemEvent.getPlayer();
        Item localItem = paramPlayerPickupItemEvent.getItem();
        ItemStack localItemStack = localItem.getItemStack();
        FilterUser filterUser = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(localPlayer.getUniqueId());
        if(!filterUser.isEnabled())
            return;

        if (!filterUser.canPickup(localItemStack.getType())) {
            paramPlayerPickupItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent paramInventoryCloseEvent) {
        Inventory localInventory = paramInventoryCloseEvent.getInventory();
        final Player localPlayer = (Player) paramInventoryCloseEvent.getPlayer();
        if ((localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.equipment-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.ores-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.specialty-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.food-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.raiding-inventory.title")))) ||
                (localInventory.getTitle().equalsIgnoreCase(ItemFilterPlugin.color(ItemFilterPlugin.getInstance().getConfig().getString("settings.potion-inventory.title"))))) {
            new BukkitRunnable() {
                public void run() {
                    MainInventory.getInstance().mainInventory(localPlayer);
                    cancel();
                }
            }

                    .runTaskTimer(ItemFilterPlugin.getInstance(), 5L, 0L);
            if (ItemFilterPlugin.getInstance().getConfig().getBoolean("settings.main-inventory.settings.category-close.enable")) {
                localPlayer.playSound(localPlayer.getLocation(), Sound.valueOf(ItemFilterPlugin.getInstance().getConfig().getString("settings.main-inventory.settings.category-close.sound-effect")), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-close.sound-volume"), ItemFilterPlugin.getInstance().getConfig().getInt("settings.main-inventory.settings.category-close.sound-pitch"));
            }
        }
    }
}

