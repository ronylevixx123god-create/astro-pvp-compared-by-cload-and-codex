package me.venir.astrocollectors.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.PermissibleAction;
import me.astropvp.astrospawners.AstroSpawners;
import me.venir.astrocollectors.collector.CollectorManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCollectorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Location location = ((Player)commandSender).getLocation();

        Player player = ((Player) commandSender).getPlayer();

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if (!CollectorManager.DELEGATE.isCollectorContained(location.getChunk())) {
            player.sendMessage(CC.translate("&c&l(!)&c No collector could be found in this chunk."));
            return true;
        }

        Location collectorLocation = CollectorManager.DELEGATE.getCollector(location).getLocation();

        if (FactionUtils.getFaction(player) != FactionUtils.getFactionAt(collectorLocation) && !player.isOp()) {
            player.sendMessage(CC.translate("&c&l(!)&c You need to be in the " + FactionUtils.getFactionAt(collectorLocation).getTag()) + " to remove this collector!");
            return true;
        }
        boolean hasAccess = fPlayer.getFaction().hasAccess(fPlayer, PermissibleAction.COLLECTOR_ACCESS);

        if (!hasAccess) {
            player.sendMessage(CC.translate("&c&l(!)&c You need a higher role in your faction to remove this collector."));
            return true;
        }

        assert collectorLocation != null;
        CollectorManager.DELEGATE.removeCollector(location);
        collectorLocation.getBlock().setType(Material.AIR,true);
        commandSender.sendMessage(CC.translate("&a&l(!)&a You have removed the chunk collector in this chunk. You may now place a new one!"));
        return true;
    }
}
