package net.crankedpvp.shards.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.managers.MobcoinManager;
import net.crankedpvp.shards.data.MobcoinsUser;
import net.crankedpvp.shards.guis.MainMobcoinGUI;
import net.crankedpvp.shards.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("mobcoin")
@Alias({"mobcoins", "coins", "coin"})
public class MobcoinsCommand extends CommandBase {
    private MobcoinManager shardsManager;

    public MobcoinsCommand(MobcoinPlugin plugin) {
        this.shardsManager = MobcoinPlugin.getInstance().getShardsManager();
    }

    @Default
    public void onDefault(CommandSender sender) {
        MainMobcoinGUI mainShardsGUI = new MainMobcoinGUI((Player) sender);
        mainShardsGUI.getShardsGUI().open((Player)sender);
    }

    @SubCommand("balance")
    @Alias("bal")
    public void onBalance(CommandSender sender) {
        MobcoinsUser shardsUser = shardsManager.getShardsUser(((Player) sender).getUniqueId());
        sender.sendMessage(Language.BALANCE.getMessage().replace("%balance%", String.valueOf(shardsUser.getBalance())));
    }

    @SubCommand("shop")
    @Alias("itemshop")
    public void onShop(CommandSender sender) {
        MainMobcoinGUI mainShardsGUI = new MainMobcoinGUI((Player) sender);
        mainShardsGUI.getShardsGUI().open((Player)sender);
    }

    @SubCommand("help")
    @Alias("info")
    public void onHelp(CommandSender sender) {
        for (String s : Language.HELP.getValuesAsList()) {
            sender.sendMessage(CC.translate(s));
        }
    }
}
