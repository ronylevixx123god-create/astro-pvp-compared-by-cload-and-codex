/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.utils.PlayerUtil
 *  com.decimatemc.decimatecommons.util.CommonsUtil
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.javatuples.Pair
 */
package net.astropvp.lootboxes.lootbox;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.RewardItem;
import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class AstroLootbox
        implements Lootbox {
    private static final String LOOTBOX_ANNOUNCEMENT = "lootbox-announcement";
    private static final String LOOTBOX_LINE_PREFIX = "lootbox-line-prefix";
    private final String name;
    private final String displayName;
    private final List<RewardItem> randomRewards;
    private final List<RewardItem> bonusRewards;
    private final int rewardCount;
    private final ItemStack displayItem;
    private final boolean glowing;
    private final boolean announce;
    private final boolean isBundle;
    private final List<String> specialLore;
    private transient List<Pair<RewardItem, Double>> rarityList = Lists.newArrayList();
    private transient double rarityTotal = -1.0;

    public AstroLootbox(String name, String displayName, List<RewardItem> randomRewards, List<RewardItem> bonusRewards, int rewardCount, ItemStack displayItem, boolean glowing, boolean announce, boolean isBundle, List<String> specialLore) {
        this.name = name;
        this.displayName = displayName;
        this.randomRewards = randomRewards;
        this.bonusRewards = bonusRewards;
        this.rewardCount = rewardCount;
        this.displayItem = displayItem;
        this.glowing = glowing;
        this.announce = announce;
        this.isBundle = isBundle;
        this.specialLore = specialLore;
    }

    @Override
    public void giveItemTo(Player player, LootboxManager lootboxManager) {
        PlayerUtil.giveOrDropProtectedItem((Player) player, (ItemStack) this.getCompleteItem(lootboxManager), (int) 30);
    }

    @Override
    public ItemStack getCompleteItem(LootboxManager lootboxManager) {
        NBTWrapper wrapper = new NBTWrapper(this.displayItem.clone());
        wrapper.setNBTBase(lootboxManager.getNBTCompound(), (NBTBase) wrapper.getCompoundOrCreate(lootboxManager.getNBTCompound()).setBoolean(lootboxManager.getNBTIsLootbox(), Boolean.valueOf(true)).setString(lootboxManager.getNBTLootboxType(), this.getName()).get());
        if (this.glowing) {
            wrapper.setGlowing();
        }
        return wrapper.build();
    }

    @Override
    public void executeLootbox(Player player, JavaPlugin plugin, LootboxManager lootboxManager) {
        ArrayList<RewardItem> receivedItems = Lists.newArrayList();
        if (this.isBundle) {
            receivedItems.addAll(this.randomRewards);
            receivedItems.forEach(rewardItem -> rewardItem.execute(player, lootboxManager, false));
        } else {
            IntStream.range(0, this.rewardCount).forEach(i -> receivedItems.add(this.getRandomReward()));
            receivedItems.forEach(rewardItem -> rewardItem.execute(player, lootboxManager, false));
            this.bonusRewards.forEach(bonusItem -> bonusItem.execute(player, lootboxManager, false));
        }
        if (this.announce) {
            List announcement = plugin.getConfig().getStringList(LOOTBOX_ANNOUNCEMENT);
            String prefix = plugin.getConfig().getString(LOOTBOX_LINE_PREFIX);
            receivedItems.stream().map(rewardItem -> {
                if (rewardItem.getDisplayName() != null) {
                    return prefix + rewardItem.getDisplayName();
                }
                return prefix + WordUtils.capitalizeFully((String) rewardItem.getItem().getType().name().replace("_", " "));
            }).forEach(announcement::add);
            for (int i2 = 0; i2 < announcement.size(); ++i2) {
                String line = (String) announcement.get(i2);
                line = line.replace("%player%", player.getName());
                line = line.replace("%lootbox%", this.getDisplayName());
                announcement.set(i2, line);
            }
            Bukkit.getOnlinePlayers().forEach(announcementTarget -> CommonsUtil.sendMessage((CommandSender) announcementTarget, (List) announcement));
        }
        player.updateInventory();
    }

    @Override
    public RewardItem getRandomReward() {
        if (this.rarityTotal <= 0.0) {
            this.initializeRarities();
        }
        double rand = ThreadLocalRandom.current().nextDouble(this.rarityTotal);
        RewardItem rewardItem = (RewardItem) this.rarityList.stream().filter(pair -> rand >= (Double) pair.getValue1()).sorted((pair1, pair2) -> ((Double) pair2.getValue1()).compareTo((Double) pair1.getValue1())).findFirst().orElseThrow(IllegalStateException::new).getValue0();
        return rewardItem;
    }

    @Override
    public List<RewardItem> getRandomRewards(int amount) {
        ArrayList items = Lists.newArrayList();
        IntStream.range(0, amount).forEach(i -> items.add(this.getRandomReward()));
        return items;
    }

    private void initializeRarities() {
        this.rarityList = Lists.newArrayList();
        this.rarityTotal = -1.0;
        double sum = 0.0;
        for (RewardItem randomReward : this.randomRewards) {
            this.rarityList.add((Pair<RewardItem, Double>) Pair.with(randomReward, sum));
            sum += randomReward.getRarity().getChance();
        }
        this.rarityTotal = sum;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<RewardItem> getRandomRewards() {
        return this.randomRewards;
    }

    @Override
    public List<RewardItem> getBonusRewards() {
        return this.bonusRewards;
    }

    @Override
    public int getRewardCount() {
        return this.rewardCount;
    }

    @Override
    public int getTotalRewardCount() {
        return this.rewardCount + this.bonusRewards.size();
    }

    @Override
    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public boolean isGlowing() {
        return this.glowing;
    }

    @Override
    public boolean isAnnounce() {
        return this.announce;
    }

    @Override
    public boolean isBundle() {
        return this.isBundle;
    }

    @Override
    public List<String> getSpecialLore() {
        return this.specialLore;
    }

    public String toString() {
        return "AstroLootbox{name='" + this.name + '\'' + ", displayName='" + this.displayName + '\'' + ", randomRewards=" + this.randomRewards + ", bonusRewards=" + this.bonusRewards + ", rewardCount=" + this.rewardCount + ", displayItem=" + this.displayItem + ", glowing=" + this.glowing + ", announce=" + this.announce + ", isBundle=" + this.isBundle + ", specialLore=" + this.specialLore + ", rarityList=" + this.rarityList + ", rarityTotal=" + this.rarityTotal + '}';
    }
}

