package com.conaxgames.customskulls.guis;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.CrateKeyAPI;
import com.conaxgames.customskulls.util.CustomBlockUtil;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import de.inventivegames.particle.ParticleEffect;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.enums.CrateType;

import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CrateKeyOpenerGUI {
    private final PaginatedGui customBlockGui;

    public CrateKeyOpenerGUI(AbilityData data, Block block, Player player) {
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

        this.customBlockGui.setItem(2, 3, new GuiItem(CoreSkulls.customBlocks.get(data.getAbilityType().getName()).getCustomBlockItem()));


        customBlockGui.setItem(2, 5, new GuiItem(confirmButton, event -> {

            if (finalTickets == 0) {
                player.sendMessage(CC.translate("&c&l(!)&c You have no cratekeys in your inventory!"));
                return;
            }



            Location nearbyChest = CustomBlockUtil.getClosestChestLocation(data.getLocation());
            if (nearbyChest != null) {
                Chest chest = (Chest) nearbyChest.getBlock().getState();
                if (!CustomBlockUtil.isEmpty(chest.getInventory())) {
                    player.sendMessage(CC.translate("&c&l(!)&c Please empty the chest before opening new cratekeys!"));
                    return;
                }
                for (Map.Entry<CrateType, Integer> crate : keys.entrySet()) {
                    CrateType type = crate.getKey();
                    Integer amount = crate.getValue();
                    while (amount != 0) {
                        if (!CustomBlockUtil.isEmpty(chest.getInventory())) {
                            Location newNearbyChest = CustomBlockUtil.getClosestChestLocation(data.getLocation());
                            if (newNearbyChest != null) {
                                chest = (Chest) newNearbyChest.getBlock().getState();
                            }
                        }
                        ItemStack itemStack = LootTableAPI.getRandomLootItem(LootTable.valueOf(type.name()));
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

            try {
                ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 2, 2, 2, 0.05f, 40);
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
                player.sendMessage(CC.translate("&c&l(!)&c There are no nearby chests to clear."));
                return;
            }

            player.closeInventory();
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 5, 5);
            player.sendMessage(CC.translate("&c&l(!)&c Cleared " + counter + " nearby chests."));

            try {
                ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 2, 2, 2, 0.05f, 40);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }));


    }


}
