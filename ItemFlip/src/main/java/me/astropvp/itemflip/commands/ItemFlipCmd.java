package me.astropvp.itemflip.commands;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import me.astropvp.itemflip.AstroItemFlipPlugin;
import me.astropvp.itemflip.struct.ItemFlipManager;
import me.astropvp.itemflip.struct.ItemFlipMatch;
import org.bukkit.Bukkit;
import me.ifamasssxd.cosmicbosses.struct.RewardInventory;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.BaseCommand;

@CommandAlias("itemflip|itemcoinflip|if|icf")
public class ItemFlipCmd extends BaseCommand
{
    private final AstroItemFlipPlugin plugin;
    private final InventoryManager inventoryManager;
    private final Map<UUID, UUID> outgoingRequests;

    public ItemFlipCmd(final AstroItemFlipPlugin plugin, final InventoryManager inventoryManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.outgoingRequests = new HashMap<UUID, UUID>();
    }

    @Default
    @CommandCompletion("@players")
    public void on(final Player player, final String[] args) {
        if (WorldGuardUtils.isPvPEnabled(player.getLocation())) {
            CommonsUtil.sendMessage((CommandSender)player, "&c&l(!) &cYou cannot use this command in PvP-enabled areas!");
            return;
        }
        if (args.length == 0) {
            CommonsUtil.sendMessage((CommandSender)player, (List)new ArrayList<String>() {
                {
                    this.add("&6&l&nItem Flip Commands");
                    this.add("&6/itemflip <player>");
                    this.add("&7Sends an item flip request to the specified <player>.");
                    this.add("&7Item Flips are similar to Coin Flips (/cf) but instead of money,");
                    this.add("&7you're wagering items.");
                }
            });
        }
        else {
            final Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                CommonsUtil.sendMessage((CommandSender)player, "&c&l(!) &cPlayer " + args[0] + " is not online!");
                return;
            }
            if (target.getUniqueId().equals(player.getUniqueId())) {
                CommonsUtil.sendMessage((CommandSender)player, "&c&l(!) &cYou cannot send an item flip request to yourself!");
                return;
            }
            if (player.getLocation().distance(target.getLocation()) > 60.0) {
                CommonsUtil.sendMessage((CommandSender)player, "&c&l(!) &cYou aren't close enough to " + target.getName() + " to send an item flip request!");
                CommonsUtil.sendMessage((CommandSender)player, "&7Move within 60 blocks of them and try again.");
                return;
            }
            if (this.outgoingRequests.containsKey(player.getUniqueId()) && this.outgoingRequests.get(player.getUniqueId()).equals(target.getUniqueId())) {
                CommonsUtil.sendMessage((CommandSender)player, "&c&l(!) &cYou have already sent a request to that player!");
                return;
            }
            if (ItemFlipManager.isInMatch(target)) {
                CommonsUtil.sendMessage((CommandSender)player, "&c&l(!) &cThat player is already in an item flip!");
                return;
            }
            if (this.outgoingRequests.containsKey(target.getUniqueId()) && this.outgoingRequests.get(target.getUniqueId()).equals(player.getUniqueId())) {
                this.outgoingRequests.remove(target.getUniqueId());
                final ItemFlipMatch match = new ItemFlipMatch(this.plugin, player, target);
                match.start();
                ItemFlipManager.addMatch(match);
            }
            else {
                CommonsUtil.sendMessage((CommandSender)target, "&e&l(!) &e" + player.getName() + " has sent an item flip request to you!");
                CommonsUtil.sendMessage((CommandSender)target, "&7Use /if " + player.getName() + " to accept this request.");
                CommonsUtil.sendMessage((CommandSender)player, "&6&l(!) &6You have sent an item flip request to " + target.getName() + "!");
                this.outgoingRequests.put(player.getUniqueId(), target.getUniqueId());
            }
        }
    }
}
