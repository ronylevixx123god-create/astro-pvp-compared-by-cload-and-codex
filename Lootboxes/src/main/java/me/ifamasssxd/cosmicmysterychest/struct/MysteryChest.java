/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.LootTable
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.ifamasssxd.cosmicmysterychest.struct;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.struct.LootTable;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import lombok.Getter;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.utils.TimeUtil;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MysteryChest {
    private final String internalName;
    private ItemStack handDisplay;
    private final int itemBundleSize;
    private List<ItemStack> garunteedItems;
    private final List<CustomLoot> lootItems;
    private transient LootTable<CustomLoot> lootTable;
    public transient File file;
    private final boolean isStoreLootbox;
    private final boolean hideLore;

    public MysteryChest(String internalName, int itemBundleSize, ItemStack hand, boolean isStoreLootbox, boolean hideLore) {
        this.internalName = internalName;
        this.lootTable = new LootTable();
        this.lootItems = Lists.newArrayList();
        this.itemBundleSize = itemBundleSize;
        this.garunteedItems = Lists.newArrayList();
        this.handDisplay = hand;
        this.isStoreLootbox = isStoreLootbox;
        this.hideLore = hideLore;
    }

    public String getDisplayName() {
        if (this.handDisplay == null) {
            return this.internalName;
        }
        return ItemUtils.getDisplayName(this.handDisplay);
    }

    public File getFile() {
        if (this.file == null) {
            this.file = new File(CosmicMysteryChest.get().getDataFolder(), this.internalName + ".json");
        }
        return this.file;
    }

    public void reset() {
        this.lootItems.clear();
        if (this.garunteedItems != null) {
            this.garunteedItems.clear();
        }
        this.lootTable = new LootTable();
    }

    public void registerLoot(ItemStack item, double chance, boolean jackpot) {
        if (item.getAmount() <= 0) {
            item.setAmount(1);
        }
        CustomLoot loot = new CustomLoot(item, chance, jackpot);
        this.lootItems.add(loot);
        this.getLootTable().addItem(loot, chance);
    }

    public void registerGarunteed(ItemStack item) {
        if (item.getAmount() <= 0) {
            item.setAmount(1);
        }
        this.getGarunteedItems().add(item);
    }

    public List<ItemStack> getGarunteedItems() {
        if (this.garunteedItems == null) {
            this.garunteedItems = Lists.newLinkedList();
        }
        return this.garunteedItems;
    }

    public ItemStack createClaimableItem(boolean iid) {
        return this.createClaimableItem(iid, null);
    }

    public ItemStack createClaimableItem(boolean iid, Long expiration) {
        ItemStack hand = this.handDisplay.clone();
        ItemMeta im = hand.getItemMeta();
        List<String> lore = im.getLore();
        if (lore == null) {
            lore = Lists.newArrayList();
        }
        ArrayList<CustomLoot> lootItems = Lists.newArrayList(this.lootItems);
        List<CustomLoot> jackpotItems = lootItems.stream().filter(CustomLoot::isJackpotItem).collect(Collectors.toList());
        lootItems.removeAll(jackpotItems);
        if (!isHideLore()) {
            lore.add("");
            lore.add(CC.WhiteB + "Random Loot (" + CC.Gray + this.getItemBundleSize() + " item" + (this.getItemBundleSize() > 1 ? "s" : "") + CC.WhiteB + ")");
            for (CustomLoot item : lootItems) {
                lore.add(" " + CC.WhiteB + "* " + CC.White + item.getItem().getAmount() + "x " + ItemUtils.getDisplayName(item.getItem()));
            }
            if (jackpotItems.size() > 0) {
                lore.add("");
                lore.add(CC.WhiteB + "Jackpot Loot");
                for (CustomLoot item : jackpotItems) {
                    lore.add(" " + CC.WhiteB + "* " + CC.White + item.getItem().getAmount() + "x " + ItemUtils.getDisplayName(item.getItem()));
                }
            }
            if (this.garunteedItems != null && !this.garunteedItems.isEmpty()) {
                lore.add("");
                lore.add(CC.WhiteB + "Bonus Loot (" + CC.Gray + this.garunteedItems.size() + " item" + (this.garunteedItems.size() == 1 ? "" : "s") + CC.WhiteB + ")");
                for (ItemStack is : this.garunteedItems) {
                    lore.add(" " + CC.WhiteB + "* " + CC.White + is.getAmount() + "x " + ItemUtils.getDisplayName(is));
                }
            }
        }
        im.setLore(lore);
        hand.setItemMeta(im);
        NBTWrapper wrapper = new NBTWrapper(hand);
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("conaxData");
        builder.setString("mysteryChest", this.internalName);
        wrapper.setNBTBase("conaxData", builder.get());
        hand = wrapper.build();
        hand.setAmount(1);
        return hand;
    }

    public LootTable<CustomLoot> getLootTable() {
        if (this.lootTable == null) {
            this.lootTable = new LootTable();
            this.lootItems.forEach(is -> this.lootTable.addItem(is, is.getChance()));
        }
        return this.lootTable;
    }

    public void removeItem(ItemStack item) {
        getGarunteedItems().remove(item);
        getLootItems().removeIf(customLoot -> customLoot.getItem() == item);
        try {
            CosmicMysteryChest.get().getChestManager().saveMysteryChest(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CustomLoot getRandomPrize() {
        return this.getLootTable().getRandomLoot();
    }

    public String getInternalName() {
        return this.internalName;
    }

    public ItemStack getHandDisplay() {
        return this.handDisplay;
    }

    public int getItemBundleSize() {
        return this.itemBundleSize;
    }

    public List<CustomLoot> getLootItems() {
        return this.lootItems;
    }

    public void setHandDisplay(ItemStack handDisplay) {
        this.handDisplay = handDisplay;
    }
}

