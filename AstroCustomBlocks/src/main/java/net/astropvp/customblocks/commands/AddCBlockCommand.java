package net.astropvp.customblocks.commands;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command("addcblock")
@Alias("addcustomblock")
public class AddCBlockCommand extends CommandBase {

    @Default
    @Permission("sav.*")
    public void defCmd(Player player, String string) {
        ItemStack itemStack = player.getItemInHand();
        NBTWrapper nbtItem = new NBTWrapper(itemStack);
        NBTTagCompoundBuilder cosmicData = nbtItem.hasKey("cosmicData") ? new NBTTagCompoundBuilder(nbtItem.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        NBTTagCompoundBuilder cosmicData2 = cosmicData.hasKey("customBlockData") ? new NBTTagCompoundBuilder(cosmicData.getNBTBase("customBlockData")) : new NBTTagCompoundBuilder();
        nbtItem.setString("cosmicType", "custom_block");
        cosmicData2.setInt("level", 1);
        cosmicData2.setString("type", string);
        cosmicData.setNBTBase("customBlockData", cosmicData2.get());
        nbtItem.setNBTBase("cosmicData", cosmicData.get());
        player.setItemInHand(nbtItem.build());
        player.updateInventory();
    }
}
