package net.astropvp.core.commands;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.core.guis.GiftsGUI;
import org.bukkit.entity.Player;

@Command("gifts")
public class GiftsCommand extends CommandBase {

    @Default
    public void showGifts(Player sender) {
        GiftsGUI giftsGui = new GiftsGUI(sender);
        giftsGui.getGui().open(sender);
    }
}
