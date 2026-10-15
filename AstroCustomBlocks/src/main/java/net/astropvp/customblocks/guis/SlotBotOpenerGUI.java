package net.astropvp.customblocks.guis;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import me.ily2bb.slotbot.CosmicSlotBotAPI;
import net.astropvp.CollectPlugin;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.utils.ChunkUtil;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.struct.LootTable;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import net.astropvp.struct.PlayerCollect;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class SlotBotOpenerGUI {

    private final PaginatedGui customBlockGui;

    public SlotBotOpenerGUI(CustomBlock customBlock, Player p) {
        p.sendMessage(CC.translate("&a&l(!)&a Opening the Personal Slotbot Opener!"));
        p.playSound(p.getLocation(), Sound.CHEST_OPEN, 5, 5);
        this.customBlockGui = new PaginatedGui(3, 27, "Slotbot Custom Block Menu");

        int tickets = CosmicSlotBotAPI.getSlotsInInventory(p);



        ItemStack confirmButton = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta meta = confirmButton.getItemMeta();
        meta.setDisplayName(CC.translate("&a&lConfirm Button"));
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7&nRight-Click&7 to open &f" + tickets + "&7 Slotbot Tickets"));
        meta.setLore(lore);
        confirmButton.setItemMeta(meta);


        this.customBlockGui.setItem(2, 3, new GuiItem(customBlock.getType().build(0)));


        customBlockGui.setItem(2, 5, new GuiItem(confirmButton, event -> {


            if (tickets == 0) {
                p.sendMessage(CC.translate("&c&l(!)&c You have no slots in your inventory!"));
                return;
            }

            int a = tickets;

            Inventory inventory = p.getInventory();
            for (ItemStack itemStack : inventory) {
                if (CosmicSlotBotAPI.isSlotbotTicket(itemStack)) {
                    inventory.remove(itemStack);
                }
            }


            long money = 0L;
            int exp = 0;
            List<ItemStack> rewardedItems = new ArrayList<>();
            while (a != 0) {

                final ItemStack itemStack = CosmicSlotBotAPI.getRandomSlotBotLootItem();
                if (!LootFilterAPI.isFiltered(p, LootTable.SLOTS, itemStack)) {
                    if (ArkhamMoneyNotesAPI.isMoneyNote(itemStack)) {
                        double moneyNoteVal = ArkhamMoneyNotesAPI.getMoneyNoteVal(itemStack);
                        money += (long) moneyNoteVal;
                        //EconomyUtils.depositBalance(p, moneyNoteVal);
                        //p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                        //p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ $" + moneyNoteVal);
                    } else if (ExpBottles.isXpBottle(itemStack)) {
                        int xpValue = ExpBottles.getXpAmount(itemStack);
                        exp += xpValue;
                        /*
                        int exp = (p.getTotalExperience() + xpValue);
                        p.setTotalExperience(0);
                        p.setExp(0);
                        p.setLevel(0);
                        p.giveExp(exp);
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ " + xpValue + " xp");

                         */
                    } else {
                        if (itemStack == null) continue;
                        rewardedItems.add(itemStack);
                    }
                }
                a--;
            }

            /*
            Location nearbyChest = ChunkUtil.getClosestChestLocation(customBlock.getLocation());
            if (nearbyChest != null) {
                Chest chest = (Chest) nearbyChest.getBlock().getState();
                if (!ChunkUtil.isEmpty(chest.getInventory())) {
                    p.sendMessage(CC.translate("&c&l(!)&c Please empty the chest before opening new slotbots!"));
                    return;
                }
                //remove slots from player inv
                Inventory inventory = p.getInventory();
                for (ItemStack itemStack : inventory) {
                    if (CosmicSlotBotAPI.isSlotbotTicket(itemStack)) {
                        inventory.remove(itemStack);
                    }
                }
                p.getInventory().setContents(inventory.getContents());
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.VILLAGER_YES, 5, 5);

                while (a != 0) {

                    if (!ChunkUtil.isEmpty(chest.getInventory())) {
                        Location newNearbyChest = ChunkUtil.getClosestChestLocation(chest.getLocation());
                        if (newNearbyChest != null) {
                            chest = (Chest) newNearbyChest.getBlock().getState();
                        }
                    }
                    final ItemStack itemStack = CosmicSlotBotAPI.getRandomSlotBotLootItem();
                    if (!LootFilterAPI.isFiltered(p, LootTable.SLOTS, itemStack)) {
                        if (ArkhamMoneyNotesAPI.isMoneyNote(itemStack)) {
                            double moneyNoteVal = ArkhamMoneyNotesAPI.getMoneyNoteVal(itemStack);
                            EconomyUtils.depositBalance(p, moneyNoteVal);
                            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ $" + moneyNoteVal);
                        } else if (ExpBottles.isXpBottle(itemStack)) {
                            int xpValue = ExpBottles.getXpAmount(itemStack);
                            int exp = (p.getTotalExperience() + xpValue);
                            p.setTotalExperience(0);
                            p.setExp(0);
                            p.setLevel(0);
                            p.giveExp(exp);
                            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ " + xpValue + " xp");
                        } else {
                            if (itemStack == null) continue;
                            chest.getInventory().addItem(itemStack);
                        }
                    }
                    a--;
                }


            } else {
                p.sendMessage(CC.translate("&c&l(!)&c There are no nearby empty chests to fill with loot"));
                return;
            }


             */

            if(exp > 0) {
                int finalExp = p.getTotalExperience() + exp;
                p.setTotalExperience(0);
                p.setExp(0);
                p.setLevel(0);
                p.giveExp(finalExp);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ " + exp + " xp");
            }

            if(money > 0) {
                EconomyUtils.depositBalance(p, money);
                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ $" + NumberUtils.formatMoney(money));
            }

            p.getInventory().setContents(inventory.getContents());
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.VILLAGER_YES, 5, 5);

            PlayerCollect collect = CollectPlugin.getInstance().getCollectManager().getOrCreate(p.getUniqueId().toString());
            ItemStack[] sortedArray = rewardedItems.stream()
                    .sorted(Comparator.comparing(item -> item.getType().name()))
                    .toArray(ItemStack[]::new);
            CollectCategory collectCategory = new CollectCategory(CollectType.SLOTBOT, sortedArray, CC.translate("&6&lSlot Bot Opener Loot"), System.currentTimeMillis());
            collect.add(collectCategory);


        }));

        customBlockGui.getFiller().fillBorder(new GuiItem(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).name(CC.translate("&7")).build()));

        customBlockGui.setDefaultClickAction(event -> event.setCancelled(true));


        ItemStack clearButton = new ItemBuilder(Material.HOPPER, CC.translate("&c&lClear Chests"))
                .addLoreLine(CC.translate("&7Click here to clear all nearby chests."))
                .build();
        customBlockGui.setItem(2, 7, new GuiItem(clearButton, event -> {


            int counter = 0;
            for (Location location : ChunkUtil.getNearbyChests(customBlock.getLocation())) {
                Chest chest = (Chest) location.getBlock().getState();

                if (!ChunkUtil.isEmpty(chest.getInventory())) {
                    chest.getInventory().clear();
                    counter++;
                }
            }
            if (counter == 0) {
                p.sendMessage(CC.translate("&c&l(!)&c There are no nearby chests to clear."));

                return;
            }

            p.closeInventory();
            p.playSound(p.getLocation(), Sound.VILLAGER_YES, 5, 5);
            p.sendMessage(CC.translate("&c&l(!)&c Cleared " + counter + " nearby chests."));


        }));


    }
}
