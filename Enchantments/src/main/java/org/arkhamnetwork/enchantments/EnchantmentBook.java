package org.arkhamnetwork.enchantments;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.base.CaseFormat;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.service.ERomanNumeral;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.enchantments.util.EnchantmentDescriptions;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.ItemUtils;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentBook {
    private static List<Material> armorMaterials;

    static {
        EnchantmentBook.armorMaterials = new ArrayList<Material>(Arrays.asList(GeneralUtil.armor));
    }

    @Deprecated
    public static ItemStack getOldEnchantmentBook(final CustomEnchantment ench, final int level) {
        final ItemStack is = ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.AQUA + "Enchantment (" + ChatColor.LIGHT_PURPLE + ench.name() + " " + getRoman(level) + ChatColor.AQUA + ")", ChatColor.LIGHT_PURPLE.toString() + (new Random().nextInt(100) + 1) + "%" + ChatColor.AQUA + " Success Rate", ChatColor.AQUA.toString() + "On fail, " + ChatColor.LIGHT_PURPLE + (new Random().nextInt(100) + 1) + "%" + ChatColor.AQUA + " destruction chance.", ChatColor.AQUA.toString() + "Level " + ChatColor.LIGHT_PURPLE + level, ChatColor.AQUA.toString() + getEnchantmentType(ench.getNaturalMaterials()) + " Enchantment", ChatColor.GRAY.toString() + "Place book on item to enchant.", ChatColor.GRAY.toString() + "Right Click in hand for help.");
        return is;
    }

    public static ItemStack getEnchantmentBook(final CustomEnchantment ench, final int level) {
        final ItemStack is = ItemUtils.buildItem(Material.BOOK, (short) 0, TieredEnchantments.getTierColor(ench) + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + ench.name() + " " + getRoman(level), ChatColor.GREEN.toString() + (new Random().nextInt(100) + 1) + "% Success Rate", ChatColor.RED.toString() + (new Random().nextInt(100) + 1) + "% Destroy Rate");
        final List<String> lore = is.getItemMeta().getLore();
        final String desc = EnchantmentDescriptions.getDescription(ench);
        for (final String s : formatLore(desc)) {
            lore.add(s);
        }
        lore.add(ChatColor.GRAY.toString() + getEnchantmentType(ench.getNaturalMaterials()) + " Enchantment");
        lore.add(CC.translate("&6Max Level: &f") + ench.getMaxLevel());
        lore.add(ChatColor.GRAY.toString() + "Drag n' Drop onto item to enchant.");
        final ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getGodlyTransmog() {
        ItemBuilder builder = new ItemBuilder(Material.PAPER, CC.translate("&6&lGodly Transmog Scroll"), CC.translate("&7Apply this equipment to &6&ncustomize"), CC.translate("&7the order of &dcustom &benchantments"), CC.translate("&7displayed on the item."), "", CC.translate("&6&oPlace scroll on item to apply."));
        return (new NBTWrapper(builder.build())).setString("cosmicType", "godlyTransmog").setNBTBase("cosmicData", (new NBTTagCompoundBuilder()).setInt("npm", 5).get()).setGlowing().build();
    }

    public static boolean isGodlyTransmog(ItemStack item) {
        if (item != null && item.getType() == Material.PAPER) {
            NBTWrapper wrapper = new NBTWrapper(item);
            return wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("godlyTransmog");
        } else {
            return false;
        }
    }

    public static ItemStack getEnchantmentBook(final CustomEnchantment ench, final int level, final int success, final int failure) {
        final ItemStack is = ItemUtils.buildItem(Material.BOOK, (short) 0, TieredEnchantments.getTierColor(ench) + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + ench.name() + " " + getRoman(level), ChatColor.GREEN.toString() + success + "% Success Rate", ChatColor.RED.toString() + failure + "% Destroy Rate");
        final List<String> lore = is.getItemMeta().getLore();
        final String desc = EnchantmentDescriptions.getDescription(ench);
        for (final String s : formatLore(desc)) {
            lore.add(s);
        }
        lore.add(ChatColor.GRAY.toString() + getEnchantmentType(ench.getNaturalMaterials()) + " Enchantment");
        lore.add(CC.translate("&6Max Level: &f") + ench.getMaxLevel());
        lore.add(ChatColor.GRAY.toString() + "Drag n' Drop onto item to enchant.");
        final ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getWhiteScroll() {
        final ItemStack is = ItemUtils.buildItem(Material.EMPTY_MAP, (short) 0, ChatColor.YELLOW + "White Scroll", ChatColor.GRAY.toString() + "Prevents an item from being destroyed", ChatColor.GRAY.toString() + "due to a failed Enchantment Book.", ChatColor.YELLOW.toString() + "Place scroll on item to apply.");
        return is;
    }

    public static ItemStack getMasteryBlackscroll(int successRateMin, int successRateMax) {
        int chosenMax = successRateMin + ThreadLocalRandom.current().nextInt(successRateMax - successRateMin + 1);
        ItemStack is = ItemUtils.buildItem(Material.INK_SACK, (short)0, CC.DarkRedB + "Mastery Black Scroll", ChatColor.GRAY.toString() + "Removes a random enchantment", ChatColor.GRAY.toString() + "from an item and converts", ChatColor.GRAY.toString() + "it into a " + ChatColor.DARK_RED + successRateMin + "%-" + chosenMax + "%" + ChatColor.GRAY + " success book.", CC.DarkRedB + "(!)" + CC.DarkRed + " Chance to extract Mastery Enchantments");
        return (new NBTWrapper(is)).setString("cosmicType", "masteryBlackscroll").setNBTBase("conaxData", (new NBTTagCompoundBuilder()).setInt("min", successRateMin).setInt("max", chosenMax).get()).setGlowing().build();
    }


    public static boolean isMasteryBlackscroll(ItemStack item) {
        if (item != null && item.getType() == Material.INK_SACK) {
            NBTWrapper wrapper = new NBTWrapper(item);
            return wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("masteryBlackscroll");
        } else {
            return false;
        }
    }

    public static ItemStack getBlackScroll() {
        return getBlackScroll(new Random().nextInt(50) + 51);
    }

    public static ItemStack getBlackScroll(final int successRate) {
        final ItemStack is = ItemUtils.buildItem(Material.INK_SACK, (short) 0, ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Black Scroll", ChatColor.GRAY.toString() + "Removes a random enchantment", ChatColor.GRAY.toString() + "from an item and converts", ChatColor.GRAY.toString() + "it into a " + ChatColor.WHITE + successRate + "%" + ChatColor.GRAY + " success book.", ChatColor.WHITE.toString() + "Place scroll on item to extract.");
        return is;
    }

    public static ItemStack getHeroicBlackscroll(final int successRateMin, final int successRateMax, final boolean applyIID) {
        final int chosenMax = successRateMin + ThreadLocalRandom.current().nextInt(successRateMax - successRateMin + 1);
        final ItemStack is = ItemUtils.buildItem(Material.INK_SACK, (short) 0, CC.LightPurpleB + "Heroic Black Scroll", ChatColor.GRAY.toString() + "Removes a random enchantment", ChatColor.GRAY.toString() + "from an item and converts", ChatColor.GRAY.toString() + "it into a " + ChatColor.LIGHT_PURPLE + successRateMin + "%-" + chosenMax + "%" + ChatColor.GRAY + " success book.", CC.LightPurpleB + "(!)" + CC.LightPurple + " Chance to extract Heroic Enchantments");
        return new NBTWrapper((applyIID && ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) ? ItemID.assignItemID(null, is) : is).setString("cosmicType", "heroicBlackscroll").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("min", Integer.valueOf(successRateMin)).setInt("max", Integer.valueOf(chosenMax)).get()).setGlowing().build();
    }

    public static ItemStack getHeroicBlackscroll(final boolean applyIID) {
        int min = 10;
        int max = 35;
        final Random ran = ThreadLocalRandom.current();
        if (ran.nextBoolean()) {
            max += 10;
        }
        if (ran.nextInt(10) < 4) {
            max += 10;
        }
        if (ran.nextInt(10) < 3) {
            max += 15;
        }
        if (ran.nextInt(10) < 3) {
            min -= ran.nextInt(3) + 1;
        }
        final int selectedMax = min + ran.nextInt(max - min + 1);
        final ItemStack is = ItemUtils.buildItem(Material.INK_SACK, (short) 0, CC.LightPurpleB + "Heroic Black Scroll", ChatColor.GRAY.toString() + "Removes a random enchantment", ChatColor.GRAY.toString() + "from an item and converts", ChatColor.GRAY.toString() + "it into a " + ChatColor.LIGHT_PURPLE + min + "%-" + selectedMax + "%" + ChatColor.GRAY + " success book.", CC.LightPurpleB + "(!)" + CC.LightPurple + " Chance to extract Heroic Enchantments");
        return new NBTWrapper((applyIID && ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) ? ItemID.assignItemID(null, is) : is).setString("cosmicType", "heroicBlackscroll").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("min", Integer.valueOf(min)).setInt("max", Integer.valueOf(selectedMax)).get()).setGlowing().build();
    }

    public static ItemStack getTransmogScroll() {
        final ItemStack is = ItemUtils.buildItem(Material.PAPER, (short) 0, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Transmog Scroll", ChatColor.GRAY.toString() + "Organizes enchants by " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString() + "rarity" + ChatColor.GRAY + " on item", ChatColor.GRAY.toString() + "and adds the " + ChatColor.LIGHT_PURPLE + "lore " + ChatColor.AQUA + "count" + ChatColor.GRAY + " to name.", "", ChatColor.YELLOW.toString() + ChatColor.ITALIC.toString() + "Place scroll on item to apply.");
        return is;
    }

    public static ItemStack getJunkDust() {
        final ItemStack is = ItemUtils.buildItem(Material.SULPHUR, (short) 0, ChatColor.WHITE + "Mystery Dust", ChatColor.GRAY + "The failed bi-product of", ChatColor.GRAY + "Magic and Primal dust.");
        return is;
    }

    public static ItemStack getSecretDust(final ItemRarity ir, final int bookSuccess) {
        final ChatColor cc = CosmicSpaceChestsAPI.getColor(ir);
        final int maxSuccess = Math.max(1, bookSuccess / 10);
        final ItemStack is = ItemUtils.buildItem(Material.FIREBALL, (short) 0, cc + ir.name + " Secret Dust " + ChatColor.GRAY + "(Right Click)", ChatColor.GREEN + "Success: +0-" + maxSuccess + "%", ChatColor.GRAY + "Contains: " + ChatColor.AQUA + "Magic, " + ChatColor.YELLOW + "Primal, " + ChatColor.GRAY + "or " + ChatColor.WHITE + "Mystery" + ChatColor.GRAY + " dust.", ChatColor.GRAY + "An unidentified satchel of dust.");
        return is;
    }

    public static ItemStack getMagicDust(final ItemRarity ir, final int successIncrease) {
        final ChatColor cc = CosmicSpaceChestsAPI.getColor(ir);
        final ItemStack is = ItemUtils.buildItem(Material.SUGAR, (short) 0, cc + ir.name + " Magic Dust", ChatColor.GREEN.toString() + "+" + successIncrease + "% Success", ChatColor.GRAY.toString() + "Apply to a " + cc + ir.name + ChatColor.GRAY + " Enchantment Book", ChatColor.GRAY.toString() + "to increase its success rate by " + cc + successIncrease + "%", "", ChatColor.GRAY.toString() + "Place dust on enchantment book.");
        return is;
    }

    public static ItemStack getPlatinumMagicDust(final ItemRarity ir, final int successIncrease) {
        final ChatColor cc = CosmicSpaceChestsAPI.getColor(ir);
        final ItemStack is = ItemUtils.buildItem(Material.GLOWSTONE_DUST, (short) 0, cc + ChatColor.BOLD.toString() + ir.name + " Primal Dust", ChatColor.GREEN + ChatColor.BOLD.toString() + "+" + successIncrease + "% SUCCESS", ChatColor.GRAY.toString() + "Apply to a " + cc + ChatColor.BOLD.toString() + ir.name + ChatColor.GRAY + " Enchantment Book", ChatColor.GRAY.toString() + "to increase its success rate by " + cc + ChatColor.BOLD.toString() + successIncrease + "%", "", ChatColor.GRAY.toString() + "Place dust on enchantment book.");
        return is;
    }

    public static boolean isHeroicBlackScroll(final ItemStack item) {
        final NBTWrapper wrapper = new NBTWrapper(item);
        return wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("heroicBlackscroll");
    }

    public static boolean isTransmogScroll(final ItemStack is) {
        return is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Transmog Scroll") && is.getItemMeta().hasLore() && is.getItemMeta().getLore().get(0).equals(ChatColor.GRAY.toString() + "Organizes enchants by " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString() + "rarity" + ChatColor.GRAY + " on item");
    }

    public static boolean isWhitescroll(final ItemStack is) {
        return is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "White Scroll") && is.getItemMeta().hasLore() && is.getItemMeta().getLore().get(0).equals(ChatColor.GRAY.toString() + "Prevents an item from being destroyed");
    }

    public static boolean isBlackscroll(final ItemStack is) {
        return is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Black Scroll") && is.getItemMeta().hasLore() && is.getItemMeta().getLore().get(0).equals(ChatColor.GRAY.toString() + "Removes a random enchantment");
    }

    public static boolean isSecretDust(final ItemStack is) {
        return is != null && is.getType() == Material.FIREBALL && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().get(1).equals(ChatColor.GRAY + "Contains: " + ChatColor.AQUA + "Magic, " + ChatColor.YELLOW + "Primal, " + ChatColor.GRAY + "or " + ChatColor.WHITE + "Mystery" + ChatColor.GRAY + " dust.");
    }

    public static boolean isMagicDust(final ItemStack is) {
        return is != null && (is.getType() == Material.GLOWSTONE_DUST || is.getType() == Material.SUGAR) && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().size() >= 4 && is.getItemMeta().getLore().get(4).equals(ChatColor.GRAY.toString() + "Place dust on enchantment book.");
    }

    public static boolean isEquipment(final ItemStack is) {
        return EnchantmentBook.armorMaterials.contains(is.getType()) || is.getType() == Material.BOW || is.getType().name().toLowerCase().contains("_sword") || is.getType().name().toLowerCase().contains("_axe") || is.getType().name().toLowerCase().contains("_pickaxe") || is.getType().name().toLowerCase().contains("_hoe") || is.getType().name().toLowerCase().contains("_spade") || is.getType() == Material.FISHING_ROD;
    }

    public static boolean hasWhitescroll(final ItemStack is) {
        if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
            for (final String s : is.getItemMeta().getLore()) {
                if (s.equals(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "PROTECTED")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack applyDust(final ItemStack is_dust, final ItemStack is_book) {
        if (isMagicDust(is_dust) && isEnchantmentBook(is_book)) {
            final int successRateAdd = getMagicDustSuccessIncrease(is_dust);
            final int successRate = getEnchantmentBookSuccessRate(is_book);
            final ItemMeta im = is_book.getItemMeta();
            final List<String> lore = im.getLore();
            lore.set(0, ChatColor.GREEN.toString() + Math.min(100, successRate + successRateAdd) + "% Success Rate");
            im.setLore(lore);
            is_book.setItemMeta(im);
        }
        return is_book;
    }

    public static ItemStack applyTransmogScroll(final ItemStack equipment) {
        if (!isEquipment(equipment) || equipment.getMaxStackSize() != 1) {
            return equipment;
        }
        final List<String> lore = equipment.getItemMeta().getLore();
        final LinkedList<String> newLore = new LinkedList<String>();
        final Map<CustomEnchantment, Integer> enchants = EnchantmentAPI.getEnchantments(equipment);
        for (int i = 8; i > 0; --i) {
            for (final Map.Entry<CustomEnchantment, Integer> data : enchants.entrySet()) {
                final CustomEnchantment ce = data.getKey();
                final int level = data.getValue();
                if (TieredEnchantments.getEnchantmentTier(ce.name()) == i) {
                    final String s = EnchantmentAPI.getChatColor(ce) + ce.name() + " " + ERomanNumeral.numeralOf(level);
                    newLore.add(s);
                    lore.remove(s);
                    lore.remove(ChatColor.GRAY + ce.name() + " " + ERomanNumeral.numeralOf(level));
                    lore.remove(ChatColor.WHITE + ce.name() + " " + ERomanNumeral.numeralOf(level));
                    lore.remove(ChatColor.GREEN + ce.name() + " " + ERomanNumeral.numeralOf(level));
                    lore.remove(ChatColor.AQUA + ce.name() + " " + ERomanNumeral.numeralOf(level));
                    lore.remove(ChatColor.YELLOW + ce.name() + " " + ERomanNumeral.numeralOf(level));
                    lore.remove(ChatColor.GOLD + ce.name() + " " + ERomanNumeral.numeralOf(level));
                    lore.remove(ChatColor.LIGHT_PURPLE + ce.name() + " " + ERomanNumeral.numeralOf(level));
                }
            }
        }
        for (final String extraLore : lore) {
            newLore.add(extraLore);
        }
        String itemName = equipment.getItemMeta().hasDisplayName() ? equipment.getItemMeta().getDisplayName() : (ChatColor.AQUA + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, equipment.getType().name()));
        if (itemName != null && itemName.contains(ChatColor.LIGHT_PURPLE.toString() + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString())) {
            itemName = itemName.substring(0, itemName.lastIndexOf(" "));
        }
        itemName = itemName + " " + ChatColor.LIGHT_PURPLE + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + enchants.size() + ChatColor.LIGHT_PURPLE + "]";
        final ItemMeta im = equipment.getItemMeta();
        im.setLore(newLore);
        im.setDisplayName(itemName);
        equipment.setItemMeta(im);
        return equipment;
    }

    public static void applyWhitescroll(final ItemStack is) {
        final ItemMeta im = is.getItemMeta();
        final List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
        lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "PROTECTED");
        im.setLore(lore);
        is.setItemMeta(im);
    }

    public static void removeWhitescroll(final ItemStack is) {
        final ItemMeta im = is.getItemMeta();
        final List<String> lore = im.getLore();
        lore.remove(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "PROTECTED");
        im.setLore(lore);
        is.setItemMeta(im);
    }

    public static int getBlackScrollBookSuccessRate(final ItemStack scroll) {
        return Integer.parseInt(scroll.getItemMeta().getLore().get(2).split(ChatColor.WHITE.toString())[1].split("%")[0]);
    }

    public static int getEnchantmentBookSuccessRate(final ItemStack is) {
        try {
            return Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getLore().get(0).split(" ")[0].replace("%", "")));
        } catch (Exception err) {
            return 0;
        }
    }

    public static int getEnchantmentBookDestructionRate(final ItemStack is) {
        try {
            return Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getLore().get(1).split(" ")[0].replace("%", "")));
        } catch (Exception err) {
            return 100;
        }
    }

    public static int getMagicDustSuccessIncrease(final ItemStack is) {
        try {
            return Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getLore().get(0).split(" ")[0].replace("%", "").replace("+", "")));
        } catch (Exception err) {
            return 0;
        }
    }

    public static int getSecretDustMaxSuccessIncrease(final ItemStack is) {
        try {
            return Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getLore().get(0).split("-")[1].replace("%", "").replace("+", "")));
        } catch (Exception err) {
            return 0;
        }
    }

    @Deprecated
    public static int getOldEnchantmentBookSuccessRate(final ItemStack is) {
        try {
            return Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getLore().get(0).split(" ")[0].replace("%", "")));
        } catch (Exception err) {
            return 100;
        }
    }

    @Deprecated
    public static int getOldEnchantmentBookDestructionRate(final ItemStack is) {
        try {
            return Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getLore().get(1).split(" ")[2].replace("%", "")));
        } catch (Exception err) {
            return 0;
        }
    }

    private static String getEnchantmentType(final Material[] nm) {
        if (nm == GeneralUtil.armor) {
            return "Armor";
        }
        if (nm == GeneralUtil.boots) {
            return "Boots";
        }
        if (nm == GeneralUtil.chestplates) {
            return "Chestplate";
        }
        if (nm == GeneralUtil.helmets) {
            return "Helmet";
        }
        if (nm == GeneralUtil.pickaxe) {
            return "Pickaxe";
        }
        if (nm == GeneralUtil.weapons) {
            return "Weapon";
        }
        if (nm == GeneralUtil.axe) {
            return "Axe";
        }
        if (nm == GeneralUtil.swords) {
            return "Sword";
        }
        if (nm[0] == Material.BOW) {
            return "Bow";
        }
        final Material m = nm[0];
        if (m.name().contains("_")) {
            return toCamelCase(m.name().split("_")[1]);
        }
        return toCamelCase(m.name());
    }

    private static String toCamelCase(final String s) {
        final String[] parts = s.split(" ");
        String camelCaseString = "";
        for (final String part : parts) {
            if (part != null && part.trim().length() > 0) {
                camelCaseString += toProperCase(part);
            } else {
                camelCaseString = camelCaseString + part + " ";
            }
        }
        return camelCaseString.contains(" ") ? camelCaseString.substring(0, camelCaseString.length() - 1) : camelCaseString;
    }

    private static String toProperCase(final String s) {
        String temp = s.trim();
        String spaces = "";
        if (temp.length() != s.length()) {
            final int startCharIndex = s.charAt(temp.indexOf(0));
            spaces = s.substring(0, startCharIndex);
        }
        temp = temp.substring(0, 1).toUpperCase() + spaces + temp.substring(1).toLowerCase() + " ";
        return temp;
    }

    @Deprecated
    public static CustomEnchantment getOldEnchantment(final ItemStack ench_book) {
        if (!isOldEnchantmentBook(ench_book)) {
            return null;
        }
        final String displayName = ChatColor.stripColor(ench_book.getItemMeta().getDisplayName());
        String enchName = displayName.contains("(") ? displayName.split("\\(")[1] : displayName.split("\\[")[1];
        enchName = enchName.substring(0, enchName.lastIndexOf(" "));
        final CustomEnchantment ce = EnchantmentAPI.getEnchantment(enchName);
        return ce;
    }

    public static CustomEnchantment getEnchantment(final ItemStack ench_book) {
        if (!isEnchantmentBook(ench_book)) {
            return null;
        }
        final String displayName = ChatColor.stripColor(ench_book.getItemMeta().getDisplayName());
        String enchName = displayName.replace("Book of ", "");
        enchName = enchName.substring(0, enchName.lastIndexOf(" "));
        final CustomEnchantment ce = EnchantmentAPI.getEnchantment(enchName);
        return ce;
    }

    @Deprecated
    public static int getOldEnchantmentLevel(final ItemStack ench_book) {
        if (!isOldEnchantmentBook(ench_book)) {
            return 0;
        }
        final int level = Integer.parseInt(ChatColor.stripColor(ench_book.getItemMeta().getLore().get(2).split(" ")[1]));
        return level;
    }

    public static int getEnchantmentLevel(final ItemStack ench_book) {
        if (!isEnchantmentBook(ench_book)) {
            return 0;
        }
        final int level = decode(ChatColor.stripColor(ench_book.getItemMeta().getDisplayName().substring(ench_book.getItemMeta().getDisplayName().lastIndexOf(" "))));
        return level;
    }

    public static boolean applyEnchantmentBook(final Player pl, final ItemStack ench_book, final ItemStack item) {
        final CustomEnchantment ce = getEnchantment(ench_book);
        final int level = getEnchantmentLevel(ench_book);
        int success_rate = getEnchantmentBookSuccessRate(ench_book);
        int success_rate_mod = 0;


        int destroy_rate = getEnchantmentBookDestructionRate(ench_book);

        if (pl.hasPermission("cosmicpvp.heroic")) {
            success_rate_mod += 5;
        }
        if (pl.hasMetadata("enchanterPet_Effect")) {
            final int petLevel = pl.getMetadata("enchanterPet_Effect").get(0).asInt();
            success_rate_mod += petLevel;
            pl.removeMetadata("enchanterPet_Effect", Bukkit.getPluginManager().getPlugin("CosmicSkyInventoryPets"));
        }
        if (ce == null || level == 0) {
            return false;
        }
        if (!ce.canEnchantOnto(item)) {
            return false;
        }
        pl.sendMessage(ChatColor.GRAY + "Applying enchantment book... " + CC.Yellow + success_rate + CC.Gold + "(+" + success_rate_mod + ")" + CC.Yellow + "% success chance...");
        success_rate += success_rate_mod;
        if (new Random().nextInt(100) <= success_rate) {
            ce.addToItem(item, level);
            return true;
        }
        if (pl.hasPermission("cosmicpvp.heroic") && Math.random() <= 0.1) {
            pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.GREEN + " Your item would have been destroyed, but because of your " + ChatColor.UNDERLINE + "Heroic Rank" + ChatColor.GRAY + " the book's destructive effects did not apply!");
            return false;
        }
        if (hasWhitescroll(item)) {
            if (pl.hasPermission("cosmicpvp.heroic") && Math.random() <= 0.15) {
                pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.GREEN + " Your white scroll would have been consumed (since your item was going to be destroyed), but because of your " + ChatColor.UNDERLINE + "Heroic Rank" + ChatColor.GRAY + " your white scroll is still active!");
                return false;
            }
            final CustomCosmicPlayerEvent event = new CustomCosmicPlayerEvent(pl, "WHITESCROLL_BREAK", item);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
            removeWhitescroll(item);
            pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.GREEN + " Your item would have been destroyed, but your white scroll protected it!");
        } else if (new Random().nextInt(100) < destroy_rate) {
            pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " Your item was destroyed by the enchanting book!");
            pl.sendMessage(ChatColor.GRAY + "If you had used a White Scroll, you could have saved your item! Get one today at buy.astropvp.net!");
            item.setDurability((short) 69);
            pl.getOpenInventory().getTopInventory().remove(item);
            pl.getOpenInventory().getBottomInventory().remove(item);
        }
        return false;
    }

    public static int getCustomEnchantmentCount(final ItemStack is) {
        return EnchantmentAPI.getEnchantments(is).size();
    }

    public static int getMaxUsableEnchantAllowed(final Player pl) {
        return 69;
    }

    public static int getMaxEnchantmentCountAllowed(final Player pl) {
        if (pl.hasPermission("enchantments.max.20")) {
            return 8;
        }
        if (pl.hasPermission("enchantments.max.12")) {
            return 7;
        }
        if (pl.hasPermission("enchantments.max.9")) {
            return 6;
        }
        if (pl.hasPermission("enchantments.max.6")) {
            return 5;
        }
        if (pl.hasPermission("enchantments.max.3")) {
            return 4;
        }
        return 3;
    }

    public static boolean isEnchantmentBook(final ItemStack is) {
        if (is != null && is.getType() == Material.BOOK && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore()) {
            try {
                if (!is.getItemMeta().getDisplayName().contains(ChatColor.BOLD.toString()) || !is.getItemMeta().getDisplayName().contains(ChatColor.UNDERLINE.toString())) {
                    return false;
                }
                final String displayName = ChatColor.stripColor(is.getItemMeta().getDisplayName());
                String enchName = displayName.replace("Book of ", "");
                enchName = enchName.substring(0, enchName.lastIndexOf(" "));
                if (EnchantmentAPI.isRegistered(enchName)) {
                    return true;
                }
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Deprecated
    public static boolean isOldEnchantmentBook(final ItemStack is) {
        if (is != null && is.getType() == Material.BOOK && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(ChatColor.AQUA.toString()) && is.getItemMeta().hasLore()) {
            try {
                final String displayName = ChatColor.stripColor(is.getItemMeta().getDisplayName());
                if (!displayName.contains("(") && !displayName.contains("[")) {
                    return false;
                }
                String enchName = displayName.contains("(") ? displayName.split("\\(")[1] : displayName.split("\\[")[1];
                enchName = enchName.substring(0, enchName.lastIndexOf(" "));
                if (EnchantmentAPI.isRegistered(enchName)) {
                    return true;
                }
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static int decodeSingle(final char letter) {
        switch (letter) {
            case 'M': {
                return 1000;
            }
            case 'D': {
                return 500;
            }
            case 'C': {
                return 100;
            }
            case 'L': {
                return 50;
            }
            case 'X': {
                return 10;
            }
            case 'V': {
                return 5;
            }
            case 'I': {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }

    public static int decode(final String roman) {
        int result = 0;
        final String uRoman = roman.toUpperCase();
        for (int i = 0; i < uRoman.length() - 1; ++i) {
            if (decodeSingle(uRoman.charAt(i)) < decodeSingle(uRoman.charAt(i + 1))) {
                result -= decodeSingle(uRoman.charAt(i));
            } else {
                result += decodeSingle(uRoman.charAt(i));
            }
        }
        result += decodeSingle(uRoman.charAt(uRoman.length() - 1));
        return result;
    }

    private static String getRoman(int number) {
        final String[] riman = {"M", "XM", "CM", "D", "XD", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        final int[] arab = {1000, 990, 900, 500, 490, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final StringBuilder result = new StringBuilder();
        for (int i = 0; number > 0 || arab.length == i - 1; ++i) {
            while (number - arab[i] >= 0) {
                number -= arab[i];
                result.append(riman[i]);
            }
        }
        return result.toString();
    }

    private static List<String> formatLore(final String s) {
        final List<String> lore_l = new ArrayList<String>();
        if (s == null) {
            return lore_l;
        }
        if (s.length() <= 38) {
            lore_l.add(ChatColor.YELLOW + s);
            return lore_l;
        }
        if (s.contains(" ")) {
            String line = ChatColor.YELLOW.toString();
            for (final String word : s.split(" ")) {
                if (line.length() - 1 + word.length() <= 38) {
                    line = line + word + " ";
                } else {
                    if (line.endsWith(" ")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    lore_l.add(line);
                    line = ChatColor.YELLOW + word + " ";
                }
            }
            if (ChatColor.stripColor(line).length() > 0) {
                lore_l.add(line);
            }
            return lore_l;
        }
        lore_l.add(s);
        return lore_l;
    }
}
