package com.drtshock.playervaults.guis;

import com.drtshock.playervaults.commands.SearchCommand;
import com.drtshock.playervaults.utils.CC;
import de.tr7zw.nbtapi.NBTItem;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


    public class SearchGUI {
        private final PaginatedGui expShopUI;

        public PaginatedGui getExpShopUI() {
            return this.expShopUI;
        }

        public SearchGUI(Player executor, Player openFor, Map<Integer, ArrayList<ItemStack>> hashMap) {
            executor.sendMessage(CC.translate("&b&lVaults &8➥ &7Finished searching... Found &f" + hashMap.size() + "&7 unique vaults."));
            executor.playSound(executor.getLocation(), Sound.valueOf("CHEST_OPEN"), 10.0F, 10.0F);
            this.expShopUI = new PaginatedGui(6, 45, ChatColor.DARK_GRAY + openFor.getName() + " Vaults (" + hashMap.size() + " Unique)");
            this.expShopUI.setDefaultClickAction((event) -> {
                event.setCancelled(true);
            });
            this.expShopUI.getFiller().fillBottom(new GuiItem(this.createFillerItem()));
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName(CC.translate("&c<- Previous Page"));
            List<String> prevLore = new ArrayList();
            prevLore.add(CC.translate("&7Right-Click&7 to go to the previous page."));
            prevMeta.setLore(prevLore);
            prevPage.setItemMeta(prevMeta);
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName(CC.translate("&aNext Page ->"));
            List<String> nextLore = new ArrayList<>();
            nextLore.add(CC.translate("&7Right-Click&7 to go to the next page."));
            nextMeta.setLore(nextLore);
            nextPage.setItemMeta(nextMeta);
            this.expShopUI.setItem(6, 1, new GuiItem(prevPage, (event) -> {
                this.expShopUI.previous();
                event.setCancelled(true);
            }));
            this.expShopUI.setItem(6, 9, new GuiItem(nextPage, (event) -> {
                this.expShopUI.next();
                event.setCancelled(true);
            }));

            for (Map.Entry<Integer, ArrayList<ItemStack>> entry : hashMap.entrySet()) {
                int key = entry.getKey();
                List<ItemStack> items = entry.getValue();
                for (ItemStack itemStack : items) {
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(CC.translate(meta.getDisplayName() + " &f&c&lVault &f#" + key));
                    Object lore;
                    if (meta.getLore() == null) {
                        lore = new ArrayList<>();
                    } else {
                        lore = meta.getLore();
                    }

                    ((List) lore).add("");
                    ((List) lore).add(CC.translate("&7&nLeft-Click&7 to open vault &f&n#" + key + "&7."));
                    meta.setLore((List) lore);
                    itemStack.setItemMeta(meta);
                    NBTItem nbti = new NBTItem(itemStack);
                    nbti.setInteger("vaultNr", key);
                    this.expShopUI.addItem(new GuiItem(nbti.getItem(), (event) -> {
                        int vaultNr = nbti.getInteger("vaultNr");
                        SearchCommand.openVault(executor, openFor.getUniqueId(), vaultNr);
                    }));
                }
            }

        }

        public ItemStack createFillerItem() {
            ItemStack item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short)7);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(CC.translate("&f"));
            item.setItemMeta(meta);
            return item;
        }
    }
