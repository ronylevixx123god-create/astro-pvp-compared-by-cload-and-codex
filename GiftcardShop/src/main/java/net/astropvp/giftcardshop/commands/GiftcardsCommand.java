package net.astropvp.giftcardshop.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.giftcardshop.GiftcardShopPlugin;
import net.astropvp.giftcardshop.guis.GiftcardMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GiftcardsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player sender = (Player)commandSender;

        if (GiftcardShopPlugin.getInstance().getGiftcards().get(sender.getUniqueId()) == null) {
            sender.sendMessage(CC.translate("&c&l(!)&c You do not have available giftcards!"));
            return false;
        }


        GiftcardMenu giftcardMenu = new GiftcardMenu(sender.getUniqueId());
        giftcardMenu.getGiftcardsMenu().open(sender);
        sender.sendMessage(CC.translate("&a&l(!)&a Opening the giftcards menu..."));
        return true;
    }
}
