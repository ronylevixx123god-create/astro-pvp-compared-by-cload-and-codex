package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.astropvp.core.cooldown.HealCooldown;
import net.astropvp.core.utils.ChatManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    private HealCooldown cooldown = new HealCooldown();
    private FileConfiguration config;

    public HealCommand() {
        this.config = AstroCore.getPlugin(AstroCore.class).getConfig();
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player player = (Player) sender;
        if (cooldown.getCooldown(player)) return false;

        if (args.length == 0) {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.sendMessage(ChatManager.translate(config.getString("heal.msg")));
            cooldown.enterCooldown(player);
            return false;
        } else {
            player.sendMessage(CC.translate("&cUsage: /heal"));
            return false;
        }
    }

}

