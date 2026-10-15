package org.arkhamnetwork.enchantments.cmds;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import org.arkhamnetwork.enchantments.enchantments.armor.Phoenix;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPhoenixCooldown
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 0) {
            boolean canUse = Phoenix.canUsePhoenix(player);
            if (!canUse) {
                if (120L - (System.currentTimeMillis() - Phoenix.last_phoenix.get(player.getUniqueId().toString())) / 1000L != 0L) {
                    player.sendMessage("");
                }
                player.sendMessage(CC.translate((String)("&c&l** PHOENIX ON COOLDOWN ** &7[" + TimeUtils.formatSeconds((long)(120L - (System.currentTimeMillis() - Phoenix.last_phoenix.get(player.getUniqueId().toString())) / 1000L)) + "]")));
                player.sendMessage("");
                return true;
            }
            player.sendMessage(CC.translate((String)("&a&l(!) &a" + player.getName() + "'s phoenix is not on cooldown!")));
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(CC.translate((String)"&c&l(!) &cPlayer not found!"));
                return true;
            }
            if (target == player) {
                player.sendMessage(CC.translate((String)"&c&l(!) &cYou can review your own phoenix cooldown by using /phoenixcooldown!"));
                return true;
            }
            boolean canUse = Phoenix.canUsePhoenix(target);
            if (!canUse) {
                if (120L - (System.currentTimeMillis() - Phoenix.last_phoenix.get(target.getUniqueId().toString())) / 1000L != 0L) {
                    player.sendMessage("");
                }
                player.sendMessage(CC.translate((String)("&c&l** " + target.getName() + "'s PHOENIX IS ON COOLDOWN ** &7[" + TimeUtils.formatSeconds((long)(120L - (System.currentTimeMillis() - Phoenix.last_phoenix.get(target.getUniqueId().toString())) / 1000L)) + "]")));
                player.sendMessage("");
                return true;
            }
            player.sendMessage(CC.translate((String)("&a&l(!) &a" + target.getName() + "'s phoenix is not on cooldown!")));
            return true;
        }
        return false;
    }
}
