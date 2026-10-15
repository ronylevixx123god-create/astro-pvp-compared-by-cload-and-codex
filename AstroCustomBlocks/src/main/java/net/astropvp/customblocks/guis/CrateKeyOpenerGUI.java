package net.astropvp.customblocks.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.enums.CrateType;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import net.astropvp.CollectPlugin;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.utils.ChunkUtil;
import net.astropvp.customblocks.utils.CrateKeyAPI;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
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

import java.util.*;

@Getter
public class CrateKeyOpenerGUI {
    private final PaginatedGui customBlockGui;

    public CrateKeyOpenerGUI(CustomBlock customBlock, Player player) {
        player.sendMessage(CC.translate("&a&l(!)&a Opening the Personal CrateKey Opener!"));
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 5, 5);
        this.customBlockGui = new PaginatedGui(3, 27, "CrateKey Custom Block Menu");

        HashMap<CrateType, Integer> keys = CrateKeyAPI.getCrateKeysInInventory(player.getInventory());
        int tickets = 0;
        for (Integer amount : keys.values()) {
            tickets += amount;
        }


        ItemStack confirmButton = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta meta = confirmButton.getItemMeta();
        meta.setDisplayName(CC.translate("&a&lConfirm Button"));
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7&nRight-Click&7 to open &f" + tickets + "&7 Cratekeys"));
        meta.setLore(lore);
        confirmButton.setItemMeta(meta);

        int finalTickets = tickets;

        this.customBlockGui.setItem(2, 3, new GuiItem(customBlock.getType().build(0)));


        customBlockGui.setItem(2, 5, new GuiItem(confirmButton, event -> {

            if (finalTickets == 0) {
                player.sendMessage(CC.translate("&c&l(!)&c You have no cratekeys in your inventory!"));
                return;
            }


            long money = 0L;
            int exp = 0;
            List<ItemStack> rewardedItems = new ArrayList<>();
            for (Map.Entry<CrateType, Integer> crate : keys.entrySet()) {
                CrateType type = crate.getKey();
                Integer amount = crate.getValue();
                while (amount != 0) {
                    ItemStack itemStack = LootTableAPI.getRandomLootItem(net.astropvp.loottable.struct.LootTable.valueOf(type.name()));
                    if (!LootFilterAPI.isFiltered(player, LootTable.valueOf(type.name()), itemStack)) {
                        if (ArkhamMoneyNotesAPI.isMoneyNote(itemStack)) {
                            double moneyNoteVal = ArkhamMoneyNotesAPI.getMoneyNoteVal(itemStack);
                            money += (long) moneyNoteVal;
                            /*
                            EconomyUtils.depositBalance(player, moneyNoteVal);
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ $" + moneyNoteVal);

                             */
                        } else if (ExpBottles.isXpBottle(itemStack)) {
                            int xpValue = ExpBottles.getXpAmount(itemStack);
                            exp += xpValue;
                            /*
                            int exp = (player.getTotalExperience() + xpValue);
                            player.setTotalExperience(0);
                            player.setExp(0);
                            player.setLevel(0);
                            player.giveExp(exp);
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ " + xpValue + " xp");

                             */
                        } else {
                            rewardedItems.add(itemStack);
                        }
                    }
                    amount--;
                }
            }

            /*
            Location nearbyChest = ChunkUtil.getClosestChestLocation(customBlock.getLocation());
            if (nearbyChest != null) {
                Chest chest = (Chest) nearbyChest.getBlock().getState();
                if (!ChunkUtil.isEmpty(chest.getInventory())) {
                    player.sendMessage(CC.translate("&c&l(!)&c Please empty the chest before opening new cratekeys!"));
                    return;
                }
                for (Map.Entry<CrateType, Integer> crate : keys.entrySet()) {
                    CrateType type = crate.getKey();
                    Integer amount = crate.getValue();
                    while (amount != 0) {
                        if (!ChunkUtil.isEmpty(chest.getInventory())) {
                            Location newNearbyChest = ChunkUtil.getClosestChestLocation(customBlock.getLocation());
                            if (newNearbyChest != null) {
                                chest = (Chest) newNearbyChest.getBlock().getState();
                            }
                        }
                        ItemStack itemStack = LootTableAPI.getRandomLootItem(net.astropvp.loottable.struct.LootTable.valueOf(type.name()));
                        if (!LootFilterAPI.isFiltered(player, LootTable.valueOf(type.name()), itemStack)) {
                            if (ArkhamMoneyNotesAPI.isMoneyNote(itemStack)) {
                                double moneyNoteVal = ArkhamMoneyNotesAPI.getMoneyNoteVal(itemStack);
                                EconomyUtils.depositBalance(player, moneyNoteVal);
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                                player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ $" + moneyNoteVal);
                            } else if (ExpBottles.isXpBottle(itemStack)) {
                                int xpValue = ExpBottles.getXpAmount(itemStack);
                                int exp = (player.getTotalExperience() + xpValue);
                                player.setTotalExperience(0);
                                player.setExp(0);
                                player.setLevel(0);
                                player.giveExp(exp);
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                                player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ " + xpValue + " xp");
                            } else {
                                chest.getInventory().addItem(itemStack);
                            }
                        }
                        amount--;
                    }
                }

            } else {
                player.sendMessage(CC.translate("&c&l(!)&c There are no nearby empty chests to fill with loot"));
                return;
            }

             */

            if(exp > 0) {
                int finalExp = player.getTotalExperience() + exp;
                player.setTotalExperience(0);
                player.setExp(0);
                player.setLevel(0);
                player.giveExp(finalExp);
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ " + exp + " xp");
            }

            if(money > 0) {
                EconomyUtils.depositBalance(player, money);
                player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+ $" + NumberUtils.formatMoney(money));
            }

            //remove slots from player inv
            Inventory inventory = player.getInventory();
            for (ItemStack itemStack : inventory) {
                if (AstroCrateKeysAPI.isCrateKey(itemStack)) {
                    inventory.remove(itemStack);
                }
            }
            player.getInventory().setContents(inventory.getContents());
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 5, 5);

            PlayerCollect collect = CollectPlugin.getInstance().getCollectManager().getOrCreate(player.getUniqueId().toString());
            ItemStack[] sortedArray = rewardedItems.stream()
                    .sorted(Comparator.comparing(item -> item.getType().name()))
                    .toArray(ItemStack[]::new);
            CollectCategory collectCategory = new CollectCategory(CollectType.CRTAEKEY, sortedArray, CC.translate("&6&lCrate Key Opener Loot"), System.currentTimeMillis());
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
                player.sendMessage(CC.translate("&c&l(!)&c There are no nearby chests to clear."));
                return;
            }

            player.closeInventory();
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 5, 5);
            player.sendMessage(CC.translate("&c&l(!)&c Cleared " + counter + " nearby chests."));

        }));


    }


}
