package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.astropvp.core.AstroCore;
import net.astropvp.core.struct.AstroPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ToggleKillTitleCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!(sender instanceof Player)) return false;
        AstroPlayer astroPlayer = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(((Player) sender).getUniqueId());
        if (astroPlayer.isKillTitleEnabled()) {
            astroPlayer.setKillTitleEnabled(false);
            sender.sendMessage(CC.translate("&e&l(!)&e Kill titles have been &c&lDISABLED&e."));
        } else {
            astroPlayer.setKillTitleEnabled(true);
            sender.sendMessage(CC.translate("&e&l(!)&e Kill titles have been &a&lENABLED&e."));
        }
        return true;
    }
}