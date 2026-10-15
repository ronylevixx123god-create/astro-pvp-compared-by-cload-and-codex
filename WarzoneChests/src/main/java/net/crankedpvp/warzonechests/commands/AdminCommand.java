package net.crankedpvp.warzonechests.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Optional;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.warzonechests.struct.items.WarzoneChestPlacerItem;
import net.crankedpvp.warzonechests.struct.items.WarzoneChestSummoner;
import org.bukkit.entity.Player;

@Command("cwc")
public class AdminCommand extends CommandBase {

    @Default
    public void command(Player sender) {
        sender.sendMessage(CC.translate("cwc item [player]"));
    }

    @SubCommand("item")
    public void giveChest(Player sender, @Optional Player player) {
       if (player != null) {
           player.getInventory().addItem(WarzoneChestPlacerItem.getItem());
           return;
       }

       sender.getInventory().addItem(WarzoneChestPlacerItem.getItem());
    }

    @SubCommand("summoner")
    public void giveSummoner(Player sender, @Optional Player player) {
        if (player != null) {
            player.getInventory().addItem(WarzoneChestSummoner.getItem());
            return;
        }

        sender.getInventory().addItem(WarzoneChestSummoner.getItem());
    }
}
