/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmasks.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import java.util.Arrays;
import me.ifamasssxd.cosmicmasks.CosmicMasks;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicmasks.menu.MaskHelpMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMask
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            new MaskHelpMenu((Player)sender).openGUI(CosmicMasks.get());
            return true;
        }
        if (!sender.isOp()) {
            return true;
        }
        if (!(args.length == 3 || args.length == 2 && args[0].equalsIgnoreCase("giverandom"))) {
            sender.sendMessage(CC.Red + "Usage: /mask give <name> <mask>");
            sender.sendMessage(CC.Red + "Usage: /mask giverandom <name>");
            return false;
        }
        Player pl = Bukkit.getPlayer((String)args[1]);
        if (pl == null) {
            sender.sendMessage(CC.Red + "Invalid Player!");
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            MaskType type = MaskType.getFromName(args[2].toUpperCase());
            if (type == null) {
                sender.sendMessage(CC.Red + "Invalid Mask given! Valid Masks: " + Arrays.toString(MaskType.values()));
                return true;
            }
            PlayerUtil.giveOrDropProtectedItem((Player)pl, (ItemStack)type.createMask(true), (int)-1, (String)null, (String)(CC.RedB + "(!) " + CC.Red + "Your mask has been dropped on the floor!"));
            sender.sendMessage(CC.Red + "Mask given to " + pl.getName() + "!");
        } else if (args[0].equalsIgnoreCase("giverandom")) {
            PlayerUtil.giveOrDropProtectedItem((Player)pl, (ItemStack)MaskType.createRandomMaskItem(true), (int)-1);
            sender.sendMessage(CC.Red + "Random mask given to " + pl.getName() + "!");
        }
        return true;
    }
}

