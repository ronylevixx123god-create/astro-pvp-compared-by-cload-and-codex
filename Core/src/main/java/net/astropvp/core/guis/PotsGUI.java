package net.astropvp.core.guis;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.astropvp.core.AstroCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class PotsGUI {
    private PaginatedGui potsGUI;



    public PaginatedGui getPotsGUI() {
        return this.potsGUI;
    }





    public PotsGUI(Player player) {

        Economy economy = AstroCore.getInstance().getEcon();



        this.potsGUI = new PaginatedGui(1, 9, "Pots GUI");

        potsGUI.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });




        ItemStack pot = new ItemBuilder(new ItemStack(Material.POTION, 1, (short) 16421))
                .name(CC.translate("&a&lInstant Health II"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Click to purchase 1x potion"))
                .addLoreLine(CC.translate("&7Shift-Click to fill your inventory"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&a&lCOST: &f$55&7/pot"))

                .build();
        ItemStack stackedPot = new ItemBuilder(new ItemStack(Material.POTION, 8, (short) 16421))
                .name(CC.translate("&a&l8x Instant Health II &7(Stacked)"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Click to purchase 1x potions &8(Stacked)"))
                .addLoreLine(CC.translate("&7Shift-Click to fill your inventory"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&a&lCOST: &f$1000&7/pot"))
                .build();




        potsGUI.setItem(1, 4, new GuiItem((pot), event -> {
            int amount = 0;
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(CC.translate("&c&l(!)&c You have a full inventory, please try again."));
                return;
            }

            if (event.isShiftClick()) {
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR)
                        amount++;
                }
            } else {
                amount = 1;
            }


            double cost = (amount * 55);
            if (economy.has(player, cost)) {

                economy.withdrawPlayer(player, cost);
                for (int i = 0; i < amount; i++) {
                    player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                }
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 10, 1);
                player.sendMessage(CC.translate( "&c&l- $" + NumberUtils.formatMoney(cost)));
                player.sendMessage(CC.translate("&a+" + amount + "&a Instant Health II Pots"));
            } else {
                player.sendMessage(CC.translate("&cYou don't have enough money, requires $" + NumberUtils.formatMoney(cost) + "!"));
            }
        }));
        potsGUI.setItem(1, 6, new GuiItem((stackedPot), event -> {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(CC.translate("&c&l(!)&c You have a full inventory, please try again."));
                return;
            }
            Potion splash = new Potion(PotionType.INSTANT_HEAL, 2);
            splash.setSplash(true);

            ItemStack stackPot = splash.toItemStack(8);
            ItemMeta im = stackPot.getItemMeta();
            List<String> lore = Lists.newArrayList(CC.Gray + "Stackable (8)");
            im.setLore(lore);
            stackPot.setItemMeta(im);

            int amount = 0;

            if (event.isShiftClick()) {
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR)
                        amount++;
                }
            } else {
                amount = 1;
            }


            double cost = (amount * 1000);
            if (economy.has(player, cost)) {

                economy.withdrawPlayer(player, cost);
                for (int i = 0; i < amount; i++) {
                    stackPot = new NBTWrapper(stackPot).setString("cosmicType", "stackable").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("maxStackSize", 8).setLong("uniquePots", Long.valueOf(System.nanoTime())).get()).build();
                    player.getInventory().addItem(stackPot);
                }
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 10, 1);
                player.sendMessage(CC.translate( "&c&l- $" + NumberUtils.formatMoney(cost)));
                player.sendMessage(CC.translate("&a+" + amount + "&a Stacked Instant Health II Pots"));
            } else {
                player.sendMessage(CC.translate("&cYou don't have enough money, requires $" + NumberUtils.formatMoney(cost) + "!"));
            }
        }));




    }


}
