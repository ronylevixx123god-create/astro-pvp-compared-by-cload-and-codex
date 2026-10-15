package me.ifamasssxd.cosmicconquest.commands;

import me.ifamasssxd.cosmicconquest.struct.ConquerData;
import java.util.List;
import me.ifamasssxd.cosmicconquest.struct.ConquestManager;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import me.ifamasssxd.cosmicconquest.utils.TimeUtils;
import me.ifamasssxd.cosmicconquest.listeners.ConquestListener;
import me.ifamasssxd.cosmicconquest.struct.ChestData;
import org.bukkit.Location;
import java.util.Map;
import me.ifamasssxd.cosmicconquest.CosmicConquests;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandConquest implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "ASTRO CONQUEST HELP");
            sender.sendMessage(ChatColor.GRAY + "Astro Conquests occurs every couple of hours. A /wild location is chosen at random, the coordinates are announced, and a " + ChatColor.UNDERLINE + "Conquest Chest" + ChatColor.GRAY + " spawns there 5 minutes later. This chest is defended by high level, powerful Astro Bosses and is often highly contested by enemy players too. The chest can only be opened after its " + ChatColor.GREEN + "150HP" + ChatColor.GRAY + " has been depleted (by hitting it). Astro Conquest chests drop Faction Tokens used for /f upgrade, Depth Strider books, and " + ChatColor.UNDERLINE + "other high tier loot.");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "If you are " + ChatColor.GRAY + ChatColor.UNDERLINE + "Tier 4 Rank+" + ChatColor.GRAY + " you can use " + ChatColor.GREEN + "/conquest" + ChatColor.GRAY + " to view more information about recent Astro Conquests.");
            sender.sendMessage("");
            return true;
        }
        if (args.length == 0) {
            if (!sender.hasPermission("enchantments.max.12") && !sender.hasPermission("enchantments.max.20")) {
                sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You do not have permission to view Conquest information!");
                sender.sendMessage(CC.Gray + "Unlock 4th rank or higher at " + CC.GrayU + "buy.astropvp.net" + CC.Gray + " to gain access!");
                return true;
            }
            final ConquestManager manager = CosmicConquests.get().getManager();
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "ASTRO CONQUEST INFORMATION");
            sender.sendMessage("");
            if (manager.getConquestChests().size() > 0) {
                final Map.Entry<Location, ChestData> active = manager.getConquestChests().entrySet().iterator().next();
                final Location first = active.getKey();
                final ChestData data = active.getValue();
                sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Location: " + ChatColor.GREEN + first.getBlockX() + "x " + first.getBlockY() + "y " + first.getBlockZ() + "z");
                sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Chest HP: " + ChatColor.GREEN + data.getHits() + " / " + ConquestListener.maxHits);
                int nearbyPlayers = 0;
                try {
                    final AxisAlignedBB bb = AxisAlignedBB.a(first.getX() - 100.0, first.getY() - 100.0, first.getZ() - 100.0, first.getX() + 100.0, first.getY() + 100.0, first.getZ() + 100.0);
                    final WorldServer server = ((CraftWorld)first.getWorld()).getHandle();
                    final List ents = server.a((Class) EntityPlayer.class, bb);
                    if (ents != null && ents.size() > 0) {
                        nearbyPlayers = ents.size();
                    }
                    sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Players Near Chest: " + ChatColor.GREEN + nearbyPlayers);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                final ConquerData data2 = manager.getLastConquer();
                sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Last Conquest: " + ChatColor.GREEN + TimeUtils.formatDifference((System.currentTimeMillis() - data2.getConquestTime()) / 1000L) + "ago");
                if (data2.getConquestLocation() != null) {
                    sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Last Location: " + ChatColor.GREEN + data2.getConquestLocation().getBlockX() + "x " + data2.getConquestLocation().getBlockY() + "y " + data2.getConquestLocation().getBlockZ() + "z");
                }
                if (data2.getWhoUnlocked() != null) {
                    sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Last Conquerer: " + ChatColor.GREEN + data2.getWhoUnlocked());
                }
            }
            sender.sendMessage("");
            return true;
        }
        else {
            if (!sender.isOp()) {
                return true;
            }
            if (args.length != 1) {
                return true;
            }
            if (args[0].equalsIgnoreCase("summon")) {
                final Location location = CosmicConquests.get().getManager().spawnConquestChest();
                ((Player)sender).teleport(location);
                sender.sendMessage(ChatColor.RED + "Conquest spawned...");
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                CosmicConquests.get().getManager().getConquestChests().forEach((loc, spawnTime) -> sender.sendMessage(ChatColor.RED + "Conquest at " + loc.getX() + "x " + loc.getY() + "y " + loc.getZ() + "z  with " + spawnTime.getHits() + " health."));
                final Location next = CosmicConquests.get().getManager().getNextSpawnLocation();
                if (next != null) {
                    sender.sendMessage(ChatColor.GREEN + "Next Conquest Location: " + next.getX() + "x " + next.getY() + "y " + next.getZ() + "z");
                }
                return true;
            }
            return false;
        }
    }
}
