package me.ifamasssxd.cosmicbosses.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.ifamasssxd.cosmicbosses.struct.RewardInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandBossRewards implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player) sender;
        final RewardInventory rewardInventory = RewardInventory.getRewardInventory(player);
        if (rewardInventory == null) {
            player.sendMessage(ChatColor.RED + "You do not have items in your claim.");
            return true;
        }
        final boolean expired = rewardInventory.getExpirationTime() > 0L && rewardInventory.getExpirationTime() < System.currentTimeMillis();
        if (expired) {
            Bukkit.getLogger().info("Expiring previous collect items for " + player.getName() + "!");
            RewardInventory.removeRewardInventory(player);
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You had expired items inside your /bossrewards!");
            sender.sendMessage(CC.Gray + "Items can only be stored for 24h.");
            return true;
        }
        final int space = this.getEmptySpace(player);
        final List<ItemStack> items = rewardInventory.getItems();
        if (items.size() > player.getInventory().getSize()) {
            for (int i = 0; i < 9 && !items.isEmpty(); ++i) {
                final ItemStack item = items.get(0);
                PlayerUtil.giveOrDropProtectedItem(player, item, -1);
                items.remove(0);
            }
            player.sendMessage(CC.RedB + "You had > 54 items in your bossrewards, 9 have been given to you or dropped at your feet to make room.");
            player.sendMessage(CC.Gray + "Please re-run bossrewards to receive additional items.");
            return true;
        }
        if (space < rewardInventory.getItems().size()) {
            player.sendMessage(ChatColor.RED + "You must have " + ChatColor.RED + ChatColor.UNDERLINE.toString() + rewardInventory.getItems().size() + ChatColor.RED + " empty inventory slots to claim these items.");
            return true;
        }
        for (final ItemStack is : rewardInventory.getItems()) {
            player.getInventory().addItem(new ItemStack[]{is});
        }
        RewardInventory.removeRewardInventory(player);
        player.updateInventory();
        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.4f);
        player.sendMessage(CC.GreenB + "(!) " + CC.Green + "Rewards added to your inventory!");
        return false;
    }

    private int getEmptySpace(final Player pl) {
        int space = 0;
        for (final ItemStack is : pl.getInventory()) {
            if (is == null || is.getType() == Material.AIR) {
                ++space;
            }
        }
        return space;
    }
}
