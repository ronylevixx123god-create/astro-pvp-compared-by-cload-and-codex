package net.crankedpvp.pvptop.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.pvptop.PvPTopPlugin;
import net.crankedpvp.pvptop.data.FactionData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("pvptop")
public class PvPTopCommand extends CommandBase {

    private final PvPTopPlugin plugin;
    public PvPTopCommand(PvPTopPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(final Player player) {
        this.plugin.getPvPTopInventory().getUI().open(player);
        player.updateInventory();
    }

    @SubCommand("force")
    @Alias("update")
    @Permission("pvptop.admin")
    public void forceUpdateCommand(final CommandSender commandSender) {
        this.plugin.getUpdateLeaderboardTask().update();
    }

    @SubCommand("add")
    @Permission("pvptop.admin")
    public void addPointsCommand(final CommandSender commandSender, @Completion("#factions") String factionTag, @Completion("#enum") final FactionData.Points points) {
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if(faction == null) {
            commandSender.sendMessage(CC.translate("&cThat is not a faction g"));
            return;
        }
        this.plugin.getPvpTopManager().getOrCreate(faction).addPoint(points);
        commandSender.sendMessage(CC.translate("&aSuccessfully added " + points.getPoints() + " points to " + factionTag));
    }

    @SubCommand("remove")
    @Permission("pvptop.admin")
    public void removePointsCommand(final CommandSender commandSender, @Completion("#factions") String factionTag, @Completion("#enum") final FactionData.Points points) {
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if(faction == null) {
            commandSender.sendMessage(CC.translate("&cThat is not a faction g"));
            return;
        }
        this.plugin.getPvpTopManager().getOrCreate(faction).removePoint(points);
        commandSender.sendMessage(CC.translate("&aSuccessfully removed " + points.getPoints() + " points from " + factionTag));
    }
}
