/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.astropvp.lootboxes.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface Lootbox {
    void giveItemTo(Player var1, LootboxManager var2);

    ItemStack getCompleteItem(LootboxManager var1);

    void executeLootbox(Player var1, JavaPlugin var2, LootboxManager var3);

    RewardItem getRandomReward();

    List<RewardItem> getRandomRewards(int var1);

    String getName();

    List<RewardItem> getRandomRewards();

    List<RewardItem> getBonusRewards();

    int getRewardCount();

    int getTotalRewardCount();

    ItemStack getDisplayItem();

    String getDisplayName();

    boolean isGlowing();

    boolean isAnnounce();

    boolean isBundle();

    List<String> getSpecialLore();
}

