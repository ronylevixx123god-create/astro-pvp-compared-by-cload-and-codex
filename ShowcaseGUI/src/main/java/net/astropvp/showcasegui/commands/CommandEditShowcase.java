package net.astropvp.showcasegui.commands;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import net.astropvp.showcasegui.ShowcaseAPI;
import net.astropvp.showcasegui.custominventory.CustomInventory;
import net.astropvp.showcasegui.custominventory.CustomInventoryItem;
import net.astropvp.showcasegui.struct.Showcase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class CommandEditShowcase implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        } else if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /editshowcase <player> <add/set/remove> <rows>");
            return true;
        } else {
            String name = args[0];
            if (!StringUtils.isNumeric(args[2])) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid amount!");
                return true;
            } else {
                String action = args[1];
                int rows = Integer.parseInt(args[2]);
                Player pl = Bukkit.getPlayer(name);
                if (pl == null) {
                    sender.sendMessage(ChatColor.RED + "Not a valid player!");
                    return true;
                } else if (!ShowcaseAPI.hasShowcase(pl)) {
                    ShowcaseAPI.createShowcase(pl.getUniqueId(), pl.getName(), (String)null, ShowcaseAPI.getMaxShowcaseSize(pl));
                    if (!pl.hasPermission("showcasegui.showcase")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + pl.getName() + " permission set showcasegui.showcase");
                        pl.sendMessage((String)(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "You now have access to /showcase!"));
                    }

                    sender.sendMessage(ChatColor.RED + "Showcase created for " + pl.getName());
                    return true;
                } else {
                    Showcase showcase = ShowcaseAPI.getShowcase(pl.getUniqueId());
                    editShowcase(showcase, action, rows);
                    pl.sendMessage((String)(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Your Showcase has been updated."));
                    return true;
                }
            }
        }
    }

    public static Showcase editShowcase(Showcase showcase, String action, int rows) {
        int oldSize = showcase.getSize();
        int newSize = action.equalsIgnoreCase("remove") ? showcase.getSize() - rows * 9 : (action.equalsIgnoreCase("add") ? showcase.getSize() + rows * 9 : (action.equalsIgnoreCase("set") ? rows * 9 : showcase.getSize()));
        int currentPages = showcase.getPages();
        showcase.setSize(newSize);
        showcase.setEditted(true);
        boolean newPage = currentPages < showcase.getPages();
        if (oldSize != newSize) {
            int lastPage;
            CustomInventory newLastPage;
            if (newPage) {
                showcase.addInventory(showcase.createInventory(showcase.getPages()));
                lastPage = showcase.getPages() - 1;
                newLastPage = showcase.getPage(lastPage);
                if (newLastPage.getItem(newLastPage.getInventorySize() - 1) != null) {
                    CustomInventoryItem is = newLastPage.getItem(newLastPage.getInventorySize() - 1);
                    if (!Showcase.isPageButton(is)) {
                        newLastPage.removeItem(newLastPage.getInventorySize() - 1);
                        newLastPage.insertItem(newLastPage.getInventorySize() - 1, showcase.getPageButton(lastPage, true));
                        newLastPage.updateInventoryItems();
                        showcase.addItem(is);
                    }
                } else {
                    newLastPage.insertItem(newLastPage.getInventorySize() - 1, showcase.getPageButton(lastPage, false), true);
                    newLastPage.updateInventoryItems();
                }
            } else {
                if (newSize < oldSize && showcase.getPages() < showcase.getInventories().size()) {
                    for(lastPage = showcase.getInventories().size() - 1; lastPage > showcase.getPages(); --lastPage) {
                        showcase.getInventories().remove(lastPage);
                    }
                }

                CustomInventory var11 = showcase.getPage(showcase.getPages());
                newLastPage = showcase.createInventory(showcase.getPages());
                Iterator var12 = Lists.newArrayList((Iterable)var11.inventoryChest.getViewers()).iterator();

                while(var12.hasNext()) {
                    HumanEntity item = (HumanEntity)var12.next();
                    item.closeInventory();
                    newLastPage.openInventory((Player)item, false);
                }

                var12 = var11.getItems().entrySet().iterator();

                while(var12.hasNext()) {
                    Entry var13 = (Entry)var12.next();
                    if (!Showcase.isPageButton((ItemStack)var13.getValue())) {
                        newLastPage.insertItem((Integer)var13.getKey(), (CustomInventoryItem)var13.getValue());
                    }
                }

                newLastPage.updateInventoryItems();
                showcase.getInventories().remove(var11);
                showcase.getInventories().add(showcase.getPages() - 1, newLastPage);
                showcase.setEditted(true);
            }
        }

        return showcase;
    }
}
