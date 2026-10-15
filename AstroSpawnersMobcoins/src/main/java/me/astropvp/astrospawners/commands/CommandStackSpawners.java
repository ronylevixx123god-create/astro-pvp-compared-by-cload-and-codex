package me.astropvp.astrospawners.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.astropvp.astrospawners.items.ItemSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CommandStackSpawners implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!(sender instanceof Player)) return false;
        Map<EntityType, Integer> spawners = new HashMap<>();
        Player player = ((Player) sender).getPlayer();

        for (ItemStack item : player.getInventory().getContents()) {

            int amount = ItemSpawner.getSpawnerAmount(item);
            EntityType type = ItemSpawner.getEntityTypeFromItem(item);



            if(type == null || amount <= -1) {
                continue;
            }

            if (item.getAmount() >1) {
                amount *=item.getAmount();
            }

            player.getInventory().remove(item);
            if (spawners.containsKey(type)) {
                int currentAmount = spawners.get(type);
                spawners.put(type, currentAmount+amount);
            } else {
                spawners.put(type, amount);
            }
        }

        if (spawners.isEmpty()) {
            player.sendMessage(CC.translate("&c&l(!)&c Could not find valid spawners to stack."));
            return false;
        }

        for (Map.Entry<EntityType, Integer> entry : spawners.entrySet()) {
            EntityType type = entry.getKey();
            int amount = entry.getValue();
            PlayerUtil.giveOrDropProtectedItem(player, ItemSpawner.getItem(type, amount), -1);
        }
        player.sendMessage(CC.translate("&a&l(!)&a Stacked all the spawners in your inventory."));

        return true;
    }
}
