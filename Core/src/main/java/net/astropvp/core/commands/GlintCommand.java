package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GlintUtils;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

@Command("addglint")
public class GlintCommand extends CommandBase {

    @Default
    @Permission("glint.*")
    public void glint(Player player, Integer colour) {
        GlintUtils.addGlint(player.getItemInHand(), colour);
        player.updateInventory();
        player.sendMessage(CC.translate("&aGlinted Item!"));
    }


}
