package me.ifamasssxd.cosmicmysterychest.commands;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.guis.FilterGUI;
import me.ifamasssxd.cosmicmysterychest.data.LootboxFilterData;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLootboxFilter implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player))
            return true;
        Player player = (Player) commandSender;
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be holding a lootbox to do this!");
            return true;
        }

        NBTTagCompoundBuilder builder = null;
        NBTWrapper wrapper = new NBTWrapper(item);
        try {
            builder = wrapper.getCompound("conaxData");
        } catch (NullPointerException ignored) {
        }
        if (builder == null) {
            try {
                builder = wrapper.getCompound("conaxData");
            } catch (NullPointerException ignored) {
            }
        }
        if (builder != null && builder.hasKey("mysteryChest")) {
            MysteryChest chest = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(builder.getString("mysteryChest"));
            LootboxFilterData filter = CosmicMysteryChest.get().getFilterManager().getLootFilterData(player.getUniqueId());

            new FilterGUI(player, chest).getFilterGUI().open(player);
        } else {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be holding a lootbox to do this!");
        }
        return false;
    }
}
