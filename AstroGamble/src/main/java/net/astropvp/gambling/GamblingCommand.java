package net.astropvp.gambling;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("gambling")
@Alias({"gamble"})
public class GamblingCommand extends CommandBase {

    private GamblingPlugin plugin;
    public GamblingCommand(GamblingPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;

        GamblingInventory gamblingInventory = new GamblingInventory(player);
        gamblingInventory.getUI().open(player);
    }
}
