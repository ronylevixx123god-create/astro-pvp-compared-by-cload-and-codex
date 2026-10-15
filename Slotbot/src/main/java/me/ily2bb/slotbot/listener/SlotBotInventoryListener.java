package me.ily2bb.slotbot.listener;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import me.ily2bb.slotbot.CosmicSlotBotAPI;
import me.ily2bb.slotbot.SlotBotPlugin;
import me.ily2bb.slotbot.managers.SlotBotManager;
import me.ily2bb.slotbot.objects.ItemDeleter;
import me.ily2bb.slotbot.utils.GuiItems;
import me.ily2bb.slotbot.utils.Inventories;
import me.ily2bb.slotbot.utils.Utils;
import net.astropvp.core.utils.OutpostUtil;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SlotBotInventoryListener implements Listener {
    public static void returnTickets(int tickets, Player player) {
        player.sendMessage(CC.GreenB + "(!) " + CC.Green + "The bot has returned " + tickets + " " + ((tickets > 1) ? "tickets" : "ticket") + " to your inventory.");
        for (int i = 0; i < tickets; i++) {
            if (!Utils.isInventoryFull(player)) {
                player.getInventory().addItem(CosmicSlotBotAPI.createSlotBotItem());
            } else {
                player.getWorld().dropItem(player.getLocation(), CosmicSlotBotAPI.createSlotBotItem());
            }
            player.updateInventory();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void inventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        if (inventory.getTitle().contentEquals(CC.DarkGray + "Astro Roll Bot")) {
            ItemStack item = e.getCurrentItem();
            e.setCancelled(true);
            if (item == null)
                return;
            int tickets = SlotBotManager.getSlotBotTickets(inventory);
            if (item.getType() == GuiItems.createLootTable().getType()) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 3.0F, 1F);
                Bukkit.getScheduler().runTaskLater(SlotBotPlugin.getInstance(), () -> player.openInventory(Inventories.createLootboxInventory(player.isOp())), 1L);
            } else if (item.equals(GuiItems.createRoller(0))) {
                if (tickets > 0)
                    returnTickets(1, player);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 3.0F);
                inventory.setContents(Inventories.createInventory(tickets - 1).getContents());
                player.updateInventory();
            } else if (item.equals(GuiItems.createSpinTicket(false))) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
            } else if (item.equals(GuiItems.createRoller(5))) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
                player.sendMessage(CC.translate("&c&l(!)&c This slot requires 5 more ticket in the bot to use!"));
                player.sendMessage(CC.translate("&7Purchase Slot Bot Tickets at &rbuy.astropvp.net"));
            } else if (item.equals(GuiItems.createRoller(4))) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
                player.sendMessage(CC.translate("&c&l(!)&c This slot requires 4 more ticket in the bot to use!"));
                player.sendMessage(CC.translate("&7Purchase Slot Bot Tickets at &rbuy.astropvp.net"));
            } else if (item.equals(GuiItems.createRoller(3))) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
                player.sendMessage(CC.translate("&c&l(!)&c This slot requires 3 more ticket in the bot to use!"));
                player.sendMessage(CC.translate("&7Purchase Slot Bot Tickets at &rbuy.astropvp.net"));
            } else if (item.equals(GuiItems.createRoller(2))) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
                player.sendMessage(CC.translate("&c&l(!)&c This slot requires 2 more ticket in the bot to use!"));
                player.sendMessage(CC.translate("&7Purchase Slot Bot Tickets at &rbuy.astropvp.net"));
            } else if (item.equals(GuiItems.createRoller(1))) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
                player.sendMessage(CC.translate("&c&l(!)&c This slot requires 1 more ticket in the bot to use!"));
                player.sendMessage(CC.translate("&7Purchase Slot Bot Tickets at &rbuy.astropvp.net"));
            } else if (CosmicSlotBotAPI.isSlotbotTicket(item) || CosmicSlotBotAPI.isHeroicSlotbotTicket(item)) {
                if (tickets >= 5)
                    return;
                for (int i = 2; i <= 6; i++) {
                    if (player.hasMetadata("slot_rolling_" + i))
                        return;
                }
                inventory.setContents(Inventories.createInventory(tickets + 1).getContents());
                if (item.getAmount() == 1) {
                    e.setCurrentItem(null);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 50.0F);
                player.updateInventory();
            } else if (tickets > 0 && item.equals(GuiItems.createReturnSlotBotsButton(tickets))) {
                returnTickets(tickets, player);
                inventory.setContents(Inventories.createInventory(0).getContents());
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 3.0F);
            } else if (tickets > 0 && item.equals(GuiItems.createSpinTicket(true))) {
                ItemStack[] contents = Inventories.createRollingInventory(player, 5 - tickets).getContents();
                inventory.setContents(contents);
                SlotBotManager.startRolling(player, inventory, tickets);
                player.updateInventory();

                if (FactionUtils.getFaction(player) != null && HCFOutpostAPI.isControllingOutpost(FactionUtils.getFaction(player), OutpostType.COURTYARD) && Math.random() < 0.1) {
                    returnTickets(tickets, player);
                    player.sendMessage("");
                    player.sendMessage(CC.translate("&c&l** INFERNAL STRONGHOLD BONUS &7Saved &dSlotbot Tickets &c&l**"));
                    player.sendMessage("");
                } else if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.COSMIC_CITIZEN_HELMET_CG) && Math.random() < 0.05) {
                    returnTickets(tickets, player);
                    player.sendMessage("");
                    player.sendMessage(CC.translate("&b&lCosmicGames Hat: &fHas returned your SlotBot Tickets!"));
                    player.sendMessage("");
                } else if (OutpostUtil.hasPlutoOutpost(player) && Math.random() < 0.075) {
                    returnTickets(tickets, player);
                    player.sendMessage("");
                    player.sendMessage(CC.translate("&e&l* PLUTO OUTPOST &7Saved Slotbot Ticket(s) &e&l*"));
                    player.sendMessage("");

                }
            }
        } else if (inventory.getTitle().contentEquals(CC.DarkGray + "Astro Roll Bot Loot")) {
            ItemStack item = e.getCurrentItem();
            e.setCancelled(true);
            if (item == null)
                return;
            player.sendMessage(CC.translate("&7The odds of pulling this item is equal to all others"));
            if (!inventory.equals(e.getInventory()))
                return;
            inventory = e.getInventory();
            if (item.equals(GuiItems.createBackButton())) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 3.0F, 1F);
                Bukkit.getScheduler().runTaskLater(SlotBotPlugin.getInstance(), () -> player.openInventory(Inventories.createInventory(0)), 1L);
            } else {
                if (!player.hasPermission("slotbot.item.delete"))
                    return;
                int count = 0;
                ItemDeleter other = ItemDeleter.getPlayer(player);
                if (other == null || !other.isDeletionPane(item)) {
                    ItemStack copy = item.clone();
                    if (copy.hasItemMeta() && copy.getItemMeta().hasLore()) {
                        ItemMeta meta = copy.getItemMeta();
                        List<String> lore = meta.getLore();
                        lore.remove(meta.getLore().size() - 1);
                        meta.setLore(lore);
                        copy.setItemMeta(meta);
                    }
                    List<ItemStack> lootTableItems;
                    for (int length = (lootTableItems = GuiItems.getLootTableItems()).size(), j = 0; j < length; j++) {
                        ItemStack x = lootTableItems.get(j);
                        if (x.equals(copy))
                            count++;
                    }
                    if (count == 0)
                        return;
                    ItemDeleter deleter = new ItemDeleter(player, item, inventory);
                    if (deleter.getPlayer() != null)
                        deleter.addDeletionPane(e.getSlot());
                } else {
                    if (e.getClick() == ClickType.LEFT) {
                        if (other.isDeletionPane(item)) {
                            ItemDeleter.removeItemDeleter(other);
                            ItemStack[] items = Inventories.createLootboxInventory(player.isOp()).getContents();
                            inventory.setContents(items);
                            player.updateInventory();
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                            player.sendMessage(CC.Red + "You have cancelled your current Item Deletion of the item: '" + Utils.getDisplayName(other.getItem()) + "'");
                        }
                        return;
                    }
                    if (e.getClick() == ClickType.RIGHT && other.isDeletionPane(item)) {
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0F, 3.0F);
                        other.deleteItem();
                    }
                }
            }
        }
    }




    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player))
            return;
        Inventory inventory = e.getInventory();
        if (!inventory.getTitle().contentEquals(CC.DarkGray + "Astro Roll Bot"))
            return;
        int slots = SlotBotManager.getSlotBotTickets(inventory);
        Player player = (Player) e.getPlayer();
        if (slots <= 0) {
            for (int i = 2; i <= 6; i++) {
                if (player.hasMetadata("slot_rolling_" + i)) {
                    player.removeMetadata("slot_rolling_" + i, SlotBotPlugin.getInstance());
                }
            }
            return;
        }

        Bukkit.getScheduler().runTaskLater(SlotBotPlugin.getInstance(), ()->{
            if (player == null) return;
            if (player.isOnline()) {
                returnTickets(slots, player);
            }
        },100);

    }

    @EventHandler
    public void trash(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String msg = event.getMessage().toLowerCase();
        if (CombatLog.inCombat(player) && msg.startsWith("/eec")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot use /eec while in combat!");

        }
    }





    @EventHandler
    public void itemDeletionInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player))
            return;
        Inventory inventory = e.getInventory();
        if (!inventory.getTitle().contentEquals(CC.DarkGray + "Astro Roll Bot Loot"))
            return;
        Player player = (Player) e.getPlayer();
        ItemDeleter deleter = ItemDeleter.getPlayer(player);
        if (deleter == null)
            return;
        ItemDeleter.removeItemDeleter(deleter);
    }

    @EventHandler
    public void anticrash(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) {
            if(e.getEntity().getWorld().getName().contains("dungeon"))
                return;
            e.setCancelled(true);
        }
    }

}

