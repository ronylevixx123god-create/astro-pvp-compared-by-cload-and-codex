package net.astropvp.core.cachers;

import com.google.common.collect.Lists;
import net.astropvp.core.enums.CapType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class LastManStandingCacher {
    private static List<LastManStandingCacher> cachers = Lists.newArrayList();

    private String player;

    private long whenPlayerCapped;

    private int time;

    private int xpEarned;

    private int moneyEarned;

    private CapType cap;

    public LastManStandingCacher(Player player, CapType cap) {
        this.player = player.getName();
        this.whenPlayerCapped = System.currentTimeMillis();
        this.time = 0;
        this.xpEarned = 0;
        this.moneyEarned = 0;
        this.cap = cap;
        cachers.add(this);
    }

    public static LastManStandingCacher getPlayer(Player player) {
        String name = player.getName();
        for (LastManStandingCacher cacher : cachers) {
            if (cacher.getPlayerName().equals(name))
                return cacher;
        }
        return null;
    }

    public static LastManStandingCacher getLongestPlayerAlive(CapType type) {
        if (cachers.size() == 0)
            return null;
        LastManStandingCacher cacher = null;
        for (LastManStandingCacher x : cachers) {
            if (x.getCapType() != type)
                continue;
            long time = x.getSecondsPlayerWasCapping();
            if (cacher != null && time <= cacher.getMilisecondsPlayerWasCapping())
                continue;
            cacher = x;
        }
        return cacher;
    }

    public static int getPlayersAround(CapType type) {
        int count = 0;
        for (LastManStandingCacher cacher : cachers) {
            if (cacher.getCapType() != type)
                continue;
            count++;
        }
        return count;
    }

    public static List<LastManStandingCacher> getCachers() {
        return cachers;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.player);
    }

    public int getLMSTime() {
        return this.time;
    }

    public CapType getCapType() {
        return this.cap;
    }

    public boolean canCollectCash() {
        int min = 6;
        for (LastManStandingCacher cacher : cachers) {
            if (cacher.getLMSTime() > getLMSTime())
                min--;
        }
        return (min >= 0);
    }

    public int getSecondsPlayerWasCapping() {
        return getMilisecondsPlayerWasCapping() / 1000;
    }

    public void addMoney(int amount) {
        this.moneyEarned += amount;
    }

    public void addExperience(int amount) {
        this.xpEarned += amount;
    }

    public int getMoneyEarned() {
        return this.moneyEarned;
    }

    public int getExperienceEarned() {
        return this.xpEarned;
    }

    public void add() {
        this.time++;
    }

    public int getMilisecondsPlayerWasCapping() {
        return (int) (System.currentTimeMillis() - this.whenPlayerCapped);
    }

    public String getPlayerName() {
        return this.player;
    }

    public void remove() {
        cachers.remove(this);
    }

    public String formatTimeCapped(int seconds) {
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds %= 60;
        StringBuilder builder = new StringBuilder();
        if (hours > 0)
            builder.append(String.valueOf(hours) + "h, ");
        if (minutes > 0)
            builder.append(String.valueOf(minutes) + "m, ");
        if (seconds > 0 || builder.toString().length() == 0)
            builder.append(String.valueOf(seconds) + "s, ");
        return builder.toString().substring(0, builder.toString().length() - 2);
    }
}
