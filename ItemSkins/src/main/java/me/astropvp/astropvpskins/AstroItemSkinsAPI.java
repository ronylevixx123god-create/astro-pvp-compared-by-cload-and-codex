package me.astropvp.astropvpskins;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AstroItemSkinsAPI {
    public static List<SkinType> getItemSkinsOnPlayer(Player player) {
        List<SkinType> skins = new ArrayList<>();
        PlayerInventory inventory = player.getInventory();
        if (inventory.getHelmet() != null && inventory.getHelmet().getType() != Material.AIR && hasItemSkin(inventory.getHelmet()))
            skins.addAll(getItemSkinByItem(inventory.getHelmet()));
        if (inventory.getChestplate() != null && inventory.getChestplate().getType() != Material.AIR && hasItemSkin(inventory.getChestplate()))
            skins.addAll(getItemSkinByItem(inventory.getChestplate()));
        if (inventory.getLeggings() != null && inventory.getLeggings().getType() != Material.AIR && hasItemSkin(inventory.getLeggings()))
            skins.addAll(getItemSkinByItem(inventory.getLeggings()));
        if (inventory.getBoots() != null && inventory.getBoots().getType() != Material.AIR && hasItemSkin(inventory.getBoots()))
            skins.addAll(getItemSkinByItem(inventory.getBoots()));
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && hasItemSkin(player.getItemInHand()))
            skins.addAll(getItemSkinByItem(player.getItemInHand()));
        return skins;
    }


    public static boolean hasBackpack(final ItemStack item) {
        final NBTWrapper wrapper = new NBTWrapper(item);
        return wrapper.hasKey("cosmicData") && (!wrapper.hasKey("cosmicType") || !wrapper.getString("cosmicType").equals("item_skin")) && new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")).hasKey("backpackSkinType");
    }

    public static ItemStack applyBackpackSkin(final ItemStack item, SkinType skinType) {
        final ItemMeta meta = item.getItemMeta();
        final List<String> lore = (meta.getLore() == null) ? new ArrayList<String>() : meta.getLore();
        for (final String line : lore) {
            if (line.equals(CC.translate("&3&l+ BACKPACK [&8N/A&3&l]"))) {
                lore.set(lore.indexOf(line), CC.translate("&3&l+ BACKPACK [&b" + CC.strip(skinType.getDisplay()) + "&3&l]"));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack applyBackpackSocket(ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        final List<String> lore = meta.getLore();
        lore.add(CC.translate("&3&l+ BACKPACK [&8N/A&3&l]"));
        meta.setLore(lore);
        item.setItemMeta(meta);

        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder conaxData = wrapper.getCompoundOrCreate("conaxData");
        conaxData.setBoolean("hasBackpackSocket", true);
        wrapper.setNBTBase("conaxData", conaxData.get());

        return  wrapper.build();
    }

    public static boolean hasBackpackSocket(final ItemStack item) {
        final NBTWrapper wrapper = new NBTWrapper(item);
        return wrapper.hasKey("conaxData") && new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData")).hasKey("hasBackpackSocket");
    }

    public static boolean hasItemSkin(ItemStack item, ItemStack item2) {
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicData"))
            return false;
        if (wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("item_skin"))
            return false;
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType"))
            return false;
        NBTWrapper wrapper2 = new NBTWrapper(item2);
        NBTTagCompoundBuilder cosmicData2 = wrapper2.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper2.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData2.hasKey("itemSkinType"))
            return false;

        String[] skins = cosmicData2.getString("itemSkinType").split(",");
        if (skins.length >= 2) {
            SkinType type1 = SkinType.valueOf(skins[0]);
            SkinType type2 = SkinType.valueOf(skins[1]);


            for (SkinType type : getItemSkinByItem(item)) {
                if (type.getItemSkinType() == type1.getItemSkinType() || type.getItemSkinType() == type2.getItemSkinType())
                    return true;
            }

        } else {

            SkinType type2 = SkinType.valueOf(cosmicData2.getString("itemSkinType"));
            for (SkinType type : getItemSkinByItem(item)) {
                if (type.getItemSkinType() == type2.getItemSkinType())
                    return true;
            }
        }
        return false;
    }

    public static boolean hasSkinEquipped(Player player, SkinType type) {
        switch (type.getItemSkinType()) {
            case HELMET:
                return (player.getInventory().getHelmet() != null && hasItemSkin(player.getInventory().getHelmet()) && Objects.<List>requireNonNull(getItemSkinByItem(player.getInventory().getHelmet())).contains(type));
            case AMULET:
            case BACKPACK:
                return (player.getInventory().getChestplate() != null && hasItemSkin(player.getInventory().getChestplate()) && Objects.<List>requireNonNull(getItemSkinByItem(player.getInventory().getChestplate())).contains(type));
            case BELT:
                return (player.getInventory().getLeggings() != null && hasItemSkin(player.getInventory().getLeggings()) && Objects.<List>requireNonNull(getItemSkinByItem(player.getInventory().getLeggings())).contains(type));
            case BOOTS:
                return (player.getInventory().getBoots() != null && hasItemSkin(player.getInventory().getBoots()) && Objects.<List>requireNonNull(getItemSkinByItem(player.getInventory().getBoots())).contains(type));
            case SWORD:
            case AXE:
            case PICKAXE:
            case BOW:
            case TOOL:
                if((player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR)) {
                    if(hasItemSkin(player.getItemInHand())) {
                        return Objects.requireNonNull(getItemSkinByItem(player.getItemInHand())).contains(type);
                    }
                }

        }
        return false;
    }

    public static boolean hasItemSkin(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicData"))
            return false;
        if (wrapper.hasKey("cosmicType") && wrapper.getString("cosmicType").equals("item_skin"))
            return false;
        return (new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"))).hasKey("itemSkinType");
    }

    public static List<SkinType> getItemSkinByItem(ItemStack item) {
        List<SkinType> skins = new ArrayList<>();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta())
            return null;
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType"))
            return skins;
        return StringUtils.stringToSkinList(cosmicData.getString("itemSkinType"));
        /*
        List<String> stringSkins = new ArrayList<>(Arrays.asList(cosmicData.getString("itemSkinType").split(",")));
        List<String> copy = new ArrayList<>(stringSkins);
        return StringUtils.getSkinTypes(stringSkins, copy, skins);

         */
    }

    public static List<SkinType> getItemSkin(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta())
            return null;
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        return StringUtils.stringToSkinList(cosmicData.getString("itemSkinType"));
    }


    public static ItemStack removeMultiSkinFromItem(ItemStack item, SkinType skinType1, SkinType skinType2) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        String skinList = cosmicData.getString("itemSkinType");
        List<String> actualSkins = StringUtils.fixSkinList(skinList);
        if (actualSkins.isEmpty()) return null;
        List<String> skinsCopy = new ArrayList<>(actualSkins);

        cosmicData.remove("itemSkinType");
        if (cosmicData.getString("tintColor").equals("-10237440")) {
            cosmicData.remove("tintColor");
        }


        for (String skinType : actualSkins) {
            if (skinType.equals(skinType1.name())) {
                skinsCopy.remove(skinType1.name());
            }
            if (skinType.equals(skinType2.name())) {
                skinsCopy.remove(skinType2.name());

            }
        }


        if (skinsCopy.size() != 0) {
            String[] remaining = skinsCopy.toArray(new String[0]);
            String s = Arrays.toString( remaining);
            s = s.replaceAll(" ", "");
            s = s.replace("[", "");
            s = s.replace("]", "");
            cosmicData.setString("itemSkinType", s);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        item = wrapper.build();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore = lore.stream().filter(s -> !ChatColor.stripColor(s).startsWith("ITEM SKIN: " + ChatColor.stripColor(skinType1.getName()))).collect(Collectors.toList());
        lore = lore.stream().filter(s -> !ChatColor.stripColor(s).startsWith("ITEM SKIN: " + ChatColor.stripColor(skinType2.getName()))).collect(Collectors.toList());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeItemSkinFromItem(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        String skinList = cosmicData.getString("itemSkinType");
        List<String> skins = StringUtils.fixSkinList(skinList);
        cosmicData.remove("itemSkinType");
        if (cosmicData.getString("tintColor").equals("-10237440")) {

            cosmicData.remove("tintColor");
        }

        SkinType type = SkinType.valueOf(skins.get(0));
        skins.remove(skins.get(0));
        if (!skins.isEmpty()) {
            String[] remaining = skins.toArray(new String[0]);
            String s = Arrays.toString( remaining);
            s = s.replaceAll(" ", "");
            s = s.replace("[", "");
            s = s.replace("]", "");
            cosmicData.setString("itemSkinType", s);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        item = wrapper.build();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore = lore.stream().filter(s -> !ChatColor.stripColor(s).startsWith("ITEM SKIN: " + ChatColor.stripColor(type.getName()))).collect(Collectors.toList());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isItemSkin(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return false;
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicType"))
            return false;
        return wrapper.getString("cosmicType").equals("item_skin");
    }

    public static boolean isCorrectType(SkinType skin, ItemStack item) {
        if (skin.getItemSkinType() == ItemSkinType.HELMET)
            return (item.getType() == Material.CHAINMAIL_HELMET || item
                    .getType() == Material.DIAMOND_HELMET || item
                    .getType() == Material.GOLD_HELMET || item
                    .getType() == Material.IRON_HELMET || item
                    .getType() == Material.LEATHER_HELMET);
        if (skin.getItemSkinType() == ItemSkinType.AMULET || skin.getItemSkinType() == ItemSkinType.BACKPACK)
            return (item.getType() == Material.CHAINMAIL_CHESTPLATE || item
                    .getType() == Material.DIAMOND_CHESTPLATE || item
                    .getType() == Material.GOLD_CHESTPLATE || item
                    .getType() == Material.IRON_CHESTPLATE || item
                    .getType() == Material.LEATHER_CHESTPLATE);
        if (skin.getItemSkinType() == ItemSkinType.BELT)
            return (item.getType() == Material.CHAINMAIL_LEGGINGS || item
                    .getType() == Material.DIAMOND_LEGGINGS || item
                    .getType() == Material.GOLD_LEGGINGS || item
                    .getType() == Material.IRON_LEGGINGS || item
                    .getType() == Material.LEATHER_LEGGINGS);
        if (skin.getItemSkinType() == ItemSkinType.BOOTS)
            return (item.getType() == Material.CHAINMAIL_BOOTS || item
                    .getType() == Material.DIAMOND_BOOTS || item
                    .getType() == Material.GOLD_BOOTS || item
                    .getType() == Material.IRON_BOOTS || item
                    .getType() == Material.LEATHER_BOOTS);
        if (skin.getItemSkinType() == ItemSkinType.AXE)
            return (item.getType() == Material.DIAMOND_AXE || item
                    .getType() == Material.GOLD_AXE || item
                    .getType() == Material.IRON_AXE || item
                    .getType() == Material.STONE_AXE || item
                    .getType() == Material.WOOD_AXE);
        if (skin.getItemSkinType() == ItemSkinType.SWORD)
            return (item.getType() == Material.DIAMOND_SWORD || item
                    .getType() == Material.GOLD_SWORD || item
                    .getType() == Material.IRON_SWORD || item
                    .getType() == Material.STONE_SWORD || item
                    .getType() == Material.WOOD_SWORD);
        if (skin.getItemSkinType() == ItemSkinType.BOW)
            return (item.getType() == Material.BOW);
        if (skin.getItemSkinType() == ItemSkinType.PICKAXE)
            return item.getType().toString().endsWith("_PICKAXE");
        if (skin.getItemSkinType() == ItemSkinType.TOOL)
            return (item.getType().toString().endsWith("_PICKAXE") || item.getType().toString().endsWith("_SWORD") || item.getType().toString().endsWith("_AXE"));
        return false;
    }

    public static void updateSkins(Player player, boolean everything, ItemStack armorPiece) {
            PlayerInventory inventory = player.getInventory();
            if (everything) {
                if (inventory.getHelmet() != null && inventory.getHelmet().getType() != Material.AIR && hasItemSkin(inventory.getHelmet())) {
                    getItemSkinByItem(inventory.getHelmet()).forEach(skin -> skin.getItemSkin().onUnequip(player));
                    getItemSkinByItem(inventory.getHelmet()).forEach(skin -> skin.getItemSkin().onEquip(player));

                }
                if (inventory.getChestplate() != null && inventory.getChestplate().getType() != Material.AIR && hasItemSkin(inventory.getChestplate())) {
                    getItemSkinByItem(inventory.getChestplate()).forEach(skin -> skin.getItemSkin().onUnequip(player));
                    getItemSkinByItem(inventory.getChestplate()).forEach(skin -> skin.getItemSkin().onEquip(player));
                }
                if (inventory.getLeggings() != null && inventory.getLeggings().getType() != Material.AIR && hasItemSkin(inventory.getLeggings())) {
                    getItemSkinByItem(inventory.getLeggings()).forEach(skin -> skin.getItemSkin().onUnequip(player));
                    getItemSkinByItem(inventory.getLeggings()).forEach(skin -> skin.getItemSkin().onEquip(player));
                }
                if (inventory.getBoots() != null && inventory.getBoots().getType() != Material.AIR && hasItemSkin(inventory.getBoots())) {
                    getItemSkinByItem(inventory.getBoots()).forEach(skin -> skin.getItemSkin().onUnequip(player));
                    getItemSkinByItem(inventory.getBoots()).forEach(skin -> skin.getItemSkin().onEquip(player));
                }
            } else {
                if (hasItemSkin(armorPiece)) {
                    getItemSkinByItem(armorPiece).forEach(skin -> skin.getItemSkin().onUnequip(player));
                    getItemSkinByItem(armorPiece).forEach(skin -> skin.getItemSkin().onEquip(player));
                }
            }

    }


    public static void refreshAllSkinsOfPlayer(Player player) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.getHelmet() != null && inventory.getHelmet().getType() != Material.AIR && hasItemSkin(inventory.getHelmet())) {
            getItemSkinByItem(inventory.getHelmet()).forEach(skin -> skin.getItemSkin().onUnequip(player));
            getItemSkinByItem(inventory.getHelmet()).forEach(skin -> skin.getItemSkin().onEquip(player));
        }
        if (inventory.getChestplate() != null && inventory.getChestplate().getType() != Material.AIR && hasItemSkin(inventory.getChestplate())) {
            getItemSkinByItem(inventory.getChestplate()).forEach(skin -> skin.getItemSkin().onUnequip(player));
            getItemSkinByItem(inventory.getChestplate()).forEach(skin -> skin.getItemSkin().onEquip(player));
        }
        if (inventory.getLeggings() != null && inventory.getLeggings().getType() != Material.AIR && hasItemSkin(inventory.getLeggings())) {
            getItemSkinByItem(inventory.getLeggings()).forEach(skin -> skin.getItemSkin().onUnequip(player));
            getItemSkinByItem(inventory.getLeggings()).forEach(skin -> skin.getItemSkin().onEquip(player));
        }
        if (inventory.getBoots() != null && inventory.getBoots().getType() != Material.AIR && hasItemSkin(inventory.getBoots())) {
            getItemSkinByItem(inventory.getBoots()).forEach(skin -> skin.getItemSkin().onUnequip(player));
            getItemSkinByItem(inventory.getBoots()).forEach(skin -> skin.getItemSkin().onEquip(player));
        }
    }

    public static ItemStack removeNoDisplay(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        cosmicData.remove("itemSkinType");
        if (cosmicData.getString("tintColor").equals("-10237440")) {

            cosmicData.remove("tintColor");
        }

        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

}
