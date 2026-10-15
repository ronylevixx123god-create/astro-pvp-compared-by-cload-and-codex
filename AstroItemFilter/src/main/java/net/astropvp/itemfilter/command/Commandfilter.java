package net.astropvp.itemfilter.command;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.itemfilter.ItemFilterPlugin;
import net.astropvp.itemfilter.data.FilterUser;
import net.astropvp.itemfilter.utils.MainInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

@Command("filter")
@Alias({"ft", "filters"})
public class Commandfilter extends CommandBase {

    @Default
    public void onDefeault(CommandSender sender) {
        Iterator<String> localObject1;
        String localObject2;
        for (localObject1 = ItemFilterPlugin.getInstance().getConfig().getStringList("messages.item-filter-usage").iterator(); localObject1.hasNext(); ) {
            localObject2 = (String) ((Iterator) localObject1).next();
            sender.sendMessage(ItemFilterPlugin.color(localObject2));
        }
    }

    @SubCommand("edit")
    public void onEdit(CommandSender sender) {
        MainInventory.getInstance().mainInventory((Player) sender);
    }

    @SubCommand("toggle")
    public void onToggle(CommandSender sender) {
        Player player = (Player) sender;
        Iterator<String> localObject1;
        String localObject2;
        FilterUser filterUser = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(player.getUniqueId());
        if (filterUser.isEnabled()) {

            filterUser.setEnabled(false);
            for (localObject1 = ItemFilterPlugin.getInstance().getConfig().getStringList("messages.item-filter-disable").iterator(); localObject1.hasNext(); ) {
                localObject2 = (localObject1).next();
                sender.sendMessage(ItemFilterPlugin.color(localObject2));
            }
        } else {
            filterUser.setEnabled(true);
            for (localObject1 = ItemFilterPlugin.getInstance().getConfig().getStringList("messages.item-filter-enable").iterator(); localObject1.hasNext(); ) {
                localObject2 = localObject1.next();
                sender.sendMessage(ItemFilterPlugin.color(localObject2));
            }
        }
    }

    @Permission("filter.admin")
    @SubCommand("reload")
    public void onReload(CommandSender sender) {
        Iterator<String> localObject1;
        String localObject2;
        ItemFilterPlugin.getInstance().reloadConfig();
        for (localObject1 = ItemFilterPlugin.getInstance().getConfig().getStringList("messages.item-filter-reload").iterator(); localObject1.hasNext(); ) {
            localObject2 = localObject1.next();
            sender.sendMessage(ItemFilterPlugin.color(localObject2));
        }
    }


}