package me.ifamasssxd.cosmicmysterychest.halflootbox;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHalfLootbox implements CommandExecutor {
    String error = CC.Red + "Correct usage: /halflootbox give <player> <lootboxInternalName> <LEFT/RIGHT>";
    //ItemStack lootbox = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(args[2]).createClaimableItem(false);


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if (!player.isOp())
            return true;
        if (args.length < 3) {
            player.sendMessage(error);
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        String lootboxInternalName = args[2];
        if (args.length == 3) {

            HalfLootbox left = new HalfLootbox(lootboxInternalName, HalfLootboxType.LEFT);
            HalfLootbox right = new HalfLootbox(lootboxInternalName, HalfLootboxType.RIGHT);
            target.getInventory().addItem(left.getHalfLootboxItem());
            target.getInventory().addItem(right.getHalfLootboxItem());
            target.sendMessage(CC.translate("&aYou have received the half lootboxes!"));
            return true;
        }
        HalfLootboxType halfLootboxType = HalfLootboxType.valueOf(args[3]);
        HalfLootbox halfLootbox = new HalfLootbox(lootboxInternalName, halfLootboxType);

        target.getInventory().addItem(halfLootbox.getHalfLootboxItem());
        target.sendMessage(CC.translate("&aYou have received a half lootbox!"));

        return true;
    }

}
