package net.crankedpvp.hypeboxes.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.inventory.HypeBoxAnimationInventory;
import net.crankedpvp.hypeboxes.utils.Arrow;
import net.crankedpvp.hypeboxes.utils.SoundUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("viewhypeboxopen")
public class ViewHypeboxOpenCommand extends CommandBase {

    private final HypeboxesPlugin plugin;
    public ViewHypeboxOpenCommand(HypeboxesPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(final Player player, String crateID) {
        if(!this.plugin.getHypeboxesBeingOpened().containsKey(UUID.fromString(crateID))) return;

        HypeBoxAnimationInventory hypeBoxAnimationInventory = this.plugin.getHypeboxesBeingOpened().get(UUID.fromString(crateID));
        if(!hypeBoxAnimationInventory.isActive()) {
            player.sendMessage(CC.translate("&d&lHype Boxes &8" + Arrow.arrow() + " &eThis hype box has finished being opened"));
            SoundUtils.tryPlayErrorSound(player);
            return;
        }
        this.plugin.getHypeboxesBeingOpened().get(UUID.fromString(crateID)).getUI().open(player);
    }
}