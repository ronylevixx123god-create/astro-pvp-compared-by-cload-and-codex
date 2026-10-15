package net.astropvp.customblocks.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.guis.CustomBlocksGui;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("customblocks")
@Alias({"cblocks", "customblock"})
public class CustomBlockCommand extends CommandBase {

    @Default
    public void onDefault(CommandSender player)  {
        player.sendMessage(CC.translate("&e&l(!)&e Opening the Custom Blocks Menu..."));
        CustomBlocksGui customBlocksGui = null;
        try {
            customBlocksGui = new CustomBlocksGui((Player) player);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        customBlocksGui.getGui().open((Player) player);
    }

    @SubCommand("give")
    @Permission("astro.admin")
    public void onGive(Player player, String type, Integer level) {
        player.getInventory().addItem(CustomBlockType.valueOf(type).build(level));
        player.sendMessage(CC.Green + "Received Custom Block!");
    }

    @SubCommand("reload")
    @Permission("astro.admin")
    public void onReload(Player player) {
        CustomBlocks.getInstance().getConfigManager().reloadFiles();
        player.sendMessage(CC.Green + "Reloaded Custom Blocks");
    }
}
