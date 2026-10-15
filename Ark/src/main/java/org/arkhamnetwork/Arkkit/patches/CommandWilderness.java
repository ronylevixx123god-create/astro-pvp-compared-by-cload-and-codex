/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

final class CommandWilderness
        extends AbstractCommand {
    public CommandWilderness(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();
            if (!WildernessCommand.worlds.contains(world.getName())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', WildernessCommand.messageWorld));
                return true;
            }
            if (!WorldGuardUtils.isPvPDisabled(player.getLocation())) {
                player.sendMessage(ChatColor.RED + "This command cannot be used in PvP-enabled areas.");
                player.sendMessage(ChatColor.GRAY + "Teleport to /spawn and try again!");
                return true;
            }
            for (int counter = 0; counter < 25; ++counter) {
                int y;
                Block block;
                Random r = new Random();
                int xRange = WildernessCommand.minRange + r.nextInt(WildernessCommand.maxRange - WildernessCommand.minRange);
                int zRange = WildernessCommand.minRange + r.nextInt(WildernessCommand.maxRange - WildernessCommand.minRange);
                int x = WildernessCommand.xCenter - xRange;
                int z = WildernessCommand.zCenter - zRange;
                if (new Random().nextBoolean()) {
                    x = Math.abs(x);
                }
                if (new Random().nextBoolean()) {
                    z = Math.abs(z);
                }
                if (WildernessCommand.disabledBlocks.contains((block = world.getBlockAt(x, (y = world.getHighestBlockYAt(x, z)) - 1, z)).getType().toString()) || WildernessCommand.disabledBlocks.contains(block.getLocation().add(0.0, 1.0, 0.0).getBlock().getType().toString()) || !FactionUtils.getFactionAt(new FLocation(block.getLocation())).isNone())
                    continue;
                player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.67f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                player.teleport(new Location(world, (double) x + 0.5, y+4, (double) z + 0.5), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', WildernessCommand.messageSuccess.replace("{NAME}", player.getName()).replace("{X}", "" + x).replace("{Y}", "" + y).replace("{Z}", "" + z).replace("{WORLD}", world.getName())));
                //Bukkit.getLogger().info("Wilderness teleporting " + player.getName() + " to " + player.getLocation());
                return true;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', WildernessCommand.messageFailed));
            return true;
        }
        return false;
    }
}

