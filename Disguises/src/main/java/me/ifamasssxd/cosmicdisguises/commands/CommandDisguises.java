/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  me.libraryaddict.disguise.DisguiseAPI
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  org.apache.commons.lang.StringUtils
 *  org.arkhamnetwork.Arkkit.patches.CombatLog
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicdisguises.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.ifamasssxd.cosmicdisguises.CosmicDisguises;
import me.ifamasssxd.cosmicdisguises.gui.AvailableDisguiseMenu;
import me.ifamasssxd.cosmicdisguises.managers.CosmicDisguiseAPI;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDisguises
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DisguiseType type;
        Player player;
        block16: {
            block17: {
                player = null;
                if (!(sender instanceof Player)) break block16;
                player = (Player)sender;
                if (!CombatLog.inCombat(player)) break block17;
                player.sendMessage(CC.Red + "You cannot /disguise while in combat!");
                return true;
            }
            try {
                if (player.getWorld().getName().equals("world_duels2")) {
                    player.sendMessage(CC.Red + "You cannot /disguise while in a duel arena!");
                    return true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (player != null && args.length == 1 && (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("toggle"))) {
            if (DisguiseAPI.isDisguised(player)) {
                DisguiseAPI.undisguiseToAll(player);
                sender.sendMessage(CC.Red + "Your Disguise has been removed!");
            } else {
                sender.sendMessage(CC.Red + "You do not have an active disguise!");
                sender.sendMessage(CC.Gray + "Use /disguises to view available disguises!");
            }
            return true;
        }
        if (args.length == 1 && sender.isOp() && args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(CC.Red + "Invalid usage! /disguises give <playerName> <disguise>");
            return true;
        }
        if (!sender.isOp() || args.length != 3 || !args[0].equalsIgnoreCase("give")) {
            if (player != null) {
                String timeTillCanRun = CosmicDisguises.get().getDisguiseManager().getDisguiseCooldownString(player);
                if (timeTillCanRun != null) {
                    sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You must wait " + CC.RedU + timeTillCanRun + CC.Red + " before disguising again!");
                    return true;
                }
                new AvailableDisguiseMenu(player, CosmicDisguises.get().getDisguiseManager().getAvailableDisguises(player, false)).openGUI(CosmicDisguises.get());
            }
            return true;
        }
        Player pl = Bukkit.getPlayer(args[1]);
        if (pl == null) {
            sender.sendMessage(CC.Red + "Invalid player given!");
            return true;
        }
        try {
            type = DisguiseType.valueOf(args[2].toUpperCase());
        }
        catch (Exception e2) {
            sender.sendMessage(CC.Red + "Invalid disguise type given: " + args[2]);
            return true;
        }
        if (PlayerUtil.giveOrDropProtectedItem(pl, CosmicDisguiseAPI.createRedeemableDisguise(type), -1)) {
            sender.sendMessage(CC.Red + "Disguise added into " + pl.getName() + "'s inventory!");
        } else {
            sender.sendMessage(CC.Red + "Disguise dropped on floor for " + pl.getName() + "!");
        }
        pl.sendMessage(CC.GreenB + "(!) " + CC.Green + "You have been a claimable " + CC.GreenU + StringUtils.capitaliseAllWords(type.name().replace("_", " ").toLowerCase()) + CC.Green + " disguise!");
        return true;
    }
}

