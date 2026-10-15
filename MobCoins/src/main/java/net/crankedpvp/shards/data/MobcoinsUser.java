package net.crankedpvp.shards.data;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.storage.utils.KeyToSave;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.struct.MobcoinTransaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MobcoinsUser implements KeyToSave {
    private UUID uuid;
    private long balance;
    private long spent;
    private List<MobcoinTransaction> transactions;
    private long lastDailyRewardClaimed;

    public MobcoinsUser(UUID uuid) {
        this.uuid = uuid;
        this.balance = 0;
        this.spent = 0;
        this.transactions = new ArrayList<>();
        this.lastDailyRewardClaimed = 0;
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }

    public boolean canAfford(double price) {
        return balance >= price;
    }

    public boolean canRedeemDailyreward() {
        return System.currentTimeMillis() >= this.lastDailyRewardClaimed + 86400000;
    }

    public void addShards(double amount) {
        MobcoinPlugin.getInstance().getShardsManager().setTotalShards((long) (MobcoinPlugin.getInstance().getShardsManager().getTotalShards()+amount));
        balance+=amount;
    }

    public void removeShards(double amount) {
        MobcoinPlugin.getInstance().getShardsManager().setTotalShards((long) (MobcoinPlugin.getInstance().getShardsManager().getTotalShards()-amount));
        spent+=amount;
        balance-=amount;
    }

    public void resetShards() {
        balance = 0;
    }

    public void addTransation(MobcoinTransaction shardTransaction) {
        boolean canOffer = MobcoinPlugin.getInstance().getShardsManager().getRecentTransactions().offer(shardTransaction);
        if (!canOffer) {
            MobcoinPlugin.getInstance().getShardsManager().getRecentTransactions().poll();
            MobcoinPlugin.getInstance().getShardsManager().getRecentTransactions().offer(shardTransaction);
        }
        transactions.add(shardTransaction);
    }

    public OfflinePlayer getOfflinePlayer() {
       return Bukkit.getOfflinePlayer(uuid);
    }
}
