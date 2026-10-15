package com.drtshock.playervaults.commands;


import com.drtshock.playervaults.PlayerVaults;
import com.drtshock.playervaults.guis.SearchGUI;
import com.drtshock.playervaults.utils.CC;
import com.drtshock.playervaults.vaultmanagement.VaultManager;
import com.drtshock.playervaults.vaultmanagement.VaultOperations;
import com.drtshock.playervaults.vaultmanagement.VaultViewInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SearchCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] array) {
        if (VaultOperations.isLocked()) {
            commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7Vaults are locked!"));
            return true;
        } else {
            switch(array.length) {
                case 0:
                    if (commandSender instanceof Player) {
                        commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7/pvsearch <Item Name>"));
                        if (commandSender.hasPermission("playervaults.search.other")) {
                            commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7/pvsearch <item name> <Player>"));
                        }
                        return true;
                    }
                case 1:
                    if (commandSender instanceof Player) {
                        this.search((Player)commandSender, array[0], (Player)commandSender);
                    } else {
                        commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7Only players can execute this command!"));
                    }

                    return true;
                case 2:
                    if (!(commandSender instanceof Player)) {
                        return true;
                    } else if (!commandSender.hasPermission("playervaults.search.other")) {
                        commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7No perms to search other players' pv!"));
                        return true;
                    } else {
                        if (Bukkit.getPlayer(array[1]) == null) {
                            commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7No online player found with this name"));
                            return true;
                        }

                        this.search((Player)commandSender, array[0], Bukkit.getPlayer(array[1]));
                        return true;
                    }
                default:
                    commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7/pvsearch <item name>"));
                    if (commandSender.hasPermission("playervaults.search.other")) {
                        commandSender.sendMessage(CC.translate("&b&lVaults &8➥ &7/pvsearch <item name> <Player>"));

                    }
                    return true;
            }
        }
    }

    public static void openVault(Player player, UUID otherUUID, int vaultNr) {
        if (VaultOperations.openOtherVault(player, otherUUID.toString(), String.valueOf(vaultNr))) {
            PlayerVaults.getInstance().getInVault().put(player.getUniqueId().toString(), new VaultViewInfo(otherUUID.toString(), vaultNr));
        }

    }

    private void search(Player executor, String itemName, Player findFor) {
        Map<Integer, ArrayList<ItemStack>> found = new HashMap<>();
        itemName = itemName.toLowerCase();
        YamlConfiguration yamlConfiguration = VaultManager.getInstance().getPlayerVaultFile(findFor.getPlayer().getUniqueId().toString(), false);
        if (yamlConfiguration == null) {
            executor.sendMessage("&b&lVaults &8➥ &7You have no vaults to search trough.");
        } else {
            executor.sendMessage(CC.translate("&b&lVaults &8➥ &7Executing vault search query on &f\"" + itemName + "\"&7."));
            Iterator var6 = yamlConfiguration.getKeys(false).iterator();

            label48:
            while(true) {
                int pvNumber;
                Inventory inventory;
                do {
                    if (!var6.hasNext()) {
                        break label48;
                    }

                    String str = (String)var6.next();
                    pvNumber = Integer.parseInt(str.replace("vault", ""));
                    inventory = VaultManager.getInstance().getVault(findFor.getUniqueId().toString(), pvNumber);
                } while(inventory.getContents().length == 0);

                ArrayList<ItemStack> arrayFoundItems = new ArrayList<>();
                ItemStack[] var11 = inventory.getContents();

                for (ItemStack item : var11) {
                    if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null) {
                        String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase();
                        if (displayName.contains(itemName) && !found.containsKey(pvNumber)) {
                            arrayFoundItems.add(item);
                        }
                    }
                }

                if (!arrayFoundItems.isEmpty()) {
                    found.put(pvNumber, arrayFoundItems);
                }
            }
        }

        Map<Integer, ArrayList<ItemStack>> sortedFound = new TreeMap<>(found);
        if (sortedFound.isEmpty()) {
            executor.sendMessage(CC.translate("&b&lVaults &8➥ &7No items found containing &f\"" + itemName + "\" &7in their name."));
        } else {
            SearchGUI searchCommand = new SearchGUI(executor, findFor, sortedFound);
            searchCommand.getExpShopUI().open(executor);
        }

    }
}
