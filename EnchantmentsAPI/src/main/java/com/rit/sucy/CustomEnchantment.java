package com.rit.sucy;

import com.rit.sucy.service.ENameParser;
import com.rit.sucy.service.ERomanNumeral;
import com.rit.sucy.service.MaterialClass;
import com.rit.sucy.service.MaterialsParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomEnchantment
implements Comparable<CustomEnchantment> {
    public static final String DEFAULT_GROUP = "Default";
    protected List<String> suffixGroups = new ArrayList<String>();
    protected final String enchantName;
    protected String description;
    protected Material[] naturalItems;
    protected Map<MaterialClass, Integer> weight;
    protected boolean isEnabled;
    protected boolean isTableEnabled;
    protected String group;
    protected double interval;
    protected double base;
    protected int max;
    public int uniqueId;
    public boolean heroicEnchantment = false;
    public CustomEnchantment nonHeroicVersion = null;
    public CustomEnchantment heroicVersion = null;
    public int cachedTier = -1;
    public static int nextId = 0;

    public CustomEnchantment(String name) {
        this(name, null, new Material[0], DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, String[] naturalItems) {
        this(name, null, MaterialsParser.toMaterial(naturalItems), DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, Material[] naturalItems) {
        this(name, null, naturalItems, DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, String description) {
        this(name, description, new Material[0], DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, String[] naturalItems, int weight) {
        this(name, null, MaterialsParser.toMaterial(naturalItems), DEFAULT_GROUP, weight);
    }

    public CustomEnchantment(String name, Material[] naturalItems, int weight) {
        this(name, null, naturalItems, DEFAULT_GROUP, weight);
    }

    public CustomEnchantment(String name, Material[] naturalItems, String group) {
        this(name, null, naturalItems, group, 5);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems) {
        this(name, description, naturalItems, DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, String description, String group) {
        this(name, description, new Material[0], group, 5);
    }

    public CustomEnchantment(String name, String description, int weight) {
        this(name, description, new Material[0], DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems, String group) {
        this(name, description, naturalItems, group, 5);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems, int weight) {
        this(name, description, naturalItems, DEFAULT_GROUP, 5);
    }

    public CustomEnchantment(String name, String description, String group, int weight) {
        this(name, description, new Material[0], group, weight);
    }

    public CustomEnchantment(String name, Material[] naturalItems, String group, int weight) {
        this(name, null, naturalItems, group, weight);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems, String group, int weight) {
        Validate.notEmpty((String)name, (String)"Your Enchantment needs a name!");
        Validate.notNull((Object)naturalItems, (String)"Input an empty array instead of \"null\"!");
        Validate.isTrue((weight >= 0 ? 1 : 0) != 0, (String)"Weight can't be negative!");
        this.enchantName = name;
        this.description = description;
        this.naturalItems = naturalItems;
        this.isEnabled = true;
        this.group = group;
        this.max = 1;
        this.base = 1.0;
        this.interval = 10.0;
        this.isTableEnabled = true;
        this.uniqueId = nextId++;
        this.weight = new HashMap<MaterialClass, Integer>();
        this.weight.put(MaterialClass.DEFAULT, weight);
    }

    public String name() {
        return this.enchantName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setTableEnabled(boolean value) {
        this.isTableEnabled = value;
    }

    public boolean isTableEnabled() {
        return this.isTableEnabled;
    }

    public int getEnchantLevel(int expLevel) {
        for (int i = this.max; i >= 1; --i) {
            if (!((double)expLevel >= this.base + this.interval * (double)(i - 1))) continue;
            return i;
        }
        return 0;
    }

    public int getMaxLevel() {
        return this.max;
    }

    public void setMaxLevel(int value) {
        this.max = value;
    }

    public double getBase() {
        return this.base;
    }

    public void setBase(double value) {
        this.base = value;
    }

    public double getInterval() {
        return this.interval;
    }

    public void setInterval(double value) {
        this.interval = value;
    }

    public List<String> getSuffixGroups() {
        return this.suffixGroups;
    }

    public int getCostPerLevel(boolean withBook) {
        int divisor;
        int costIndex = this.weight.get((Object)MaterialClass.DEFAULT) * this.max;
        int n = divisor = withBook ? 2 : 1;
        return this.weight.get((Object)MaterialClass.DEFAULT) == 1 ? 8 / divisor : (costIndex < 10 ? 4 / divisor : (costIndex < 30 ? 2 / divisor : 1));
    }

    public int getTier() {
        if (this.cachedTier == -1) {
            this.cachedTier = TieredEnchantments.getEnchantmentTier((String)this.name());
        }
        return this.cachedTier;
    }

    public void setNaturalMaterials(Material[] materials) {
        this.naturalItems = materials;
    }

    public String[] getNaturalItems() {
        String[] natItems = new String[this.naturalItems.length];
        for (int i = 0; i < this.naturalItems.length; ++i) {
            natItems[i] = this.naturalItems[i].name();
        }
        return natItems;
    }

    public Material[] getNaturalMaterials() {
        return this.naturalItems;
    }

    public void setWeight(int weight) {
        this.weight.put(MaterialClass.DEFAULT, weight);
    }

    public int getWeight() {
        return this.weight.get((Object)MaterialClass.DEFAULT);
    }

    public int getWeight(MaterialClass material) {
        return this.weight.containsKey((Object)material) ? this.weight.get((Object)material).intValue() : this.weight.get((Object)MaterialClass.DEFAULT).intValue();
    }

    public boolean canEnchantOnto(ItemStack item) {
        if (this.naturalItems == null || item == null) {
            return false;
        }
        for (Material validItem : this.naturalItems) {
            if (item.getType() != validItem) continue;
            return true;
        }
        return item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return this.group;
    }

    public boolean conflictsWith(CustomEnchantment enchantment) {
        Validate.notNull((Object)enchantment);
        return !this.group.equals(DEFAULT_GROUP) && this.group.equalsIgnoreCase(enchantment.group);
    }

    public boolean conflictsWith(List<CustomEnchantment> enchantmentsToCheck) {
        Validate.notNull(enchantmentsToCheck);
        for (CustomEnchantment enchantment : enchantmentsToCheck) {
            if (!this.conflictsWith(enchantment)) continue;
            return true;
        }
        return false;
    }

    public boolean conflictsWith(CustomEnchantment ... enchantmentsToCheck) {
        Validate.notNull((Object)enchantmentsToCheck);
        for (CustomEnchantment enchantment : enchantmentsToCheck) {
            if (!this.conflictsWith(enchantment)) continue;
            return true;
        }
        return false;
    }

    public ItemStack addToItem(ItemStack item, int enchantLevel) {
        List<String> metaLore;
        Validate.notNull((Object)item);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getServer().getItemFactory().getItemMeta(item.getType());
        }
        List list = metaLore = meta.getLore() == null ? new ArrayList() : meta.getLore();
        if (!EnchantmentAPI.itemHasEnchantment(item, this.name()) && this.heroicEnchantment && this.nonHeroicVersion != null) {
            if (!EnchantmentAPI.itemHasEnchantment(item, this.nonHeroicVersion.name())) {
                return item;
            }
            int nonHeroicLevel = EnchantmentAPI.getEnchantments(item).get(this.nonHeroicVersion);
            if (nonHeroicLevel < this.nonHeroicVersion.getMaxLevel()) {
                return item;
            }
        }
        if (this.heroicVersion != null && EnchantmentAPI.itemHasEnchantment(item, this.heroicVersion.name())) {
            return item;
        }
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentAPI.getEnchantments(item).entrySet()) {
            if (ChatColor.stripColor((String)entry.getKey().name()).equals(ChatColor.stripColor((String)this.name()))) {
                if (entry.getValue() <= enchantLevel) {
                    metaLore.remove((Object)ChatColor.GRAY + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)ChatColor.WHITE + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)ChatColor.GREEN + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)ChatColor.AQUA + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)ChatColor.YELLOW + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)ChatColor.GOLD + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)ChatColor.LIGHT_PURPLE + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                    metaLore.remove((Object)EnchantmentAPI.getChatColor(this) + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
                } else {
                    return item;
                }
            }
            if (this.nonHeroicVersion == null || !ChatColor.stripColor((String)entry.getKey().name()).equals(ChatColor.stripColor((String)this.nonHeroicVersion.name()))) continue;
            metaLore.remove((Object)ChatColor.GRAY + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.WHITE + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.GREEN + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.AQUA + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.YELLOW + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.GOLD + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.LIGHT_PURPLE + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)EnchantmentAPI.getChatColor(this) + this.nonHeroicVersion.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
        }
        metaLore.add(0, (Object)EnchantmentAPI.getChatColor(this) + this.enchantName + " " + ERomanNumeral.numeralOf(enchantLevel));
        meta.setLore(metaLore);
        String name = ENameParser.getName(item);
        if (name != null) {
            meta.setDisplayName(name);
        }
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack removeFromItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (!meta.hasLore()) {
            return item;
        }
        List metaLore = meta.getLore();
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentAPI.getEnchantments(item).entrySet()) {
            if (!ChatColor.stripColor((String)entry.getKey().name()).equals(ChatColor.stripColor((String)this.name()))) continue;
            metaLore.remove((Object)EnchantmentAPI.getChatColor(this) + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.GRAY + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.WHITE + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.GREEN + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.AQUA + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.YELLOW + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.GOLD + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
            metaLore.remove((Object)ChatColor.LIGHT_PURPLE + this.name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
        }
        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CustomEnchantment) {
            return this.name().equalsIgnoreCase(((CustomEnchantment)obj).name());
        }
        return false;
    }

    @Override
    public int compareTo(CustomEnchantment customEnchantment) {
        return this.name().compareTo(customEnchantment.name());
    }

    double currentdamage = 0;

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) throws Exception {
    }

    public void applyDefenseEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageEvent event) {
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
    }

    public void applyMiscEffect(Player player, int enchantLevel, PlayerInteractEvent event) {
    }

    public void applyEquipEffect(Player player, int enchantLevel) {
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
    }

    public void applyEntityEffect(Player player, int enchantLevel, PlayerInteractEntityEvent event) {
    }

    public void applyProjectileEffect(LivingEntity user, int enchantLevel, ProjectileLaunchEvent event) {
    }

    public void applyEquipEffect(Player player, int level, ItemStack item) {
    }

    public void applyUnequipEffect(Player player, int level, ItemStack item) {
    }
}

