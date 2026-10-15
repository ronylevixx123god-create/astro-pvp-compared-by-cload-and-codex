package net.crankedpvp.shards.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.earth2me.essentials.Mob;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.language.Language;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command("amobcoins")
@Alias({"amc", "acoins", "acoin"})
public class MobcoinsAdminCommand extends CommandBase {

    @SubCommand("reload")
    @Permission("cranked.admin")
    public void onReload(CommandSender sender) {
        Language.ADMIN_RELOADING.send(sender);
        long then = System.currentTimeMillis();

        MobcoinPlugin.getInstance().getLanguageHandler().load();
        MobcoinPlugin.getInstance().getShardsShopManager().reload(MobcoinPlugin.getInstance());

        long diff = System.currentTimeMillis() - then;
        Language.ADMIN_RELOADED.send(sender, str -> str.replace("%took%", String.valueOf(diff)));
    }

    @SubCommand("additem")
    @Permission("cranked.admin")
    public void onAddItem(CommandSender sender, Integer price) {
        Player player = ((Player)sender);
        ItemStack item = player.getItemInHand().clone();
        if (item == null || player.getItemInHand().getType() == Material.AIR) {
            Language.EMPTY_HAND.send(player);
            return;
        }
        MobcoinPlugin.getInstance().getShardsShopManager().getShopItems().put(item, price);
        MobcoinPlugin.getInstance().getShardsShopManager().addItemStack(item, price);

        Language.ADDED_ITEM_SHOP.send(player);

    }

    @SubCommand("give")
    @Permission("cranked.admin")
    public void onGiveShards(CommandSender sender, OfflinePlayer toPlayer, Integer amount) {
        Player player = ((Player)sender);
        MobcoinPlugin.getInstance().getShardsManager().getShardsUser(toPlayer.getUniqueId()).addShards(amount);
        if (toPlayer.isOnline()) {
            toPlayer.getPlayer().sendMessage(CC.translate(Language.SHARDS_ADDED.getMessage().replace("%amount%", NumberUtils.formatMoney(amount))));
        }
        String giving = Language.SHARDS_GIVING.getMessage().replace("%amount%", NumberUtils.formatMoney(amount));
        giving = giving.replace("%player%", toPlayer.getName());
        player.sendMessage(CC.translate(giving));
    }

    @SubCommand("reset")
    @Permission("cranked.admin")
    public void onResetShards(CommandSender sender, OfflinePlayer toPlayer) {
        Player player = ((Player)sender);
        MobcoinPlugin.getInstance().getShardsManager().getShardsUser(toPlayer.getUniqueId()).resetShards();
        if (toPlayer.isOnline()) {
            Language.RESET_SHARDS.send(toPlayer.getPlayer());
        }
        String reset = Language.RESET_SOMEONES_SHARDS.getMessage().replace("%player%", toPlayer.getName());
        player.sendMessage(CC.translate(reset));

    }

    @SubCommand("removeitem")
    @Permission("cranked.admin")
    public void onRemoveItem(CommandSender sender) {
        Player player = ((Player)sender);
        ItemStack item = player.getItemInHand().clone();
        if (item == null || item.getType() == Material.AIR) {
            Language.EMPTY_HAND.send(player);
            return;
        }
        if (!MobcoinPlugin.getInstance().getShardsShopManager().getShopItems().containsKey(item)) {
            Language.NOT_IN_SHOP.send(player);
            return;
        }
        MobcoinPlugin.getInstance().getShardsShopManager().getShopItems().remove(item);
        MobcoinPlugin.getInstance().getShardsShopManager().deleteItemStack(item);

        Language.REMOVED_ITEM_SHOP.send(player);

    }

}
