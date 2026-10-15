package net.astropvp.adminitems.commands;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;

import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.language.Language;
import net.astropvp.adminitems.language.LanguageHandler;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.config.AdminItemConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("ai")
public class AdminItemCommand extends CommandBase {

    private final LanguageHandler languageHandler;
    private final AdminItemConfigManager adminItemConfigManager;

    public AdminItemCommand(AdminItemsPlugin plugin) {
        this.languageHandler = plugin.getLanguageHandler();
        this.adminItemConfigManager = plugin.getPetConfigManager();
    }

    @Default
    @Permission("cranked.admin")
    public void mainCommand(CommandSender sender) {
        Language.ADMIN_COMMAND_HELP.send(sender);
    }

    @SubCommand("item")
    @WrongUsage("&cUsage: /ai item <adminItem> <uses> [player]")
    @Permission("cranked.admin")
    public void itemCommand(CommandSender sender,  @Completion("#adminItems") AdminItem adminItem, Integer uses ,@Completion("#players") @Optional Player player) {
        if (adminItem == null) {
            Language.INVALID_ADMIN_ITEM.send(sender);
            return;
        }

        if (player != null) {
            player.getInventory().addItem(adminItem.getItem(uses));
            return;
        }

        if (sender instanceof Player) {
            ((Player) sender).getInventory().addItem(adminItem.getItem(uses));
        }
    }

    @SubCommand("reload")
    @WrongUsage("&cUsage: /ai reload")
    @Permission("cranked.admin")
    public void reloadCommand(Player sender) {
        Language.ADMIN_RELOADING.send(sender);
        long then = System.currentTimeMillis();

        this.languageHandler.load();
        this.adminItemConfigManager.reloadFiles();

        long diff = System.currentTimeMillis() - then;
        Language.ADMIN_RELOADED.send(sender, str -> str.replace("%took%", String.valueOf(diff)));
    }
}