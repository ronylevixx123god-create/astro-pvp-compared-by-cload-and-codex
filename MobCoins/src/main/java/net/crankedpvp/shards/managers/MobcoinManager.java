package net.crankedpvp.shards.managers;

import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.data.MobcoinsUser;
import net.crankedpvp.shards.events.PlayerMobcoinBalanceUpdateEvent;
import net.crankedpvp.shards.language.Language;
import net.crankedpvp.shards.struct.MobcoinBalanceUpdateType;
import net.crankedpvp.shards.struct.MobcoinTransaction;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class MobcoinManager extends SaveLoadManager<MobcoinsUser> {
    private long totalShards;
    public Queue<MobcoinTransaction> recentTransactions;
    private long value;
    private MobcoinShopManager shardsShopManager;

    public MobcoinManager(MobcoinPlugin plugin) {
        super(plugin, StoragePlugin.getInstance().getGson(), "data/", MobcoinsUser.class);
        this.totalShards = 0;
        this.recentTransactions = new LinkedBlockingQueue<>(10);
        this.shardsShopManager = MobcoinPlugin.getInstance().getShardsShopManager();
    }

    public MobcoinsUser getShardsUser(UUID uuid) {
        if (!contains(uuid.toString())) {
            MobcoinsUser shardsUser = new MobcoinsUser(uuid);
            addToCache(shardsUser);
        }
        return getFromCache(uuid.toString());
    }

    public void onEnable() {
        loadAll();
        for (MobcoinsUser shardsUser : this.getCacheValues()) {
            totalShards += shardsUser.getBalance();
        }
    }

    public void purchaseItem(UUID uuid, ItemStack item, int price) {
        MobcoinsUser shardsUser = getShardsUser(uuid);
        if (shardsUser.canAfford(price)) {
            MobcoinTransaction shardTransaction = new MobcoinTransaction(shardsUser.getUuid(),price, System.currentTimeMillis(), item);
            shardsUser.addTransation(shardTransaction);
            shardsUser.removeShards(price);
            PlayerMobcoinBalanceUpdateEvent event = new PlayerMobcoinBalanceUpdateEvent(shardsUser.getBalance(), (long) (shardsUser.getBalance() - price), MobcoinBalanceUpdateType.DECREASE);
            Bukkit.getServer().getPluginManager().callEvent(event);
            String message = Language.SUCCESSFUL_ITEM_PURCHASE.getMessage().replace("%price%", NumberUtils.formatMoney(price));
            message = message.replace("%itemName%", item.getItemMeta().getDisplayName() == null ? StringUtils.getProperCase(item.getType().name()) : item.getItemMeta().getDisplayName());
            Bukkit.getPlayer(uuid).sendMessage(message);
            PlayerUtil.giveOrDropProtectedItem(Bukkit.getPlayer(uuid), item, 60);
        } else {
            double missing = price - shardsUser.getBalance();
            String message = Language.NOT_ENOUGH_SHARDS.getMessage().replace("%missing%", NumberUtils.formatMoney(missing));
            message = message.replace("%itemName%", item.getItemMeta().getDisplayName() == null ? StringUtils.getProperCase(item.getType().name()) : item.getItemMeta().getDisplayName());
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }


    public List<MobcoinTransaction> getRecentTransactionsList() {
        return ImmutableList.copyOf(recentTransactions).reverse();
    }

    public MobcoinsUser getRichestPlayer() {
        MobcoinsUser richest = getShardsUser(UUID.fromString("a86208fa-ecb2-4a3c-bed0-5136645e7d80"));
        long cash = 0;

        for (MobcoinsUser shardsUser : getCacheValues()) {

            long balance = shardsUser.getBalance();
            if (balance > cash) {
                cash = balance;
                richest = shardsUser;
            }
        }

        return richest;
    }


}
