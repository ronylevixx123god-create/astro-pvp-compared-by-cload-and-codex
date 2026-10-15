package me.astropvp.astrospawners.customitems;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.items.ItemSpawner;
import me.astropvp.astrospawners.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SpawnerCustomItem extends CustomItem {

    public SpawnerCustomItem(String prefix, String usage) {
        super(prefix, usage);
    }

    @Override
    public void proccessCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        int fixedLength = args.length - 1;
        if (fixedLength <= 1) {
            sender.sendMessage(getUnknownUsage());
            return;
        }
        int amount = Integer.parseInt(args[1]);

        ItemStack item = ItemSpawner.getItem(EntityType.valueOf(args[2].toUpperCase()), amount);
        player.getInventory().addItem(item);
        sender.sendMessage(CC.Red + "Gave " + player.getName() + CC.WhiteB + " " + amount + "x "
                + item.getItemMeta().getDisplayName() + "!");
    }
}
