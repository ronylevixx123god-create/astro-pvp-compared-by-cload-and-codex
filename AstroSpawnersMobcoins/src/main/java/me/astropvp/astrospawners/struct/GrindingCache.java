package me.astropvp.astrospawners.struct;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ExpUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import net.crankedpvp.shards.MobcoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GrindingCache {
    private int mobsKilled;
    private double moneyEarned;
    private int expEarned;
    private int coinsEarned;
    private UUID uuid;

    public GrindingCache(UUID uuid) {
        this.moneyEarned = 0;
        this.expEarned = 0;
        this.coinsEarned = 0;
        this.mobsKilled = 0;
        this.uuid = uuid;
    }

    public void claimGrindingRewards() {
        if (expEarned == 0 && moneyEarned == 0 && coinsEarned == 0) {
            return;
        }

        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) return;

        ExpUtils.giveXP(player, expEarned);
        EconomyUtils.depositBalance(player,moneyEarned);
        //MobcoinAPI.addMobcoins(player, coinsEarned);

        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("&6&lGrinding Rewards"));
        player.sendMessage(CC.translate("&8 - &7EXP: &d" + NumberUtils.formatMoney(expEarned)));
        player.sendMessage(CC.translate("&8 - &7Money: &2$&a" + NumberUtils.formatMoney(moneyEarned)));
        //player.sendMessage(CC.translate("&8 - &7Mobcoins: &e" + NumberUtils.formatMoney(coinsEarned)));
        player.sendMessage(CC.translate("&8 - &7Mobs Killed: &c" + NumberUtils.formatMoney(mobsKilled)));
        player.sendMessage(CC.translate("&7&o(( Every &f15s&7&o you will receive your grinding rewards from the last 15s. ))"));
        player.sendMessage(CC.translate(""));

        this.expEarned = 0;
        this.moneyEarned = 0;
        this.coinsEarned = 0;
        this.mobsKilled = 0;
    }

    public void addExp(int amount) {
        this.expEarned += amount;
    }

    public void addCoins(int amount) {
        this.coinsEarned += amount;
    }

    public void addMoney(double amount) {
        this.moneyEarned += amount;
    }

    public void addMobKilled(int amount) {
        this.mobsKilled += amount;
    }

}
