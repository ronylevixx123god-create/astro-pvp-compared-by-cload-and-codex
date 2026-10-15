package net.crankedpvp.arcade.commands;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.arcade.guis.ArcadeGUI;
import org.bukkit.entity.Player;

@Command("arcade")
public class ArcadeCommand extends CommandBase {
    @Default
    public void onDefault(Player player) {
        ArcadeGUI arcadeGUI = new ArcadeGUI();
        arcadeGUI.getArcadeGui().open(player);
    }
}
