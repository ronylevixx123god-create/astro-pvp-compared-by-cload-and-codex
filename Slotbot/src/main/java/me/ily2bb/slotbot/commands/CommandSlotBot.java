package me.ily2bb.slotbot.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ily2bb.slotbot.CosmicSlotBotAPI;
import me.ily2bb.slotbot.SlotBotPlugin;
import me.ily2bb.slotbot.utils.Inventories;
import me.ily2bb.slotbot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandSlotBot implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command arg1, final String arg2, final String[] args) {
        if (args.length >= 1) {
            if (args[0].contentEquals("enable")) {
                SlotBotPlugin.getInstance().getConfig().set("slot-bot-enabled", true);
                SlotBotPlugin.getInstance().saveConfig();
                SlotBotPlugin.getInstance().loadConfig();
                sender.sendMessage(CC.Green + "Slot bots enabled.");
                return false;
            }
            if (args[0].contentEquals("disable")) {
                SlotBotPlugin.getInstance().getConfig().set("slot-bot-enabled", false);
                SlotBotPlugin.getInstance().saveConfig();
                SlotBotPlugin.getInstance().loadConfig();
                sender.sendMessage(CC.Red + "Slot bots disabled.");
                return false;
            }
        }
        if (args.length >= 3 && args[0].contentEquals("give")) {
            final Player player = Bukkit.getPlayer(args[1]);
            final int amount = Integer.parseInt(args[2]);
            final ItemStack item2 = CosmicSlotBotAPI.createSlotBotItem();
            item2.setAmount(amount);

            if (Utils.isInventoryFull(player)) {
                player.getWorld().dropItem(player.getLocation(), item2);
                sender.sendMessage(CC.Green + "Dropped " + amount + " slot bot tickets at " + player.getName() + "'s location.");
                return false;
            }
            player.getInventory().addItem(item2);
            sender.sendMessage(CC.Green + "Gave " + player.getName() + " " + amount + " slot bot tickets");
            return false;
        }
        if (args.length >= 3 && args[0].contentEquals("giveheroic")) {
            final Player player = Bukkit.getPlayer(args[1]);
            final int amount = Integer.parseInt(args[2]);
            final ItemStack item2 = CosmicSlotBotAPI.createHeroicSlotBotItem();
            item2.setAmount(amount);

            if (Utils.isInventoryFull(player)) {
                player.getWorld().dropItem(player.getLocation(), item2);
                sender.sendMessage(CC.Green + "Dropped " + amount + " heroic slot bot tickets at " + player.getName() + "'s location.");
                return false;
            }
            player.getInventory().addItem(item2);
            sender.sendMessage(CC.Green + "Gave " + player.getName() + " " + amount + " heroic slot bot tickets");
            return false;
        }
        final Player player = (Player) sender;
        if (!SlotBotPlugin.getInstance().getConfig().getBoolean("slot-bot-enabled") && !player.isOp()) {
            player.sendMessage(CC.Red + "Slot bots are currently disabled.");
            return false;
        }
        player.openInventory(Inventories.createInventory(0));
        return false;
    }
}
