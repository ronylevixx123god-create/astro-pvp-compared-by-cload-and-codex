package me.astropvp.astropvpskins.commands;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RemoveBrokenSkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(CC.translate("&cNo Perms."));
            return false;
        }

        Player p = (Player) commandSender;

        ItemStack item = p.getItemInHand();


        ItemMeta meta = item.getItemMeta();
        ItemStack newItem = new ItemStack(item.getType());
        newItem.setItemMeta(meta);
        p.getInventory().addItem(newItem);
        p.getItemInHand().setType(null);
        p.sendMessage(CC.translate("&aDone!"));

        return true;

    }
}
