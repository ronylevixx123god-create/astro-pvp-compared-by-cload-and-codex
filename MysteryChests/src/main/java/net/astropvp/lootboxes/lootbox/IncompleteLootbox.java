/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.decimatemc.decimatecommons.util.CommonsUtil
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.javatuples.Pair
 */
package net.astropvp.lootboxes.lootbox;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.RewardItem;
import net.astropvp.lootboxes.util.NBTUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IncompleteLootbox {
    private static final String LOOTBOX_LORE = "lootbox-addedlore";
    private static final String LOOTBOX_RANDOM_LORE = "lootbox-randomloot-lore";
    private static final String LOOTBOX_BONUS_LORE = "lootbox-bonusloot-lore";
    private static final String LOOTBOX_PREFIX = "lootbox-line-prefix";
    private static final String LOOTBOX_CUTOFF = "lootbox-cutoff";
    private static final String LOOTBOX_CUTOFF_LORE = "lootbox-cutoff-lore";
    private String name;
    private String displayName;
    private final Map<Integer, List<RewardItem>> rewardPages;
    private List<RewardItem> rewardItems;
    private List<RewardItem> bonusItems;
    private int rewardCount;
    private Material chosenMaterial;
    private ItemStack displayItem;
    private boolean announce;
    private boolean glowing;
    private boolean isBundle;
    private List<String> specialLore;
    private boolean isEditing = false;

    public IncompleteLootbox() {
        this.name = null;
        this.displayName = null;
        this.rewardPages = Maps.newHashMap();
        this.rewardItems = Lists.newArrayList();
        this.bonusItems = Lists.newArrayList();
        this.rewardCount = 3;
        this.chosenMaterial = Material.ENDER_CHEST;
        this.displayItem = null;
        this.announce = true;
        this.glowing = false;
        this.isBundle = false;
        this.specialLore = Lists.newArrayList();
    }

    public IncompleteLootbox(Lootbox lootbox) {
        this.name = lootbox.getName();
        this.displayName = lootbox.getDisplayName();
        this.rewardPages = Maps.newHashMap();
        this.rewardItems = new ArrayList<RewardItem>(lootbox.getRandomRewards());
        this.bonusItems = new ArrayList<RewardItem>(lootbox.getBonusRewards());
        this.rewardCount = lootbox.getRewardCount();
        this.chosenMaterial = lootbox.getDisplayItem().getType();
        this.displayItem = null;
        this.announce = lootbox.isAnnounce();
        this.glowing = lootbox.isGlowing();
        this.isEditing = true;
        this.isBundle = lootbox.isBundle();
        this.specialLore = lootbox.getSpecialLore();
        this.shufflePagesBack();
    }

    public Lootbox convert(JavaPlugin plugin, LootboxManager lootboxManager) {
        this.generateDisplayItem(plugin, lootboxManager);
        return new AstroLootbox(this.name, this.displayName, this.rewardItems, this.bonusItems, this.rewardCount, this.displayItem, this.glowing, this.announce, this.isBundle, this.specialLore);
    }

    private void generateDisplayItem(JavaPlugin plugin, LootboxManager lootboxManager) {
        ItemStack displayItem = new ItemStack(this.chosenMaterial);
        ItemMeta displayItemMeta = displayItem.getItemMeta();
        displayItemMeta.setDisplayName(CommonsUtil.color((String) this.displayName));
        String prefixString = plugin.getConfig().getString(LOOTBOX_PREFIX);
        if (!this.isBundle()) {
            ArrayList lore = Lists.newArrayList();
            lore.addAll(this.specialLore != null && !this.specialLore.isEmpty() ? this.specialLore : plugin.getConfig().getStringList(LOOTBOX_LORE));
            if (this.specialLore != null && !this.specialLore.isEmpty()) {
                this.replaceEmpties(lore);
            }
            if (!this.getName().equalsIgnoreCase("autogivealllootbox")) {
                List randomLore = plugin.getConfig().getStringList(LOOTBOX_RANDOM_LORE);
                this.implementPlaceholders(randomLore, "%amount%", String.valueOf(this.rewardCount));
                List bonusLore = plugin.getConfig().getStringList(LOOTBOX_BONUS_LORE);
                this.implementPlaceholders(bonusLore, "%amount%", String.valueOf(this.bonusItems.size()));
                lore.addAll(randomLore);
                int cutoff = plugin.getConfig().getInt(LOOTBOX_CUTOFF);
                this.rewardItems.stream().limit(cutoff).forEach(rewardItem -> {
                    if (rewardItem.isItem()) {
                        if (rewardItem.getDisplayName() != null) {
                            lore.add(prefixString + rewardItem.getItem().getAmount() + "x " + rewardItem.getDisplayName());
                        } else {
                            lore.add(prefixString + rewardItem.getItem().getAmount() + "x " + WordUtils.capitalizeFully(rewardItem.getItem().getType().name().replace("_", " ")));
                        }
                    } else {
                        String cmd = rewardItem.getCommand();
                        lore.add(prefixString + rewardItem.getDisplayName());
                    }
                });
                if (this.rewardItems.size() > cutoff) {
                    lore.addAll(plugin.getConfig().getStringList(LOOTBOX_CUTOFF_LORE));
                }
                if (!this.bonusItems.isEmpty()) {
                    lore.addAll(bonusLore);
                    this.bonusItems.forEach(bonusItem -> lore.add(prefixString + bonusItem.getDisplayName()));
                }
            }
            displayItemMeta.setLore(CommonsUtil.color((List) lore));
        } else {
            ArrayList lore = Lists.newArrayList();
            lore.addAll(this.specialLore != null && !this.specialLore.isEmpty() ? this.specialLore : plugin.getConfig().getStringList(LOOTBOX_LORE));
            if (this.specialLore != null && !this.specialLore.isEmpty()) {
                this.replaceEmpties(lore);
            }
            int cutoff = plugin.getConfig().getInt(LOOTBOX_CUTOFF);
            this.rewardItems.stream().limit(cutoff).forEach(rewardItem -> {
                if (rewardItem.isItem()) {
                    if (rewardItem.getDisplayName() != null) {
                        lore.add(prefixString + rewardItem.getReward().getAmount() + "x " + rewardItem.getDisplayName());
                    } else {
                        lore.add(prefixString + rewardItem.getItem().getAmount() + "x " + WordUtils.capitalizeFully(rewardItem.getItem().getType().name().replace("_", " ")));
                    }
                } else {
                    lore.add(prefixString + rewardItem.getDisplayName());
                }
            });
            if (this.rewardItems.size() > cutoff) {
                lore.addAll(plugin.getConfig().getStringList(LOOTBOX_CUTOFF_LORE));
            }
            displayItemMeta.setLore(CommonsUtil.color((List) lore));
        }
        displayItem.setItemMeta(displayItemMeta);
        displayItem = NBTUtil.setBoolean(displayItem, lootboxManager.getNBTIsLootbox(), true);
        this.displayItem = displayItem = NBTUtil.setString(displayItem, lootboxManager.getNBTLootboxType(), this.name);
    }

    private void replaceEmpties(List<String> list) {
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            String loreLine = iterator.next();
            if (!loreLine.equalsIgnoreCase("empty")) continue;
            iterator.set("");
        }
    }

    private void implementPlaceholders(List<String> list, String placeholder, String replacement) {
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            iterator.set(line.replace(placeholder, replacement));
        }
    }

    public void shufflePagesBack() {
        ArrayList allItems = Lists.newArrayList(this.getRewardItems());
        int splitFactor = 45;
        ArrayList splitPages = Lists.newArrayList();
        while (allItems.size() > 0) {
            ArrayList added = Lists.newArrayList((Iterable) allItems.stream().limit(splitFactor).collect(Collectors.toList()));
            allItems.removeAll(added);
            splitPages.add(added);
        }
        this.getRewardPages().clear();
        IntStream.range(0, splitPages.size()).forEach(i -> this.getRewardPages().put(i, (List<RewardItem>) splitPages.get(i)));
        this.getRewardItems().clear();
        this.getRewardPages().values().forEach(rewardItems -> this.getRewardItems().addAll(rewardItems));
    }

    public int addToFirstAvailablePage(RewardItem item) {
        List<Pair> items = Lists.newArrayList();
        for (Map.Entry<Integer, List<RewardItem>> entry : this.getRewardPages().entrySet()) {
            Integer pageInt = entry.getKey();
            List<RewardItem> rewards = entry.getValue();
            items.add(Pair.of(pageInt, rewards));
        }
        items = items.stream().sorted(Comparator.comparingInt(value -> (int) value.getValue())).collect(Collectors.toList());
        for (Pair pair : items) {
            List value = (List)pair.getValue();
            if (value.size() >= 45)
                continue;
            value.add(item);
            return (Integer)pair.getKey();
        }
        int pageNum = this.getHighestPage() + 1;
        this.getRewardPages().put(pageNum, Lists.newArrayList(item));
        return pageNum;
    }

    public int getHighestPage() {
        int highestPage = -1;
        for (int i : this.getRewardPages().keySet()) {
            if (i <= highestPage) continue;
            highestPage = i;
        }
        return highestPage;
    }

    public boolean isComplete() {
        return this.rewardItems.size() > 0 && this.name != null && this.displayName != null;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Material getChosenMaterial() {
        return this.chosenMaterial;
    }

    public boolean isAnnounce() {
        return this.announce;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setChosenMaterial(Material chosenMaterial) {
        this.chosenMaterial = chosenMaterial;
    }

    public void setAnnounce(boolean announce) {
        this.announce = announce;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public List<RewardItem> getRewardItems() {
        return this.rewardItems;
    }

    public Map<Integer, List<RewardItem>> getRewardPages() {
        return this.rewardPages;
    }

    public void setRewardItems(List<RewardItem> rewardItems) {
        this.rewardItems = rewardItems;
    }

    public List<RewardItem> getBonusItems() {
        return this.bonusItems;
    }

    public void setBonusItems(List<RewardItem> bonusItems) {
        this.bonusItems = bonusItems;
    }

    public int getRewardCount() {
        return this.rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public boolean isEditing() {
        return this.isEditing;
    }

    public boolean isBundle() {
        return this.isBundle;
    }

    public void setBundle(boolean bundle) {
        this.isBundle = bundle;
    }

    public List<String> getSpecialLore() {
        return this.specialLore;
    }

    public void setSpecialLore(List<String> specialLore) {
        this.specialLore = specialLore;
    }
}

