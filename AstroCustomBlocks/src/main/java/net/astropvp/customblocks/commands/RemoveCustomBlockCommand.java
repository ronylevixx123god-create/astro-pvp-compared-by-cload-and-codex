package net.astropvp.customblocks.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.customblocks.CustomBlocksAPI;
import net.astropvp.customblocks.struct.CustomBlockType;
import org.bukkit.entity.Player;

@Command("removecblock")
public class RemoveCustomBlockCommand extends CommandBase {

    @Default
    @Permission("cblocks.remove")
    public void removeCblock(Player player, @Completion("#cblocks") CustomBlockType customBlockType) {
        if(!CustomBlocksAPI.chunkContainsCustomBlock(player.getLocation(), customBlockType)) {
            player.sendMessage(CC.translate("&cThere is no " + customBlockType.name() + " in this chunk"));
            return;
        }

        CustomBlocksAPI.removeCustomBlockFromChunkOfLocation(player.getLocation(), customBlockType);
        player.sendMessage(CC.translate("&aTried to break: " + customBlockType.name() + " in your chunk."));
    }
}
