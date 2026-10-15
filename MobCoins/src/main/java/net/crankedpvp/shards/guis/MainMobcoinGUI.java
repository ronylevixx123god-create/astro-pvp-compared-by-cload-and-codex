package net.crankedpvp.shards.guis;

import com.cosmicpvp.cosmicutils.utils.*;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;

import net.astropvp.core.utils.ItemBuilder;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.data.MobcoinsUser;
import net.crankedpvp.shards.language.Language;
import net.crankedpvp.shards.managers.MobcoinManager;
import net.crankedpvp.shards.struct.MobcoinTransaction;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class MainMobcoinGUI {

    private final PaginatedGui shardsGUI;
    private final MobcoinManager shardsManager = MobcoinPlugin.getInstance().getShardsManager();

    public MainMobcoinGUI(Player player) {
        MobcoinsUser shardsUser = shardsManager.getShardsUser(player.getUniqueId());
        this.shardsGUI = new PaginatedGui(6, 54, "Mobcoins Menu");
        this.shardsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        this.shardsGUI.getFiller().fillBorder(new GuiItem(createFillerItem()));
        this.shardsGUI.setItem(6, 5, new GuiItem(createUserInfoItem(shardsUser)));
        this.shardsGUI.setItem(3, 3, new GuiItem(createShopItem(player.getUniqueId()), event -> {
            MobcoinShopGUI shardsShopGUI = new MobcoinShopGUI(player);
            shardsShopGUI.getShardsGUI().open(player);
        }));
        this.shardsGUI.setItem(4, 5, new GuiItem(createDailyRewardItem(shardsUser), event -> {
            if (shardsUser.canRedeemDailyreward()) {
                shardsUser.addShards(Integer.parseInt(Language.DAILY_SHARDS.getValue()));
                shardsUser.setLastDailyRewardClaimed(System.currentTimeMillis());
                player.sendMessage(CC.translate(Language.SHARDS_ADDED.getMessage().replace("%amount%", Language.DAILY_SHARDS.getValue())));
            } else {
                player.sendMessage(CC.translate(Language.REWARD_ON_COOLDOWN.getMessage().replace("%time%", TimeUtils.formatSeconds(getSecondsTillRewardAvailable(shardsUser)))));
            }
            this.shardsGUI.updateItem(4, 5, new GuiItem(createDailyRewardItem(shardsUser), e -> {
                if (shardsUser.canRedeemDailyreward()) {
                    shardsUser.addShards(Integer.parseInt(Language.DAILY_SHARDS.getValue()));
                    shardsUser.setLastDailyRewardClaimed(System.currentTimeMillis());
                    player.sendMessage(CC.translate(Language.SHARDS_ADDED.getMessage().replace("%amount%", Language.DAILY_SHARDS.getValue())));
                } else {
                    player.sendMessage(CC.translate(Language.REWARD_ON_COOLDOWN.getMessage().replace("%time%", TimeUtils.formatSeconds(getSecondsTillRewardAvailable(shardsUser)))));
                }

            }));
        }));
        this.shardsGUI.setItem(3, 7, new GuiItem(createRecentTransactions()));
    }

    public ItemStack createUserInfoItem(MobcoinsUser shardsUser) {
        return new ItemBuilder(SkullUtils.getSkullFromPlayer(Bukkit.getPlayer(shardsUser.getOfflinePlayer().getUniqueId())))
                .setName("&e&l" + shardsUser.getOfflinePlayer().getName() + "'s Mobcoins Info")
                .addLore("")
                .addLore("&6&lPlayer Info")
                .addLore("&e&l * &fBalance&7: &6⛃&e" + NumberUtils.formatMoney(shardsUser.getBalance()))
                .addLore("&e&l * &fSpent&7: &6⛃&e" + NumberUtils.formatMoney(shardsUser.getSpent()))
                .addLore("&e&l * &fTransactions&7: &e" + NumberUtils.formatMoney(shardsUser.getTransactions().size()))
                .addLore("")
                .addLore("&6&lServer Info")
                .addLore("&e&l * &fTotal Mobcoins&7: &6⛃&e" + NumberUtils.formatMoney(shardsManager.getTotalShards()))
                .addLore("&e&l * &fRichest Player&7: &d" + shardsManager.getRichestPlayer().getOfflinePlayer().getName() + " &8➥ &6⛃&e" + shardsManager.getRichestPlayer().getBalance())
                .colorize()
                .build();
    }

    public ItemStack createRecentTransactions() {
        ItemBuilder builder = new ItemBuilder(Material.PAPER, "&e&lRecent Transactions").addLore("&7This will show the 10 most recent transactions.").addLore("").colorize();
        for (MobcoinTransaction transaction : shardsManager.getRecentTransactionsList()) {
            builder.addLore("&e&l * &f" + Bukkit.getOfflinePlayer(transaction.getBuyer()).getName() + "&7 purchased " + (transaction.getItem().getItemMeta().getDisplayName() == null ? StringUtils.getProperCase(transaction.getItem().getType().name()) : transaction.getItem().getItemMeta().getDisplayName()) + "&7 for &6⛃&e" + NumberUtils.formatMoney(transaction.getPrice()) + "&7. &8(&f" + TimeUtils.formatSeconds(((System.currentTimeMillis() - transaction.getTime()) / 1000)) + " ago&8)");
        }

        return builder.colorize().build();
    }


    public ItemStack createPreviousItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&c<- Previous Page"), "&7Right-Click&7 to go to the previous page.").colorize().build();
    }

    public ItemStack createNextItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&aNext Page ->"), "&7Right-Click&7 to go to the next page.").colorize().build();

    }

    public ItemStack createFillerItem() {
        return new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData())).setName("&7").colorize().build();
    }

    public ItemStack createDailyRewardItem(MobcoinsUser shardsUser) {
        return new ItemBuilder(Material.NETHER_STAR, "&e&lDaily Reward")
                .addLore("&7Click here to redeem your daily mobcoins.")
                .addLore("")
                .addLore("&e&l * &fAvailable: &7" + TimeUtils.formatSeconds(getSecondsTillRewardAvailable(shardsUser)))
                .addLore("&e&l * &fReward: &6⛃&e" + Language.DAILY_SHARDS.getValue())
                .colorize()
                .build();
    }

    public long getSecondsTillRewardAvailable(MobcoinsUser shardsUser) {
        long claimedTimeStamp = shardsUser.getLastDailyRewardClaimed();
        long currentTimeStamp = System.currentTimeMillis();
        long difference = currentTimeStamp - claimedTimeStamp;
        long cooldown = 86400000;
        return TimeUnit.MILLISECONDS.toSeconds(cooldown-difference);
    }

    public ItemStack createShopItem(UUID uuid) {
        MobcoinManager shardsManager = MobcoinPlugin.getInstance().getShardsManager();
        MobcoinsUser shardsUser = shardsManager.getShardsUser(uuid);
        return new ItemBuilder(Material.CHEST, "&e&lMobcoins ItemShop", Arrays.asList("&7Click here to open the mobcoins", "&7itemshop and purchase goodies", "&7using the mobcoins you earned.", "", "&ePurchaseable Items: &f" + MobcoinPlugin.getInstance().getShardsShopManager().getPurchaseAbleItemsAmount(shardsUser.getBalance()))
        ).colorize().build();
    }
}
