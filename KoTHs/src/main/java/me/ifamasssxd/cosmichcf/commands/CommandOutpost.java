/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONMessage
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.LocationUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.massivecraft.factions.Faction
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmichcf.commands;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.massivecraft.factions.Faction;
import java.util.HashSet;

import me.ifamasssxd.cosmichcf.managers.OutpostManager;
import me.ifamasssxd.cosmichcf.menus.OutpostMenu;
import me.ifamasssxd.cosmichcf.struct.HCFOutpost;
import me.ifamasssxd.cosmichcf.struct.Outpost;
import me.ifamasssxd.cosmichcf.struct.OutpostState;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOutpost
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        OutpostManager manager = CosmicKOTHPlugin.get_().getOutpostManager();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("menu") && sender instanceof Player) {
                new OutpostMenu((Player)sender).openGUI(CosmicKOTHPlugin.get_());
                return true;
            }
            if (args[0].equalsIgnoreCase("sell") && sender instanceof Player) {
                manager.sellStronghold((Player)sender);
                return true;
            }
            if (!sender.isOp()) {
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                CosmicKOTHPlugin.get_().reloadConfig();
                sender.sendMessage(CC.Red + "Config reloaded!");
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(CC.Red + "Sell Region needs to contain: outpost_sell");
                sender.sendMessage(CC.RedB + "Elevator sign needs to be inside a region containing 'outpost'");
            } else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(CC.Red + "Showing internal outpost info..");
                sender.sendMessage("");
                for (Outpost post : manager.getOutposts()) {
                    sender.sendMessage(CC.RedB + post.getOutpostName());
                    sender.sendMessage(CC.RedB + " Region: " + post.getRegionName());
                    sender.sendMessage(CC.RedB + " Block region: " + post.getBlockRegionNames());
                    sender.sendMessage(CC.RedB + " Internal Name : " + post.getInternalOutpostName());
                    sender.sendMessage(CC.RedB + " last capper: " + post.getLastFactionToCap() + " (" + (post.getLastFactionToCapTag() == null ? "N/A" : post.getLastFactionToCapTag()) + ")");
                    sender.sendMessage(CC.RedB + " Defending Fac: " + (post.getDefendingFaction() != null ? post.getDefendingFaction().getTag() : "N/A"));
                    sender.sendMessage(CC.RedB + " Attacking Fac: " + (post.getAttackingFaction() != null ? post.getAttackingFaction().getTag() : "N/A"));
                    sender.sendMessage(CC.RedB + " Trophy Loc: " + (post.getTrophyChestLocation() != null ? LocationUtils.getStringFromLocation((Location)post.getTrophyChestLocation()) : "N/A"));
                }
                return true;
            }
        }
        if (args.length == 0) {
            if (!CosmicUtils.get().isVanillaGalaxy() && !CosmicUtils.get().isHcfGalaxy()) {
                new OutpostMenu((Player)sender).openGUI(CosmicKOTHPlugin.get_());
                return true;
            }
            sender.sendMessage("");
            sender.sendMessage(CosmicKOTHPlugin.get_().getMessage("cmd-header", new String[0]));
            sender.sendMessage("");
            Player player = (Player)sender;
            for (Outpost outpost : manager.getOutposts()) {
                JSONMessage hoverMessage = new JSONMessage("");
                OutpostType rewards = OutpostType.getByInternalName(outpost.getInternalOutpostName());
                hoverMessage.addHoverData(outpost.getOutpostName() + ": " + outpost.getOutpostState().getName() + (outpost.getOutpostState() == OutpostState.CONTROLLED ? CC.Gray + " (" + CC.Green + outpost.getLastFactionToCapTag() + CC.Gray + ")" : "") + CC.Gray + " " + outpost.getPrettyFormattedCapturePercent(), rewards != null ? rewards.getHoverFriendlyData(outpost) : "");
                hoverMessage.sendToPlayer(player);
                if (outpost.getTeleportLocation() != null) {
                    Location l = outpost.getTeleportLocation();
                    String color = outpost.getNameColor();
                    String colorB = outpost.getNameColor() + CC.Bold;
                    player.sendMessage("  " + color + l.getBlockX() + colorB + "x " + color + l.getBlockY() + colorB + "y " + color + l.getBlockZ() + colorB + "z" + (player.getWorld().equals((Object)outpost.getTeleportLocation().getWorld()) ? color + " (" + colorB + outpost.getDistanceDecimalFormat().format(outpost.getTeleportLocation().distance(player.getLocation())) + "m" + color + " away)" : ""));
                } else if (player.isOp()) {
                    player.sendMessage(CC.Red + "No Teleport Location set! Use /outpost create!");
                }
                player.sendMessage("");
            }
            sender.sendMessage(CC.Gray + "Hover over each " + CosmicKOTHPlugin.get_().getBaseName() + " to view their bonuses!");
            sender.sendMessage("");
            return true;
        }
        if (args.length >= 3) {
            if (sender.isOp()) {
                if (args.length == 5 && args[0].equalsIgnoreCase("create")) {
                    String internalName = args[1];
                    String displayName = ChatColor.translateAlternateColorCodes((char)'&', (String)args[2].replace("_", " "));
                    if (manager.getOutpostFromName(internalName) != null) {
                        sender.sendMessage(CC.RedB + "Outpost already exists! Use /outpost remove <internal> to delete it first!");
                        return true;
                    }
                    HCFOutpost newOutpost = new HCFOutpost(internalName, displayName, args[3], args[4], null, 0L);
                    manager.getOutposts().add(newOutpost);
                    if (sender instanceof Player) {
                        Player pl = (Player)sender;
                        newOutpost.setTeleportLocation(pl.getLocation());
                        sender.sendMessage(CC.Red + "Teleport location registered: " + LocationUtils.printPretty((Location)pl.getLocation(), (ChatColor)ChatColor.RED, (boolean)false));
                    }
                    manager.saveOutpostData(newOutpost, CosmicKOTHPlugin.get_().getConfig(), true);
                    CosmicKOTHPlugin.get_().saveConfig();
                    sender.sendMessage(CC.RedB + "Outpost registered: " + internalName + "!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("setcapper") || args[0].equalsIgnoreCase("setcap")) {
                    Double found;
                    Outpost from = manager.getOutpostFromName(args[1]);
                    if (from == null) {
                        sender.sendMessage(CC.RedB + "Unable to find outpost with that internal name! Use /outpost list to view internal names!");
                        return true;
                    }
                    Faction toSet = null;
                    boolean wiping = args[2].equalsIgnoreCase("none");
                    if (!(wiping || (toSet = FactionUtils.getFactionByTag((String)args[2])) != null && FactionUtils.isNormal((Faction)toSet))) {
                        sender.sendMessage(CC.RedB + "You must provide a valid faction to replace OR 'none'!");
                        return true;
                    }
                    from.onCap(toSet);
                    double capPercentage = 0.0;
                    if (args.length > 3 && (found = NumberUtils.parseDoubleOrNull((String)args[3])) != null) {
                        capPercentage = found;
                    }
                    from.setCapPercentage(capPercentage);
                    sender.sendMessage(CC.Red + "Outpost attacker now: " + (toSet == null ? "None" : toSet.getTag()) + " Cap Percentage now: " + from.getCapPercentage());
                    return true;
                }
            }
        } else if (args.length == 4) {
            if (sender.isOp() && args[0].equalsIgnoreCase("create")) {
                String internalName = args[1];
                String displayName = ChatColor.translateAlternateColorCodes((char)'&', (String)args[2].replace("_", " "));
                if (manager.getOutpostFromName(internalName) != null) {
                    sender.sendMessage(CC.RedB + "Outpost already exists! Use /outpost remove <internal> to delete it first!");
                    return true;
                }
                HCFOutpost newOutpost = new HCFOutpost(internalName, displayName, args[2], args[3], null, 0L);
                manager.getOutposts().add(newOutpost);
                manager.saveOutpostData(newOutpost, CosmicKOTHPlugin.get_().getConfig(), true);
                CosmicKOTHPlugin.get_().saveConfig();
                sender.sendMessage(CC.RedB + "Outpost registered: " + internalName + "!");
                if (sender instanceof Player) {
                    Player pl = (Player)sender;
                    newOutpost.setTeleportLocation(pl.getLocation());
                    sender.sendMessage(CC.Red + "Teleport location registered: " + LocationUtils.printPretty((Location)pl.getLocation(), (ChatColor)ChatColor.RED, (boolean)false));
                }
                return true;
            }
        } else if (args.length == 2) {
            if (sender.isOp() && args[0].equalsIgnoreCase("remove")) {
                Outpost found = manager.getOutpostFromName(args[1]);
                if (found == null) {
                    sender.sendMessage(CC.Red + "No outpost found with that name! Use /outpost list!");
                    return true;
                }
                manager.getOutposts().remove(found);
                found.restoreBrokenBlocks();
                found.restorePlacedBlocks();
                CosmicKOTHPlugin.get_().getConfig().set("outposts." + found.getInternalOutpostName(), null);
                CosmicKOTHPlugin.get_().saveConfig();
                sender.sendMessage(CC.Red + "Outpost removed!");
                return true;
            }
            if (sender.isOp() && (args[0].equalsIgnoreCase("settrophychest") || args[0].equalsIgnoreCase("spawntrophychest"))) {
                Outpost found = manager.getOutpostFromName(args[1]);
                if (found == null) {
                    sender.sendMessage(CC.Red + "No outpost found with that name! Use /outpost list!");
                    return true;
                }
                boolean set = args[0].equalsIgnoreCase("settrophychest");
                if (!set) {
                    found.spawnTrophyChest(1);
                    sender.sendMessage(CC.Red + "Trophy chest refilled!");
                    return true;
                }
                Player pl = (Player)sender;
                Block looking = pl.getTargetBlock((HashSet<Byte>) null, 4);
                if (looking != null && (looking.getType() == Material.CHEST || looking.getType() == Material.TRAPPED_CHEST)) {
                    found.setTrophyChestLocation(looking.getLocation());
                    manager.saveOutpostData(found, CosmicKOTHPlugin.get_().getConfig(), true);
                    sender.sendMessage(CC.Red + "Trophy chest set at your location!");
                    return true;
                }
                sender.sendMessage(CC.Red + "You must be looking at a chest!");
                return true;
            }
            if (sender.isOp() && args[0].equalsIgnoreCase("setsellroom")) {
                Outpost from = manager.getOutpostFromName(args[1]);
                if (from == null) {
                    sender.sendMessage(CC.RedB + "Unable to find outpost with that internal name! Use /outpost list to view internal names!");
                    return true;
                }
                sender.sendMessage(CC.Red + "Sell room set at your location!");
                from.setSellRoomLocation(((Player)sender).getLocation());
                manager.saveOutpostData(from, CosmicKOTHPlugin.get_().getConfig(), true);
                CosmicKOTHPlugin.get_().saveConfig();
                return true;
            }
        }
        if (sender.isOp()) {
            sender.sendMessage(CC.Red + "/outpost create <internalName> <display_Name> <cappingRegionName> <blockRegionName>");
            sender.sendMessage(CC.White + " Create outpost at your location with the given parameters.");
            sender.sendMessage(CC.Red + "/outpost setcapper <internalName> <name/none> [capPercent]");
            sender.sendMessage(CC.Red + "/outpost settrophychest <internalName>");
            sender.sendMessage(CC.Red + "/outpost setsellroom <internalName>");
            sender.sendMessage(CC.White + " Set the sell room location for the given name at your location");
        }
        return false;
    }
}

