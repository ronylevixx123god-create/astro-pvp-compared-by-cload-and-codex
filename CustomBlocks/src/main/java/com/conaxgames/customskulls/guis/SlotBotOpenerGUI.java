package com.conaxgames.customskulls.guis;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.CrateKeyAPI;
import com.conaxgames.customskulls.util.CustomBlockUtil;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import de.inventivegames.particle.ParticleEffect;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.AstroCrateKeysPlugin;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.utils.ConfigUtils;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import me.ily2bb.slotbot.CosmicSlotBotAPI;

import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.struct.LootTable;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.arkhamnetwork.Arkkit.patches.balancetop.BalanceTop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
public class SlotBotOpenerGUI {

    private final PaginatedGui customBlockGui;

    public SlotBotOpenerGUI(AbilityData data, Block block, Player p) {
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


        this.customBlockGui.setItem(2, 3, new GuiItem(CoreSkulls.customBlocks.get(data.getAbilityType().getName()).getCustomBlockItem()));


        customBlockGui.setItem(2, 5, new GuiItem(confirmButton, event -> {


            if (tickets == 0) {
                p.sendMessage(CC.translate("&c&l(!)&c You have no slots in your inventory!"));
                return;
            }

            int a = tickets;

            Location nearbyChest = CustomBlockUtil.getClosestChestLocation(data.getLocation());
            if (nearbyChest != null) {
                Chest chest = (Chest) nearbyChest.getBlock().getState();
                if (!CustomBlockUtil.isEmpty(chest.getInventory())) {
                    p.sendMessage(CC.translate("&c&l(!)&c Please empty the chest before opening new slotbots!"));
                    return;
                }

                while (a != 0) {

                    if (!CustomBlockUtil.isEmpty(chest.getInventory())) {
                        Location newNearbyChest = CustomBlockUtil.getClosestChestLocation(data.getLocation());
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
                            chest.getInventory().addItem(itemStack);
                        }
                    }
                    a--;
                }


            } else {
                p.sendMessage(CC.translate("&c&l(!)&c There are no nearby empty chests to fill with loot"));
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

            try {
                ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation(), 2, 2, 2, 0.05f, 40);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }));

        customBlockGui.getFiller().fillBorder(new GuiItem(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).name(CC.translate("&7")).build()));

        customBlockGui.setDefaultClickAction(event -> event.setCancelled(true));


        ItemStack clearButton = new ItemBuilder(Material.HOPPER, CC.translate("&c&lClear Chests"))
                .addLoreLine(CC.translate("&7Click here to clear all nearby chests."))
                .build();
        customBlockGui.setItem(2, 7, new GuiItem(clearButton, event -> {


            int counter = 0;
            for (Location location : CustomBlockUtil.getNearbyChests(data.getLocation())) {
                Chest chest = (Chest) location.getBlock().getState();

                if (!CustomBlockUtil.isEmpty(chest.getInventory())) {
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

            try {
                ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation(), 2, 2, 2, 0.05f, 40);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }));


    }
}
