/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

final class CommandBump
        extends AbstractCommand {
    public CommandBump(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            FPlayer fp = FPlayers.getInstance().getByPlayer(p);
            FLocation fl = new FLocation(p.getLocation());
            if (!fp.getRelationToLocation().isAtLeast(Relation.ALLY) && !FactionUtils.getFactionAt(fl).isWarZone() && !FactionUtils.getFactionAt(fl).isNone()) {
                p.sendMessage(ChatColor.RED + "You cannot use /bump in enemy territory.");
                return true;
            }
            if (p.getHealth() - 1.0 > 0.0) {
                p.damage(1.0);
            } else {
                p.sendMessage(ChatColor.RED + "You cannot execute this command at 1HP.");
            }
        }
        return true;
    }
}

