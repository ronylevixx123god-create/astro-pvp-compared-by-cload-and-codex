package net.crankedpvp.hypeboxes.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.inventory.HypeboxBattleInventory;
import net.crankedpvp.hypeboxes.utils.Arrow;
import net.crankedpvp.hypeboxes.utils.SoundUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("viewbattleopen")
public class ViewBattleOpenCommand extends CommandBase {

    private final HypeboxesPlugin plugin;
    public ViewBattleOpenCommand(HypeboxesPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(final Player player, String crateID) {
        if(!this.plugin.getHypeboxBattles().containsKey(UUID.fromString(crateID))) return;

        HypeboxBattleInventory hypeboxBattleInventory = this.plugin.getHypeboxBattles().get(UUID.fromString(crateID));
        if(!hypeboxBattleInventory.isActive()) {
            player.sendMessage(CC.translate("&4&lHypebox Battles &8" + Arrow.arrow() + " &eThis hype box battle has finished being opened"));
            SoundUtils.tryPlayErrorSound(player);
            return;
        }
        this.plugin.getHypeboxBattles().get(UUID.fromString(crateID)).getUI().open(player);
    }
}