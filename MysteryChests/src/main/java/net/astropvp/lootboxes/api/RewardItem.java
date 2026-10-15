/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.astropvp.lootboxes.api;

import net.astropvp.lootboxes.pojo.Rarity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface RewardItem {
    boolean isCommand();

    boolean isItem();

    boolean isCommandAndItem();

    ItemStack generateViewableStack();

    ItemStack generatePlayerViewableStack(LootboxManager var1);

    @Nullable
    ItemStack getItem();

    void execute(Player var1, LootboxManager var2, boolean var3);

    void setDisplayName(String var1);

    String getDisplayName();

    @Nullable
    ItemStack getReward();

    @Nullable
    String getCommand();

    UUID getUUID();

    Rarity getRarity();

    void setRarity(Rarity var1);
}

