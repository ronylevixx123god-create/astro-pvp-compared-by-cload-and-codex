package net.crankedpvp.hypeboxes.struct;

import de.tr7zw.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.crankedpvp.hypeboxes.utils.Arrow;
import net.crankedpvp.hypeboxes.utils.ItemBuilder;
import net.crankedpvp.hypeboxes.utils.RandomUtils;
import net.crankedpvp.hypeboxes.utils.SkullUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter @AllArgsConstructor
public class HypeBox {

    private final String id;
    private final String displayName;
    private final List<String> lore;
    private final Material material;
    private final boolean glowing;
    private final boolean broadcastRewards;
    private final String skullValue;
    private final List<HypeBoxReward> rewards;

    public ItemStack createHypeBox(int amount) {
        ItemBuilder itemBuilder = this.skullValue.equals("???") ? new ItemBuilder(material) : new ItemBuilder(SkullUtils.getSkullFromSkin(this.skullValue));
        itemBuilder.setAmount(amount);
        itemBuilder.setName(this.displayName);
        itemBuilder.setLore(this.getLore()).colorize();
        if (glowing) itemBuilder.setGlowing();
        NBTItem nbtItem = new NBTItem(itemBuilder.build());
        nbtItem.setBoolean("broadcast", this.broadcastRewards);
        nbtItem.addCompound("cosmicData");
        nbtItem.getCompound("cosmicData").setString("customItem", "hypebox");
        nbtItem.getCompound("cosmicData").setString("hypeboxID", id);
        ItemStack itemStack = nbtItem.getItem();
        if(!this.skullValue.equals("???")) {
            return SkullUtils.getSkull(itemStack, this.skullValue);
        }
        return nbtItem.getItem();
    }

    public ItemStack buildHalf(String side) {
        String string = side.equals("left") ? "&b&lLeft Half (" + this.getDisplayName() + "&b&l)" : "&d&lRight Half (" + this.getDisplayName() + "&d&l)";
        String opposite = side.equals("left") ? "right" : "left";
        ItemBuilder itemBuilder = new ItemBuilder(Material.PRISMARINE_CRYSTALS, string, "&7Combine with the " + opposite + " half to",
                "&7create a full " + this.getDisplayName());
        NBTItem nbtItem = new NBTItem(itemBuilder.colorize().setGlowing().build());
        nbtItem.setString("halfLootbox", this.getId());
        nbtItem.setString("side", side);
        return nbtItem.getItem();

    }

    public ItemStack buildLeftOrRightHalf() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER, "&b&lLEFT &f&l&nOR&r &d&lRIGHT &f&lHalf " + Arrow.arrow() + " " + this.getDisplayName(),
                "&7Right-Click this lootbox to receive",
                "&7either the &bleft &7or &dright&7 half",
                "&7of the " + this.getDisplayName() + "&r &7lootbox",
                "",
                "&6&l(!) &7Obtained from &6buy.astropvp.net &7(/buy)",
                "",
                "&f&lRandom Loot &7(1 Item)",
                "&f&l* 1x &b&lLeft Half (" + this.getDisplayName() + "&b&l)",
                "&f&l* 1x &d&lRight Half (" + this.getDisplayName() + "&d&l)"
        );
        NBTItem nbtItem = new NBTItem(itemBuilder.colorize().setGlowing().build());
        nbtItem.setString("leftOrRightHalf", this.getId());
        return nbtItem.getItem();
    }

    public HypeBoxReward pickRandomRewardAsReward() {
        double totalChance = 0.0;
        for (HypeBoxReward reward : rewards) {
            totalChance += reward.getChance();
        }

        Random random = new Random();
        double randomValue = random.nextDouble() * totalChance;

        double cumulativeChance = 0.0;
        for (HypeBoxReward reward : rewards) {
            cumulativeChance += reward.getChance();
            if (randomValue <= cumulativeChance) {
                return reward;
            }
        }

        throw new IllegalStateException("No reward was selected. Check if the list of rewards and their chances are set up correctly.");
    }

    public ItemStack pickRandomReward() {
        final Map<ItemStack, Double> rewards = new HashMap<>();
        this.getRewards().stream()
                .filter(Objects::nonNull)
                .forEach(reward -> rewards.put(reward.getItemStack(), reward.getChance()));
        return RandomUtils.getRandomItem(rewards);
    }

    public boolean doesBroadcast() {
        return this.broadcastRewards;
    }
}
